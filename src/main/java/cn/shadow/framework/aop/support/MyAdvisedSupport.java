package cn.shadow.framework.aop.support;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

import cn.shadow.framework.aop.config.MyAopConfig;

public class MyAdvisedSupport {
	
	private Class<?> targetClass;
	private Object target;
	private MyAopConfig config;
	private Pattern pointCutClassPattern;
	
	public MyAdvisedSupport(MyAopConfig config) {
		// TODO Auto-generated constructor stub
		this.config=config;
	}
	//获取执行链。以method作为key值
	public List<Object>getInterceptorsAndDynamicInterceptionAdvice(Method method,Class<?>targetClass){
		
		return null;
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
		
		String pointCutForClass=poingCut.substring(0,poingCut.lastIndexOf("\\(")-4);
	}
	
}
