package cn.shadow.framework.webmvc.servlet;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.shadow.framework.annotation.MyRequestParam;

public class HandlerAdapter {
	public boolean support(Object handler) {
		return (handler instanceof HandlerMapping);
	}
	public MyModleAndView handle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
		HandlerMapping handlerMapping=(HandlerMapping) handler;
		
		//将方法的形式参数和request的实际参数进行一一对应
		Map<String,Integer> paramIndexMapping=new HashMap<String, Integer>();
		//提取方法中添加了注解的参数
		//拿到方法上的注解，得到的是一个二维数组
		//因为一个参数可以有多个注解，而一个方法又可以有多个参数
		Annotation[][]pa=handlerMapping.getMethod().getParameterAnnotations();
		for(int i=0;i<pa.length;i++) {
			for(Annotation a:pa[i]) {
				if(a instanceof MyRequestParam) {
					String paramName=((MyRequestParam)a).value();
					if(!"".equals(paramName.trim())) {
						paramIndexMapping.put(paramName, i);
					}
					
				}
			}
		}
		Class<?>[] paramTypes=handlerMapping.getMethod().getParameterTypes();
		for(int i=0;i<paramTypes.length;i++) {
			Class<?> type=paramTypes[i];
			if(type==HttpServletRequest.class||type==HttpServletResponse.class) {
				paramIndexMapping.put(type.getName(), i);
			}
		}
		
		//对象列表
		//获得方法的形参列表
		Map<String,String[]>params=request.getParameterMap();
		//实参列表
		Object[] paramValues=new Object[paramTypes.length];
		//http协议是基于字符串的url？para=aaa,键值对其中key值会有多个值所以这里是数组
		for (Map.Entry<String, String[]> parm : params.entrySet()) {
			//将数组化解
			String value =Arrays.toString(parm.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", ",");
			if(!paramIndexMapping.containsKey(parm.getKey())) {
				//如果没有对应的key值这跳出
				continue;
			}
			//依照方法的参数类型列表将值春芳在列表
			int index=paramIndexMapping.get(parm.getKey());
			paramValues[index]=caseStringValue(paramTypes[index], value);
		}
		//调用找到的方法去执行
		if(paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
			int reqIndex=paramIndexMapping.get(HttpServletRequest.class.getName());
			paramValues[reqIndex]=request;
		}
		if(paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
			int respIndex=paramIndexMapping.get(HttpServletResponse.class.getName());
			paramValues[respIndex]=response;
		}
		
		Object result=handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
		//当返回值是空或者方法是void的时候
		if(result==null||result instanceof Void) {
			return null;
			//正常返回
		}
		boolean isModelAndView = handlerMapping.getMethod().getReturnType() == MyModleAndView.class;

		if(isModelAndView) {
			return (MyModleAndView) result;
		}
		
		return null;
	}
	private Object caseStringValue(Class<?> paramsType, String value) {
		// TODO Auto-generated method stub
		//类型转化
		if(String.class == paramsType){
			return value;
		}
		//如果是int
		if(Integer.class == paramsType){
			return Integer.valueOf(value);
		}
		else if(Double.class == paramsType){
			return Double.valueOf(value);
		}else {
			if(value != null){
				return value;
			}
			return null;
		}
	}
	
}
