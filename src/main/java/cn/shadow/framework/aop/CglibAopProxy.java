package cn.shadow.framework.aop;

import cn.shadow.framework.aop.support.MyAdvisedSupport;

public class CglibAopProxy implements MyAopProxy{

	private MyAdvisedSupport advised;
	
	
	public CglibAopProxy(MyAdvisedSupport advised) {
		this.advised = advised;
	}

	public Object getProxy() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getProxy(ClassLoader classLoader) {
		// TODO Auto-generated method stub
		return null;
	}

}
