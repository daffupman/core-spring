package mini.springframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mini.springframework.core.annotation.Component;
import mini.springframework.core.annotation.Controller;
import mini.springframework.core.annotation.Repository;
import mini.springframework.core.annotation.Service;
import mini.springframework.util.ClassUtil;
import mini.springframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 可抵御反射和序列化攻击的ioc容器
 *
 * @author daffupman
 * @since 2020/3/24
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {

    /**
     * 保存Class对象及其实例的载体
     */
    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    /**
     * 加载Bean的注解列表
     */
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION = Arrays.asList(
            Component.class, Controller.class, Service.class, Repository.class
    );

    public static BeanContainer getInstance() {
        return ContainerHolder.HOLDER.instance;
    }

    private enum ContainerHolder {
        HOLDER;

        private BeanContainer instance;
        ContainerHolder() {
            instance = new BeanContainer();
        }
    }

    /**
     * 标记容器是否已经加载过
     */
    private Boolean loaded = false;

    /**
     * 返回是否加载
     */
    public Boolean isLoaded() {
        return loaded;
    }

    /**
     * 返回容器内Bean的个数
     */
    public int size() {
        return beanMap.size();
    }

    /**
     * 扫描加载所有Bean
     */
    public synchronized void loadBeans(String packageName) {

        if (isLoaded()) {
            log.warn("BeanContainer has been loaded.");
            return;
        }
        Set<Class<?>> classSet = ClassUtil.extractClassFromPackage(packageName);
        if (ValidationUtil.isEmpty(classSet)) {
            log.warn("extra nothing from package :" + packageName);
            return;
        }
        for (Class<?> clazz : classSet) {
            for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                // 如果类上标记了自定义的注解
                if (clazz.isAnnotationPresent(annotation)) {
                    beanMap.put(clazz, ClassUtil.newInstance(clazz, true));
                }
            }
        }
        loaded = true;
    }
}
