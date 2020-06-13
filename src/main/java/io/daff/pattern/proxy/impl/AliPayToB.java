package io.daff.pattern.proxy.impl;

import io.daff.pattern.proxy.ToBPayment;

/**
 * JDK的静态代理
 *
 * @author daffupman
 * @since 2020/6/10
 */
public class AliPayToB implements ToBPayment {

	private ToBPayment toBPayment;

	public AliPayToB(ToBPayment toBPayment) {
		this.toBPayment = toBPayment;
	}

	@Override
	public void pay() {
		beforePay();
		toBPayment.pay();
		afterPay();
	}

	private void beforePay() {
		System.out.println("从招行取款");
	}

	private void afterPay() {
		System.out.println("支付给imooc");
	}
}
