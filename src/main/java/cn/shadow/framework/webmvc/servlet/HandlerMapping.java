package cn.shadow.framework.webmvc.servlet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.shadow.framework.annotation.MyRequestParam;


public class HandlerMapping {
	/*
	 * //必须将俩放到mapping中 private Pattern url; private Method method; private Object
	 * controller; private Class<?>[] paramTypes; //还可以保存形参列表 public
	 * Map<String,Integer>paramIndexMapping; public HandlerMapping(Pattern url,
	 * Method method, Object controller) { super(); this.url = url; this.method =
	 * method; this.controller = controller; paramTypes=method.getParameterTypes();
	 * paramIndexMapping=new HashMap<String,Integer>();
	 * putParamIndexMapping(method); } private void putParamIndexMapping(Method
	 * method) { // TODO Auto-generated method stub
	 * Annotation[][]pa=method.getParameterAnnotations(); for(int
	 * i=0;i<pa.length;i++) { for(Annotation a:pa[i]) { if(a instanceof
	 * MyRequestParam) { String paramName=((MyRequestParam)a).value();
	 * if(!"".equals(paramName.trim())) { paramIndexMapping.put(paramName, i); }
	 * 
	 * } } } Class<?>[] paramTypes=method.getParameterTypes(); for(int
	 * i=0;i<paramTypes.length;i++) { Class<?> type=paramTypes[i];
	 * if(type==HttpServletRequest.class||type==HttpServletResponse.class) { } } }
	 * 
	 * public Pattern getUrl() { return url; } public void setUrl(Pattern url) {
	 * this.url = url; } public Method getMethod() { return method; } public void
	 * setMethod(Method method) { this.method = method; } public Object
	 * getController() { return controller; } public void setController(Object
	 * controller) { this.controller = controller; } public Map<String, Integer>
	 * getParamIndexMapping() { return paramIndexMapping; } public void
	 * setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
	 * this.paramIndexMapping = paramIndexMapping; } public Class<?>[]
	 * getParamTypes() { return paramTypes; } public void setParamTypes(Class<?>[]
	 * paramTypes) { this.paramTypes = paramTypes; }
	 */
	
	protected Object controller;
	protected Method method;
	protected Pattern pattern;
	protected Map<String,Integer>paramIndexMapping;
	public HandlerMapping(Object controller, Method method, Pattern pattern) {
		this.controller = controller;
		this.method = method;
		this.pattern = pattern;
	}
	public Object getController() {
		return controller;
	}
	public void setController(Object controller) {
		this.controller = controller;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Pattern getPattern() {
		return pattern;
	}
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	public Map<String, Integer> getParamIndexMapping() {
		return paramIndexMapping;
	}
	public void setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
		this.paramIndexMapping = paramIndexMapping;
	}
}
