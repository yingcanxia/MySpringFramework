package cn.shadow.framework.beans;

public class MyBeanWrapper {
	
	private Object wrappedInstance;
	private Class<?> wrappedClass;
	
	public MyBeanWrapper(Object wrappedInstance) {
		this.wrappedInstance=wrappedInstance;
	}
	
	//����ǵ���ģʽ��ֱ��get����
	public Object getWappedInstance() {
		return this.wrappedInstance;
	}
	//������ǵ������ӱ�������new�������
	public Class<?> getWrappedClass() {
		return this.wrappedClass;
	}
}
