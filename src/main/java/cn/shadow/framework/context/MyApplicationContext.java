package cn.shadow.framework.context;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import cn.shadow.framework.annotation.MyAutowired;
import cn.shadow.framework.annotation.MyController;
import cn.shadow.framework.annotation.MyService;
import cn.shadow.framework.aop.CglibAopProxy;
import cn.shadow.framework.aop.JdkDynamicAopProxy;
import cn.shadow.framework.aop.MyAopProxy;
import cn.shadow.framework.aop.config.MyAopConfig;
import cn.shadow.framework.aop.support.MyAdvisedSupport;
import cn.shadow.framework.beans.MyBeanWrapper;
import cn.shadow.framework.beans.config.MyBeanDefinition;
import cn.shadow.framework.beans.config.MyBeanPostProcessor;
import cn.shadow.framework.beans.support.MyBeanDefinitionReader;
import cn.shadow.framework.beans.support.MyDefaultListableBeanFactory;
import cn.shadow.framework.context.support.MyAbstractApplicationContext;
import cn.shadow.framework.core.MyBeanFactory;

/**
 * 按照代码分析套路 IOC>DI>MVC>AOP
 * @author notto
 *
 */
public class MyApplicationContext extends MyDefaultListableBeanFactory implements MyBeanFactory{
	private String[]configLocayions;
	private MyBeanDefinitionReader reader;
	//单例模式的IOC容器缓存
	private Map<String,Object>singlettonObject=new ConcurrentHashMap<String, Object>();
	//所有的容器：通用IOC
	private Map<String,MyBeanWrapper> factoryBeanInstanceCache=new ConcurrentHashMap<String, MyBeanWrapper>();
	
	public MyApplicationContext(String... configLocayions) {
		this.configLocayions=configLocayions;
		refresh();
	}
	public Object getBean(Class<?> beanClass) throws Exception{
		return getBean(beanClass.getName());
		
	}
	public String[] getBeanDefinitionNames() {
		return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
	}
	public int getBeanDefinitionCount() {
		return this.beanDefinitionMap.keySet().toArray().length;
	}
	
	public Object getBean(String beanName) throws Exception {
		// TODO Auto-generated method stub
		//为防止循环依赖的情况将初始化和注入方法拆分。防止先有鸡还是先有蛋的问题
		//1:初始化
		MyBeanDefinition beanDefinition=this.beanDefinitionMap.get(beanName);
		MyBeanPostProcessor postProcessor=new MyBeanPostProcessor();
		Object instance=null;
		postProcessor.postProcessBeforeInitializetion(instance, beanName);
		instance=instantiateBean(beanName,beanDefinition);
		/*MyBeanDefinition beanDefinition=new MyBeanDefinition();
		beanDefinition.setFactoryBeanName(beanName);*/
		MyBeanWrapper beanWrapper=new MyBeanWrapper(instance); 
		
		//创建一个代理的策略,是通过jdk还是Cglib并返回实例
		/*
		 * MyAopProxy aopProxy; //通过代理实例返回被代理对象 Object proxy=aopProxy.getProxy(); proxy
		 */
		/* MyBeanWrapper beanWrapper=instantiateBean(beanName,beanDefinition); */
		//2：拿到beanwrapper之后，将其保存到IOC容器之后
		
		this.factoryBeanInstanceCache.put(beanName, beanWrapper);
		
		postProcessor.postProcessAfterInitializetion(instance, beanName);
		//3：注入
		populateBean(beanName,new MyBeanDefinition(),beanWrapper);
		
		
		return factoryBeanInstanceCache.get(beanName).getWappedInstance();
	}

