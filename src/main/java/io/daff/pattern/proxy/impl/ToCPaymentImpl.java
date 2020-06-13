package io.daff.pattern.proxy.impl;

import io.daff.pattern.proxy.ToCPayment;

/**
 * @author daffupman
 * @since 2020/6/10
 */
public class ToCPaymentImpl implements ToCPayment {
	@Override
	public void pay() {
		System.out.println("以用户的名义支付");
	}
}
