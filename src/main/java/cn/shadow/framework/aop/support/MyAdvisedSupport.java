package cn.shadow.framework.aop.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.shadow.framework.aop.aspect.MyAfterReturningAdviceInterceptor;
import cn.shadow.framework.aop.aspect.MyAfterThrowingAdviceInterceptor;
import cn.shadow.framework.aop.aspect.MyMethodBeforeAdviceInterceptor;
import cn.shadow.framework.aop.config.MyAopConfig;
import cn.shadow.framework.aop.intercept.MyMethodInterceptor;

public class MyAdvisedSupport {
	
	private Class<?> targetClass;
	private Object target;
	private MyAopConfig config;
	private Pattern pointCutClassPattern;
	private Map<Method,List<Object>>methodCache;
	public MyAdvisedSupport(MyAopConfig config) {
		// TODO Auto-generated constructor stub
		this.config=config;
	}
	//获取执行链。以method作为key值
	public List<Object>getInterceptorsAndDynamicInterceptionAdvice(Method method,Class<?>targetClass) throws Exception{
		List<Object>cached=methodCache.get(method);
		if(cached==null) {
			Method m=targetClass.getMethod(method.getName(), method.getParameterTypes());
			cached.add(m);
			this.methodCache.put(m,cached);
		}
		return cached;
	}
	
	public boolean pointCut() {
		
		return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
	}
	
	public Class<?> getTargetClass(){
		return this.targetClass;
	}
	
	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public Object getTarget() {
		
		return this.target;
	}
	
	public void setTarget(Object target) {
		this.target = target;
	}
	private void prase() {
		String poingCut=config.getPointCut()
				.replaceAll("\\.", "\\\\.")
				.replaceAll("\\\\.\\*", ".*")
				.replaceAll("\\(", "\\\\(")
				.replaceAll("\\)", "\\\\)");
		/* pointCut=public .* cn.shadow.service..*service..*(.*) */
		String pointCutForClassRegex=poingCut.substring(0,poingCut.lastIndexOf("\\(")-4);
		pointCutClassPattern=Pattern.compile("class"+pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ")+1));
		Pattern pattern=Pattern.compile(poingCut);
		
		methodCache=new HashMap<Method, List<Object>>();
		try {
			Class aspectClass = Class.forName(this.config.getAspectClass());
			Map<String, Method>aspectMethods=new HashMap<String, Method>();
			for (Method method:aspectClass.getMethods()) {
				aspectMethods.put(method.getName(), method);
			}
			for(Method method:this.targetClass.getMethods()) {
				String strMethod=method.toString();
				if(strMethod.contains("throws")) {
					strMethod=strMethod.substring(0,strMethod.lastIndexOf("throws")).trim();
				}
				Matcher matcher=pattern.matcher(strMethod);
				if(matcher.matches()) {
					//执行器链
					List<Object>advices=new LinkedList<Object>();
					
					//把每一个方法包装成methodIterceptor
					//1.before
					if(null!=config.getAspectBefore()&&!"".equals(config.getAspectBefore())) {
						//创建一个advise对象
						//有前置通知变成了前置拦截器
						advices.add(new MyMethodBeforeAdviceInterceptor(aspectMethods.get(config.getAspectBefore()),aspectClass.newInstance()));
					}
					//2.after
					if(null!=config.getAspectAfter()&&!"".equals(config.getAspectAfter())) {
						//创建一个advise对象
						advices.add(new MyAfterReturningAdviceInterceptor(aspectMethods.get(config.getAspectAfter()),aspectClass.newInstance()));
					}
					//3.afterThrowing 
					if(null!=config.getAspectAfterThrow()&&!"".equals(config.getAspectAfterThrow())) {
						//创建一个advise对象
						MyAfterThrowingAdviceInterceptor throwingAdviceInterceptor=
								new MyAfterThrowingAdviceInterceptor(aspectMethods.get(config.getAspectAfterThrow()),aspectClass.newInstance());
						throwingAdviceInterceptor.setThrowingName(config.getAspectAfterThrowingName());
						advices.add(throwingAdviceInterceptor);
					}
					methodCache.put(method,advices);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
}
