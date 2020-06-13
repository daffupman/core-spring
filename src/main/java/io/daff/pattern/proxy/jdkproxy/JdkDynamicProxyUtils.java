package io.daff.pattern.proxy.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Jdk动态代理的工具类
 *
 * @author daffupman
 * @since 2020/6/12
 */
public class JdkDynamicProxyUtils {

	/**
	 * 使用jdk的动态代理给目标对象代理类
	 *
	 * @param targetObject 要代理的目标类
	 * @param handler	代理类的InvocationHandler，即切面逻辑
	 * @param <T>	目标类的类型
	 * @return	返回目标类的代理类
	 */
	public static <T>T newProxyInstance(T targetObject, InvocationHandler handler){
		ClassLoader classLoader = targetObject.getClass().getClassLoader();
		Class<?>[]  interfaces = targetObject.getClass().getInterfaces();
		return (T) Proxy.newProxyInstance(
				targetObject.getClass().getClassLoader(),
				targetObject.getClass().getInterfaces(),
				handler
		);
	}
}
