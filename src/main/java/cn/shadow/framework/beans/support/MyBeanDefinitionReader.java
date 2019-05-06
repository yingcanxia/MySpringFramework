package cn.shadow.framework.beans.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cn.shadow.framework.beans.config.MyBeanDefinition;

public class MyBeanDefinitionReader {
	private Properties config=new Properties();
	private List<String>registerBeanClasses=new ArrayList<String>();
	//�̶������ļ��е�key�൱��xml
	private final String  SACN_PACKAGE="scanPackage";
	public MyBeanDefinitionReader(String... localtions) {
		//ͨ��url��λ�ҵ�������Ӧ·�����ļ�Ȼ��ת��Ϊ�ļ���
		InputStream is=this.getClass().getClassLoader().getResourceAsStream(localtions[0].replace("classpath:", ""));
		try {
			config.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(null!=is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		doScanner(config.getProperty(SACN_PACKAGE));
	}
	private void doScanner(String property) {
		// TODO Auto-generated method stub
		//��������ļ��еļ�ֵ��
		URL url=this.getClass().getClassLoader().getResource("/"+property.replaceAll("\\.", "/"));
		File classPath=new File(url.getFile());
		for (File file : classPath.listFiles()) {
			if(file.isDirectory()) {
				doScanner(property+"."+file.getName());
			}else {
				if(!file.getName().endsWith(".class")) {continue;}
				String className=property+"."+file.getName().replace(".class", "");	
				//��������
				registerBeanClasses.add(className);
			} 
		}
	}
	public Properties getConfig() {
		return this.config;
	}
	
	//�������ļ�������ɨ�赽��������Ϣת����MyBeanDefinition�����Ա���֮��IOC��������
	public List<MyBeanDefinition> loadBeanDefinitions() {
		//������н����������װ�����
		List<MyBeanDefinition>result=new ArrayList<MyBeanDefinition>();
		try {
			for(String className:registerBeanClasses) {
				Class<?>beanClass=Class.forName(className);
				//�����һ���ӿ����ܱ�ʵ����
				//����ʵ������ʵ��ʵ����
				if(beanClass.isInterface()) {
					continue;
				}
				result.add(doCreateBeanDefinition(className));
				Class<?>[]interfaces=beanClass.getInterfaces();
				for(Class<?>i:interfaces) {
					result.add(doCreateBeanDefinition(i.getName()));
				}
				
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
		
	}
	//��ÿһ��������Ϣ������һ��BeanDefinition
	private MyBeanDefinition doCreateBeanDefinition(String className) {
		
		
		try {
			Class<?>beanClass=Class.forName(className);
			if(beanClass.isInterface()) {
				return null;
			}
			MyBeanDefinition beanDefinition=new MyBeanDefinition();
			beanDefinition.setBeanClassName(className);
			beanDefinition.setFactoryBeanName(toLowerFirstCase(beanClass.getSimpleName()));
			return  beanDefinition;
			//�п����ǽӿڣ�����ʵ������д�������ʵ������ΪBeanClassName
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String  toLowerFirstCase(String simpleName) {
		char[] chars=simpleName.toCharArray();
		chars[0]+=32;
		return String.valueOf(chars);
	}
}
