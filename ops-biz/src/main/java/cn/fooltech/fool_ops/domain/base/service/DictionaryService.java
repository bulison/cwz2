package cn.fooltech.fool_ops.domain.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.entity.Dictionary;
import cn.fooltech.fool_ops.domain.base.repository.DictionaryRepository;
import cn.fooltech.fool_ops.domain.base.vo.DictionaryVo;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;



@Service
@Transactional
public class DictionaryService extends BaseService<Dictionary,DictionaryVo,String>{
	@Autowired
	private DictionaryRepository dictionaryRepository;
	/*
	 * 查找全部字典
	 */
	public Page<DictionaryVo> queryAll(PageParamater pageParamater){
		PageRequest request = getPageRequest(pageParamater);
		Page<DictionaryVo> pageVo=getPageVos(dictionaryRepository.query(request),request);
	//	return dictionaryRepository.query(request);
		return pageVo;
	}
	/*
	 * 根据主键查找单个字典
	 */
	public DictionaryVo queryById(String id){
		Dictionary entity=dictionaryRepository.findOne(id);
		return getVo(entity);
	}
	/**
	 * 根据key查找单个字典
	 * @param vo
	 * @return
	 */
	public Dictionary queryOneByKey(String key){
		return dictionaryRepository.queryOneByKey(key);
	}
	/**
	 * 查找某一个key下所有的字典 
	 * @param vo
	 * @return
	 */
	public List<Dictionary> queryByKey(String key){
		return dictionaryRepository.queryByKey(key);
	}
	/*
	 * 添加或修改字典
	 */
	@Transactional
	public RequestResult save(DictionaryVo vo){
		String inValid = ValidatorUtils.inValidMsg(vo);
		if (inValid != null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
        }
		Dictionary entity = new Dictionary();
		if(!Strings.isNullOrEmpty(vo.getId())){
			entity=dictionaryRepository.findOne(vo.getId());
		}
		if(!Strings.isNullOrEmpty(vo.getCode())){
			entity.setCode(vo.getCode());
		}
		if(!Strings.isNullOrEmpty(vo.getDescribe())){
			entity.setDescribe(vo.getDescribe());
		}
		if(!Strings.isNullOrEmpty(vo.getKey())){
			entity.setKey(vo.getKey());
		}
		if(!Strings.isNullOrEmpty(vo.getName())){
			entity.setName(vo.getName());
		}
		if(!Strings.isNullOrEmpty(vo.getValue())){
			entity.setValue(vo.getValue());
		}
		dictionaryRepository.save(entity);
		return  buildSuccessRequestResult(getVo(entity));
	}

	/**
	 * 单个实体类转化为VO
	 * @return
	 */
	public DictionaryVo getVo(Dictionary entity){
		if(entity==null){
			return null;
		}
		DictionaryVo vo=VoFactory.createValue(DictionaryVo.class,entity);
		if(!Strings.isNullOrEmpty(entity.getCode())){
			vo.setCode(entity.getCode());
		}
		if(!Strings.isNullOrEmpty(entity.getDescribe())){
			vo.setDescribe(entity.getDescribe());
		}
		if(!Strings.isNullOrEmpty(entity.getKey())){
			vo.setKey(entity.getKey());
		}
		if(!Strings.isNullOrEmpty(entity.getName())){
			vo.setName(entity.getName());
		}
		if(!Strings.isNullOrEmpty(entity.getValue())){
			vo.setName(entity.getValue());
		}
		return vo;
				
	}
	@Override
	public CrudRepository getRepository() {
		// TODO Auto-generated method stub
		return dictionaryRepository;
	}
	/**
	 * 根据ID删除
	 * @param id
	 * @return
	 */
	public RequestResult deleteDictionaryById(String id){
		Dictionary entity=dictionaryRepository.findOne(id);
		dictionaryRepository.delete(entity);
		return buildSuccessRequestResult();
	}

	
}


