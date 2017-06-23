package cn.fooltech.fool_ops.domain.analysis.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cn.fooltech.fool_ops.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBill;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBilldetail;
import cn.fooltech.fool_ops.domain.analysis.repository.CostAnalysisBilldetailRepository;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBilldetailVo;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPrice;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.basedata.service.TransportPriceService;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.report.entity.SysReportSql;
import cn.fooltech.fool_ops.domain.report.entity.UserTemplateDetail;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * 服务类
 */
@Service
public class CostAnalysisBilldetailService
		extends BaseService<CostAnalysisBilldetail, CostAnalysisBilldetailVo, String> {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private CostAnalysisBilldetailRepository repository;
	@Autowired
	private CostAnalysisBillService billService;
	@Autowired
	private TransportPriceService priceService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private FreightAddressService addressService;
	@Autowired
	private AuxiliaryAttrService attrService;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public CostAnalysisBilldetailVo getVo(CostAnalysisBilldetail entity) {
		CostAnalysisBilldetailVo vo = VoFactory.createValue(CostAnalysisBilldetailVo.class, entity);
		 CostAnalysisBill bill = entity.getBill();
		if(bill!=null){
			vo.setBillId(bill.getId());
		}
		TransportPrice transportPrice = entity.getTransportBill();
		if(transportPrice!=null){
			vo.setTransportBillId(transportPrice.getId());
		}
		vo.setBillDate(DateUtils.getStringByFormat(entity.getBillDate(), "yyyy-MM-dd HH:mm:ss"));
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
		FreightAddress deliveryPlace = entity.getDeliveryPlace();
		if(deliveryPlace!=null){
			vo.setDeliveryPlaceId(deliveryPlace.getFid());
			vo.setDeliveryPlaceName(deliveryPlace.getName());
		}
		FreightAddress receiptPlace = entity.getReceiptPlace();
		if(receiptPlace!=null){
			vo.setReceiptPlaceId(receiptPlace.getFid());
			vo.setReceiptPlaceName(receiptPlace.getName());
		}
		AuxiliaryAttr shipmentType = entity.getShipmentType();
		if(shipmentType!=null){
			vo.setShipmentTypeId(shipmentType.getFid());
			vo.setShipmentTypeName(shipmentType.getName());
			
		}
		Supplier supplier = entity.getSupplier();
		if(supplier!=null){
			vo.setSupplierId(supplier.getFid());
			vo.setSupplierName(supplier.getName());
		}
		AuxiliaryAttr transportType = entity.getTransportType();
		if(transportType!=null){
			vo.setTransportTypeId(transportType.getFid());
			vo.setTransportTypeName(transportType.getName());
		}
		AuxiliaryAttr unit = entity.getTransportUnit();
		if(unit!=null){
			vo.setTransportUnitId(unit.getFid());
			vo.setTransportUnitName(unit.getName());
		}
		return vo;
	}

	@Override
	public CrudRepository<CostAnalysisBilldetail, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<CostAnalysisBilldetailVo> query(CostAnalysisBilldetailVo vo, PageParamater paramater) {

		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Sort.Direction.DESC, "billDate");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<CostAnalysisBilldetail> page = repository.findPageBy(accId,vo, pageRequest);
		return getPageVos(page, pageRequest);
	}
	public List<CostAnalysisBilldetailVo> query(String billId) {
		PageParamater paramater = new PageParamater();
		paramater.setPage(1);
		paramater.setStart(0);
		paramater.setRows(Integer.MAX_VALUE);
		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Sort.Direction.DESC, "billDate");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		CostAnalysisBilldetailVo vo = new CostAnalysisBilldetailVo();
		vo.setBillId(billId);
		Page<CostAnalysisBilldetail> page = repository.findPageBy(accId,vo, pageRequest);
		Page<CostAnalysisBilldetailVo> vos = getPageVos(page, pageRequest);
		return vos.getContent();
	}
	/**
	 * 查询报表的所有数据
	 * @param billId	主表id
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryAll(String billId,String sql){
		Query query = em.createNativeQuery(sql);
		query.setParameter("billId", billId);
//		setPageForProcedure(query, 0, Integer.MAX_VALUE);
		List<Object[]> datas = query.getResultList();
		return datas;
	}
	/**
	 * 设置分页参数
	 * @param query
	 * @param start 起始位置
	 * @param maxResult 分页大小
	 */
	private void setPageForProcedure(Query query, int start, int maxResult){
		query.setParameter("COUNTFLAG", 0);
		query.setParameter("START", start);
		query.setParameter("MAXRESULT", maxResult);
	}
	/**
	 * 修改或新增
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(CostAnalysisBilldetailVo vo) {
        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
        }

		CostAnalysisBilldetail entity = null;
		if (Strings.isNullOrEmpty(vo.getId())) {
			entity = new CostAnalysisBilldetail();
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setCreateTime(new Date());
			entity.setUpdateTime(new Date());
			entity.setOrg(SecurityUtil.getCurrentOrg());
		} else {
			entity = repository.findOne(vo.getId());
			if (entity.getUpdateTime().compareTo(DateUtils.getDateFromString(vo.getUpdateTime())) != 0) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
//			Date billDate = DateUtils.getDateFromString(vo.getBillDate());
//			//当前时间，用于修改时判断单据日期是否当天，如果不是，不允许修改记录
//			Date date = DateUtils.getDateFromString(DateUtils.getCurrentDate());
//			
//			if(date.compareTo(billDate)!=0){
//				return buildFailRequestResult("只能修改当天的有效数据!");
//			}
			entity.setUpdateTime(new Date());
		}
		String billId = vo.getBillId();
		CostAnalysisBill analysisBill=null;
		if(!Strings.isNullOrEmpty(billId)){
			analysisBill = billService.findOne(billId);
			entity.setBill(analysisBill);
		}
		entity.setNo(vo.getNo());
		if(!Strings.isNullOrEmpty(vo.getTransportBillId())){
			entity.setTransportBill(priceService.findOne(vo.getTransportBillId()));
		}
		entity.setBillDate(DateUtils.getDateFromString(vo.getBillDate()));
		if(!Strings.isNullOrEmpty(vo.getSupplierId())){
			entity.setSupplier(supplierService.findOne(vo.getSupplierId()));
		}
		if(!Strings.isNullOrEmpty(vo.getDeliveryPlaceId())){
			entity.setDeliveryPlace(addressService.findOne(vo.getDeliveryPlaceId()));
		}
		if(!Strings.isNullOrEmpty(vo.getReceiptPlaceId())){
			entity.setReceiptPlace(addressService.findOne(vo.getReceiptPlaceId()));
		}
		if(!Strings.isNullOrEmpty(vo.getTransportTypeId())){
			entity.setTransportType(attrService.findOne(vo.getTransportTypeId()));
		}
		if(!Strings.isNullOrEmpty(vo.getShipmentTypeId())){
			entity.setShipmentType(attrService.findOne(vo.getShipmentTypeId()));
		}
		if(!Strings.isNullOrEmpty(vo.getTransportUnitId())){
			entity.setTransportUnit(attrService.findOne(vo.getTransportUnitId()));
		}
		//折算运输单价【基本运费】
		BigDecimal basePrice = vo.getBasePrice()==null?BigDecimal.ZERO:vo.getBasePrice();
		//调整折算运输单价【对外基本运费】
		BigDecimal publishBasePrice = vo.getPublishBasePrice()==null?BigDecimal.ZERO:vo.getPublishBasePrice();
		//换算关系  运输单位与货品基本单位的换算关系
		BigDecimal conversionRate2 = vo.getConversionRate()==null?new BigDecimal(1):vo.getConversionRate();
		entity.setConversionRate(conversionRate2);
		//折算运输单价【基本运费】
		entity.setBasePrice(basePrice);
		//调整折算运输单价【对外基本运费】
		entity.setPublishBasePrice(publishBasePrice);
		//运输费用
		entity.setFreightPrice(conversionRate2.multiply(basePrice));
		//调整运输费用
		entity.setPublishFreightPrice(conversionRate2.multiply(publishBasePrice));
		//可执行标识(1-可执行 2-难执行 3-无法执行)
		entity.setExecuteSign(vo.getExecuteSign());
		//预计天数
		entity.setExpectedDays(vo.getExpectedDays());
		//场地费用单价
		entity.setGroundCostPrice(vo.getGroundCostPrice()==null?BigDecimal.ZERO:vo.getGroundCostPrice());
		entity.setRemark(vo.getRemark());//备注

		repository.save(entity);
		/*注：修改明细表对外运费，按换算率自动计算对外基本运费，相反当修改对外基本运费自动计算对外运费；
		举例：如果换算率为28，外运费为2800，则计算对外基本运费=2800/28；
		主表的对外运费由明细表的对外基本运费汇总得出，主表的预计时间由明细表的预计时间汇出得出；
		主表的对外总价=对外出厂价+对外运费；*/
		//注：对外总价==对外总价
		if(analysisBill!=null){
			//明细表总运输费用
			BigDecimal freightPrice = BigDecimal.ZERO;
			//明细表总对外运输费用
			BigDecimal publishFreightPrice = BigDecimal.ZERO;
			//总预计时间
			Integer expectedDays=0;
			//可执行标识最大值,主表可执行标识等于从表可执行标识的最大值
			Integer maxExecuteSign=0;
			List<CostAnalysisBilldetail> list = repository.findByBillId(analysisBill.getId());
			for (CostAnalysisBilldetail billdetail : list) {
				BigDecimal basePrice2 = billdetail.getBasePrice();
				BigDecimal publishBasePrice2 = billdetail.getPublishBasePrice();
				freightPrice=freightPrice.add(basePrice2);
				publishFreightPrice=publishFreightPrice.add(publishBasePrice2);
				//预计时间
				 Integer days = billdetail.getExpectedDays();
				 expectedDays = expectedDays+days;
				 Integer executeSign = billdetail.getExecuteSign();
				 //主表可执行标识等于从表可执行标识的最大值
				 if(executeSign>maxExecuteSign) {
					 maxExecuteSign=executeSign;
				 }
			}
			//设置主表运输费用
			analysisBill.setFreightPrice(freightPrice);
			//设置主表对外运输费用
			analysisBill.setPublishFreightPrice(publishFreightPrice);
			//设置主表预计时间
			analysisBill.setExpectedDays(expectedDays);
			//对外出厂价
			BigDecimal publishFactoryPrice = analysisBill.getPublishFactoryPrice();
			//主表的对外总价=对外出厂价+对外运费；
			BigDecimal publishTotalPrice = publishFactoryPrice.add(publishFreightPrice);
			analysisBill.setPublishTotalPrice(publishTotalPrice);
			//设置主表可执行标识
			analysisBill.setExecuteSign(maxExecuteSign);
			billService.save(analysisBill);

		}
		

		return buildSuccessRequestResult(getVo(entity));
	}
	/**
	 * 	查询其他运输公司在有效期内的报价记录
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<CostAnalysisBilldetailVo> findOtherCompany(CostAnalysisBilldetailVo vo,PageParamater paramater){
		String accId = SecurityUtil.getFiscalAccountId();
		String deliveryPlaceId=vo.getDeliveryPlaceId();
		String receiptPlaceId=vo.getReceiptPlaceId();
		String transportTypeId=vo.getTransportTypeId();
		String shipmentTypeId=vo.getShipmentTypeId();
		String supplierId = vo.getSupplierId();
		Sort sort = new Sort(Sort.Direction.DESC, "billDate");
		PageRequest request = getPageRequest(paramater, sort);
		Page<CostAnalysisBilldetail> page = repository.findOtherCompany(accId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId,supplierId, request);
		return getPageVos(page, request);

		
	}
	/**
	 * 查询运输公司在有效期内的报价记录
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表	
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @return
	 */
	public CostAnalysisBilldetailVo findByCompany(String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String supplierId){
		String accId = SecurityUtil.getFiscalAccountId();
		CostAnalysisBilldetail entity = repository.findByCompany(accId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId, supplierId);
		if(entity!=null){
			return getVo(entity);
		}else{
			return null;
		}
	}
	
	/**
	 * 根据单据ID查询明细
	 * @param billId 单据id
	 * @return
	 */
	public List<CostAnalysisBilldetailVo> findVosByBillId(String billId){
		List<CostAnalysisBilldetail> list = repository.findByBillId(billId);
		List<CostAnalysisBilldetailVo> vos = getVos(list);
		return vos;
	}

	/**
	 * 根据单据ID查询明细
	 * @param billId 单据id
	 * @return
	 */
	public List<CostAnalysisBilldetail> findByBillId(String billId){
		List<CostAnalysisBilldetail> list = repository.findByBillId(billId);
		return list;
	}
}
