package cn.fooltech.fool_ops.domain.asset.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.asset.entity.Asset;
import cn.fooltech.fool_ops.domain.asset.entity.AssetDetail;
import cn.fooltech.fool_ops.domain.asset.repository.AssetRepository;
import cn.fooltech.fool_ops.domain.asset.vo.AssetVo;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>固定资产卡片网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-12-28 14:23:18
 */
@Service
public class AssetService extends BaseService<Asset,AssetVo,String> {
	
	/**
	 * 固定资产卡片持久层
	 */
	@Autowired
	private AssetRepository assetRepository;
	
	/**
	 * 科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 固定资产计提服务类
	 */
	@Autowired
	private AssetDetailService detailWebService;
	
	/**
	 * 属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService;
	
	/**
	 * 凭证、单据关联服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;
	/**
	 * 机构服务类
	 */
	@Autowired
	protected OrganizationRepository orgService;
	
	/**
	 * 查询固定资产卡片列表信息，按照固定资产卡片创建时间降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param assetVo
	 */
	public Page<Asset> query(AssetVo assetVo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(pageParamater,sort);
		return assetRepository.query(assetVo, request);
	}
	
	
	/**
	 * 单个固定资产卡片实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public AssetVo getVo(Asset entity){
		if(entity == null)
			return null;
		AssetVo vo = new AssetVo();
		vo.setAssetCode(entity.getAssetCode());
		vo.setAssetName(entity.getAssetName());
		vo.setQuentity(entity.getQuentity());
		vo.setInitialValue(entity.getInitialValue());
		vo.setDiscountYear(entity.getDiscountYear());
		vo.setResidualRate(entity.getResidualRate());
		vo.setSupplier(entity.getSupplier());
		vo.setGrade(entity.getGrade());
		vo.setManufactor(entity.getManufactor());
		vo.setModel(entity.getModel());
		vo.setUseDate(DateUtilTools.date2String(entity.getUseDate(), DATE));
		vo.setBuyDate(DateUtilTools.date2String(entity.getBuyDate(), DATE));
		vo.setShareDate(DateUtilTools.date2String(entity.getShareDate(), DATE));
		vo.setRecordStatus(entity.getRecordStatus());
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime()));
		vo.setAuditTime(DateUtilTools.date2String(entity.getAuditTime()));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS));
		vo.setFid(entity.getFid());
		
		vo.setResidualValue(NumberUtil.divide(entity.getResidualValue(), entity.getQuentity(), 2));
		
		Organization dept = entity.getDept();
		if(dept!=null){
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}
		
		FiscalAccountingSubject assetSubject = entity.getAssetSubject();
		if(assetSubject!=null){
			vo.setAssetSubjectId(assetSubject.getFid());
			vo.setAssetSubjectName(assetSubject.getName());
		}
		
		FiscalAccountingSubject depreciationSubject = entity.getDepreciationSubject();
		if(depreciationSubject!=null){
			vo.setDepreciationSubjectId(depreciationSubject.getFid());
			vo.setDepreciationSubjectName(depreciationSubject.getName());
		}
		
		FiscalAccountingSubject paymentSubject = entity.getPaymentSubject();
		if(paymentSubject!=null){
			vo.setPaymentSubjectId(paymentSubject.getFid());
			vo.setPaymentSubjectName(paymentSubject.getName());
		}
		
		FiscalAccountingSubject clearSubject = entity.getClearSubject();
		if(clearSubject!=null){
			vo.setClearSubjectId(clearSubject.getFid());
			vo.setClearSubjectName(clearSubject.getName());
		}
		
		FiscalAccountingSubject expenseSubject = entity.getExpenseSubject();
		if(expenseSubject!=null){
			vo.setExpenseSubjectId(expenseSubject.getFid());
			vo.setExpenseSubjectName(expenseSubject.getName());
		}
		
		AuxiliaryAttr assetType = entity.getAssetType();
		if(assetType!=null){
			vo.setAssetTypeId(assetType.getFid());
			vo.setAssetTypeName(assetType.getName());
		}
		
		User auditor = entity.getAuditor();
		if(auditor!=null){
			vo.setAuditorId(auditor.getFid());
			vo.setAuditorName(auditor.getUserName());
		}
		User creator = entity.getCreator();
		if(creator!=null){
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}
		
		vo.setSumAccruedValue(getAccruedValue(entity));
		
		return vo;
	}
	
	/**
	 * 判断能否修改科目
	 * @return
	 */
	public boolean isEnableEdit(String assetId){
		String accId = SecurityUtil.getFiscalAccountId();
		
		Asset asset = assetRepository.findOne(assetId);
		List<String> detailIds = Lists.newArrayList();
		
		for(AssetDetail detail:asset.getDetails()){
			detailIds.add(detail.getFid());
		}
		
		//如果单据之前已经生成过凭证，则不能再次生成
		if(detailIds.size()>0 && voucherBillService.countVoucher(detailIds, WarehouseBuilderCodeHelper.gdzc, accId)>0){
			return false;
		}
		return true;
	}
	
