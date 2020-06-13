package mini.springframework.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author daffupman
 * @since 2020/6/13
 */
public class ProxyCreator {

    /**
     * 创建动态代理对象并返回
     */
    public static Object createProxy(Class<?> targetClass, MethodInterceptor mi) {
        return Enhancer.create(targetClass, mi);
    }
}
