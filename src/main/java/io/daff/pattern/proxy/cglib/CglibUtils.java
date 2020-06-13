package io.daff.pattern.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * cglib动态代理
 *
 * @author daffupman
 * @since 2020/6/13
 */
public class CglibUtils {

    /**
     * 给目标对象生成代理类
     *
     * @param targetObject 目标对象
     * @param methodInterceptor 切面
     * @param <T> 目标对象的类型
     * @return  目标对象的代理类
     */
    public static <T> T createProxy(T targetObject, MethodInterceptor methodInterceptor) {
        return (T) Enhancer.create(targetObject.getClass(), methodInterceptor);
    }
}
