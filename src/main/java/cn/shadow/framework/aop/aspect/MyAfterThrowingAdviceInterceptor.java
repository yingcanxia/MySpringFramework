package cn.shadow.framework.aop.aspect;

import java.lang.reflect.Method;

import cn.shadow.framework.aop.intercept.MyMethodInterceptor;
import cn.shadow.framework.aop.intercept.MyMethodInvocation;

public class MyAfterThrowingAdviceInterceptor extends MyAbstractAspectAdvice implements MyMethodInterceptor{

	public MyAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
		super(aspectMethod, aspectTarget);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object invoke(MyMethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		try {
			return invocation.proceed();
		}catch (Exception e) {
			// TODO: handle exception
			invokeadviceMethod(invocation, null, e.getCause());
			throw e;
		}
	}

	public void setThrowName(String throwName) {
		
	}
}
