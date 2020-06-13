package io.daff.pattern.proxy.impl;

import io.daff.pattern.proxy.ToCPayment;

/**
 * JDK的静态代理
 *
 * @author daffupman
 * @since 2020/6/10
 */
public class AlipayToC implements ToCPayment {

	private ToCPayment toCPayment;

	public AlipayToC(ToCPayment toCPayment) {
		this.toCPayment = toCPayment;
	}

	@Override
	public void pay() {
		beforePay();
		toCPayment.pay();
		afterPay();
	}

	private void beforePay() {
		System.out.println("从招行取款");
	}

	private void afterPay() {
		System.out.println("支付给imooc");
	}
}
