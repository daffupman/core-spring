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
@Aspect(pointcut = "within(io.daff.controller.superadmin.*)")
@Order(10)
@Slf4j
public class ControllerInfoRecordAspect extends DefaultAspect {

    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {
        log.info(
                "方法执行：执行的类是[{}]，执行的方法是[{}]，参数是[{}]",
                targetClass.getName(),
                method.getName(),
                args
        );
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object returnValue) throws Throwable {
        log.info(
                "方法执行结束，执行的类是[{}]，执行的方法是[{}]，参数是[{}]，返回值是[{}]",
                targetClass.getName(),
                method.getName(),
                args,
                returnValue
        );
        return returnValue;
    }

    @Override
    public void afterThrowing(Class<?> targetClass, Method method, Object[] args, Throwable e) throws Throwable {
        log.info(
                "方法执行结束，执行的类是[{}]，执行的方法是[{}]，参数是[{}]，抛出的异常是[{}]",
                targetClass.getName(),
                method.getName(),
                args,
                e
        );
    }
}
