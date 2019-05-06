package cn.shadow.framework.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerAdapter {
	public boolean support(Object handler) {
		return (handler instanceof HandlerMapping);
	}
	public MyModleAndView handle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
		return null;
	}
}
