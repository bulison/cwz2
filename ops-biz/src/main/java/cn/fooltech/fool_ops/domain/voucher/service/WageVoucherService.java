package cn.fooltech.fool_ops.domain.voucher.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Splitter;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.voucher.entity.WageVoucher;
import cn.fooltech.fool_ops.domain.voucher.repository.WageVoucherRepository;
import cn.fooltech.fool_ops.domain.voucher.vo.WageVoucherVo;
import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;
import cn.fooltech.fool_ops.domain.wage.service.WageFormulaService;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>工资凭证网页服务类，用于工资生成凭证</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月12日
 */
@Service
public class WageVoucherService extends BaseService<WageVoucher, WageVoucherVo, String> {
	
	@Autowired
	private WageVoucherRepository wageVoucherRepo;
	
	/**
	 * 工资公式服务类
	 */
	@Autowired
	private WageFormulaService wageFormulaService;
	
	/**
	 * 科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 机构服务类
	 */
	@Autowired
	private OrgService orgService;
	
	/**
	 * 列表查询
	 * @param paramater
	 * @return
	 */
	public Page<WageVoucherVo> query(PageParamater paramater){
		String accId = SecurityUtil.getFiscalAccountId();
		
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		
		Page<WageVoucher> page = wageVoucherRepo.findPageByAccId(accId, pageRequest);

		return getPageVos(page, pageRequest);
	}
	
	/**
	 * 新增
	 * @param vo
	 * @return
	 */
	public RequestResult save(WageVoucherVo vo){
		String wageFormulaId = vo.getWageFormulaId(); //工资项目ID
		String wageSubjectId = vo.getWageSubjectId(); //应付工资科目ID
		String deptId = vo.getDeptId();
		Integer direction = vo.getDirection()==null?0:vo.getDirection();
		Date now = Calendar.getInstance().getTime();
		
		if(StringUtils.isBlank(wageSubjectId)){
			return buildFailRequestResult("科目不能为空!");
		}
		
		WageVoucher entity = new WageVoucher();
		entity.setCreateTime(now);
		entity.setUpdateTime(now);
		entity.setOrg(SecurityUtil.getCurrentOrg());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setDept(orgService.get(deptId));
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		entity.setWageSubject(subjectService.get(wageSubjectId));
		entity.setWageFormula(wageFormulaService.get(wageFormulaId));
		entity.setDirection(direction);
		wageVoucherRepo.save(entity);
		return new RequestResult();
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@Transactional
	public RequestResult delete(String fid){
		Iterable<String> split = Splitter.on(",").trimResults().omitEmptyStrings().split(fid);
		for (String id : split) {
			WageVoucher entity = wageVoucherRepo.findOne(id);
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");	
			}
			wageVoucherRepo.delete(entity);
		}
		
		return new RequestResult();
	}
	
	/**
	 * 删除全部
	 * @return
	 */
	@Transactional
	public RequestResult deleteAll(){
		String accId = SecurityUtil.getFiscalAccountId();
		List<WageVoucher> entities = wageVoucherRepo.findByAccId(accId);
		for(WageVoucher entity : entities){
			wageVoucherRepo.delete(entity);
		}
		return new RequestResult();
	}

	/**
	 * 单个实体转vo
	 */
	@Override
	public WageVoucherVo getVo(WageVoucher entity) {
		WageVoucherVo vo = new WageVoucherVo();
		vo.setFid(entity.getFid());
		vo.setRemark(entity.getRemark());
		vo.setDirection(entity.getDirection());
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd"));
		
		//部门
		Organization dept = entity.getDept();
		if(dept != null){
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}
		//费用科目
		FiscalAccountingSubject expenseSubject = entity.getExpenseSubject();
		if(expenseSubject != null){
			vo.setExpenseSubjectId(expenseSubject.getFid());
			vo.setExpenseSubjectName(expenseSubject.getName());
		}
		//凭证字
		AuxiliaryAttr voucherWord = entity.getVoucherWord();
		if(voucherWord != null){
			vo.setVoucherWordId(voucherWord.getFid());
			vo.setVoucherWordName(voucherWord.getName());	
		}
		//工资项目
		WageFormula wageFormula = entity.getWageFormula();
		if(wageFormula != null){
			vo.setWageFormulaId(wageFormula.getFid());
			vo.setWageFormulaName(wageFormula.getColumnName());
		}
		//应付工资科目
		FiscalAccountingSubject wageSubject = entity.getWageSubject();
		if(wageSubject != null){
			vo.setWageSubjectId(wageSubject.getFid());
			vo.setWageSubjectName(wageSubject.getName());
		}
		return vo;
	}

	@Override
	public CrudRepository<WageVoucher, String> getRepository() {
		return wageVoucherRepo;
	}

	/**
	 * 根据账套查找
	 * @param accId
	 * @return
	 */
	public List<WageVoucher> findByAccId(String accId) {
		return wageVoucherRepo.findByAccId(accId);
	}

	/**
	 * 根据科目ID统计引用个数
	 * @param fid
	 * @return
	 */
	public Long countBySubjectId(String fid) {
		return wageVoucherRepo.countBySubjectId(fid);
	}
	
}
