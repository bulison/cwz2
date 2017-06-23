package cn.fooltech.fool_ops.domain.basedata.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBilldetail;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBilldetailVo;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPrice;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPriceDetail1;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPriceDetail2;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.SupplierRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportPriceRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportTemplateRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail1Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail2Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceVo;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.repository.FreightAddressRepository;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.domain.transport.service.GoodsTransportService;
import cn.fooltech.fool_ops.domain.transport.vo.GoodsTransportVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
/**
 * 运输费报价 业务类
 * @author cwz
 *
 */
@Service
public class TransportPriceService extends BaseService<TransportPrice, TransportPriceVo, String> {

	@PersistenceContext
	private EntityManager em;
    @Autowired
    private TransportPriceRepository repository;
    
    @Autowired
    private TransportPriceDetail1Service detail1Service;
	/**
	 * 运输费报价(从2表)服务类
	 */
	@Autowired
	private TransportPriceDetail2Service detail2Service;
    @Autowired
    private MessageService messageService;
	/**
	 * 货品运输计价换算关系 
	 */
	@Autowired
	private GoodsTransportService goodsTransportService;
    
    //运输费报价模板 持久层
//    @Autowired
//    private TransportTemplateRepository templateRepository;
//    //运输费报价模板(从1表)
//    @Autowired
//    private TransportTemplateDetail1Service tempDetail1Service;
//    //运输费报价模板(从2表)
//    @Autowired
//    private TransportTemplateDetail2Service tempDetail2Service;
	//收货地址 持久层
    @Autowired
    private FreightAddressRepository addressRepository;
    //辅助属性持久层
    @Autowired
    private AuxiliaryAttrRepository attrRepository;
    //供应商持久层
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttachService attachService;

    @Override
    public TransportPriceVo getVo(TransportPrice entity) {
        TransportPriceVo vo = VoFactory.createValue(TransportPriceVo.class, entity);
        vo.setFid(entity.getId());
        vo.setBillDate(DateUtils.getStringByFormat(entity.getBillDate(), "yyyy-MM-dd"));
        vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
        vo.setEffectiveDate(DateUtils.getStringByFormat(entity.getEffectiveDate(), "yyyy-MM-dd"));
        Supplier supplier = entity.getSupplier();
        Supplier priceUnit =entity.getPriceUnit();
        if(supplier!=null){
        	vo.setSupplierId(supplier.getFid());
        	vo.setSupplierName(supplier.getName());
        }
        if(priceUnit!=null){
        	vo.setPriceUnitId(priceUnit.getFid());
        	vo.setPriceUnitName(priceUnit.getName());
        }
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
        AuxiliaryAttr attr = entity.getTransportType();
        if(attr!=null){
        	vo.setTransportTypeId(attr.getFid());
        	vo.setTransportTypeName(attr.getName());
        }
        AuxiliaryAttr attr2 = entity.getShipmentType();
        if(attr2!=null){
        	vo.setShipmentTypeId(attr2.getFid());
        	vo.setShipmentTypeName(attr2.getName());
        }
        AuxiliaryAttr attr3 = entity.getTransportUnit();
        if(attr3!=null){
        	vo.setTransportUnitId(attr3.getFid());
        	vo.setTransportUnitName(attr3.getName());
        }
        // 组织机构
        Organization org = entity.getOrg();
        if (org != null) {
        	vo.setOrgId(org.getFid());
        	vo.setOrgName(org.getOrgName());
        }
        FiscalAccount fiscalAccount = entity.getFiscalAccount();
        if(fiscalAccount!=null){
        	vo.setFiscalAccountId(fiscalAccount.getFid());
        	vo.setFiscalAccountName(fiscalAccount.getName());
        }
        User user = entity.getCreator();
        if(user!=null){
        	vo.setCreatorId(user.getFid());
        	vo.setCreatorName(user.getUserName());
        }
        return vo;
    }

    @Override
    public CrudRepository<TransportPrice, String> getRepository() {
        return repository;
    }

