package cn.shadow.framework.aop.aspect;

import java.lang.reflect.Method;

public abstract class MyAbstractAspectAdvice {

	private Method aspectMethod;
	private Object aspectTarget;
	public MyAbstractAspectAdvice(Method aspectMethod,Object aspectTarget) {
		this.aspectMethod=aspectMethod;
		this.aspectTarget=aspectTarget;
	}
}
 