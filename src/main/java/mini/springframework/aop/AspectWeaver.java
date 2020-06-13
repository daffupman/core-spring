package mini.springframework.aop;

import mini.springframework.aop.annotation.Aspect;
import mini.springframework.aop.annotation.Order;
import mini.springframework.aop.aspect.AspectInfo;
import mini.springframework.aop.aspect.DefaultAspect;
import mini.springframework.core.BeanContainer;
import mini.springframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author daffupman
 * @since 2020/6/13
 */
public class AspectWeaver {

    private BeanContainer beanContainer;

    public AspectWeaver() {
        this.beanContainer = BeanContainer.getInstance();
    }

    public void doAop() {
        // 获取所有的切面类
        Set<Class<?>> aspectSet = beanContainer.getClassesByAnnotation(Aspect.class);
        // 将切面类按照不同的织入目标进行切分
        Map<Class<? extends Annotation>, List<AspectInfo>> categorizedMap = new HashMap<>();
        if (ValidationUtil.isEmpty(aspectSet)) { return; }
        for (Class<?> aspectClass : aspectSet) {
            if (verifyAspect(aspectClass)) {
                categorizeAspect(categorizedMap, aspectClass);
            } else {
                throw new RuntimeException("@Aspect and @Order have not been added to the Aspect class, " +
                        "or Aspect class does not extend from DefaultAspect, or the value in Aspect Tag equals @Aspect");
            }
        }
        // 按照不同的织入目标分别去按序织入Aspect逻辑
        if (ValidationUtil.isEmpty(categorizedMap)) { return; }
        for (Map.Entry<Class<? extends Annotation>, List<AspectInfo>> entry : categorizedMap.entrySet()) {
            weaveByCategory(entry.getKey(), entry.getValue());
        }
    }

    private void weaveByCategory(Class<? extends Annotation> category, List<AspectInfo> aspectInfoList) {
        // 获取被代理类的集合
        Set<Class<?>> classSet = beanContainer.getClassesByAnnotation(category);
        if (ValidationUtil.isEmpty(classSet)) { return; }
        // 遍历被代理类，分别为每个被代理类生成动态代理实例
        for (Class<?> targetClass : classSet) {
            AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass, aspectInfoList);
            Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);
            // 将动态代理对象实例添加到容器里，取代代理前的类实例
            beanContainer.addBean(targetClass, proxyBean);
        }
    }

    // 将切面类按照不同的织入目标进行切分，归类
    private void categorizeAspect(Map<Class<? extends Annotation>, List<AspectInfo>> categorizedMap, Class<?> aspectClass) {
        Order orderTag = aspectClass.getAnnotation(Order.class);
        Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
        DefaultAspect aspect = (DefaultAspect) beanContainer.getBean(aspectClass);
        AspectInfo aspectInfo = new AspectInfo(orderTag.value(), aspect);
        if (!categorizedMap.containsKey(aspectTag.value())) {
            List<AspectInfo> aspectInfoList = new ArrayList<>();
            aspectInfoList.add(aspectInfo);
            categorizedMap.put(aspectTag.value(), aspectInfoList);
        } else {
            List<AspectInfo> aspectInfoList = categorizedMap.get(aspectTag.value());
            aspectInfoList.add(aspectInfo);
        }
    }

    /**
     * 校验AspectClass的合法性：
     *  - @Aspect和@Order需要同时存在
     *  - 该类必须继承自DefaultAspect
     *  - @Aspect的属性值不能是它本身
     */
    private boolean verifyAspect(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class)
                && aspectClass.isAnnotationPresent(Order.class)
                && DefaultAspect.class.isAssignableFrom(aspectClass)
                && aspectClass.getAnnotation(Aspect.class).value() != Aspect.class;

    }
}
