package cn.shadow.framework.aop.intercept;



public interface MyMethodInterceptor{
	public Object invoke(MyMethodInvocation invocation)throws Throwable;
}
