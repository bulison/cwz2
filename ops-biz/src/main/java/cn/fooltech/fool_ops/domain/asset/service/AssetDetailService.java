package cn.fooltech.fool_ops.domain.asset.service;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.asset.entity.Asset;
import cn.fooltech.fool_ops.domain.asset.entity.AssetDetail;
import cn.fooltech.fool_ops.domain.asset.repository.AssetDetailRepository;
import cn.fooltech.fool_ops.domain.asset.vo.AssetDetailVo;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.fooltech.fool_ops.utils.SecurityUtil.getFiscalAccountId;


/**
 * <p>固定资产计提网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-12-28 14:24:15
 */
@Service
public class AssetDetailService extends BaseService<AssetDetail,AssetDetailVo,String> {
	 @Autowired  
     private JdbcTemplate jdbcTemplate;  
	/**
	 * 固定资产计提服务类
	 */
	@Autowired
	private AssetDetailRepository assetDetailRepository;
	
	/**
	 * 凭证、单据关联服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;
	
	/**
	 * 财务会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService periodService;
	
	/**
	 * 固定资产卡片服务类
	 */
	@Autowired
	private AssetService assetService;
	
	/**
	 * 查询固定资产计提列表信息，按照固定资产计提创建时间升序排列<br>
	 * @param assetId
	 */
	public List<AssetDetailVo> query(String assetId){
		List<AssetDetail> entitys = assetDetailRepository.query(assetId);
		return getVos(entitys);
	}
	
	
	/**
	 * 单个固定资产计提实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public AssetDetailVo getVo(AssetDetail entity){
		if(entity == null)
			return null;
		AssetDetailVo vo = new AssetDetailVo();
		vo.setType(entity.getType());
		vo.setDate(DateUtils.getStringByFormat(entity.getDate(), "yyyy-MM-dd"));
		vo.setAmount(entity.getAmount());
		vo.setRemark(entity.getRemark());
		vo.setFid(entity.getFid());
		vo.setAssetId(entity.getAsset().getFid());
		
		return vo;
	}
	
	/**
	 * 单个固定资产计提实体转换为vo
	 * @param entity
	 * @param loadDetail
	 * @return
	 * @author rqh
	 */
	public AssetDetailVo getVo(AssetDetail entity, boolean loadDetail){
		AssetDetailVo vo = getVo(entity);
		if(loadDetail){
			Asset asset = entity.getAsset();
			vo.setAssetCode(asset.getAssetCode());
			vo.setAssetName(asset.getAssetName());
			//部门
			Organization dept = asset.getDept();
			if(dept != null){
				vo.setDeptId(dept.getFid());
				vo.setDeptName(dept.getOrgName());
			}
			//凭证
			Voucher voucher = voucherBillService.getVoucher(entity.getFid(), WarehouseBuilderCodeHelper.gdzc, getFiscalAccountId());
			if(voucher != null){
				AuxiliaryAttr voucherWord = voucher.getVoucherWord();
				vo.setVoucherId(voucher.getFid());
				vo.setVoucherWordNumber(voucherWord.getName() + "-" + voucher.getVoucherNumber());
			}
			//贷方科目
			FiscalAccountingSubject creditSubject = getCreditSubject(asset, entity);
			if(creditSubject != null){
				vo.setCreditSubject(creditSubject.getName());
			}
			//借方科目
			FiscalAccountingSubject debitSubject = getDebitSubject(asset, entity);
			if(debitSubject != null){
				vo.setDebitSubject(debitSubject.getName());
			}
			//资产清算-折旧科目
			if(entity.getType() == AssetDetail.TYPE_SASSET_CLEAR){
				FiscalAccountingSubject depreciationSubject = asset.getDepreciationSubject();
				if(depreciationSubject != null){
					vo.setDepreciationSubject(depreciationSubject.getName());
				}
			}
			
		}
		return vo;
	}
	
	/**
	 * 多个实体转vo
	 * @param entities
	 * @param loadDetail
	 * @return
	 * @author rqh
	 */
	public List<AssetDetailVo> getVos(List<AssetDetail> entities, boolean loadDetail){

		List<AssetDetailVo> vos = entities
				.stream()
				.map(p->getVo(p, loadDetail))
				.collect(Collectors.toList());

		return vos;
	}
	