	/**
	 * 删除固定资产卡片<br>
	 */
	public RequestResult delete(String fid){
		
		Asset asset = assetRepository.findOne(fid);
		
		if(asset.getRecordStatus()==Asset.STATUS_UNAUDIT){
			detailWebService.deleteByAssetId(fid);
			assetRepository.delete(fid);
			return buildSuccessRequestResult();
		}else{
			return new RequestResult(RequestResult.RETURN_FAILURE, "非未审核状态不能删除");
		}
	}
	
	/**
	 * 获取固定资产卡片信息
	 * @param fid 固定资产卡片ID
	 * @return
	 */
	public AssetVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(assetRepository.findOne(fid));
	}
	

	/**
	 * 新增/编辑固定资产卡片
	 * @param vo
	 */
	public RequestResult save(AssetVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE,inValid);
		}
		RequestResult check = checkSave(vo);
		if(check.getReturnCode()==RequestResult.RETURN_FAILURE){
			return check;
		}
		
		Organization dept = orgService.findOne(vo.getDeptId());
		AuxiliaryAttr assetType = attrService.get(vo.getAssetTypeId());
		FiscalAccountingSubject assetSubject = null;
		FiscalAccountingSubject depreciationSubject = null;
		FiscalAccountingSubject paymentSubject = null;
		FiscalAccountingSubject clearSubject = null;
		FiscalAccountingSubject expenseSubject = null;
		
		if(StringUtils.isNotBlank(vo.getAssetSubjectId())){
			assetSubject = subjectService.get(vo.getAssetSubjectId());
		}
		
		if(StringUtils.isNotBlank(vo.getDepreciationSubjectId())){
			depreciationSubject = subjectService.get(vo.getDepreciationSubjectId());
		}
		
		if(StringUtils.isNotBlank(vo.getPaymentSubjectId())){
			paymentSubject = subjectService.get(vo.getPaymentSubjectId());
		}
		
		if(StringUtils.isNotBlank(vo.getClearSubjectId())){
			clearSubject = subjectService.get(vo.getClearSubjectId());
		}
		
		if(StringUtils.isNotBlank(vo.getExpenseSubjectId())){
			expenseSubject = subjectService.get(vo.getExpenseSubjectId());
		}
		
		Asset entity = null;
		if(StringUtils.isBlank(vo.getFid())){
			entity = new Asset();
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setCreateTime(new Date());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setDep(SecurityUtil.getCurrentDept());
			entity.setResidualValue(vo.getResidualValue().multiply(vo.getQuentity()));
		}else {
			entity = assetRepository.findOne(vo.getFid());
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			if(!checkUpdateTime(vo.getUpdateTime(), entity.getUpdateTime())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "数据已被其他用户修改，请刷新再试");
			}
			if(!isEnableEdit(vo.getFid())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "固定资产已生成凭证，不允许修改!");
			}
		}
		
		if(Strings.isNullOrEmpty(vo.getFid()) || entity.getRecordStatus()==Asset.STATUS_UNAUDIT){
			entity.setAssetCode(vo.getAssetCode());
			entity.setAssetName(vo.getAssetName());
			entity.setQuentity(vo.getQuentity());
			entity.setInitialValue(vo.getInitialValue());
			entity.setDiscountYear(vo.getDiscountYear());
			entity.setResidualRate(vo.getResidualRate());
			entity.setSupplier(vo.getSupplier());
			entity.setGrade(vo.getGrade());
			entity.setManufactor(vo.getManufactor());
			entity.setModel(vo.getModel());
			entity.setUseDate(DateUtilTools.string2Date(vo.getUseDate(), DATE));
			entity.setBuyDate(DateUtilTools.string2Date(vo.getBuyDate(), DATE));
			entity.setShareDate(DateUtilTools.string2Date(vo.getShareDate(), DATE));
			entity.setDept(dept);
			entity.setAssetType(assetType);
		}
		
		entity.setAssetSubject(assetSubject);
		entity.setDepreciationSubject(depreciationSubject);
		entity.setPaymentSubject(paymentSubject);
		entity.setClearSubject(clearSubject);
		entity.setExpenseSubject(expenseSubject);
		
		assetRepository.save(entity);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 检查是否能新增修改
	 * @return
	 */
	private RequestResult checkSave(AssetVo vo){
		if(StringUtils.isBlank(vo.getDeptId())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "部门必填");
		}
		
		if(StringUtils.isBlank(vo.getAssetTypeId())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "资产类型必填");
		}
		
		if(StringUtils.isBlank(vo.getAssetCode())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "资产编号必填");
		}else{
			Long count = assetRepository.countByCode(SecurityUtil.getFiscalAccountId(),vo.getAssetCode(),vo.getFid());
			if(count>0)return new RequestResult(RequestResult.RETURN_FAILURE, "资产编号重复");
		}
		
		if(BigDecimal.ONE.compareTo(vo.getResidualRate())<0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "残值率最大为1");
		}
		
		if(vo.getDiscountYear()<=0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "折旧年限要大于0");
		}
		
		if(BigDecimal.ZERO.compareTo(vo.getQuentity())>=0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "数量要大于0");
		}
		
		if(StringUtils.isNotBlank(vo.getBuyDate()) && StringUtils.isNotBlank(vo.getUseDate())){
			Date buyDate = DateUtilTools.string2Date(vo.getBuyDate(), DATE);
			Date useDate = DateUtilTools.string2Date(vo.getUseDate(), DATE);
			if(buyDate.compareTo(useDate)>0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "使用日期不能前于购买日期");
			}
		}
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 审核
	 * @return
	 */
	@Transactional
	public RequestResult savePassAudit(String fid){
		Asset asset = assetRepository.findOne(fid);
		if(asset.getRecordStatus()!=Asset.STATUS_UNAUDIT){
			return new RequestResult(RequestResult.RETURN_FAILURE, "不是未审核状态不能审核");
		}
		if(asset.getBuyDate()==null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "购买日期不能为空");
		}
		asset.setRecordStatus(Asset.STATUS_AUDIT);
		asset.setAuditor(SecurityUtil.getCurrentUser());
		asset.setAuditTime(new Date());
		
		//资产净值＝（数量*资产原值－累计已提折旧－资产清算金额）/ 数量
		asset.setResidualValue(asset.getInitialValue().multiply(asset.getQuentity()));
		assetRepository.save(asset);
		
		detailWebService.savePassAudit(asset);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取累计已提折旧
	 * @param asset
	 * @return
	 */
	private BigDecimal getAccruedValue(Asset asset){
		List<AssetDetail> details = asset.getDetails();
		
		BigDecimal accruedValue = BigDecimal.ZERO;
		for(AssetDetail detail:details){
			if(detail.getType()==AssetDetail.TYPE_DEPRECIATION){
				accruedValue = accruedValue.add(detail.getAmount());
			}
		}
		return accruedValue;
	}
	
	
	/**
	 * 反审核
	 * @return
	 */
	public RequestResult saveCancleAudit(String fid){
		
		String accountId = SecurityUtil.getFiscalAccountId();
		Asset asset = assetRepository.findOne(fid);
		
		if(asset.getRecordStatus()!=Asset.STATUS_AUDIT){
			return new RequestResult(RequestResult.RETURN_FAILURE, "不是审核状态不能取消审核");
		}
		List<AssetDetail> details = asset.getDetails();
		List<AssetDetail> deleteRecords = Lists.newArrayList();
		
		for(AssetDetail detail:details){
			if(detail.getType()==AssetDetail.TYPE_DEPRECIATION
					||detail.getType()==AssetDetail.TYPE_CLEAR){
				return new RequestResult(RequestResult.RETURN_FAILURE, "有计提或清算的记录不能取消审核");
			}else if(detail.getType()==AssetDetail.TYPE_BUY){
				
				VoucherBill record = voucherBillService.getRecord(detail.getFid(),
						WarehouseBuilderCodeHelper.gdzc, accountId);
				if(record != null){
					return new RequestResult(RequestResult.RETURN_FAILURE, "类型为资产购入的记录已生成凭证，不能取消审核");
				}
				deleteRecords.add(detail);
			}
			
		}
		
		asset.setRecordStatus(Asset.STATUS_UNAUDIT);
		asset.setAuditTime(null);
		asset.setAuditor(null);
		assetRepository.save(asset);
		
		detailWebService.saveCancleAudit(deleteRecords);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 计提
	 * @return
	 */
	public RequestResult saveAccrued(String fid){
		Asset asset = assetRepository.findOne(fid);
		
		if(asset.getRecordStatus()!=Asset.STATUS_AUDIT &&
				asset.getRecordStatus()!=Asset.STATUS_ACCRUED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "状态非审核或计提中不能计提");
		}
		
		saveAccrued(asset, true);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 清算
	 * @return
	 */
	public RequestResult saveClear(String fid){
		Asset asset = assetRepository.findOne(fid);
		
		List<Short> statusList = Lists.newArrayList(
				Asset.STATUS_AUDIT, 
				Asset.STATUS_ACCRUED,
				Asset.STATUS_ACCRUED_COMPLETE);
		
		if(!statusList.contains(asset.getRecordStatus())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "状态非审核、计提中、计提完成不能进行清算");
		}
		
		Date max = detailWebService.getMaxDate(asset.getFid(), AssetDetail.TYPE_DEPRECIATION);
		Date now = new Date();
		Date clearDate = null;
		if(max!=null && max.compareTo(now)>0){
			Calendar cal = Calendar.getInstance();
			cal.setTime(max);
			int maxMonth = cal.get(Calendar.MONTH);
			
			cal.setTime(now);
			int nowMonth = cal.get(Calendar.MONTH);
			if(maxMonth==nowMonth){
				clearDate = max;
			}else{
				return new RequestResult(RequestResult.RETURN_FAILURE, "最大的计提日期大于当前日期，且计提月份不相等，不能清算");
			}
		}else{
			clearDate = now;
		}
		
		asset.setRecordStatus(Asset.STATUS_CLEAR);
		asset.setResidualValue(BigDecimal.ZERO);
		assetRepository.save(asset);
		
		detailWebService.saveClear(asset, clearDate);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 计提
	 * @param asset
	 */
	private AssetDetail saveAccrued(Asset asset, boolean residualValue){
		Long count = detailWebService.countByType(asset.getFid(), AssetDetail.TYPE_DEPRECIATION)+1;
		if(count==asset.getDiscountYear()*12){
			asset.setRecordStatus(Asset.STATUS_ACCRUED_COMPLETE);
		}else{
			asset.setRecordStatus(Asset.STATUS_ACCRUED);
		}
		
		AssetDetail detail = detailWebService.saveAccrued(asset, residualValue);
		
		assetRepository.save(asset);
		return detail;
	}


	/**
	 * 全计
	 * @return
	 */
	public RequestResult saveAccruedAll() {
		
		List<Short> statusList = Lists.newArrayList(Asset.STATUS_AUDIT, Asset.STATUS_ACCRUED);
		List<Asset> assets = assetRepository.queryByStatus(SecurityUtil.getFiscalAccountId(), statusList);
		
		for(Asset asset:assets){
			if(asset.getShareDate()==null)continue;
			
			Date max = detailWebService.getMaxDate(asset.getFid(), AssetDetail.TYPE_DEPRECIATION);
			
			int month = DateUtilTools.getDateElement(Calendar.MONTH);
			int year = DateUtilTools.getDateElement(Calendar.YEAR);

			int dataMonth = 0;
			int dataYear = 0;

			boolean first = false;

			if(max!=null){
				dataMonth = DateUtilTools.getDateElement(Calendar.MONTH, max);
				dataYear = DateUtilTools.getDateElement(Calendar.YEAR, max);
				if(year==dataYear && month == dataMonth)continue;
			}else{
				dataMonth = DateUtilTools.getDateElement(Calendar.MONTH, asset.getShareDate());
				dataYear = DateUtilTools.getDateElement(Calendar.YEAR, asset.getShareDate());
				first = true;
			}

			AssetDetail detail = null;

			Long count = detailWebService.countByType(asset.getFid(), AssetDetail.TYPE_DEPRECIATION);
			Long maxCount = (long) (asset.getDiscountYear()*12);
			
			BigDecimal accruedValue = BigDecimal.ZERO;


			while(((dataMonth== month && dataYear==year && first)||(dataMonth<month && dataYear==year) || (dataYear<year)) && count<maxCount ){
				detail = this.saveAccrued(asset, false);
				count++;
				first = false;
				dataMonth = DateUtilTools.getDateElement(Calendar.MONTH, detail.getDate());
				dataYear = DateUtilTools.getDateElement(Calendar.YEAR, detail.getDate());
				accruedValue = accruedValue.add(detail.getAmount());
			}
			BigDecimal residualValue = asset.getInitialValue().multiply(asset.getQuentity())
					.subtract(accruedValue);
			asset.setResidualValue(residualValue);
			assetRepository.save(asset);
		}
		
		return buildSuccessRequestResult();
	}


	@Override
	public CrudRepository<Asset, String> getRepository() {
		return assetRepository;
	}
	/**
	 * 检查修改时间戳；相同返回true，否则返回false
	 * @param updateTimeStr
	 * @param updateTime
	 * @return
	 */
	public boolean checkUpdateTime(String updateTimeStr, Date updateTime){
		String updateTimeStr2 = DateUtilTools.date2String(updateTime, 
				DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS);
		if(updateTimeStr.equals(updateTimeStr2))return true;
		return false;
	}
	
	/**
	 * 检查是否引用到科目
	 * @return
	 */
	public boolean existRefSubject(String subjectId){
		if(Strings.isNullOrEmpty(subjectId))return false;
		Long count = assetRepository.countBySubjectId(subjectId);
		if(count!=null && count>0)return true;
		return false;
	}
}