	private void populateBean(String beanName,MyBeanDefinition beanDefinition,MyBeanWrapper beanWrapper) {
		// TODO Auto-generated method stub
		Object instance=beanWrapper.getWappedInstance();
		//判断只有加了注解的类才执行依赖注入
		Class<?> clazz=beanWrapper.getClass();
		if(!(clazz.isAnnotationPresent(MyController.class)||clazz.isAnnotationPresent(MyService.class))) {
			return;
		}
		//获得所有的fields
		Field[] fields= clazz.getDeclaredFields();
		for(Field field:fields) {
			if(!field.isAnnotationPresent(MyAutowired.class)) {
				continue;
			}
			MyAutowired autowired=field.getAnnotation(MyAutowired.class);
			String autowiredBeanName=autowired.value().trim();
			if("".equals(autowiredBeanName)) {
				//没有设置的情况下使用类型注入
				autowiredBeanName=field.getType().getName();
			}
			//强制访问
			field.setAccessible(true);
			try {
				//get这里是null
				if(instance==null||this.factoryBeanInstanceCache.get(autowiredBeanName)==null) {
					continue;
				}
				field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWappedInstance());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Object instantiateBean(String beanName,MyBeanDefinition beanDefinition) {
		//拿到要实例化的对象的类名
		String className=beanDefinition.getBeanClassName();
		//对类名进行反射实例化
		Object instance=null;
		try {
			
			//默认他就是单例模式
			if(this.singlettonObject.containsKey(className)) {
				instance=this.singlettonObject.get(className);
			}else {
				Class<?> clazz =Class.forName(className);
				instance=clazz.newInstance();
				MyAdvisedSupport config=instantionAopConfig(beanDefinition);
				config.setTargetClass(clazz);
				config.setTarget(instance);
				
				//如果符合PointCut规则则表明是代理
				if(config.pointCut()) {
					instance=createProxy(config).getProxy();
				}
				
				//根据名字
				this.singlettonObject.put(className, instance);
				//根据类型
				this.singlettonObject.put(beanDefinition.getFactoryBeanName(), instance);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//得到对象 将对象封装到beanwrapper中 把备案wrapper存入ioc容器之中
		/* MyBeanWrapper beanWrapper=new MyBeanWrapper(instance); */
		// TODO Auto-generat ed method stub
		return instance;
		
	}

	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		//初始化从这里开始
		/*1：定位配置文件
		 * 2：加载配置文件，配置相关的累。把他们封装成BeanDedinition
		 * 3：注册，把配置信息放到容器内（伪IOC）
		 * 4：把不是延时加载的类提前初始化
		 */
		//1
		reader=new MyBeanDefinitionReader(this.configLocayions);
		//2
		List<MyBeanDefinition> beanDefinitions=reader.loadBeanDefinitions();
		//3
	
		doRegisterBeanDefinition(beanDefinitions);
		
		//4
		doAutoWrited();
	}

	//只处理非延时加载的情况
	private void doAutoWrited() {
		// TODO Auto-generated method stub
		for(Map.Entry<String , MyBeanDefinition>beanDefinitionRntry:super.beanDefinitionMap.entrySet()) {
			String beanName=beanDefinitionRntry.getKey();
			if(beanDefinitionRntry.getValue().isLazyInit()) {
				try {
					getBean(beanName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		/*super.BeanDefinitionMap.entrySet()*/
	}

	private void doRegisterBeanDefinition(List<MyBeanDefinition> beanDefinitions) {
		// TODO Auto-generated method stub
		for(MyBeanDefinition beanDefinition:beanDefinitions) {
			if(super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
				System.out.println("The “" + beanDefinition.getFactoryBeanName() + "” is exists!!");
			}
			super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
		}
	}
	private MyAopProxy createProxy(MyAdvisedSupport config) {
		Class targetClass=config.getTargetClass();
		if(targetClass.getInterfaces().length>0) {
			return new JdkDynamicAopProxy(config);
		}
		
		return new CglibAopProxy(config);
	}
	
	private MyAdvisedSupport instantionAopConfig(MyBeanDefinition beanDefinition) {
		// TODO Auto-generated method stub
		
		MyAopConfig config=new MyAopConfig();
		config.setPointCut(this.reader.getConfig().getProperty("pointCut"));
		config.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
		config.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
		config.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
		config.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
		config.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));
		return new MyAdvisedSupport(config);
	}
	public Properties getConfig() {
		return this.reader.getConfig();
	}
}
