package cn.shadow.user.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import cn.shadow.framework.annotation.MyAutowired;
import cn.shadow.framework.annotation.MyController;
import cn.shadow.framework.annotation.MyRequestMapping;
import cn.shadow.framework.annotation.MyRequestParam;
import cn.shadow.framework.webmvc.servlet.MyModleAndView;
import cn.shadow.user.service.IModifyService;
import cn.shadow.user.service.IQueryService;

/**
 * 公布接口url
 * @author Tom
 *
 */
@MyController
@MyRequestMapping("/web")
public class MyAction {

	@MyAutowired IQueryService queryService;
	@MyAutowired IModifyService modifyService;

	@MyRequestMapping("/query.json")
	public MyModleAndView query(HttpServletRequest request, HttpServletResponse response,
								@MyRequestParam("name") String name){
		String result = modifyService.edit(1,"ssdf");
		return out(response,result);
	}
	
	@MyRequestMapping("/add*.json")
	public MyModleAndView add(HttpServletRequest request,HttpServletResponse response,
			@MyRequestParam("name") String name,@MyRequestParam("addr") String addr){
		String result = null;
		try {
			result = modifyService.add(name,addr);
			return out(response,result);
		} catch (Exception e) {
//			e.printStackTrace();
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("detail",e.getCause().getMessage());
//			System.out.println(Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
			model.put("stackTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
			return new MyModleAndView("500",model);
		}

	}
	
	@MyRequestMapping("/remove.json")
	public MyModleAndView remove(HttpServletRequest request,HttpServletResponse response,
			@MyRequestParam("id") Integer id){
		String result = modifyService.remove(id);
		return out(response,result);
	}
	
	@MyRequestMapping("/edit.json")
	public MyModleAndView edit(HttpServletRequest request,HttpServletResponse response,
			@MyRequestParam("id") Integer id,
			@MyRequestParam("name") String name){
		String result = modifyService.edit(id,name);
		return out(response,result);
	}
	
	
	
	private MyModleAndView out(HttpServletResponse resp,String str){
		try {
			resp.getWriter().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
