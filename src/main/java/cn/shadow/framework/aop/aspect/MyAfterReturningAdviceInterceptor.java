package cn.shadow.framework.aop.aspect;

import java.lang.reflect.Method;

import cn.shadow.framework.aop.intercept.MyMethodInterceptor;
import cn.shadow.framework.aop.intercept.MyMethodInvocation;

public class MyAfterReturningAdviceInterceptor extends MyAbstractAspectAdvice implements MyMethodInterceptor {

	public MyAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
		super(aspectMethod, aspectTarget);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object invoke(MyMethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