	/**
	 * 删除固定资产计提<br>
	 */
	@Transactional
	public RequestResult deleteByAssetId(String assetId){
		List<AssetDetail> list = assetDetailRepository.query(assetId);
		if(list!=null&&list.size()>0){
			for (AssetDetail assetDetail : list) {
				try {
					delete(assetDetail);
				} catch (Exception e) {
					e.printStackTrace();
					return buildFailRequestResult("删除固定资产有误");
				}
			}
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取固定资产计提信息
	 * @param fid 固定资产计提ID
	 * @return
	 */
	public AssetDetailVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(assetDetailRepository.findOne(fid));
	}
	

	/**
	 * 新增/编辑固定资产计提
	 * @param vo
	 */
	@Transactional
	public AssetDetail save(AssetDetailVo vo, Asset asset) {
		
		AssetDetail entity = null;
		if(StringUtils.isBlank(vo.getFid())){
			entity = new AssetDetail();
			entity.setCreateTime(asset.getCreateTime());
			entity.setCreator(asset.getCreator());
		}else {
			entity = assetDetailRepository.findOne(vo.getFid());
		}
		
		entity.setAsset(asset);
		entity.setOrg(asset.getOrg());
		entity.setFiscalAccount(asset.getFiscalAccount());
		entity.setType(vo.getType());
		entity.setDate(DateUtils.getDateFromString(vo.getDate()));
		entity.setAmount(vo.getAmount());
		entity.setRemark(vo.getRemark());
		
		assetDetailRepository.save(entity);
		return entity;
	}

	/**
	 * 删除计提
	 * @param fid
	 * @return
	 */
	@Transactional
	public RequestResult delete(String fid) {
		String accountId = getFiscalAccountId();
		
		AssetDetail detail = assetDetailRepository.findOne(fid);
		if(detail.getType()!=AssetDetail.TYPE_CLEAR &&
				detail.getType()!=AssetDetail.TYPE_DEPRECIATION){
			return new RequestResult(RequestResult.RETURN_FAILURE, "只能删除类型为资产折旧和资产清算的记录");
		}
		
		Asset asset = detail.getAsset();
		/* cwz 2017-5-3  1997 固定资产-已经生成了凭证的数据不可以删除（现在可以） start*/
		VoucherBill record = voucherBillService.getRecord(detail.getFid(), 
				WarehouseBuilderCodeHelper.gdzc, accountId);
		if(record != null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "未生成凭证才可以删除");
		}
		
		FiscalPeriod period = periodService.getPeriod(detail.getDate(), accountId);
		if(period!=null && period.getCheckoutStatus()==FiscalPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "财务会计期间未结账才可以删除");
		}
		/* cwz 2017-5-3  1997 固定资产-已经生成了凭证的数据不可以删除（现在可以） end */
		AssetDetail min = detail;
		
		List<AssetDetail> details = asset.getDetails();
		if(detail.getType()==AssetDetail.TYPE_DEPRECIATION){
			for(AssetDetail check:details){
				if(check.getType()==AssetDetail.TYPE_CLEAR){
					return new RequestResult(RequestResult.RETURN_FAILURE, "有资产清算的记录，不能删除资产折旧");
				}
				if(detail.getDate().compareTo(check.getDate())<0){
					return new RequestResult(RequestResult.RETURN_FAILURE, "删除资产折旧记录只能从最后一个日期开始删除");
				}
				if(min.getDate().compareTo(check.getDate())>0 && 
						check.getType() == AssetDetail.TYPE_DEPRECIATION){
					min = check;
				}
			}
			if(min.getFid().equals(detail.getFid())){
				asset.setRecordStatus(Asset.STATUS_AUDIT);
			}else{
				asset.setRecordStatus(Asset.STATUS_ACCRUED);
			}
		}else if(detail.getType()==AssetDetail.TYPE_CLEAR){
			Long count = assetDetailRepository.countByType(asset.getFid(), AssetDetail.TYPE_DEPRECIATION);
			if(count==asset.getDiscountYear()*12){
				asset.setRecordStatus(Asset.STATUS_ACCRUED_COMPLETE);
			}else if(count==0){
				asset.setRecordStatus(Asset.STATUS_AUDIT);
			}else{
				asset.setRecordStatus(Asset.STATUS_ACCRUED);
			}
		}
		
		assetService.save(asset);
		updateAssetValue(asset, detail, null);
		
