package cn.fooltech.fool_ops.domain.voucher.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.asset.entity.Asset;
import cn.fooltech.fool_ops.domain.asset.entity.AssetDetail;
import cn.fooltech.fool_ops.domain.asset.service.AssetDetailService;
import cn.fooltech.fool_ops.domain.asset.service.AssetService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherDetail;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StringUtils;


/**
 * <p>固定资产生成凭证服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年12月28日
 */
@Service
public class AssetVoucherMakeService extends VoucherMakeAbstractService{
	
	/**
	 * 固定资产卡片服务类
	 */
	@Autowired
	private AssetService assetService;
	
	/**
	 * 固定资产计提服务类
	 */
	@Autowired
	private AssetDetailService assetDetailService;
	
	/**
	 * 财务会计期间服务类 
	 */
	@Autowired
	private FiscalPeriodService fiscalPeriodService;
	
	/**
	 * 凭证、单据关联服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;
	
	/**
	 * 凭证服务类
	 */
	@Autowired
	private VoucherService voucherService;
	
	
	/**
	 * 凭证明细服务类
	 */
	@Autowired
	private VoucherDetailService voucherDetailService;

	@Override
	@Transactional
	public RequestResult makeVoucher(VoucherMakeVo vo) {
		//财务账套
		FiscalAccount fiscalAccount = SecurityUtil.getFiscalAccount();
		Organization org = SecurityUtil.getCurrentOrg();
		String accId = fiscalAccount.getFid();
		User user = SecurityUtil.getCurrentUser();
		
		//会计期间
		FiscalPeriod fiscalPeriod = null;
		RequestResult periodCheck = checkFiscalPeriod(accId, vo.getVoucherDate());
		if(!periodCheck.isSuccess()){
			return periodCheck;
		}
		else{
			fiscalPeriod = (FiscalPeriod) periodCheck.getData();
		}
		
		if(StringUtils.isBlank(vo.getBillIds())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "请选择固定资产!");
		}
		
		int totalSuccess = 0, totalFail = 0;
		String[] billIds = vo.getBillIds().split(",");
		boolean isOne = billIds.length == 1; //判断客户端是否只选中一条记录
		
		//添加凭证
		AuxiliaryAttr voucherWord = attrService.get(vo.getVoucherWordId()); //凭证字
		int number = voucherService.getMaxNumber(fiscalPeriod.getFid()) + 1; //凭证顺序号
		int voucherNumber = voucherService.getMaxVoucherNumber(fiscalAccount.getFid(), fiscalPeriod.getFid(), vo.getVoucherWordId()) + 1; //凭证号
		Voucher voucher = new Voucher();
		voucher.setNumber(number);
		voucher.setOrg(org);
		voucher.setCreator(user);
		voucher.setVoucherWord(voucherWord);
		voucher.setVoucherNumber(voucherNumber);
		voucher.setVoucherDate(vo.getVoucherDate());
		voucher.setAccessoryNumber(vo.getAccessoryNumber());
		voucher.setFiscalPeriod(fiscalPeriod);
		voucher.setFiscalAccount(fiscalAccount);
		voucher.setCreateTime(Calendar.getInstance().getTime());
		voucher.setUpdateTime(Calendar.getInstance().getTime());
		voucher.setVoucherWordNumber(voucherWord.getName() + "-" + voucherNumber);
		
