package cn.shadow.framework.aop.intercept;

import org.omg.PortableInterceptor.Interceptor;

public interface MyMethodInterceptor extends Interceptor{
	Object invoke(MyMethodInvocation invocation)throws Throwable;
}
