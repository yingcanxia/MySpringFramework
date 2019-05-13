package cn.shadow.framework.aop;

public interface MyAopProxy {

	Object getProxy();
	Object getProxy(ClassLoader classLoader);
}
