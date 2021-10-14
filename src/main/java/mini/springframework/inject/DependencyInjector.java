package mini.springframework.inject;

import lombok.extern.slf4j.Slf4j;
import mini.springframework.core.BeanContainer;
import mini.springframework.inject.annotation.Autowired;
import mini.springframework.util.ClassUtils;
import mini.springframework.util.ValidationUtils;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author daffupman
 * @since 2020/3/26
 */
@Slf4j
public class DependencyInjector {

    /**
     * Bean容器
     */
    private BeanContainer beanContainer;

    public DependencyInjector() {
        beanContainer = BeanContainer.getInstance();
    }

    /**
     * 执行ioc
     */
    public void doIoc() {
        // 1、遍历Bean容器中所有的Class对象
        if (ValidationUtils.isEmpty(beanContainer.getClasses())) {
            log.warn("empty classSet in BeanContainer");
            return;
        }

        for (Class<?> clazz : beanContainer.getClasses()) {
            // 2、遍历Class对象的所有成员变量
            Field[] fields = clazz.getDeclaredFields();
            if (ValidationUtils.isEmpty(fields)) {
                continue;
            }
            for (Field field : fields) {
                // 3、找出被Autowired标记的成员变量
                if (field.isAnnotationPresent(Autowired.class)) {
                    // 获取@Autowired中的value值
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String autowiredValue = autowired.value();
                    // 4、获取这些成员变量的类型
                    Class<?> fieldClass = field.getType();
                    // 5、获取这些成员变量的类型在容器里对应的实例
                    Object fieldValue = getFieldInstance(fieldClass, autowiredValue);
                    if (fieldValue == null) {
                        throw new RuntimeException("unable to inject relevant type, target fieldClass is: " + fieldClass.getName() + " autowiredValue is: [" + autowiredValue + "]");
                    } else {
                        // 6、通过反射将对应的成员变量实例注入到成员变量所在类的实例里
                        Object targetBean = beanContainer.getBean(clazz);
                        ClassUtils.setField(field, targetBean, fieldValue, true);
                    }
                }
            }
        }
    }

    /**
     * 根据Class在beanContainer里获取其实例或者实现类
     */
    private Object getFieldInstance(Class<?> fieldClass, String autowiredValue) {
        Object fieldValue = beanContainer.getBean(fieldClass);
        if (fieldValue != null) {
            return fieldValue;
        } else {
            Class<?> implementClass = getImplementClass(fieldClass, autowiredValue);
            if (implementClass != null) {
                return beanContainer.getBean(implementClass);
            } else {
                return null;
            }
        }
    }

    /**
     * 获取接口的实现类
     */
    private Class<?> getImplementClass(Class<?> fieldClass, String autowiredValue) {
        Set<Class<?>> classSet = beanContainer.getClassesBySuper(fieldClass);
        if (!ValidationUtils.isEmpty(classSet)) {
            if (ValidationUtils.isEmpty(autowiredValue)) {
                // @Autowired标签中没有配置value值
                if (classSet.size() == 1) {
                    return classSet.iterator().next();
                } else {
                    // 如果有多个实现类，那么报错
                    throw new RuntimeException("multiple implemented classes for " + fieldClass.getName() + " please set @Autowired's value to pick one");
                }
            } else {
                // @Autowired标签中配置了value值
                for (Class<?> clazz : classSet) {
                    if (autowiredValue.equals(clazz.getSimpleName())) {
                        return clazz;
                    }
                }
            }
        }
        return null;
    }
}
