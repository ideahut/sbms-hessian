package com.github.ideahut.sbms.hessian.proxy;

public class HessianProxyFactoryBean extends org.springframework.remoting.caucho.HessianProxyFactoryBean {

	public HessianProxyFactoryBean() {
		super();
		setProxyFactory(new HessianProxyFactory());
	}

	@Override
	public void setProxyFactory(com.caucho.hessian.client.HessianProxyFactory proxyFactory) {
		super.setProxyFactory(proxyFactory != null ? proxyFactory : new HessianProxyFactory());
	}
	
}