    /**
     * 运输费报价查找分页
     * @param vo
     * @param paramater
     * @return
     */
    public Page<TransportPriceVo> query(TransportPriceVo vo, PageParamater paramater) {
        Sort sort = new Sort(Sort.Direction.DESC, "billDate");
        PageRequest request = getPageRequest(paramater,sort);
        Page<TransportPrice> page = repository.findPageBy(vo, request);
        Page<TransportPriceVo> pageVos = getPageVos(page, request);
        return pageVos;
    }
    public Page<TransportPriceVo> queryByReport(TransportPriceVo vo, PageParamater paramater,Sort sort) {
//        Sort sort = new Sort(Sort.Direction.DESC, "billDate");
        PageRequest request = getPageRequest(paramater,sort);
        Page<TransportPrice> page = repository.findPageBy(vo, request);
        Page<TransportPriceVo> pageVos = getPageVos(page, request);
        return pageVos;
    }
    
    /**
     * 保存运输费报价
     * @param vo
     * @return
     */
	@Transactional
	public RequestResult save(TransportPriceVo vo) {
//    	List<TransportTemplate> list = templateRepository.findByTemp(transportTypeId, shipmentTypeId, deliveryPlaceId, receiptPlaceId);
//    	if(list==null||list.size()<0) return buildFailRequestResult("找不到运输费报价模板，请先设置模板!"); 
//    	if(list.size()>1) return buildFailRequestResult("运输费报价模版存在多个,请查看运输费报价模版!"); 
    	TransportPrice info = null;
    	Date now = new Date();
    	if(StringUtils.isBlank(vo.getFid())){
    		info = new TransportPrice();
    		info.setCreateTime(now);
    		info.setFiscalAccount(SecurityUtil.getFiscalAccount());
    		info.setCreator(SecurityUtil.getCurrentUser());
    		info.setOrg(SecurityUtil.getCurrentOrg());
    		info.setUpdateTime(now);
    		//状态:0-失效1-有效
    		//新增设置默认值:1，暂时不作修改
    		info.setEnable(1);
    	}else{
    		info = repository.findOne(vo.getFid());
			if (!checkUpdateTime(vo.getUpdateTime(), info.getUpdateTime())) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
    		info.setUpdateTime(now);
    	}
    	info.setCode(vo.getCode());
    	if(StringUtils.isNotBlank(vo.getDeliveryPlaceId())){
    		info.setDeliveryPlace(addressRepository.findOne(vo.getDeliveryPlaceId()));
    	}
    	info.setDescribe(vo.getDescribe());
    	info.setAmount(vo.getAmount());
    	info.setBillDate(DateUtilTools.string2Date(vo.getBillDate()));
    	info.setEffectiveDate(DateUtilTools.string2Date(vo.getEffectiveDate()));
    	info.setExecuteSign(vo.getExecuteSign());
    	info.setExpectedDays(vo.getExpectedDays());
    	if(StringUtils.isNotBlank(vo.getSupplierId())){
    		info.setSupplier(supplierRepository.findOne(vo.getSupplierId()));
    	}
    	if(StringUtils.isNotBlank(vo.getPriceUnitId())){
    		info.setPriceUnit(supplierRepository.findOne(vo.getPriceUnitId()));
    	}
    	info.setExpectedDays(vo.getExpectedDays());
    	if(StringUtils.isNotBlank(vo.getReceiptPlaceId())){
    		info.setReceiptPlace(addressRepository.findOne(vo.getReceiptPlaceId()));
    	}
    	if(StringUtils.isNotBlank(vo.getShipmentTypeId())){
    		info.setShipmentType(attrRepository.findOne(vo.getShipmentTypeId()));
    	}
    	if(StringUtils.isNotBlank(vo.getTransportTypeId())){
    		info.setTransportType(attrRepository.findOne(vo.getTransportTypeId()));
    	}
    	if(!Strings.isNullOrEmpty(vo.getTransportUnitId())){
    		info.setTransportUnit(attrRepository.findOne(vo.getTransportUnitId()));
    	}
    	//保存主表数据
        try {
			repository.save(info);
			if(!Strings.isNullOrEmpty(info.getId())){
				//先把从表记录删除
				detail1Service.delByTempId(info.getId());
				detail2Service.delByTempId(info.getId());
				List<TransportPriceDetail1Vo> details1 = detail1Service.getDetails(vo.getDetails1());
				 //查询从1模版，保存从1表数据
//				List<TransportTemplateDetail1> details = tempDetail1Service.queryByTemplateId(SecurityUtil.getFiscalAccountId(), list.get(0).getId());
				//用户只可以编辑从表1中的金额、预计天数、备注，
				for (TransportPriceDetail1Vo detail1Vo : details1) {
					TransportPriceDetail1 detail1 = new TransportPriceDetail1();
					detail1.setAmount(detail1Vo.getAmount());
					detail1.setExpectedDays(detail1Vo.getExpectedDays());
					detail1.setBill(info);
					detail1.setCreateTime(now);
					detail1.setCreator(SecurityUtil.getCurrentUser());
					String deliveryPlaceId = detail1Vo.getDeliveryPlaceId();
					if(!Strings.isNullOrEmpty(deliveryPlaceId)){
						detail1.setDeliveryPlace(addressRepository.findOne(deliveryPlaceId));
					}
					detail1.setDescribe(detail1Vo.getDescribe());
					detail1.setFiscalAccount(SecurityUtil.getFiscalAccount());
					String receiptPlaceId = detail1Vo.getReceiptPlaceId();
					if(!Strings.isNullOrEmpty(receiptPlaceId)){
						detail1.setReceiptPlace(addressRepository.findOne(receiptPlaceId));
					}
					detail1.setOrg(SecurityUtil.getCurrentOrg());
					String transportTypeId = detail1Vo.getTransportTypeId();
					if(!Strings.isNullOrEmpty(transportTypeId)){
						detail1.setTransportType(attrRepository.findOne(transportTypeId));
					}
					String shipmentTypeId = detail1Vo.getShipmentTypeId();
					if(!Strings.isNullOrEmpty(shipmentTypeId)){
						detail1.setShipmentType(attrRepository.findOne(shipmentTypeId));
					}
					detail1.setUpdateTime(now);
					detail1Service.save(detail1);
					//查询从2模版，保存从2表数据
					//用户只可以编辑从表2中的单位、金额、预计天数、备注；
					List<TransportPriceDetail2Vo> details2 = detail2Service.getDetails(detail1Vo.getDetails2().toString());
					for (TransportPriceDetail2Vo detail2Vo : details2) {
			    		TransportPriceDetail2 detail2= new TransportPriceDetail2();
			    		detail2.setAmount(detail2Vo.getAmount());
			    		detail2.setBill(info);
			    		detail2.setCreateTime(now);
			    		detail2.setCreator(SecurityUtil.getCurrentUser());
//			    		detail2.setDescribe(detail2Vo.getDescribe());
			    		detail2.setDetail1(detail1);
			    		detail2.setFiscalAccount(SecurityUtil.getFiscalAccount());
			    		detail2.setOrg(SecurityUtil.getCurrentOrg());
			    		String transportCostId = detail2Vo.getTransportCostId();
			    		if(!Strings.isNullOrEmpty(transportCostId)){
			    			detail2.setTransportCost(attrRepository.findOne(transportCostId));
			    		}
			    		String transportUnitId = detail2Vo.getTransportUnitId();
			    		if(!Strings.isNullOrEmpty(transportUnitId)){
			    			detail2.setTransportUnit(attrRepository.findOne(transportUnitId));
			    		}
			    	
			    		detail2.setUpdateTime(now);
			    		detail2Service.save(detail2);
					}
				}
			}
			String accId = SecurityUtil.getFiscalAccountId();
	    	String transportTypeId = vo.getTransportTypeId();
	    	String shipmentTypeId = vo.getShipmentTypeId();
	    	String deliveryPlaceId = vo.getDeliveryPlaceId();
	    	String receiptPlaceId = vo.getReceiptPlaceId();
	    	String supplierId = vo.getSupplierId();
	    	String transportUnitId = vo.getTransportUnitId();
	    	//手机端新增运输费报价，如果相同供应商、发货地、收货地、运输方式、装运方式且状态=有效的记录有两条，
	    	//且两条记录的报价日期相同，则需要判断两条记录的修改时间，将日期较前的记录的状态设为失效，日期较后的记录的状态设为有效
	    	TransportPrice price=null;
	    	if(Strings.isNullOrEmpty(supplierId)){
	    		price = repository.findByCompany(accId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId,transportUnitId);
	    	}else{
	    		price = repository.findByCompany(accId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId,supplierId,transportUnitId);
	    	}
	    	if(price!=null){
	    		Date billDate = price.getBillDate();
				Date billDate2 = info.getBillDate();
				Date updateTime = price.getUpdateTime();
				Date updateTime2 = info.getUpdateTime();
				String supplierId2 = info.getSupplier()!=null?info.getSupplier().getFid():"";
				if(billDate.compareTo(billDate2)>=0){
					if(updateTime.compareTo(updateTime2)>=0){
						updateById(accId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId,supplierId,price.getId());
						price.setEnable(1);
						price.setDayEnable(1);
						repository.save(price);
						 //设置日状态有效标识为失效，判断条件不为id的数据
				        updateEnableDate(accId, price.getId(),DateUtils.getStringByFormat(price.getBillDate(), "yyyy-MM-dd"),supplierId,deliveryPlaceId, receiptPlaceId,transportTypeId,shipmentTypeId);
					}else{
						info.setEnable(1);
						info.setDayEnable(1);
						repository.save(info);
						updateEnableDate(accId, info.getId(),DateUtils.getStringByFormat(info.getBillDate(), "yyyy-MM-dd"),supplierId,deliveryPlaceId, receiptPlaceId,transportTypeId,shipmentTypeId);
					}
					
				}else{
					updateById(accId, info.getDeliveryPlace().getFid(), 
							info.getReceiptPlace().getFid(), info.getTransportType().getFid(),
							info.getShipmentType().getFid(),supplierId2,info.getId());
					info.setEnable(1);
					info.setDayEnable(1);
					repository.save(info);
					updateEnableDate(accId, info.getId(),DateUtils.getStringByFormat(info.getBillDate(), "yyyy-MM-dd"),supplierId,deliveryPlaceId, receiptPlaceId,transportTypeId,shipmentTypeId);
				}
//		    	TransportPriceVo vo2 = new TransportPriceVo();
//		    	vo2.setBillDate(DateUtils.getStringByFormat(info.getBillDate(), "yyyy-MM-dd"));
//		    	vo2.setDeliveryPlaceName(info.getDeliveryPlace()==null?"":info.getDeliveryPlace().getName());
//		    	vo2.setReceiptPlaceName(info.getReceiptPlace()==null?"":info.getReceiptPlace().getName());
//		    	vo2.setTransportTypeName(info.getTransportType()==null?"":info.getTransportType().getName());
//		    	vo2.setShipmentTypeName(info.getShipmentType()==null?"":info.getShipmentType().getName());
//		    	vo2.setSupplierName(info.getSupplier()==null?"":info.getSupplier().getName());
//		    	//设置日状态有效标识(保存，删除操作调用)
//		    	setEnableDate(vo2);
	    	}
			
        } catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("保存运输费报价有误!");
		}
        
