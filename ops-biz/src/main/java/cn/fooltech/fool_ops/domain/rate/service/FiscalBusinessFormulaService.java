package cn.fooltech.fool_ops.domain.rate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.rate.entity.FiscalBusinessFormula;
import cn.fooltech.fool_ops.domain.rate.repository.FiscalBusinessFormulaRepository;
import cn.fooltech.fool_ops.domain.rate.vo.FiscalBusinessFormulaVo;
import cn.fooltech.fool_ops.utils.DateUtil;
@Service
public class FiscalBusinessFormulaService extends BaseService<FiscalBusinessFormula,FiscalBusinessFormulaVo,String>{
	@Autowired
	private FiscalBusinessFormulaRepository repository;
	
	@Override
	public FiscalBusinessFormulaVo getVo(FiscalBusinessFormula entity) {
		FiscalBusinessFormulaVo vo=new FiscalBusinessFormulaVo();
		vo.setFid(entity.getFid());
		vo.setDeptId(entity.getDept().getFid());
		vo.setDeptName(entity.getDept().getOrgName());
		if(entity.getFormula()!=null){
			vo.setFormula(entity.getFormula());
		}
		if(entity.getItem()!=null){
			vo.setItem(entity.getItem());
		}
		if(entity.getUpdateTime()!=null){
			vo.setUpdateTime(DateUtil.format(entity.getUpdateTime()));
		}
		return vo;
	}
	/**
	 * 根据账套ID查询
	 */
	public List<FiscalBusinessFormula> findByAccountId(String accid){
		return repository.findByAccountId(accid);
	}
	/**
	 * 根据账套ID删除
	 * @param accId
	 */
	@Transactional
	public void deleteByAccountId(String accId) {
		List<FiscalBusinessFormula> list=repository.findByAccountId(accId);
		repository.delete(list);
	}
	/**
	 * 查找该企业是否已拥有公式
	 */
	public Long existsFormula(String accId,String orgId){
		return repository.existsFormula(accId,orgId);
	}
	
	
	
	@Override
	public CrudRepository<FiscalBusinessFormula, String> getRepository() {
		return repository;
	}
	
}
