package io.daff.pattern.proxy;

import io.daff.pattern.proxy.cglib.AlipayMethodInterceptor;
import io.daff.pattern.proxy.cglib.CglibUtils;
import io.daff.pattern.proxy.impl.CommonPayment;

/**
 * @author daffupman
 * @since 2020/6/10
 */
public class ProxyDemo {

	public static void main(String[] args) {
		/*		JDK静态代理 开始		*/
		// ToCPayment toCProxy = new AlipayToC(new ToCPaymentImpl());
		// toCProxy.pay();
		// ToBPayment toBProxy = new AliPayToB(new ToBPaymentImpl());
		// toBProxy.pay();
		/*		JDK静态代理	结束	*/

		/*		JDK动态代理 开始		*/
		// ToBPaymentImpl toBPayment = new ToBPaymentImpl();
		// AlipayInvocationHandler alipayInvocationHandler = new AlipayInvocationHandler(toBPayment);
		// ToBPayment toBPaymentProxy = JdkDynamicProxyUtils.newProxyInstance(toBPayment, alipayInvocationHandler);
		// toBPaymentProxy.pay();
		//
		// ToCPayment toCPayment = new ToCPaymentImpl();
		// ToCPayment toCPaymentProxy = JdkDynamicProxyUtils.newProxyInstance(toCPayment, new AlipayInvocationHandler(toCPayment));
		// toCPaymentProxy.pay();
		/*		JDK动态代理 结束		*/

		/*		cglib动态代理 开始		*/
		CommonPayment commonPayment = new CommonPayment();
		// jdk动态代理不支持不实现接口的对象
		// AlipayInvocationHandler alipayInvocationHandler = new AlipayInvocationHandler(commonPayment);
		// CommonPayment commonPaymentProxy = JdkDynamicProxyUtils.newProxyInstance(commonPayment, alipayInvocationHandler);
		// commonPaymentProxy.pay();

		AlipayMethodInterceptor alipayMethodInterceptor = new AlipayMethodInterceptor();
		CommonPayment commonPaymentCglibProxy = CglibUtils.createProxy(commonPayment, alipayMethodInterceptor);
		commonPaymentCglibProxy.pay();
		/*		cglib动态代理 结束		*/
	}
}