        //保存base64格式的图片
        attachService.saveBase64Attach(vo.getBase64Str(), info.getId());
        String title="运输费报价";
        User sender=SecurityUtil.getCurrentUser();
        List<User> receivers=userRepository.findListByTopOrgId(SecurityUtil.getCurrentOrgId());
        String content="";
        content=content+DateUtil.format(info.getBillDate(),"yyyy年MM月dd日")+" ";
        content=content+info.getSupplier().getName()+"从"+info.getDeliveryPlace().getName()+"到"+info.getReceiptPlace().getName();
        content=content+"、"+info.getShipmentType().getName()+"、"+info.getTransportType().getName()+"报价"+info.getAmount()+"元(元/"+info.getTransportUnit().getName()+"),报价人:"+sender.getUserName();
        for(User receiver:receivers){
        	messageService.sendNormalMsg(sender,receiver,title,content,SecurityUtil.getFiscalAccount(),Message.TRIGGER_TYPE_NORMAL_PRICE_NOTIFY);
        }
        return buildSuccessRequestResult(getVo(info));
    }
    /**
     * 删除运输报价
     * @param id		编号
     * @param enable	状态
     * @return
     */
    @Transactional
    public RequestResult delete(String id) {
    	try {
			TransportPrice entity = repository.findOne(id);
			if(entity==null) return buildFailRequestResult("数据已失效,请刷新页面"); 
			String supplierId = entity.getSupplier() == null ? "" : entity.getSupplier().getFid();
			String deliveryPlaceId = entity.getDeliveryPlace() == null ? "" : entity.getDeliveryPlace().getFid();
			String receiptPlaceId = entity.getReceiptPlace() == null ? "" : entity.getReceiptPlace().getFid();
			String transportTypeId = entity.getTransportType() == null ? "" : entity.getTransportType().getFid();
			String shipmentTypeId = entity.getShipmentType() == null ? "" : entity.getShipmentType().getFid();
			String transportUnitId = entity.getTransportUnit() == null ? "" : entity.getTransportUnit().getFid();
//			String supplierName = entity.getSupplier() == null ? "" : entity.getSupplier().getName();
//			String deliveryPlaceName = entity.getDeliveryPlace() == null ? "" : entity.getDeliveryPlace().getName();
//			String receiptPlaceName = entity.getReceiptPlace() == null ? "" : entity.getReceiptPlace().getName();
//			String transportTypeName = entity.getTransportType() == null ? "" : entity.getTransportType().getName();
//			String shipmentTypeName = entity.getShipmentType() == null ? "" : entity.getShipmentType().getName();
			String accId = SecurityUtil.getFiscalAccountId();
//			Date billDate = entity.getBillDate();
	    	detail1Service.delByTempId(id);
	    	detail2Service.delByTempId(id);
	    	repository.delete(entity);
	    	TransportPrice price=null;
	    	if(Strings.isNullOrEmpty(supplierId)){
	    		price = repository.findByCompanyByDel(accId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId,transportUnitId);
	    	}else{
	    		price = repository.findByCompanyByDel(accId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId,supplierId,transportUnitId);
	    	}
	    	if(price!=null){
		    	// 删除记录时,更新单据日期最新的记录的状态为有效
		    	price.setEnable(1);
		    	price.setDayEnable(1);
		    	repository.save(price);
		    	//设置日状态有效标识(保存，删除操作调用)
		    	updateEnableDate(accId, price.getId(),DateUtils.getStringByFormat(price.getBillDate(), "yyyy-MM-dd"),supplierId,deliveryPlaceId, receiptPlaceId,transportTypeId,shipmentTypeId);
	    	}
//	    	TransportPriceVo vo = new TransportPriceVo();
//	    	vo.setBillDate(DateUtils.getStringByFormat(billDate, "yyyy-MM-dd"));
//	    	vo.setDeliveryPlaceName(deliveryPlaceName);
//	    	vo.setReceiptPlaceName(receiptPlaceName);
//	    	vo.setTransportTypeName(transportTypeName);
//	    	vo.setShipmentTypeName(shipmentTypeName);
//	    	vo.setSupplierName(supplierName);
//	    	//设置日状态有效标识(保存，删除操作调用)
//	    	setEnableDate(vo);
	 
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除运输报价出错!");
		}
        return buildSuccessRequestResult();
    }

	/**
	 * 查询运输公司在有效期内的报价记录
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表	
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @param supplierId		供应商ID(关联供应商)
	 * @return
	 */
	public TransportPriceVo findByCompany(String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String supplierId,String priceUnitId,String transportUnitId){
		String accId = SecurityUtil.getFiscalAccountId();
		TransportPrice entity=null;
		if(Strings.isNullOrEmpty(supplierId)){
			entity=repository.findByCompany(accId,deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId,transportUnitId);
		}else if(Strings.isNullOrEmpty(priceUnitId)){
			entity=repository.findByCompany(accId,deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId, supplierId,transportUnitId);
		}else{
			entity=repository.findByCompany(accId,deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId, supplierId,priceUnitId,transportUnitId);
		}
		if(entity!=null){
			return getVo(entity);
		}else{
			return null;
		}
		
	}

	/**
	 * 根据发货地查询记录
	 * @param fid 货运地址id
	 * @return
	 */
	public Long queryByDeliveryPlaceCount(String fid){
		return repository.queryByDeliveryPlaceCount(fid);
	};
	
	/**
	 * 根据收货地查询记录
	 * @param fid	货运地址id
	 * @return
	 */
	public Long queryByReceiptPlaceCount(String fid){
		return repository.queryByReceiptPlaceCount(fid);
	};
	/**
	 * 设置状态为失效，判断条件不为id的数据
	 * @param id
	 */
	@Transactional
	public void updateById(String accId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String supplierId,String id){
		if(Strings.isNullOrEmpty(supplierId)){
			repository.updateById(accId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId, id);
		}else{
			repository.updateById(accId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId,supplierId, id);
		}
	}
	/**
	 * 运输费报价分析导出
	 * @param vo
	 * @param response
	 * @return
	 */
	public RequestResult export(TransportPriceVo vo,HttpServletResponse response){
		 String sql="select  a.FCODE,DATE_FORMAT(a.FBILL_DATE,'%Y-%m-%d') as billDate,aa.FNAME as supplierName,b.FNAME as FDELIVERY_PLACE,"
		 			+ " c.FNAME as FRECEIPT_PLACE,d.FNAME as FTRANSPORT_TYPE_ID,dd.FNAME as FSHIPMENT_TYPE_ID,ROUND(a.FAMOUNT,2) FAMOUNT ,a.FEXPECTED_DAYS,"
		 			+ " DATE_FORMAT(a.FEFFECTIVE_DATE,'%Y-%m-%d')as FEFFECTIVE_DATE, "
			 		+ "	CASE WHEN a.FEXECUTE_SIGN=1 THEN '可执行' WHEN A.FEXECUTE_SIGN=2 THEN '难执行' WHEN A.FEXECUTE_SIGN=3 THEN '无法执行' ELSE '' "
			 		+ "	END FEXECUTE_SIGN,e.USER_NAME,a.FDESCRIBE from tsb_transport_price a "
			 		+ "	LEFT JOIN tbd_supplier aa ON a.FSUPPLIER_ID=aa.FID	"
			 		+ " LEFT JOIN tbd_freight_address b ON a.FDELIVERY_PLACE=b.FID "
			 		+ " LEFT JOIN tbd_freight_address c ON a.FRECEIPT_PLACE=c.FID "
			 		+ " LEFT JOIN tbd_auxiliary_attr d ON  a.FTRANSPORT_TYPE_ID=d.fid "
			 		+ " LEFT JOIN tbd_auxiliary_attr dd ON  a.FSHIPMENT_TYPE_ID=dd.fid "
			 		+ " LEFT JOIN smg_tuser e ON  a.FCREATOR_ID=e.fid "	
			 		+ " where a.FACC_ID='"+SecurityUtil.getFiscalAccountId()+"'";
		 if(!Strings.isNullOrEmpty(vo.getStartDay())){
			 sql +=" and a.FBILL_DATE>='"+vo.getStartDay()+"'";
		 }
		 if(!Strings.isNullOrEmpty(vo.getEndDay())){
			 sql +=" and a.FBILL_DATE<='"+vo.getEndDay()+"'";
		 }
		 if(!Strings.isNullOrEmpty(vo.getSupplierName())){
			 sql +=" and aa.FNAME like '%"+vo.getSupplierName()+"%'";
		 }
		 if(!Strings.isNullOrEmpty(vo.getDeliveryPlaceName())){
			 sql +=" and b.FNAME like '%"+vo.getDeliveryPlaceName()+"%'";
		 }
		 if(!Strings.isNullOrEmpty(vo.getReceiptPlaceName())){
			 sql +=" and c.FNAME like '%"+vo.getReceiptPlaceName()+"%'";
		 }
		 if(!Strings.isNullOrEmpty(vo.getTransportTypeName())){
			 sql +=" and d.FNAME like '%"+vo.getTransportTypeName()+"%'";
		 }
		 if(!Strings.isNullOrEmpty(vo.getShipmentTypeName())){
			 sql +=" and dd.FNAME like '%"+vo.getShipmentTypeName()+"%'";
		 }
		 sql +=" ORDER BY a.FBILL_DATE DESC";
		 javax.persistence.Query query = em.createNativeQuery(sql);
		 @SuppressWarnings("unchecked")
		 List<Object[]> datas = query.getResultList();
		 String[] titles={"单号","日期","报价单位","发货地","收货地","装运方式","运输方式","金额","预计时间","有效日期","可执行标识","制单人","备注"};
		 try {
			ExcelUtils.exportExcel(titles, datas, response, "运输费报价分析.xls");
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("运输费报价分析导出异常!");
		}
		return buildSuccessRequestResult();
	
	}

	/**
	 * 设置日状态有效标识为失效，判断条件不为id的数据
	 * @param accId				账套id
	 * @param fid				主键
	 * @param billDate			单据时间
	 * @param supplierId		供应商id
	 * @param deliveryPlaceId	发货地id
	 * @param receiptPlaceId	收货地id
	 * @param transportTypeId	运输方式id
	 * @param shipmentTypeId	装运方式id
	 */
	@Transactional
	public void updateEnableDate(String accId,String fid,String billDate,String supplierId,String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId){
		repository.updateEnableDate(accId, fid, billDate, supplierId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId);
	}
	
	/**
	 * 设置日状态有效标识(保存，删除操作调用)
	 * @param vo（只需设置billDate）
	 */
	@Transactional
	public void setEnableDate(TransportPriceVo vo){
		String accId = SecurityUtil.getFiscalAccountId();
        Sort sort = new Sort(Sort.Direction.DESC, "billDate","updateTime");
        PageRequest request = getPageRequest(new PageParamater(1,Integer.MAX_VALUE,0),sort);
        Page<TransportPrice> page = repository.findPageBy(vo, request);
        List<TransportPrice> list = page.getContent();
        TransportPrice temp = list.get(0);
        temp.setDayEnable(1);
        //设置当前单据日期日有效状态标识
        repository.save(temp);
        String billDate = DateUtils.getStringByFormat(temp.getBillDate(), "yyyy-MM-dd");
        String supplier = temp.getSupplier().getFid();
        String deliveryPlace = temp.getDeliveryPlace().getFid();
        String receiptPlace = temp.getReceiptPlace().getFid();
        String transportType = temp.getTransportType().getFid();
        String shipmentType = temp.getShipmentType().getFid();
        //设置日状态有效标识为失效，判断条件不为id的数据
        updateEnableDate(accId, temp.getId(),billDate,supplier,deliveryPlace, receiptPlace,transportType,shipmentType);
	}
	/**
	 * 	查询其他运输公司在有效期内的报价记录
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<TransportPriceVo> findOtherCompany(TransportPriceVo vo,PageParamater paramater){
		String accId = SecurityUtil.getFiscalAccountId();
		String deliveryPlaceId=vo.getDeliveryPlaceId();
		String receiptPlaceId=vo.getReceiptPlaceId();
		String transportTypeId=vo.getTransportTypeId();
		String shipmentTypeId=vo.getShipmentTypeId();
//		String supplierId = vo.getSupplierId();
		Sort sort = new Sort(Sort.Direction.DESC, "billDate");
		PageRequest request = getPageRequest(paramater, sort);
		Page<TransportPrice> page = repository.findOtherCompany(accId, deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId, request);
		return getPageVos(page, request);
		
	}
    /**
     * 基本运费
     * @param billId			运输报价ID
	 * @param goodsId			货品ID
	 * @param goodSpecId		货品属性ID
	 * @param transportUnitId	运输计价单位ID
	 * @param shipmentTypeId	装运方式ID
	 * @return
     */
    public BigDecimal queryByAmount(String billId,String goodsId,String goodSpecId,String transportUnitId, String shipmentTypeId){
    	//运输费报价从2表运费明细
    	List<TransportPriceDetail2Vo> transportPriceDetail2Vos = detail2Service.query(billId);
    	BigDecimal amountSum = BigDecimal.ZERO;//总的运输费用
    	for (TransportPriceDetail2Vo detail2Vo : transportPriceDetail2Vos) {
    		GoodsTransportVo spec = goodsTransportService.findTopBySpec(goodsId, goodSpecId, detail2Vo.getTransportUnitId(), shipmentTypeId);
    		//举例：如果换算率为28，运费为2800，则计算基本运费=2800/28；
        	if(spec!=null){
        		BigDecimal amount = detail2Vo.getAmount()==null?BigDecimal.ZERO:detail2Vo.getAmount();
        		amountSum=amountSum.add(amount.divide(spec.getConversionRate(),2));
        	}
		}
    	
//    	BigDecimal divide = BigDecimal.ZERO;
//    	divide = amountSum.divide(conversionRate,2);
    	BigDecimal setScale = amountSum.setScale(2, BigDecimal.ROUND_HALF_UP);
		return setScale;
    }
	/**
	 * 每天零时找出过期的报价
	 */
    @Transactional
	public void checkExpiredTransportPrice(){
		Date date=DateUtil.getSimpleDate(new Date());
		Date yesterday=DateUtil.getYesterday(date);
		List<TransportPrice> list=repository.findByEffectiveDate(yesterday,date);
		String title="运输报价过期";
		User sender=null;
		//String receiver=null;
		String content=null;
		for(TransportPrice tr:list){
			sender=tr.getCreator();
			List<User> receivers=userRepository.findListByTopOrgId(sender.getTopOrg().getFid());
			content=sender.getUserName()+"的"+tr.getDeliveryPlace().getName()+"到"+tr.getReceiptPlace().getName()+"、"+tr.getTransportType().getName()+"、"+tr.getShipmentType().getName()+"的运输费报价已过期，请尽快报价。单号:"+tr.getCode();
			for(User receiver:receivers){
				messageService.sendNormalMsg(null,receiver,title,content,tr.getFiscalAccount(),Message.TRIGGER_TYPE_NORMAL_PRICE_NOTIFY);
			}	
		}
	}
}
