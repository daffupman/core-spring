package mini.springframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author daffupman
 * @since 2020/6/13
 */
public abstract class DefaultAspect {

    /**
     * 前置拦截
     * @param targetClass 被代理的类
     * @param method 被代理的目标方法
     * @param args 目标方法的参数
     * @throws Throwable 异常
     */
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable {}

    /**
     * 后置拦截
     * @param targetClass 被代理的类
     * @param method 被代理的目标方法
     * @param args 目标方法的参数
     * @param returnValue 目标方法的返回值
     * @throws Throwable 异常
     */
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object returnValue) throws Throwable {
        return returnValue;
    }

    /**
     * 前置拦截
     * @param targetClass 被代理的类
     * @param method 被代理的目标方法
     * @param args 目标方法的参数
     * @param e 抛出的异常
     * @throws Throwable 异常
     */
    public void afterThrowing(Class<?> targetClass, Method method, Object[] args, Throwable e) throws Throwable {}
}
