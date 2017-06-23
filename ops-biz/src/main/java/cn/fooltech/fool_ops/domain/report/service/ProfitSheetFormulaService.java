package cn.fooltech.fool_ops.domain.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.report.entity.BalanceSheetFormula;
import cn.fooltech.fool_ops.domain.report.entity.ProfitSheetFormula;
import cn.fooltech.fool_ops.domain.report.repository.ProfitSheetFormulaRepository;

@Service
public class ProfitSheetFormulaService extends AbstractBaseService<ProfitSheetFormula, String>{

	@Autowired
	private ProfitSheetFormulaRepository psfRepo;
	
	@Override
	public CrudRepository<ProfitSheetFormula, String> getRepository() {
		return psfRepo;
	}

	/**
	 * 根据账套ID删除
	 * @param accId
	 */
	@Transactional
	public void deleteByAccountId(String accId) {
		List<ProfitSheetFormula> formulas = psfRepo.findByAccId(accId);
		psfRepo.delete(formulas);
	}
	
	/**
	 * 根据账套ID查找
	 * @param accId
	 */
	public List<ProfitSheetFormula> findByAccountId(String accId) {
		return psfRepo.findByAccId(accId);
	}
}
