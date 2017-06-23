package cn.fooltech.fool_ops.domain.report.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountingSubjectRepository;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalInitBalanceRepository;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherDetailRepository;
import cn.fooltech.fool_ops.utils.ExpressionUtils;
import cn.fooltech.fool_ops.utils.NumberUtil;

/**
 * 资产负债公式计算服务类
 * @author xjh
 *
 */
@Service
public class ReportFormulaService {
	
	@Autowired
	private FiscalInitBalanceRepository initRepo;
	
	@Autowired
	private FiscalAccountingSubjectRepository subjectRepo;
	
	@Autowired
	private VoucherDetailRepository detailRepo;
	
	
	/**
	 * AA（科目s）：汇总多个科目数据，取科目的所有借方-贷方的发生额；
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public BigDecimal getAA(String code, String accId, Date date, Boolean year){
		
		List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
		
		BigDecimal total = BigDecimal.ZERO;
		for(String temp:codes){
			BigDecimal voucher = detailRepo.getAA(temp, date, accId);
			BigDecimal init = initRepo.getAmountBySubjectCode(temp, null, accId);
			total = NumberUtil.add(total, voucher);
			total = NumberUtil.add(total, init);
		}
		return total;
	}
	
	/**
	 * AD（科目s）：汇总多个科目数据，取科目的所有借方的发生额；
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public BigDecimal getAD(String code, String accId, Date date, Boolean year){
		
		List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
		
		BigDecimal total = BigDecimal.ZERO;
		for(String temp:codes){
			BigDecimal voucherj = detailRepo.getADAC(temp, date, FiscalAccountingSubject.DIRECTION_BORROW, accId);
			BigDecimal initj = initRepo.getAmountBySubjectCode(temp, FiscalAccountingSubject.DIRECTION_BORROW, accId);
			total = NumberUtil.add(total, voucherj);
			total = NumberUtil.add(voucherj, initj);
		}
		
		return total;
	}
	
	/**
	 * AC（科目s）：汇总多个科目数据，取科目的所有贷方的发生额；
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public BigDecimal getAC(String code, String accId, Date date, Boolean year){
		
		List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
		
		BigDecimal total = BigDecimal.ZERO;
		for(String temp:codes){
			BigDecimal voucherd = detailRepo.getADAC(temp, date, FiscalAccountingSubject.DIRECTION_LOAN, accId);
			BigDecimal initd = initRepo.getAmountBySubjectCode(temp, FiscalAccountingSubject.DIRECTION_LOAN, accId);
			total = NumberUtil.add(total, voucherd);
			total = NumberUtil.add(total, initd);
		}
		return total;
	}
	
	/**
	 * AS（科目s）：汇总多个科目数据，取科目设置中余额方向的发生额
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public BigDecimal getAS(String code, String accId, Date date, Boolean year){
		
		List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
		
		BigDecimal total = BigDecimal.ZERO;
		for(String temp:codes){
			BigDecimal voucher = detailRepo.getAS(temp, date, accId);
			BigDecimal init = initRepo.getAmountBySubjectCode(temp, null, accId);
			total = NumberUtil.add(total, voucher);
			total = NumberUtil.add(total, init);
		}
		return total;
	}
	
	/**
	 * AX（科目s）：取科目借方余额；参照AA的计算方法，得出科目余额，如果余额小于0，则返回0，否则返回余额
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public BigDecimal getAX(String code, String accId, Date date, Boolean year){
		
		List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
		
		BigDecimal total = BigDecimal.ZERO;
		for(String temp:codes){
			FiscalAccountingSubject subject = subjectRepo.findTopByCode(accId, temp);
			if(subject==null)continue;
			
			BigDecimal voucher = detailRepo.getAA(temp, date, accId);
			BigDecimal init = initRepo.getAmountBySubjectCode(temp, null, accId);
			
			BigDecimal current =  NumberUtil.add(voucher, init);
			
			if(subject.getDirection()==FiscalAccountingSubject.DIRECTION_BORROW){
				if(current.compareTo(BigDecimal.ZERO)<0){
					current = BigDecimal.ZERO;
				}
			}else{
				if(current.compareTo(BigDecimal.ZERO)>0){
					current = BigDecimal.ZERO;
				}else{
					current = current.abs();
				}
			}
			total = NumberUtil.add(total, current);
		}
		return total;
		
	}
	
	/**
	 * AY（科目s）：取科目贷方余额；参照AA的计算方法，得出科目余额，如果余额大于0，则返回0，否则返回余额的绝对值；
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public BigDecimal getAY(String code, String accId, Date date, Boolean year){
		
		List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
		
		BigDecimal total = BigDecimal.ZERO;
		for(String temp:codes){
			FiscalAccountingSubject subject = subjectRepo.findTopByCode(accId, temp);
			if(subject==null)continue;
			
			BigDecimal voucher = detailRepo.getAA(temp, date, accId);
			BigDecimal init = initRepo.getAmountBySubjectCode(temp, null, accId);
			
			BigDecimal current =  NumberUtil.add(voucher, init);
			
			if(subject.getDirection()==FiscalAccountingSubject.DIRECTION_BORROW){
				if(current.compareTo(BigDecimal.ZERO)>0){
					current = BigDecimal.ZERO;
				}else{
					current = current.abs();
				}
			}else{
				if(current.compareTo(BigDecimal.ZERO)<0){
					current = BigDecimal.ZERO;
				}
			}
			total = NumberUtil.add(total, current);
		}
		return total;
	}
	
	
	/**
	 * DS（行号，行号）：根据行号，累加数据；（！！！此函数会被替换掉，不会被调用到！！！）
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public  BigDecimal getDS(String code, String accId, Date date, Boolean year){
		return BigDecimal.ZERO;
	}
	
	
	
	/**
	 * 利润表AA（科目s）：汇总多个科目数据，取科目的所有借方-贷方的发生额；
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param start 时间点
	 * @param end 时间点
	 * @return
	 */
	public BigDecimal getAAPeriod(String code, String accId, Date start, Date end){
		if(start!=null&&end!=null){
			
			List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
			
			BigDecimal total = BigDecimal.ZERO;
			for(String temp:codes){
				BigDecimal voucher = detailRepo.getAAPeriod(temp, start, end, accId);
				total = NumberUtil.add(total, voucher);
			}
			return total;
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 利润表AX（科目s）
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param start 时间点
	 * @param end 时间点
	 * @return
	 */
	public BigDecimal getAXPeriod(String code, String accId, Date start, Date end){
		if(start!=null&&end!=null){
			
			List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
			
			BigDecimal total = BigDecimal.ZERO;
			for(String temp:codes){
				
				FiscalAccountingSubject subject = subjectRepo.findTopByCode(accId, temp);
				if(subject==null)continue;
				
				BigDecimal voucher = detailRepo.getAAPeriod(temp, start, end, accId);
				
				if(subject.getDirection()==FiscalAccountingSubject.DIRECTION_BORROW){
					if(voucher.compareTo(BigDecimal.ZERO)<0){
						voucher = BigDecimal.ZERO;
					}
				}else{
					if(voucher.compareTo(BigDecimal.ZERO)>0){
						voucher = BigDecimal.ZERO;
					}else{
						voucher = voucher.abs();
					}
				}
				total = NumberUtil.add(total, voucher);
			}
			return total;
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 利润表AY（科目s）
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param start 时间点
	 * @param end 时间点
	 * @return
	 */
	public BigDecimal getAYPeriod(String code, String accId, Date start, Date end){
		if(start!=null&&end!=null){
			
			List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
			
			BigDecimal total = BigDecimal.ZERO;
			for(String temp:codes){
				
				FiscalAccountingSubject subject = subjectRepo.findTopByCode(accId, temp);
				if(subject==null)continue;
				
				BigDecimal voucher = detailRepo.getAAPeriod(temp, start, end, accId);
				
				if(subject.getDirection()==FiscalAccountingSubject.DIRECTION_BORROW){
					if(voucher.compareTo(BigDecimal.ZERO)>0){
						voucher = BigDecimal.ZERO;
					}else{
						voucher = voucher.abs();
					}
				}else{
					if(voucher.compareTo(BigDecimal.ZERO)<0){
						voucher = BigDecimal.ZERO;
					}
				}
				
				total = NumberUtil.add(total, voucher);
			}
			return total;
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 利润表AD（科目s）：汇总多个科目数据，取科目的所有借方的发生额；
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public BigDecimal getADPeriod(String code, String accId, Date start, Date end){

		if(start!=null&&end!=null){
			
			List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
			
			BigDecimal total = BigDecimal.ZERO;
			for(String temp:codes){

				BigDecimal voucher = detailRepo.getADACPeriod(temp,
						start, end, FiscalAccountingSubject.DIRECTION_BORROW, accId);
				total = NumberUtil.add(total, voucher);
			}
			return total;
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 利润表AC（科目s）：汇总多个科目数据，取科目的所有贷方的发生额；
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public BigDecimal getACPeriod(String code, String accId, Date start, Date end){
		
		if(start!=null&&end!=null){
			
			List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
			
			BigDecimal total = BigDecimal.ZERO;
			for(String temp:codes){

				BigDecimal voucher = detailRepo.getADACPeriod(temp,
						start, end, FiscalAccountingSubject.DIRECTION_LOAN, accId);
				total = NumberUtil.add(total, voucher);
			}
			return total;
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 利润表AS（科目s）：汇总多个科目数据，取科目设置中余额方向的发生额
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public BigDecimal getASPeriod(String code, String accId, Date start, Date end){
		
		if(start!=null&&end!=null){
			
			List<String> codes = ExpressionUtils.splitter_comma.splitToList(code);
			
			BigDecimal total = BigDecimal.ZERO;
			for(String temp:codes){

				BigDecimal voucher = detailRepo.getASPeriod(temp, start, end, accId);
				total = NumberUtil.add(total, voucher);
			}
			return total;
		}
		return BigDecimal.ZERO;
	}
	
	
	/**
	 * DS（行号，行号）：根据行号，累加数据；（！！！此函数会被替换掉，不会被调用到！！！）
	 * @param code 科目编号
	 * @param accId 账套ID
	 * @param date 时间点
	 * @param year 是否年初数
	 * @return
	 */
	public  BigDecimal getDSPeriod(String code, String accId, Date startDate, Date endDate){
		return BigDecimal.ZERO;
	}
}