		List<VoucherBill> relations = new ArrayList<VoucherBill>();
		for(String billId : billIds){
			AssetDetail assetDetail = assetDetailService.get(billId);
			Asset asset = assetDetail.getAsset();
			Organization dept = asset.getDept();
			
			//如果单据之前已经生成过凭证，则不能再次生成
			Voucher record = voucherBillService.getVoucher(assetDetail.getFid(), WarehouseBuilderCodeHelper.gdzc, fiscalAccount.getFid());
			if(record != null){
				continue;
			}
			
			//借方科目
			FiscalAccountingSubject debitSubject = assetDetailService.getDebitSubject(asset, assetDetail);
			if(debitSubject == null){
				if(isOne){
					return buildFailRequestResult("借方科目不能为空!");
				}
				else{
					totalFail++;
					continue;
				}
			}
			
			//贷方科目
			FiscalAccountingSubject creditSubject = assetDetailService.getCreditSubject(asset, assetDetail);
			if(creditSubject == null){
				if(isOne){
					return buildFailRequestResult("贷方科目不能为空!");
				}
				else{
					totalFail++;
					continue;
				}
			}
			
			//资产清算-折旧科目
			FiscalAccountingSubject depreciationSubject = null;
			if(assetDetail.getType() == AssetDetail.TYPE_CLEAR){
				depreciationSubject = asset.getDepreciationSubject();
				if(depreciationSubject == null){
					if(isOne){
						return buildFailRequestResult("折旧科目不能为空!");
					}
					else{
						totalFail++;
						continue;
					}
				}
				
				RequestResult depreciationCheck = check(WarehouseBuilderCodeHelper.gdzc, depreciationSubject, dept);
				if(!depreciationCheck.isSuccess()){
					if(isOne){
						return depreciationCheck;
					}
					else{
						totalFail++;
						continue;
					}
				}
			}
			
			RequestResult debitCheck = check(WarehouseBuilderCodeHelper.gdzc, debitSubject, dept);
			if(debitCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
				if(isOne){
					return debitCheck;
				}
				else{
					totalFail++;
					continue;
				}
			}
			
			RequestResult creditCheck = check(WarehouseBuilderCodeHelper.gdzc, creditSubject, dept);
			if(creditCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
				if(isOne){
					return creditCheck;
				}
				else{
					totalFail++;
					continue;
				}
			}
			
			//添加凭证明细
			if(assetDetail.getType() == AssetDetail.TYPE_CLEAR){
				BigDecimal assetBuyAmount = assetDetailService.getAssetAmount(asset.getFid(), AssetDetail.TYPE_BUY); //资产购入金额
				BigDecimal depreciationAmount = assetDetailService.getAssetAmount(asset.getFid(), AssetDetail.TYPE_DEPRECIATION); //折旧金额
				
				addVoucherDetail(voucher, assetDetail, fiscalAccount, getSubject(debitSubject, dept), 
						FiscalAccountingSubject.DIRECTION_BORROW, vo.getVoucherResume(), assetDetail.getAmount());
				
				addVoucherDetail(voucher, assetDetail, fiscalAccount, getSubject(depreciationSubject, dept), //折旧科目
					FiscalAccountingSubject.DIRECTION_BORROW, vo.getVoucherResume(), depreciationAmount);
				
				addVoucherDetail(voucher, assetDetail, fiscalAccount, getSubject(creditSubject, dept),
						FiscalAccountingSubject.DIRECTION_LOAN, vo.getVoucherResume(), assetBuyAmount);
			}
			else{
				addVoucherDetail(voucher, assetDetail, fiscalAccount, getSubject(debitSubject, dept), 
						FiscalAccountingSubject.DIRECTION_BORROW, vo.getVoucherResume(), assetDetail.getAmount());
				
				addVoucherDetail(voucher, assetDetail, fiscalAccount, getSubject(creditSubject, dept),
						FiscalAccountingSubject.DIRECTION_LOAN, vo.getVoucherResume(), assetDetail.getAmount());
			}
			
			//添加单据、凭证关联
			VoucherBill voucherBill = new VoucherBill();
			voucherBill.setVoucher(voucher);
			voucherBill.setBillType(WarehouseBuilderCodeHelper.gdzc);
			voucherBill.setBillId(assetDetail.getFid());
			voucherBill.setFiscalAccount(fiscalAccount);
			voucherBill.setOrg(org);
			voucherBill.setCreator(user);
			voucherBill.setCreateTime(Calendar.getInstance().getTime());
			relations.add(voucherBill);
				
			totalSuccess++;
		}
		
