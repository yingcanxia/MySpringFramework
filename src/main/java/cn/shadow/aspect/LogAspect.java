package cn.shadow.aspect;

import cn.shadow.framework.aop.aspect.MyJoinPoint;

public class LogAspect {
	
	public void before() {
		//���������¼���ÿ�ʼʱ��
		System.out.println("invoke method before");
	}
	
	public void after() {
		//���������¼���ý���ʱ��
		System.out.println("invoke method after");
	}
	
	public void afterThrowing() {
		
	}
}
