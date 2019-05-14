package cn.shadow.framework.aop.support;

import java.lang.reflect.Method;
import java.util.List;

public class MyAdvisedSupport {
	
	private Class<?> targetClass;
	public Class<?> getTargetClass(){
		return this.targetClass;
	}
	public Object getTarget() {
		
		return null;
	}
	
	//获取执行链。以method作为key值
	public List<Object>getInterceptorsAndDynamicInterceptionAdvice(Method method,Class<?>targetClass){
		
		return null;
	}
}
