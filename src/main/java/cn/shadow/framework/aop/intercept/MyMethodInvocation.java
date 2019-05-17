package cn.shadow.framework.aop.intercept;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.shadow.framework.aop.aspect.MyJoinPoint;

public class MyMethodInvocation implements MyJoinPoint{
	
	private Object proxy;
	private Method method;
	private Object target;
	private Object[] arguments;
	private List<Object>interceptorsAndDynamicMethodMatchers;
	private Class<?>targetClass;
	private Map<String,Object>userAttributes;
	//定义一个索引，从-1开始来记录当前拦截器执行的位置
	private int currentInterceptorIndex=-1;
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
		//如果整个执行链执行完成后
		if(this.currentInterceptorIndex==this.interceptorsAndDynamicMethodMatchers.size()-1) {
			return this.method.invoke(this.target, this.arguments);
		}
		Object interceptorOrInterceptionAdvice=this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
		if(interceptorOrInterceptionAdvice instanceof MyMethodInterceptor) {
			MyMethodInterceptor mi=(MyMethodInterceptor)interceptorOrInterceptionAdvice;
			return mi.invoke(this);
		}else {
			//动态匹配失败时，掠过当前拦截器调用下一个
			return proceed();
		}
	}

	@Override
	public Object getThis() {
		// TODO Auto-generated method stub
		return this.target;
	}

	@Override
	public Object[] getArguments() {
		// TODO Auto-generated method stub
		return this.arguments;
	}

	@Override
	public Method getMethod() {
		// TODO Auto-generated method stub
		return this.method;
	}

	@Override
	public void setUserAttribute(String key, Object value) {
		// TODO Auto-generated method stub
		if(value!=null) {
			if(this.userAttributes==null) {
				this.userAttributes=new HashMap<String, Object>();
				
			}
			this.userAttributes.put(key, value);
		}else {
			if(this.userAttributes!=null) {
				this.userAttributes.remove(key);
			}
		}
	}

	@Override
	public Object getUserAttribute(String key) {
		// TODO Auto-generated method stub
		
		return (this.userAttributes!=null?this.userAttributes.get(key):null);
		
	}

}
