package cn.shadow.framework.aop.aspect;

import java.lang.reflect.Method;

public interface MyJoinPoint {

	Object getThis();
	
	Object[] getArguments();
	
	Method getMethod();
	
	void setUserAttribute(String key,Object obj);
	
	Object getUserAttribute(String key);
}