		assetDetailRepository.delete(detail);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 更新资产净值
	 * @param asset
	 * @param delete
	 */
	@Transactional
	private void updateAssetValue(Asset asset, AssetDetail delete, AssetDetail add){
		
		String deleteFid = "", addFid="";
		if(delete!=null)deleteFid = delete.getFid();
		if(add!=null)addFid = add.getFid();
		
		//资产净值＝数量*资产原值－累计已提折旧 - 清算金额;
		
		BigDecimal accruedValue = BigDecimal.ZERO;//累计已提折旧
		BigDecimal clearValue = BigDecimal.ZERO;//清算金额
		
		List<AssetDetail> details = asset.getDetails();
		for(AssetDetail detail:details){
			if(!detail.getFid().equals(deleteFid) && !detail.getFid().equals(addFid)){
				if(detail.getType()==AssetDetail.TYPE_DEPRECIATION){
					accruedValue = accruedValue.add(detail.getAmount());
				}else if(detail.getType()==AssetDetail.TYPE_CLEAR){
					clearValue = detail.getAmount();
				}
			}
		}
		
		if(add!=null){
			if(add.getType()==AssetDetail.TYPE_DEPRECIATION){
				accruedValue = accruedValue.add(add.getAmount());
			}else if(add.getType()==AssetDetail.TYPE_CLEAR){
				clearValue = add.getAmount();
			}
		}
		
		
		BigDecimal residualValue = asset.getInitialValue().multiply(asset.getQuentity())
				.subtract(accruedValue).subtract(clearValue);
		asset.setResidualValue(residualValue);
		assetService.save(asset);
	}
	
	
	/**
	 * 通过审核
	 */
	@Transactional
	public void savePassAudit(Asset asset){
		AssetDetail detail = new AssetDetail();
		detail.setAmount(asset.getInitialValue().multiply(asset.getQuentity()));
		detail.setAsset(asset);
		detail.setCreateTime(new Date());
		detail.setCreator(SecurityUtil.getCurrentUser());
		detail.setDate(asset.getBuyDate());
		detail.setType(AssetDetail.TYPE_BUY);
		detail.setFiscalAccount(SecurityUtil.getFiscalAccount());
		detail.setOrg(SecurityUtil.getCurrentOrg());
		detail.setRemark("资产购入");
		
		assetDetailRepository.save(detail);
	}
	
	/**
	 * 保存计提
	 */
	@Transactional
	public AssetDetail saveAccrued(Asset asset, boolean residualValue){
		
		// 计提金额＝数量*（资产原值*（1-残值率）/（折旧年限*12））
		BigDecimal accruedAmount = BigDecimal.ONE.subtract(asset.getResidualRate());
		accruedAmount = accruedAmount.multiply(asset.getInitialValue());
		accruedAmount = accruedAmount.multiply(asset.getQuentity());
		accruedAmount = accruedAmount.divide(new BigDecimal(asset.getDiscountYear()*12), 2);
		
		Date next = asset.getShareDate();
		Date maxDate = assetDetailRepository.getMaxDate(asset.getFid(), AssetDetail.TYPE_DEPRECIATION);
		if(maxDate!=null){
			next = getNextMonth(maxDate);
		}
		
		AssetDetail detail = new AssetDetail();
		detail.setAmount(accruedAmount);
		detail.setAsset(asset);
		detail.setCreateTime(new Date());
		detail.setCreator(SecurityUtil.getCurrentUser());
		detail.setDate(next);
		detail.setType(AssetDetail.TYPE_DEPRECIATION);
		detail.setFiscalAccount(SecurityUtil.getFiscalAccount());
		detail.setOrg(SecurityUtil.getCurrentOrg());
		detail.setRemark("资产折旧");
		
		if(residualValue){
			updateAssetValue(asset, null, detail);
		}
		assetDetailRepository.save(detail);
		
		return detail;
	}
	
	/**
	 * 清算
	 * @param asset
	 */
	@Transactional
	public void saveClear(Asset asset, Date clearDate) {
		
		BigDecimal accruedValue = BigDecimal.ZERO;
		List<AssetDetail> details = asset.getDetails();
		for(AssetDetail detail:details){
			if(detail.getType()==AssetDetail.TYPE_DEPRECIATION){
				accruedValue = accruedValue.add(detail.getAmount());
			}
		}
		
		//清算金额＝数量*资产原值－累计已提折旧；
		BigDecimal clearAmount = asset.getInitialValue().multiply(asset.getQuentity());
		clearAmount = clearAmount.subtract(accruedValue);
		
		AssetDetail detail = new AssetDetail();
		detail.setAmount(clearAmount);
		detail.setAsset(asset);
		detail.setCreateTime(new Date());
		detail.setCreator(SecurityUtil.getCurrentUser());
		detail.setDate(clearDate);
		detail.setType(AssetDetail.TYPE_CLEAR);
		detail.setFiscalAccount(SecurityUtil.getFiscalAccount());
		detail.setOrg(SecurityUtil.getCurrentOrg());
		detail.setRemark("资产清算");
		
		updateAssetValue(asset, null, detail);
		assetDetailRepository.save(detail);
	}
	
	/**
	 * 获得当前日期的下一月份
	 * @param date
	 * @return
	 */
	private Date getNextMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		int oldMonth = calendar.get(Calendar.MONTH)+1;
		
		calendar.set(Calendar.MONTH, oldMonth);
		
