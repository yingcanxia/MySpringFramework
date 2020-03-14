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
	//固定配置文件中的key相当于xml
	private final String  SACN_PACKAGE="scanPackage";
	public MyBeanDefinitionReader(String... localtions) {
		//通过url定位找到其所对应路径的文件然后转换为文件流
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
		//获得配置文件中的键值对
		URL url=this.getClass().getClassLoader().getResource("/"+property.replaceAll("\\.", "/"));
		File classPath=new File(url.getFile());
		for (File file : classPath.listFiles()) {
			if(file.isDirectory()) {
				doScanner(property+"."+file.getName());
			}else {
				if(!file.getName().endsWith(".class")) {continue;}
				String className=property+"."+file.getName().replace(".class", "");	
				//整理类名
				registerBeanClasses.add(className);
			} 
		}
	}
	public Properties getConfig() {
		return this.config;
	}
	
	//把配置文件中所有扫描到的配置信息转化成MyBeanDefinition对象，以便于之后IOC操作方便
	public List<MyBeanDefinition> loadBeanDefinitions() {
		//对其进行解析在这里封装最合理
		List<MyBeanDefinition>result=new ArrayList<MyBeanDefinition>();
		try {
			for(String className:registerBeanClasses) {
				Class<?>beanClass=Class.forName(className);
				//如果是一个接口则不能被实例化
				//用它实现类来实现实例化
				if(beanClass.isInterface()) {
					continue;
				}
				result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()),beanClass.getName()));
				Class<?>[]interfaces=beanClass.getInterfaces();
				for(Class<?>i:interfaces) {
					result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
				}
				
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
		
	}
	//将每一个配置信息解析成一个BeanDefinition
	private MyBeanDefinition doCreateBeanDefinition(String className,String beanClassName) {
		MyBeanDefinition beanDefinition=new MyBeanDefinition();
		beanDefinition.setBeanClassName(beanClassName);
		beanDefinition.setFactoryBeanName(className);
		return  beanDefinition;
		//有可能是接口，对其实现类进行处理，用其实现类作为BeanClassName

	}
	private String  toLowerFirstCase(String simpleName) {
		char[] chars=simpleName.toCharArray();
		chars[0]+=32;
		return String.valueOf(chars);
	}
}
