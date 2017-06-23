package cn.fooltech.fool_ops.domain.voucher.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.PageService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>凭证制作抽象网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年12月8日
 */
public abstract class VoucherMakeAbstractService implements PageService{
	
	/**
	 * 辅助属性服务类
	 */
	@Autowired
	public AuxiliaryAttrService attrService;
	
	/**
	 * 财务会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService fiscalPeriodService;
	
	/**
	 * 没有销售商和供应商的单据类型
	 */
	public static final List<Integer> specialTypes = Lists.newArrayList(
			WarehouseBuilderCodeHelper.pdd,
			WarehouseBuilderCodeHelper.dcd,
			WarehouseBuilderCodeHelper.bsd,
			WarehouseBuilderCodeHelper.scll,
			WarehouseBuilderCodeHelper.cprk);
	
	/**
	 * 获取人民币类别
	 * @return
	 */
	public AuxiliaryAttr getRmbCurrency(FiscalAccount fiscalAccount){
		String orgId = SecurityUtil.getCurrentOrgId();
		AuxiliaryAttr attr = attrService.getByCode(orgId, AuxiliaryAttrType.CODE_CURRENCY, "01", fiscalAccount.getFid());
		Assert.notNull(attr, "人民币币别不能为空!");
		return attr;
	}
	
	/**
	 * 判断科目是否勾选了核算项
	 * @param subject 科目
	 * @return
	 */
	public boolean isSigned(FiscalAccountingSubject subject){
		Assert.notNull(subject, "科目不能为空!");
		if(subject.getCashSign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		else if(subject.getCurrencySign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		else if(subject.getCussentAccountSign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		else if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		else if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		else if(subject.getDepartmentSign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		else if(subject.getMemberSign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		else if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		else if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		else if(subject.getGoodsSign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		else if(subject.getQuantitySign() == FiscalAccountingSubject.ACCOUNT){
			return true;
		}
		return false;
	}
	
	/**
	 * 检测财务会计期间
	 * @param fiscalAccountId 账套ID
	 * @param date 凭证日期
	 * @return
	 */
	public RequestResult checkFiscalPeriod(String fiscalAccountId, Date date){
		FiscalPeriod fiscalPeriod = fiscalPeriodService.getPeriod(date, fiscalAccountId);
		if(fiscalPeriod == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.FISCAL_PERIOD_NOT_EXIST, "财务会计期间不存在!");
		}
		if(fiscalPeriod.getCheckoutStatus() == FiscalPeriod.UN_USED){
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.FISCAL_PERIOD_UN_USED, "无效操作，财务会计期间未启用!");
		}
		if(fiscalPeriod.getCheckoutStatus() == FiscalPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.FISCAL_PERIOD_CHECKED, "无效操作，财务会计期间已结账!");
		}
		RequestResult result = new RequestResult();
		result.setData(fiscalPeriod);
		return result;
	}
	
	/**
	 * 制作凭证
	 * @param vo
	 * @return
	 */
	public abstract RequestResult makeVoucher(VoucherMakeVo vo);
	
	/**
	 * 校验数据
	 * @param billType 单据类型
	 * @param subject 科目
	 * @param args 其他参数
	 * @return
	 */
	public abstract RequestResult check(Integer billType, FiscalAccountingSubject subject, Object ... args);
	
}
