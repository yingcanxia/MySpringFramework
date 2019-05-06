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
		
		//����������ʽ������request��ʵ�ʲ�������һһ��Ӧ
		Map<String,Integer> paramIndexMapping=new HashMap<String, Integer>();
		//��ȡ������������ע��Ĳ���
		//�õ������ϵ�ע�⣬�õ�����һ����ά����
		//��Ϊһ�����������ж��ע�⣬��һ�������ֿ����ж������
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
		
		//�����б�
		//��÷������β��б�
		Map<String,String[]>params=request.getParameterMap();
		//ʵ���б�
		Object[] paramValues=new Object[paramTypes.length];
		//httpЭ���ǻ����ַ�����url��para=aaa,��ֵ������keyֵ���ж��ֵ��������������
		for (Map.Entry<String, String[]> parm : params.entrySet()) {
			//�����黯��
			String value =Arrays.toString(parm.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", ",");
			if(!handlerMapping.paramIndexMapping.containsKey(parm.getKey())) {
				//���û�ж�Ӧ��keyֵ������
				continue;
			}
			//���շ����Ĳ��������б���ֵ�������б�
			int index=handlerMapping.paramIndexMapping.get(parm.getKey());
			paramValues[index]=caseStringValue(paramTypes[index], value);
		}
		//�����ҵ��ķ���ȥִ��
		Object returnValue=handlerMapping.method.invoke(handlerMapping.controller, paramValues);
		//������ֵ�ǿջ��߷�����void��ʱ��
		if(returnValue==null||returnValue instanceof Void) {
			return null;
			//��������
		}
		//�����ǵ�ʱ����responseȥд�����
		//response.getWriter().write(returnValue.toString());
		return null;
	}
	private Object caseStringValue(Class<?> class1, String value) {
		// TODO Auto-generated method stub
		//����ת��
				if(Integer.class==class1) {
					return Integer.valueOf(value);
				}else if(String.class==class1) {
					value.replaceAll("\\[\\]", "").replaceAll("\\s", "");
					return value;
				}
				//����Ӧ��ʹ�ò���ģʽ
				return value;
	}
	
}