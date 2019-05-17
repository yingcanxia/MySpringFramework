package cn.shadow.framework.webmvc.servlet;

import java.io.File;
import java.util.Locale;

public class MyViewResolvers {

	private final String DEFAULT_TEMPLATE_SUFIX=".html";
	private File templateRootDir;
	//private String viewName;
	
	public MyViewResolvers(String templateRoot) {
		// TODO Auto-generated constructor stub
		String templateRootPath=this.getClass().getClassLoader().getResource(templateRoot).getFile();
		this.templateRootDir=new File(templateRootPath);
	}
	public MyView resolveViewName(String viewName,Locale locale) {
		if(null==viewName||"".equals(viewName.trim())) {
			return null;
		}
		viewName=viewName.endsWith(DEFAULT_TEMPLATE_SUFIX)? viewName:(viewName + DEFAULT_TEMPLATE_SUFIX);
		File templateFile=new File((templateRootDir.getPath()+"/"+viewName).replaceAll("/+", "/"));
		return new MyView(templateFile);
	}
	public File getTemplateRootDir() {
		return templateRootDir;
	}
	public void setTemplateRootDir(File templateRootDir) {
		this.templateRootDir = templateRootDir;
	}

	
	
}
