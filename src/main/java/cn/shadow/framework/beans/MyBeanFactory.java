package cn.shadow.framework.beans;
//单例工厂的顶层设计
public interface MyBeanFactory {
	//本方法非常关键通过唯一的入口获取对象
	//单例模式/单例对象，私有化构造方法发
	
	Object getBean(String beanName) throws Exception;
	Object getBean(Class<?> beanClass) throws Exception;
}
