package mini.springframework.aop;

import mini.springframework.aop.annotation.Aspect;
import mini.springframework.aop.annotation.Order;
import mini.springframework.aop.aspect.AspectInfo;
import mini.springframework.aop.aspect.DefaultAspect;
import mini.springframework.core.BeanContainer;
import mini.springframework.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        // 拼装AspectInfoList
        if (ValidationUtil.isEmpty(aspectSet)) { return; }
        List<AspectInfo> aspectInfoList = packAspectInfoList(aspectSet);
        // 遍历容器里的类
        Set<Class<?>> classSet = beanContainer.getClasses();
        for (Class<?> targetClass : classSet) {
            if (targetClass.isAnnotationPresent(Aspect.class)) { continue; }
            // 初筛符合条件的Aspect
            List<AspectInfo> roughMatchedAspectList = collectRoughMatchedAspectListForSpecificClass(aspectInfoList, targetClass);
            // 尝试进行Aspect的织入
            wrapIfNeccessary(roughMatchedAspectList, targetClass);
        }
    }

    private void wrapIfNeccessary(List<AspectInfo> roughMatchedAspectList, Class<?> targetClass) {
        if (ValidationUtil.isEmpty(roughMatchedAspectList)) { return; }
        AspectListExecutor aspectListExecutor = new AspectListExecutor(targetClass, roughMatchedAspectList);
        Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);
        beanContainer.addBean(targetClass, proxyBean);
    }

    private List<AspectInfo> collectRoughMatchedAspectListForSpecificClass(List<AspectInfo> aspectInfoList, Class<?> targetClass) {
        List<AspectInfo> roughMatchedAspectList = new ArrayList<>();
        for (AspectInfo aspectInfo : aspectInfoList) {
            if (aspectInfo.getPointcutLocator().roughMatches(targetClass)) {
                roughMatchedAspectList.add(aspectInfo);
            }
        }
        return roughMatchedAspectList;
    }

    private List<AspectInfo> packAspectInfoList(Set<Class<?>> aspectSet) {
        List<AspectInfo> aspectInfoList = new ArrayList<>();
        for (Class<?> aspectClass : aspectSet) {
            if (verifyAspect(aspectClass)) {
                Order orderTag = aspectClass.getAnnotation(Order.class);
                Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
                DefaultAspect defaultAspect = (DefaultAspect) beanContainer.getBean(aspectClass);
                PointcutLocator pointcutLocator = new PointcutLocator(aspectTag.pointcut());
                AspectInfo aspectInfo = new AspectInfo(orderTag.value(), defaultAspect, pointcutLocator);
                aspectInfoList.add(aspectInfo);
            } else {
                throw new RuntimeException("@Aspect and @Order must be added to the Aspect class, and Aspect class must extend from DefaultAspect");
            }
        }
        return aspectInfoList;
    }

    /**
     * 校验AspectClass的合法性：
     *  - @Aspect和@Order需要同时存在
     *  - 该类必须继承自DefaultAspect
     */
    private boolean verifyAspect(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class)
                && aspectClass.isAnnotationPresent(Order.class)
                && DefaultAspect.class.isAssignableFrom(aspectClass);

    }
}
