package cn.fooltech.fool_ops.domain.sysman.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.sysman.entity.MupdateRecord;
import cn.fooltech.fool_ops.domain.sysman.repository.MupdateRecordRepository;
import cn.fooltech.fool_ops.domain.sysman.vo.MupdateRecordVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * 服务类
 */
@Service
public class MupdateRecordService extends BaseService<MupdateRecord, MupdateRecordVo, String> {

	@Autowired
	private MupdateRecordRepository repository;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public MupdateRecordVo getVo(MupdateRecord entity) {
		MupdateRecordVo vo = VoFactory.createValue(MupdateRecordVo.class, entity);
		return vo;
	}

	@Override
	public CrudRepository<MupdateRecord, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<MupdateRecordVo> query(MupdateRecordVo vo, PageParamater paramater) {

		Sort sort = new Sort(Sort.Direction.DESC, "deviceType","version");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<MupdateRecord> page = repository.findPageBy(vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

	/**
	 * 修改或新增
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(MupdateRecordVo vo) {

		MupdateRecord entity = null;
		if (Strings.isNullOrEmpty(vo.getId())) {
			entity = new MupdateRecord();
			entity.setCreateTime(new Date());
		} else {
			entity = repository.findOne(vo.getId());

		}
		entity.setDeviceType(vo.getDeviceType());
		entity.setVersion(vo.getVersion());
		entity.setRemark(vo.getRemark());
		entity.setIsNeed(vo.getIsNeed());
		entity.setCreateTime(vo.getCreateTime());
		entity.setDownUrl(vo.getDownUrl());
		entity.setDownType(vo.getDownType());

		repository.save(entity);

		return buildSuccessRequestResult(getVo(entity));
	}
}
