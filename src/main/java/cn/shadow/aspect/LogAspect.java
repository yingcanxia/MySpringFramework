package cn.shadow.aspect;

import cn.shadow.framework.aop.aspect.MyJoinPoint;

public class LogAspect {
	
	public void before() {
		//往对象里记录调用开始时间
	}
	
	public void after() {
		//往对象里记录调用结束时间
	}
	
	public void afterThrowing() {
		
	}
}
