package io.daff.pattern.proxy.impl;

import io.daff.pattern.proxy.ToBPayment;

/**
 * @author daffupman
 * @since 2020/6/10
 */
public class ToBPaymentImpl implements ToBPayment {

	@Override
	public void pay() {
		System.out.println("以公司的名义进行支付");
	}
}