		int newMonth = calendar.get(Calendar.MONTH);
		if(newMonth>oldMonth){
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.MONTH, oldMonth);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return calendar.getTime();
	}
	
	/**
	 * 反审核
	 */
	@Transactional
	public void saveCancleAudit(List<AssetDetail> deleteRecords){
		for(AssetDetail detail:deleteRecords){
			assetDetailRepository.delete(detail);
		}
	}
	
	/**
	 * 根据类型统计
	 * @param assetId
	 * @param type
	 * @return
	 */
	public Long countByType(String assetId, int type){
		return assetDetailRepository.countByType(assetId, type);
	}

	/**
	 * 根据类型统计
	 * @param assetId
	 * @param type
	 * @return
	 */
	public Date getMaxDate(String assetId, int type) {
		Date maxDate = assetDetailRepository.getMaxDate(assetId, type);
		return maxDate;
	}
	
	/**
	 * 分页查询所有固定资产卡片
	 * @param vo
	 * @param paramater
	 * @author rqh
	 */
	public PageJson queryByPage(AssetDetailVo vo , PageParamater paramater){
		Sort sort = new Sort(Direction.DESC, "asset.assetCode","date");
		if(paramater.getPage()<=0)paramater.setPage(1);
		PageRequest request = getPageRequest(paramater,sort);
		Page<AssetDetail> page = assetDetailRepository.queryByPage(vo, request);
		List<AssetDetail> datas = page.getContent();
		List<AssetDetailVo> vos = getVos(datas, true);
		AssetDetailVo tempvo = new AssetDetailVo();
		tempvo.setAssetName("合计");
		tempvo.setAmount(getSumAmount(vo));
		vos.add(tempvo);
		
		PageJson pageJson = new PageJson();
		pageJson.setRows(vos);
		pageJson.setTotal(page.getTotalElements());
		return pageJson;
	}
	
	/**
	 * 获得查询接口
	 * @param vo
	 * @return
	 */
	private BigDecimal getSumAmount(AssetDetailVo vo){
		String sql="select  sum(d.FAMOUNT) as amount from tbd_asset_detail d,tbd_asset a where a.fid=d.FASSET_ID ";
		sql += " and d.FACC_ID='"+ getFiscalAccountId()+"'";
		//类型
		if(vo.getType() != null){
			sql +=" and d.FTYPE="+vo.getType();
		}
		//日期
		if(!Strings.isNullOrEmpty(vo.getStartDay())){
			sql +=" and FDATE>='"+vo.getStartDay()+"'";
		}
		if(!Strings.isNullOrEmpty(vo.getEndDay())){
			sql +=" and FDATE<='"+vo.getEndDay()+"'";;
		}
		//固定资产编号
		if(StringUtils.isNotBlank(vo.getAssetCode())){
			sql +=" and a.FASSET_CODE like '%"+vo.getAssetCode()+"%'";
		}
		//固定资产名称
		if(StringUtils.isNotBlank(vo.getAssetName())){
			sql +=" and a.FASSET_NAME like '%"+vo.getAssetName()+"%'";
		}
		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		if (map.isEmpty()) return BigDecimal.ZERO;;
		Double count = map.get("amount")==null?0:Double.parseDouble(map.get("amount").toString());
		return new BigDecimal(count);
	}


	@Override
	public CrudRepository<AssetDetail, String> getRepository() {
		return assetDetailRepository;
	}
	/**
	 * 获取贷方科目
	 * @param asset
	 * @param assetDetail
	 * @return
	 */
	public FiscalAccountingSubject getCreditSubject(Asset asset, AssetDetail assetDetail){
		if(assetDetail.getType() == AssetDetail.TYPE_SASSET_BUY){
			return asset.getPaymentSubject();
		}
		else if(assetDetail.getType() == AssetDetail.TYPE_SASSET_CLEAR){
			return asset.getAssetSubject();	
		}
		else if(assetDetail.getType() == AssetDetail.TYPE_DEPRECIATION){
			return asset.getDepreciationSubject();
		}
		else {
			return null;
		}
	}
	
	/**
	 * 获取借方科目
	 * @param asset
	 * @param assetDetail
	 * @return
	 */
	public FiscalAccountingSubject getDebitSubject(Asset asset, AssetDetail assetDetail){
		if(assetDetail.getType() == AssetDetail.TYPE_SASSET_BUY){
			return asset.getAssetSubject();
		}
		else if(assetDetail.getType() == AssetDetail.TYPE_SASSET_CLEAR){
			//清算科目、折旧科目
			return asset.getClearSubject();	
		}
		else if(assetDetail.getType() == AssetDetail.TYPE_DEPRECIATION){
			return asset.getExpenseSubject();
		}
		else {
			return null;
		}
	}
	
	/**
	 * 获取固定资产的资产金额
	 * @param assetId 固定资产ID
	 * @return
	 */
	public BigDecimal getAssetAmount(String assetId, int detailType){
		BigDecimal result = assetDetailRepository.getAssetAmount(assetId, detailType);
		if(result!=null){
			return result;
		}
		return BigDecimal.ZERO;
	}
}
