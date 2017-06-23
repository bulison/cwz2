package cn.fooltech.fool_ops.domain.base.service;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;

public interface PageService {

	/**
	 * 代码转换适应easyui
	 * @param pageParamater
	 * @return
	 */
	public default PageRequest getPageRequest(PageParamater pageParamater){
		PageRequest pageRequest = new PageRequest(pageParamater.getPage()-1, pageParamater.getRows());
		return pageRequest;
	}
	
	/**
	 * 代码转换适应easyui
	 * @param pageParamater
	 * @return
	 */
	public default PageRequest getPageRequest(PageParamater pageParamater, Sort sort){
		PageRequest pageRequest = new PageRequest(pageParamater.getPage()-1, pageParamater.getRows(), sort);
		return pageRequest;
	}
	
	/**
	 * 创建返回的对象
	 * @return
	 */
	public default RequestResult buildSuccessRequestResult(){
		RequestResult ret = new RequestResult();
		ret.setReturnCode(RequestResult.RETURN_SUCCESS);
		return ret;
	}
	
	/**
	 * 创建返回的对象
	 * @return
	 */
	public default RequestResult buildSuccessRequestResult(Object obj){
		RequestResult ret = new RequestResult();
		ret.setReturnCode(RequestResult.RETURN_SUCCESS);
		ret.setData(obj);
		return ret;
	}

	/**
	 * 创建返回的对象
	 * @param map
	 * @return
	 */
	public default RequestResult buildSuccessRequestResult(Map map){
		RequestResult ret = new RequestResult();
		ret.setReturnCode(RequestResult.RETURN_SUCCESS);
		ret.setDataExt(map);
		return ret;
	}
	
	/**
	 * 创建返回的对象
	 * @param mesage
	 * @return
	 */
	public default RequestResult buildFailRequestResult(String mesage){
		RequestResult ret = new RequestResult();
		ret.setReturnCode(RequestResult.RETURN_FAILURE);
		ret.setMessage(mesage);
		return ret;
	}
	
	/**
	 * 创建返回的对象
	 * @param errorCode
	 * @param mesage
	 * @return
	 */
	public default RequestResult buildFailRequestResult(int errorCode, String mesage){
		RequestResult ret = new RequestResult();
		ret.setErrorCode(errorCode);
		ret.setReturnCode(RequestResult.RETURN_FAILURE);
		ret.setMessage(mesage);
		return ret;
	}

	/**
	 * 创建返回的对象
	 * @param errorCode
	 * @param mesage
	 * @return
	 */
	public default RequestResult buildFailRequestResult(int errorCode, String mesage, Object data){
		RequestResult ret = new RequestResult();
		ret.setErrorCode(errorCode);
		ret.setReturnCode(RequestResult.RETURN_FAILURE);
		ret.setMessage(mesage);
		ret.setData(data);
		return ret;
	}
}
