package cn.fooltech.fool_ops.domain.base.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import org.springframework.data.repository.CrudRepository;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.utils.DateJsonValueProcessor;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 基础Service类
 * @author xjh
 *
 */
public abstract class AbstractBaseService<E, ID extends Serializable> implements PageService{
	
	protected final String YEAR = "yyyy";
	protected final String MONTH = "yyyy-MM";
	protected final String DATE = "yyyy-MM-dd";
	protected final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	
	public abstract CrudRepository<E, ID> getRepository();
	
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public E findOne(ID id){
		return getRepository().findOne(id);
	}
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public E get(ID id){
		return getRepository().findOne(id);
	}
	
	/**
	 * 根据ID删除
	 * @param id
	 */
	public RequestResult delete(ID id){
		//E entity = get(id);
		//if(entity==null)return buildFailRequestResult("数据不存在，请刷新再试");
		//delete(entity);
		getRepository().delete(id);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 删除
	 * @param e
	 */
	public RequestResult delete(E e){
		getRepository().delete(e);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 修改或保存
	 * @param entity
	 */
	public void save(E entity){
		getRepository().save(entity);
	}
	
	/**
	 * 获得对象的JSON字符串
	 * @param obj
	 * @return
	 */
	public String getJsonData(Object obj){
		DateJsonValueProcessor dateProcessor = new DateJsonValueProcessor(DateJsonValueProcessor.Default_DATE_PATTERN);
		JsonConfig config = new JsonConfig();
		config.addIgnoreFieldAnnotation(JsonIgnore.class);
		config.registerJsonValueProcessor(Date.class, dateProcessor);
		JSONObject json = JSONObject.fromObject(obj, config);
		return json.toString();
	}
	
	/**
	 * 获得分页数据
	 * @param data
	 * @param totalCount
	 * @return
	 */
	public PageJson getPageJson(List data, long totalCount){
		PageJson pageJson = new PageJson();
		pageJson.setRows(data);
		pageJson.setTotal(totalCount);
		return pageJson;
	}
	
	/**
	 * 检查修改时间戳；相同返回true，否则返回false
	 * @param updateTimeStr
	 * @param updateTime
	 * @return
	 */
	public boolean checkUpdateTime(String updateTimeStr, Date updateTime){
		String updateTimeStr2 = DateUtilTools.date2String(updateTime, 
				DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS);
		if(updateTimeStr.equals(updateTimeStr2))return true;
		return false;
	}
	
}
