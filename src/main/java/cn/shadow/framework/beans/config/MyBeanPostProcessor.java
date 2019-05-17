package cn.shadow.framework.beans.config;

public class MyBeanPostProcessor {

	public Object postProcessBeforeInitializetion(Object bean,String beanName) throws Exception{
		
		return bean;
	}
	public Object postProcessAfterInitializetion(Object bean,String beanName) throws Exception{
		
		return bean;
	}
}
