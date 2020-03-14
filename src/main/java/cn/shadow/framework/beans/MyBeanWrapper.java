package cn.shadow.framework.beans;

public class MyBeanWrapper {
	
	private Object wrappedInstance;
	private Class<?> wrappedClass;
	
	public MyBeanWrapper(Object wrappedInstance) {
		this.wrappedInstance=wrappedInstance;
	}
	
	//如果是单例模式则直接get即可
	public Object getWappedInstance() {
		return this.wrappedInstance;
	}
	//如果不是单例将从本方法中new对象出来
	public Class<?> getWrappedClass() {
		return this.wrappedInstance.getClass();
	}
}
