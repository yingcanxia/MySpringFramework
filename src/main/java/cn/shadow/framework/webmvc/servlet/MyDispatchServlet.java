package cn.shadow.framework.webmvc.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.bcel.internal.classfile.Field;

import cn.shadow.framework.annotation.MyController;
import cn.shadow.framework.annotation.MyRequestMapping;
import cn.shadow.framework.context.MyApplicationContext;
import jdk.nashorn.internal.runtime.logging.Logger;


public class MyDispatchServlet extends HttpServlet{
	private final String CONTEXT_CONFIG_LOCATION="contextConfigLocation";
	private MyApplicationContext context;
	private List<HandlerMapping>handlerMappings=new ArrayList<HandlerMapping>();
	private Map<HandlerMapping,HandlerAdapter>handlerAdapters=new HashMap<HandlerMapping, HandlerAdapter>();
	private List<MyViewresolvers>viewresolvers;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doDispatcher(req,resp);
		
		
	}

	private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		//��һ����getHandler
		try {
			HandlerMapping handler =getHandler(req);
			if(handler==null) {
				
				//new ModleAndView("404")
				return;
			}
			
			//�ڶ�����getAdapter
			//׼���õ���ǰ�Ĳ���
			HandlerAdapter ha=getHandlerAdapter(handler);
			//��������������,�������ĵ��÷���
			//�洢Ҫ����ҳ���ϵ�ֵ���;���Ҫ���Ǹ�ҳ��
			MyModleAndView mv=ha.handle(req,resp,handler);
			
			
			processDispatchResult(req,resp,mv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, MyModleAndView mv) {
		// TODO Auto-generated method stub
		//��ModleAndViewת������Ҫ�ĸ�ʽ����jsp��html��json
		
		if(null==mv){
			return;
		}
	}

	private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
		// TODO Auto-generated method stub
		if(this.handlerAdapters.isEmpty()) {
			return null;
		}
		HandlerAdapter ha=this.handlerAdapters.get(handler);
		if(ha.support(handler)) {
			return ha;
		}
		
		return null;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		//�������һ������
		//��ʼ��applicationcontext
		context=new MyApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
		//��ʼ���Ŵ����
		initStrategies(context);
	}
	protected void initStrategies(MyApplicationContext context) {
		
		
		//��ʼ��9�����
		//���ļ��ϴ����
		initMultipartResolver(context);
		//��ʼ���������Ժͻ���
		initLocaleResolver(context);
		//��ʼ��ģ�崦����
		initThemeResolver(context);
		//��ʼ��handlerMapping
		initHandlerMappings(context);
		//��ʼ������������
		initHandlerAdapters(context);
		//��ʼ���쳣������
		initHandlerExceptionResolvers(context);
		//��ʼ����ͼԤ������
		initRequestToViewNameTranslator(context);
		//��ʼ����ͼת����
		initViewResolvers(context);
		//��ʼ������������
		initFlashMapManager(context);
	}

	private void initFlashMapManager(MyApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	private void initViewResolvers(MyApplicationContext context) {
		// TODO Auto-generated method stub
		//�õ�һ��ģ����Ŀ¼ 
		String templateRoot=context.getConfig().getProperty("");
		String templateRootPath=this.getClass().getClassLoader().getResource(templateRoot).getFile();
		File templateRootDir=new File(templateRootPath);
		for (File file : templateRootDir.listFiles()) {
			//file
			this.viewresolvers.add(new MyViewresolvers(templateRoot));
		}
		
	}

	private void initRequestToViewNameTranslator(MyApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	private void initHandlerExceptionResolvers(MyApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	private void initHandlerAdapters(MyApplicationContext context) {
		// TODO Auto-generated method stub
		//���������ת����handlerMapping���в��������ַ��������ַ������õ�handler�еĸ����ֶ���ȥ
		//����Ҫ�õ�handlerMapping���ܸɻ�м���mapping���м���Adapter����Ҫ���е���
		for (HandlerMapping handlerMapping : handlerMappings) {
			this.handlerAdapters.put(handlerMapping, new HandlerAdapter());
		}
	}

	private void initHandlerMappings(MyApplicationContext context) {
		// TODO Auto-generated method stub
		String[]beanNames=context.getBeanDefinitionNames();
		try {
			for (String beanName : beanNames) {
				Object bean=context.getBean(beanName);
				Class<?>clazz=bean.getClass();
				if(!clazz.isAnnotationPresent(MyController.class)) {
					continue;
				}
				String baseUrl="";
				if(clazz.isAnnotationPresent(MyRequestMapping.class)) {
					MyRequestMapping requestMapping=clazz.getAnnotation(MyRequestMapping.class);
					baseUrl=requestMapping.value();
				}
				for (Method method : clazz.getMethods()) {
					if(!method.isAnnotationPresent(MyRequestMapping.class)) {
						continue;
					}
					MyRequestMapping requestMapping=method.getAnnotation(MyRequestMapping.class);
					String url=("/"+baseUrl+"/"+requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
					Pattern pattern=Pattern.compile(url);
					
					this.handlerMappings.add(new HandlerMapping(bean, method,pattern));
					//handlerMappings.put(url, method);
					System.out.println(url+","+method.getName());
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void initThemeResolver(MyApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	private void initLocaleResolver(MyApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	private void initMultipartResolver(MyApplicationContext context) {
		// TODO Auto-generated method stub
		
	}
	
	
	private HandlerMapping getHandler(HttpServletRequest req)throws Exception {
		if(this.handlerMappings.isEmpty()) {
			return null;
		}
		String url =req.getRequestURI();
		String contextPath=req.getContextPath();
		url=url.replace(contextPath, "").replaceAll("/+", "/");
		for(HandlerMapping mapping:this.handlerMappings) {
			//����list
			Matcher match=mapping.getPattern().matcher(url);
			if(!match.matches()) {continue;}
			return mapping;
			
		}
		
		
		
		
		return null;
		
	}


	

}
