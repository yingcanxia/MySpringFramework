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
import cn.shadow.framework.beans.MyBeanFactory;
import cn.shadow.framework.beans.MyBeanWrapper;
import cn.shadow.framework.beans.config.MyBeanDefinition;
import cn.shadow.framework.beans.config.MyBeanPostProcessor;
import cn.shadow.framework.beans.support.MyBeanDefinitionReader;
import cn.shadow.framework.beans.support.MyDefaultListableBeanFactory;
import cn.shadow.framework.context.support.MyAbstractApplicationContext;

/**
 * ���մ��������· IOC>DI>MVC>AOP
 * @author notto
 *
 */
public class MyApplicationContext extends MyDefaultListableBeanFactory implements MyBeanFactory{
	private String[]configLocayions;
	private MyBeanDefinitionReader reader;
	//����ģʽ��IOC��������
	private Map<String,Object>singlettonObject=new ConcurrentHashMap<String, Object>();
	//���е�������ͨ��IOC
	private Map<String,MyBeanWrapper> factoryBeanInstanceCache=new ConcurrentHashMap<String, MyBeanWrapper>();
	
	public MyApplicationContext(String... configLocayions) {
		this.configLocayions=configLocayions;
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
		//Ϊ��ֹѭ���������������ʼ����ע�뷽����֡���ֹ���м��������е�������
		//1:��ʼ��
		MyBeanDefinition beanDefinition=this.beanDefinitionMap.get(beanName);
		MyBeanPostProcessor postProcessor=new MyBeanPostProcessor();
		Object instance=null;
		instance=instantiateBean(beanName,beanDefinition);
		postProcessor.postProcessBeforeInitializetion(instance, beanName);
		/*MyBeanDefinition beanDefinition=new MyBeanDefinition();
		beanDefinition.setFactoryBeanName(beanName);*/
		MyBeanWrapper beanWrapper=new MyBeanWrapper(instance); 
		
		//����һ������Ĳ���,��ͨ��jdk����Cglib������ʵ��
		/*
		 * MyAopProxy aopProxy; //ͨ������ʵ�����ر�������� Object proxy=aopProxy.getProxy(); proxy
		 */
		/* MyBeanWrapper beanWrapper=instantiateBean(beanName,beanDefinition); */
		//2���õ�beanwrapper֮�󣬽��䱣�浽IOC����֮��
		
		this.factoryBeanInstanceCache.put(beanName, beanWrapper);
		
		postProcessor.postProcessAfterInitializetion(instance, beanName);
		//3��ע��
		populateBean(beanName,new MyBeanDefinition(),beanWrapper);
		
		
		return factoryBeanInstanceCache.get(beanName).getWappedInstance();
	}

	private void populateBean(String beanName,MyBeanDefinition beanDefinition,MyBeanWrapper beanWrapper) {
		// TODO Auto-generated method stub
		Object instance=beanWrapper.getWappedInstance();
		//�ж�ֻ�м���ע������ִ������ע��
		Class<?> clazz=beanWrapper.getClass();
		if(!(clazz.isAnnotationPresent(MyController.class)||clazz.isAnnotationPresent(MyService.class))) {
			return;
		}
		//������е�fields
		Field[] fields= clazz.getDeclaredFields();
		for(Field field:fields) {
			if(!field.isAnnotationPresent(MyAutowired.class)) {
				continue;
			}
			MyAutowired autowired=field.getAnnotation(MyAutowired.class);
			String autowiredBeanName=autowired.value().trim();
			if("".equals(autowiredBeanName)) {
				//û�����õ������ʹ������ע��
				autowiredBeanName=field.getType().getName();
			}
			//ǿ�Ʒ���
			field.setAccessible(true);
			try {
				//get������null
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
		//�õ�Ҫʵ�����Ķ��������
		String className=beanDefinition.getBeanClassName();
		//���������з���ʵ����
		Object instance=null;
		try {
			
			//Ĭ�������ǵ���ģʽ
			if(this.singlettonObject.containsKey(className)) {
				instance=this.singlettonObject.get(className);
			}else {
				Class<?> clazz =Class.forName(className);
				instance=clazz.newInstance();
				MyAdvisedSupport config=instantionAopConfig(beanDefinition);
				config.setTargetClass(clazz);
				config.setTarget(instance);
				
				//�������PointCut����������Ǵ���
				if(config.pointCut()) {
					instance=createProxy(config).getProxy();
				}
				
				//��������
				this.singlettonObject.put(className, instance);
				//��������
				this.singlettonObject.put(beanDefinition.getFactoryBeanName(), instance);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//�õ����� �������װ��beanwrapper�� �ѱ���wrapper����ioc����֮��
		/* MyBeanWrapper beanWrapper=new MyBeanWrapper(instance); */
		// TODO Auto-generat ed method stub
		return instance;
		
	}

	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		//��ʼ�������￪ʼ
		/*1����λ�����ļ�
		 * 2�����������ļ���������ص��ۡ������Ƿ�װ��BeanDedinition
		 * 3��ע�ᣬ��������Ϣ�ŵ������ڣ�αIOC��
		 * 4���Ѳ�����ʱ���ص�����ǰ��ʼ��
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

	//ֻ�������ʱ���ص����
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
