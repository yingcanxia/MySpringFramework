package cn.shadow.framework.aop.aspect;

import java.lang.reflect.Method;

import cn.shadow.framework.aop.intercept.MyMethodInterceptor;
import cn.shadow.framework.aop.intercept.MyMethodInvocation;

public class MyAfterReturningAdviceInterceptor extends MyAbstractAspectAdvice implements MyMethodInterceptor {

	private MyJoinPoint joinPoint;
	public MyAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
		super(aspectMethod, aspectTarget);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object invoke(MyMethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		Object returnValue=invocation.proceed();
		this.joinPoint=invocation;
		this.afterReturning(returnValue,invocation.getMethod(),invocation.getArguments(),invocation.getThis());
		return returnValue;
	}

	private void afterReturning(Object returnValue, Method method, Object[] arguments, Object this1) {
		// TODO Auto-generated method stub
		try {
			super.invokeadviceMethod(this.joinPoint, returnValue, null);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
