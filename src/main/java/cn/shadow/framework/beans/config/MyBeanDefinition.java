package cn.shadow.framework.beans.config;

public class MyBeanDefinition {
	private String beanClassName;
	private boolean lazyInit;
	private String factoryBeanName;
	private boolean isSingleton=true;
	
	public String getBeanClassName() {
		return beanClassName;
	}
	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}
	public boolean isLazyInit() {
		return lazyInit;
	}
	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}
	public String getFactoryBeanName() {
		return factoryBeanName;
	}
	public void setFactoryBeanName(String factoryBeanName) {
		this.factoryBeanName = factoryBeanName;
	}
	public boolean isSingleton() {
		return isSingleton;
	}
	public void setSingleton(boolean isSingleton) {
		this.isSingleton = isSingleton;
	}
	
}
