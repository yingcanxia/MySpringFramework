package cn.shadow.framework.beans.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.shadow.framework.beans.config.MyBeanDefinition;
import cn.shadow.framework.context.support.MyAbstractApplicationContext;

public class MyDefaultListableBeanFactory extends MyAbstractApplicationContext{

	//MyBeanDefinition存储配置文件信息的
	protected final Map<String,MyBeanDefinition>beanDefinitionMap =new ConcurrentHashMap<String,MyBeanDefinition>();
	
	
	
}
