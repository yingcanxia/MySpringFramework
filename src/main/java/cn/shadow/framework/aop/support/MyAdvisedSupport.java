package cn.shadow.framework.aop.support;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.shadow.framework.aop.aspect.MyMethodBeforeAdviceInterceptor;
import cn.shadow.framework.aop.config.MyAopConfig;
import cn.shadow.framework.aop.intercept.MyMethodInterceptor;

public class MyAdvisedSupport {
	
	private Class<?> targetClass;
	private Object target;
	private MyAopConfig config;
	private Pattern pointCutClassPattern;
	
	public MyAdvisedSupport(MyAopConfig config) {
		// TODO Auto-generated constructor stub
		this.config=config;
	}
	//��ȡִ��������method��Ϊkeyֵ
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
		/* pointCut=public .* cn.shadow.service..*service..*(.*) */
		String pointCutForClassRegex=poingCut.substring(0,poingCut.lastIndexOf("\\(")-4);
		pointCutClassPattern=Pattern.compile("class"+pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ")+1));
		Pattern pattern=Pattern.compile(poingCut);
		for(Method method:this.targetClass.getMethods()) {
			String strMethod=method.toString();
			if(strMethod.contains("throws")) {
				strMethod=strMethod.substring(0,strMethod.lastIndexOf("throws")).trim();
			}
			Matcher matcher=pattern.matcher(strMethod);
			if(matcher.matches()) {
				//ִ������
				List<Object>advices=new LinkedList<Object>();
				
				//��ÿһ��������װ��methodIterceptor
				//1.before
				if(null!=config.getAspectBefore()&&!"".equals(config.getAspectBefore())) {
					//����һ��advise����
					advices.add(new MyMethodBeforeAdviceInterceptor());
				}
				//2.after
				if(null!=config.getAspectAfter()&&!"".equals(config.getAspectAfter())) {
					//����һ��advise����
					//advices.add(new MyMethodInterceptor())
				}
				//3.afterThrowing 
				if(null!=config.getAspectAfterThrow()&&!"".equals(config.getAspectAfterThrow())) {
					//����һ��advise����
					//advices.add(new MyMethodInterceptor())
				}
			}
		}
	}
	
	
}
