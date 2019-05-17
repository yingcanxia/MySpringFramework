package cn.shadow.user.aspect;

import cn.shadow.framework.aop.aspect.MyJoinPoint;

public class LogAspect {
	
	public void before() {
		//往对象里记录调用开始时间
		System.out.println("invoke method before");
	}
	
	public void after() {
		//往对象里记录调用结束时间
		System.out.println("invoke method after");
	}
	
	public void afterThrowing() {
		
	}
}
