package cn.shadow.framework.aop.intercept;

import java.lang.reflect.Method;
import java.util.List;

public class MyMethodInvocation {
	
	private Object proxy;
	private Method method;
	private Object target;
	private Object[] arguments;
	private List<Object>interceptorsAndDynamicMethodMatchers;
	private Class<?>targetClass;
	

	//拦截器链调用过程
	public MyMethodInvocation(
			Object proxy,Object target,Method method,Object[] arguments,
			Class<?> targetClass,List<Object>interceptorsAndDynamicMethodMatchers) {
		this.proxy=proxy;
		this.target=target;
		this.targetClass=targetClass;
		this.method=method;
		this.arguments=arguments;
		this.interceptorsAndDynamicMethodMatchers=interceptorsAndDynamicMethodMatchers;
	}
	
	public Object proceed()throws Throwable{
		return null;
		
	}
}
