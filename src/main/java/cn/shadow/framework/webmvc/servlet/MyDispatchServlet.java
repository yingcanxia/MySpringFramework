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
		//第一步：getHandler
		try {
			HandlerMapping handler =getHandler(req);
			if(handler==null) {
				
				//new ModleAndView("404")
				return;
			}
			
			//第二部：getAdapter
			//准备好调用前的参数
			HandlerAdapter ha=getHandlerAdapter(handler);
			//第三部：处理结果,并真正的调用方法
			//存储要传到页面上的值，和具体要走那个页面
			MyModleAndView mv=ha.handle(req,resp,handler);
			
			
			processDispatchResult(req,resp,mv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, MyModleAndView mv) {
		// TODO Auto-generated method stub
		//把ModleAndView转化成需要的格式例如jsp，html，json
		
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
		//这里会有一定区别
		//初始化applicationcontext
		context=new MyApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
		//初始化九大组件
		initStrategies(context);
	}
	protected void initStrategies(MyApplicationContext context) {
		
		
		//初始化9大组件
		//多文件上传组件
		initMultipartResolver(context);
		//初始化本地语言和环境
		initLocaleResolver(context);
		//初始化模板处理器
		initThemeResolver(context);
		//初始化handlerMapping
		initHandlerMappings(context);
		//初始化参数适配器
		initHandlerAdapters(context);
		//初始化异常拦截器
		initHandlerExceptionResolvers(context);
		//初始化视图预处理器
		initRequestToViewNameTranslator(context);
		//初始化视图转换器
		initViewResolvers(context);
		//初始化参数缓存器
		initFlashMapManager(context);
	}

	private void initFlashMapManager(MyApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	private void initViewResolvers(MyApplicationContext context) {
		// TODO Auto-generated method stub
		//拿到一个模板存放目录 
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
		//建议个请求转换成handlerMapping所有参数都是字符串，将字符串配置到handler中的各个字段中去
		//他需要拿到handlerMapping才能干活。有几个mapping就有几个Adapter，需要进行迭代
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
			//遍历list
			Matcher match=mapping.getPattern().matcher(url);
			if(!match.matches()) {continue;}
			return mapping;
			
		}
		
		
		
		
		return null;
		
	}


	

}
