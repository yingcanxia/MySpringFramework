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
	//����һ����������-1��ʼ����¼��ǰ������ִ�е�λ��
	private int currentInterceptorIndex=-1;
	//�����������ù���
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
		//�������ִ����ִ����ɺ�
		if(this.currentInterceptorIndex==this.interceptorsAndDynamicMethodMatchers.size()-1) {
			return this.method.invoke(this.target, this.arguments);
		}
		Object interceptorOrInterceptionAdvice=this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
		if(interceptorOrInterceptionAdvice instanceof MyMethodInterceptor) {
			MyMethodInterceptor mi=(MyMethodInterceptor)interceptorOrInterceptionAdvice;
			return mi.invoke(this);
		}else {
			//��̬ƥ��ʧ��ʱ���ӹ���ǰ������������һ��
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
