package cn.shadow.framework.aop.aspect;

import java.lang.reflect.Method;

import cn.shadow.framework.aop.intercept.MyMethodInterceptor;
import cn.shadow.framework.aop.intercept.MyMethodInvocation;

public class MyMethodBeforeAdviceInterceptor extends MyAbstractAspectAdvice implements MyAdvice,MyMethodInterceptor {

	
	private MyJoinPoint joinPoint;
	public MyMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
		super(aspectMethod, aspectTarget);
		// TODO Auto-generated constructor stub
	}

	public void before(Method method,Object[] args,Object target)throws Throwable {
		//将这些东西穿个
		//method.invoke(target);
		super.invokeadviceMethod(this.joinPoint,null,null);
	}
	@Override
	public Object invoke(MyMethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		this.joinPoint=invocation;
		before(invocation.getMethod(), invocation.getArguments(), invocation);
		return invocation.proceed();
	}

}
