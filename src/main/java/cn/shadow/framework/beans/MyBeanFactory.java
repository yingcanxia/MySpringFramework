package cn.shadow.framework.beans;
//���������Ķ������
public interface MyBeanFactory {
	//�������ǳ��ؼ�ͨ��Ψһ����ڻ�ȡ����
	//����ģʽ/��������˽�л����췽����
	
	Object getBean(String beanName) throws Exception;
	Object getBean(Class<?> beanClass) throws Exception;
}
