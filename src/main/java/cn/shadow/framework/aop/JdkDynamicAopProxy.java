package cn.shadow.framework.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import cn.shadow.framework.aop.intercept.MyMethodInvocation;
import cn.shadow.framework.aop.support.MyAdvisedSupport;

public class JdkDynamicAopProxy implements MyAopProxy,InvocationHandler{

	private MyAdvisedSupport advised;
	public JdkDynamicAopProxy(MyAdvisedSupport config) {
		this.advised=config;
	}
	public Object getProxy() {
		// TODO Auto-generated method stub
		
		return getProxy(this.advised.getTargetClass().getClassLoader());
	}

	public Object getProxy(ClassLoader classLoader) {
		// TODO Auto-generated method stub
		return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(), this);
	}
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		//��ȡ��������
		List<Object> interceptorsAndDynamicMethodMatchers= this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, advised.getTargetClass());
		MyMethodInvocation methodInvocation=new MyMethodInvocation(proxy, null, method, args, this.advised.getTargetClass(), null);
		return methodInvocation.proceed();
	}

}
