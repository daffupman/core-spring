package io.daff.aspect;

import lombok.extern.slf4j.Slf4j;
import mini.springframework.aop.annotation.Aspect;
import mini.springframework.aop.annotation.Order;
import mini.springframework.aop.aspect.DefaultAspect;

import java.lang.reflect.Method;

/**
 * @author daffupman
 * @since 2020/6/13
 */
@Aspect(pointcut = "execution(* io.daff.controller.frontend..*.*(..))")
@Order(0)
@Slf4j
public class ControllerCalculatorAspect extends DefaultAspect {

    private long timestampCache;

    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {
        log.info(
                "开始计时，执行的类：[{}]，执行的方法是[{}]，参数是[{}]",
                targetClass.getName(),
                method.getName(),
                args
        );
        timestampCache = System.currentTimeMillis();
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object returnValue) throws Throwable {
        log.info(
                "结束技术，执行的类是[{}]，执行的方法是[{}]，参数是[{}]，返回值是[{}]，耗时[{}]ms",
                targetClass.getName(),
                method.getName(),
                args,
                returnValue,
                System.currentTimeMillis() - timestampCache
        );
        return returnValue;
    }
}
