package cn.shadow.user.aspect;


import java.util.Arrays;

import cn.shadow.framework.aop.aspect.MyJoinPoint;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class LogAspect {
	
	public void before(MyJoinPoint joinPoint) {
		//往对象里记录调用开始时间
		joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(),System.currentTimeMillis());
        //这个方法中的逻辑，是由我们自己写的
        log.info("Invoker Before Method!!!" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
	}
	
	public void after(MyJoinPoint joinPoint) {
		//往对象里记录调用结束时间
		log.info("Invoker After Method!!!" +
                "\nTargetObject:" +  joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.println("use time :" + (endTime - startTime));
	}
	
	public void afterThrowing(MyJoinPoint joinPoint, Throwable ex) {
		 log.info("出现异常" +
	                "\nTargetObject:" +  joinPoint.getThis() +
	                "\nArgs:" + Arrays.toString(joinPoint.getArguments()) +
	                "\nThrows:" + ex.getMessage());
	}
}
