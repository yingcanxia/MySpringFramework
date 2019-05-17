package cn.shadow.user.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.shadow.framework.annotation.MyService;
import cn.shadow.user.service.IQueryService;
import lombok.extern.slf4j.Slf4j;



/**
 * ��ѯҵ��
 * @author Tom
 *
 */
@MyService
@Slf4j
public class QueryService implements IQueryService {


	/**
	 * ��ѯ
	 */
	public String query(String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
		log.info("������ҵ�񷽷��д�ӡ�ģ�" + json);
		return json;
	}

}