		List<VoucherDetail> details = voucher.getDetails();
		if(CollectionUtils.isNotEmpty(details)){
			//添加凭证
			voucherService.save(voucher);
			//添加凭证明细
			for(VoucherDetail detail : details){
				voucherDetailService.save(detail);
			}
			//添加凭证关联
			for(VoucherBill relation : relations){
				voucherBillService.save(relation);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalFail", String.valueOf(totalFail));
		map.put("totalSuccess", String.valueOf(totalSuccess));
		
		RequestResult result = new RequestResult();
		result.setDataExt(map);
		result.setData(voucher.getFid());
		return result;
	}
	
	/**
	 * 添加凭证明细信息
	 * @param voucher 凭证
	 * @param assetDetail 固定资产明细
	 * @param fiscalAccount 财务账套
	 * @param subject 科目
	 * @param direction 科目的余额方向
	 * @param voucherResume 摘要
	 * @param amount 金额
	 * @return
	 */
	private void addVoucherDetail(Voucher voucher, AssetDetail assetDetail, FiscalAccount fiscalAccount, 
			FiscalAccountingSubject subject, Integer direction, String voucherResume, BigDecimal amount){
		VoucherDetail voucherDetail = new VoucherDetail();
		voucherDetail.setVoucher(voucher);
		voucherDetail.setResume(voucherResume);
		voucherDetail.setAccountingSubject(subject);
		voucherDetail.setFiscalAccount(fiscalAccount);
		voucherDetail.setOrg(SecurityUtil.getCurrentOrg());
		voucherDetail.setCreator(SecurityUtil.getCurrentUser());
		voucherDetail.setCreateTime(Calendar.getInstance().getTime());
		voucherDetail.setUpdateTime(Calendar.getInstance().getTime());

		amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		//借方金额、贷方金额
		if(direction == FiscalAccountingSubject.DIRECTION_BORROW){
			voucherDetail.setDebitAmount(amount);
		}
		else{
			voucherDetail.setCreditAmount(amount);
		}
		
		//核算部门
		if(subject.getDepartmentSign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setDepartment(assetDetail.getAsset().getDept());
		}
		//核算数量
		if(subject.getQuantitySign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setQuantity(BigDecimal.ZERO);
		}
		//核算外币
		AuxiliaryAttr rmbCurrency = getRmbCurrency(fiscalAccount);
		if(subject.getCurrencySign() == FiscalAccountingSubject.ACCOUNT){
			voucherDetail.setCurrency(rmbCurrency);
			voucherDetail.setExchangeRate(BigDecimal.ONE);
			voucherDetail.setCurrencyAmount(assetDetail.getAmount());
		}
		voucher.getDetails().add(voucherDetail);
	}
	
	/**
	 * 根据约定规则，获取科目
	 * @param subject 科目
	 * @param dept 部门
	 * @return
	 */
	private FiscalAccountingSubject getSubject(final FiscalAccountingSubject subject, Organization dept){
		if(subject.getFlag() == FiscalAccountingSubject.FLAG_CHILD){
			return subject;
		}
		
		FiscalAccountingSubject resultSubject = null;
		Set<FiscalAccountingSubject> childSubjects = subject.getChilds();
		for(FiscalAccountingSubject childSubject : childSubjects){
			if(childSubject.getRelationType() != null && StringUtils.isNotBlank(childSubject.getRelationId())){
				if(childSubject.getRelationType() == FiscalAccountingSubject.RELATION_DEPARTMENT && childSubject.getRelationId().equals(dept.getFid())){
					resultSubject = childSubject;
					break;
				}
			}
		}
		if(resultSubject == null || isSigned(resultSubject)){
			return null;
		}
		else{
			return resultSubject;
		}
	}
	
	@Override
	public RequestResult check(Integer billType, FiscalAccountingSubject subject, Object... args) {
		if(subject.getFlag() == FiscalAccountingSubject.FLAG_PARENT){
			FiscalAccountingSubject resultSubject = getSubject(subject, (Organization) args[0]);
			if(resultSubject == null){
				return buildFailRequestResult("科目设置错误!");
			}
			else{
				return buildSuccessRequestResult();
			}
		}
		
		if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算销售商!");
		}
		if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算供应商!");
		}
		if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算项目!");
		}
		if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult( subject.getName() + "科目设置了核算仓库!");
		}
		if(subject.getGoodsSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算货品!");
		}
		if(subject.getMemberSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算人员!");
		}
		return new RequestResult();
	}
	
}
