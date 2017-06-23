package cn.fooltech.fool_ops.domain.datainit;

import java.util.LinkedHashMap;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>数据初始化服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015年9月15日
 */
@Service("ops.DataInitService")
public class DataInitService {

	/**
	 * 所有实现IDataInit接口的类都注入
	 */
	@Autowired
	private Set<ISqlDataInit> initServiceSet;
	
	/**
	 * 初始化所有的数据
	 */
	public void initAll(LinkedHashMap<String,String> params){
		for(ISqlDataInit initService:initServiceSet){
			initService.init(params);
		}
	}
}
