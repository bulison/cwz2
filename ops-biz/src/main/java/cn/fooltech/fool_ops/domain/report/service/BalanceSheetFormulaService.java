package cn.fooltech.fool_ops.domain.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.report.entity.BalanceSheetFormula;
import cn.fooltech.fool_ops.domain.report.repository.BalanceSheetFormulaRepository;

@Service
public class BalanceSheetFormulaService extends AbstractBaseService<BalanceSheetFormula, String> {

	@Autowired
	private BalanceSheetFormulaRepository bsfRepo;
	
	@Override
	public CrudRepository<BalanceSheetFormula, String> getRepository() {
		return bsfRepo;
	}

	/**
	 * 根据账套ID删除
	 * @param accId
	 */
	@Transactional
	public void deleteByAccountId(String accId) {
		List<BalanceSheetFormula> formulas = bsfRepo.findByAccId(accId);
		bsfRepo.delete(formulas);
	}
	
	/**
	 * 根据账套ID查找
	 * @param accId
	 */
	public List<BalanceSheetFormula> findByAccountId(String accId) {
		return bsfRepo.findByAccId(accId);
	}
}
