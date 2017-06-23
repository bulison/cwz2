package cn.fooltech.fool_ops.domain.base.service;

import java.io.Serializable;
import java.util.Date;

import cn.fooltech.fool_ops.component.core.DtoTransfer;
import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import cn.fooltech.fool_ops.component.exception.ThrowException;
import cn.fooltech.fool_ops.utils.DateUtilTools;

/**
 * 基础Service类
 * @author xjh
 *
 */
public abstract class BaseService<E, V, ID extends Serializable> extends AbstractBaseService<E, ID> implements DtoTransfer<E, V>{
	
	public V getById(ID id){
		E entity = getRepository().findOne(id);
		if(entity==null)return null;
		return getVo(entity);
	}
	
	public V getById(ID id, ThrowException thro){
		E entity = getRepository().findOne(id);
		if(entity==null && thro==ThrowException.Throw){
			throw new DataNotExistException();
		}
		return getVo(entity);
	}

	/**
	 * 判断time1是否等于time2 是则返回true，否则返回false
	 * @param time1
	 * @param time2
	 * @return
	 */
	public boolean checkUpdateTime(Date time1, Date time2){
		String time1Str = DateUtilTools.time2String(time1);
		String time2Str = DateUtilTools.time2String(time2);
		if(time1Str.compareTo(time2Str)==0){
			return true;
		}
		return false;
	}
}
