package cn.fooltech.fool_ops.domain.voucher.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubject;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubjectDetail;
import cn.fooltech.fool_ops.domain.voucher.repository.BillSubjectDetailRepository;
import cn.fooltech.fool_ops.domain.voucher.vo.BillSubjectDetailVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * <p>单据关联会计科目模板明细服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月17日
 */
@Service
public class BillSubjectDetailService extends BaseService<BillSubjectDetail, BillSubjectDetailVo, String> {
	
	@Autowired
	private BillSubjectService billSubjectService;
	
	@Autowired
	private BillSubjectDetailRepository bsdRepo;
	
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 获取模板明细
	 * @param templateId 模板ID
	 * @see com.gever.ops.voucher.entity.BillSubject
	 * @return
	 */
	public List<BillSubjectDetail> getByTemplateId(String templateId){
		String orgId = SecurityUtil.getCurrentOrgId();
		return bsdRepo.getByTemplateId(orgId, templateId);
	}
	
	/**
	 * 新增
	 * @param vo
	 */
	public void save(BillSubjectDetailVo vo){
		String resume = vo.getResume();
		Integer direction = vo.getDirection();
		String subjectId = vo.getSubjectId(); //科目ID
		Integer subjectSource = vo.getSubjectSource(); //科目来源
		Integer amountSource = vo.getAmountSource(); //金额来源
		String billSubjectId = vo.getBillSubjectId(); //模板ID
		Date now = Calendar.getInstance().getTime();
		Integer hedge = vo.getHedge()==null?BillSubjectDetail.HEDGE_BLUE:vo.getHedge();
		
		BillSubjectDetail entity = new BillSubjectDetail();
		entity.setResume(resume);
		entity.setDirection(direction);
		entity.setAmountSource(amountSource);
		entity.setSubjectSource(subjectSource);
		entity.setHedge(hedge);
		entity.setSubject(subjectService.get(subjectId));
		entity.setBillSubject(billSubjectService.get(billSubjectId));
		entity.setCreateTime(now);
		entity.setOrg(SecurityUtil.getCurrentOrg());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		bsdRepo.save(entity);
	}
	
	/**
	 * 删除模板下的所有明细
	 * @param templateId 模板ID
	 * @see com.gever.ops.voucher.entity.BillSubject
	 */
	public void deleteByTemplateId(String templateId){
		String orgId = SecurityUtil.getCurrentOrgId();
		List<BillSubjectDetail> entities = bsdRepo.getByTemplateId(orgId, templateId);
		for(BillSubjectDetail entity : entities){
			bsdRepo.delete(entity);
		}
	}
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @return
	 */
	@Override
	public BillSubjectDetailVo getVo(BillSubjectDetail entity){
		BillSubjectDetailVo vo = new BillSubjectDetailVo();
		vo.setFid(entity.getFid());
		vo.setResume(entity.getResume());
		vo.setDirection(entity.getDirection());
		vo.setAmountSource(entity.getAmountSource());
		vo.setSubjectSource(entity.getSubjectSource());
		vo.setHedge(entity.getHedge()==null?BillSubjectDetail.HEDGE_BLUE:entity.getHedge());
		//模板
		BillSubject billSubject = entity.getBillSubject();
		if(billSubject != null){
			vo.setBillSubjectId(billSubject.getFid());
			vo.setBillSubjectCode(billSubject.getTemplateCode());
			vo.setBillSubjectName(billSubject.getTemplateName());
		}
		//科目
		FiscalAccountingSubject subject = entity.getSubject();
		if(subject != null){
			vo.setSubjectId(subject.getFid());
			vo.setSubjectName(subject.getName());
		}
		return vo;
	}

	@Override
	public CrudRepository<BillSubjectDetail, String> getRepository() {
		return bsdRepo;
	}
	
	/**
	 * 统计某个科目被引用的次数
	 * @param subjectId
	 * @return
	 */
	public Long countBySubjectId(String subjectId){
		return bsdRepo.countBySubjectId(subjectId);
	}
	
}
