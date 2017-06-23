package cn.fooltech.fool_ops.domain.basedata.service;


import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;


import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.basedata.repository.*;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsVo;
import cn.fooltech.fool_ops.domain.basedata.vo.PurchasePriceVo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceVo;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.repository.FreightAddressRepository;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 货品价格报价 服务类
 */
@Service
public class PurchasePriceService extends BaseService<PurchasePrice, PurchasePriceVo, String> {

	@Autowired
	private FiscalAccountService fiscalAccountService;
    @Autowired
    private PurchasePriceRepository repository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsSpecRepository specRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private FreightAddressRepository freightAddressRepository;
    
    @Autowired
    private AttachService attachService;

    /**
     * 实体转换VO
     * @param entity
     * @return
     */
    @Override
    public PurchasePriceVo getVo(PurchasePrice entity) {
        PurchasePriceVo vo = VoFactory.createValue(PurchasePriceVo.class, entity);

        if(!Strings.isNullOrEmpty(entity.getSupplierId())){
            try {
            Supplier supplier = supplierRepository.findOne(entity.getSupplierId());
            if(null==supplier)supplier=new Supplier();
            vo.setSupplierName(supplier.getName());
            vo.setSupplierId(entity.getSupplierId());
//            vo.setSupplier(supplier);
            }catch (Exception e){

            }
        }

        if(entity.getGoods()!=null){
            try {
            Goods goods = entity.getGoods();
            vo.setGoodsName(goods.getName());
            vo.setGoodsId(goods.getFid());
            vo.setUnitGroupId(goods.getUnitGroup().getFid());    ;
        }catch (Exception e){

        }
        }
        if(!Strings.isNullOrEmpty(entity.getGoodSpecId())){
            try {
            GoodsSpec spec = specRepository.findOne(entity.getGoodSpecId());
            if(null==spec){spec=new GoodsSpec();}
            vo.setGoodSpecId(entity.getGoodSpecId());
            vo.setSpecName(spec.getName());
            }catch (Exception e){

            }
        }
        if(!Strings.isNullOrEmpty(entity.getUnitId())){
            try {
            Unit unit = unitRepository.findOne(entity.getUnitId());
            if(null==unit){unit=new Unit();}
            vo.setUnitName(unit.getName());
            vo.setUnitId(entity.getUnitId());
            }catch (Exception e){

            }
        }

        if(!Strings.isNullOrEmpty(entity.getDeliveryPlace())){
            try {
                FreightAddress freightAddress = freightAddressRepository.findOne(entity.getDeliveryPlace());
                if(null==freightAddress){freightAddress=new FreightAddress();}
                vo.setDeliveryPlaceName(freightAddress.getName());
                vo.setDeliveryPlace(freightAddress.getFid());
            }catch (Exception e){

            }

        }
        return vo;
    }

    @Override
    public CrudRepository<PurchasePrice, String> getRepository() {
        return repository;
    }

    /**
     * 查找分页
     * @param vo
     * @param paramater
     * @return
     */
    public Page<PurchasePriceVo> query(PurchasePriceVo vo, PageParamater paramater){

        String accId = SecurityUtil.getFiscalAccountId();
        String creatorId = SecurityUtil.getCurrentUserId();
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = getPageRequest(paramater, sort);
        Page<PurchasePrice> page = repository.findPageBy(accId, creatorId, vo, pageRequest);

        return getPageVos(page, pageRequest);
    }

    /**
     * 修改或新增
     * @param vo
     * @return
     */
    /**
     * @param vo
     * @return
     */
    @Transactional
    public RequestResult save(PurchasePriceVo vo){

        PurchasePrice entity = null;
        if(Strings.isNullOrEmpty(vo.getId())){
            entity = new PurchasePrice();
            entity.setAccId(SecurityUtil.getFiscalAccountId());
            entity.setCreatorId(SecurityUtil.getCurrentUserId());
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entity.setOrgId(SecurityUtil.getCurrentOrgId());
    		//状态:0-失效1-有效
    		//新增设置默认值:1，暂时不作修改
            entity.setEnable(1);
        }else{
            entity = repository.findOne(vo.getId());

            if(entity.getUpdateTime().compareTo(vo.getUpdateTime())!=0){
                return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
            }
        }
//        entity.setEnable(vo.getEnable());
        entity.setDayEnable(vo.getDayEnable());
        entity.setCode(vo.getCode());
        entity.setBillDate(vo.getBillDate());
        //有效日期默认是7天
        //Date effectiveDate = DateUtilTools.changeDateTime(vo.getBillDate(), 0, 7, 0, 0, 0);
        entity.setEffectiveDate(vo.getEffectiveDate());
        entity.setSupplierId(vo.getSupplierId());
        entity.setGoods(goodsService.findOne(vo.getGoodsId()));
        entity.setGoodSpecId(vo.getGoodSpecId());
        entity.setUnitId(vo.getUnitId());
        entity.setFactoryPrice(vo.getFactoryPrice());
        entity.setTaxPoint(vo.getTaxPoint());
        entity.setAfterTaxPrice(vo.getAfterTaxPrice());
        entity.setPickUpCharge(vo.getPickUpCharge());
        entity.setDeliveryPrice(vo.getDeliveryPrice());
        entity.setDeliveryPlace(vo.getDeliveryPlace());
        entity.setFdescribe(vo.getFdescribe());
        entity.setUpdateTime(new Date());
        repository.save(entity);
        
        String accId = SecurityUtil.getFiscalAccountId();
        String supplierId = vo.getSupplierId();
        String goodsId = vo.getGoodsId();
        String goodSpecId = vo.getGoodSpecId();
        String deliveryPlace = vo.getDeliveryPlace();
        PurchasePrice purchasePrice = null;
        /**
         * 保存时检索相同供应商、货品ID、货品属性ID、发货地且状态=有效的记录，判断两条记录的报价日期较后（如果相同按修改时间来判定），
         * 将日期较前的记录的状态设为失效，日期较后的记录的状态设为有效；
         */
        if(Strings.isNullOrEmpty(goodSpecId)){
        	purchasePrice = queryBySupplier(supplierId, goodsId, deliveryPlace, accId);
        }else{
        	purchasePrice = queryBySupplier(supplierId, goodsId, goodSpecId, deliveryPlace, accId);
        }
        if(purchasePrice!=null){
    		Date billDate = purchasePrice.getBillDate();
			Date billDate2 = entity.getBillDate();
			Date updateTime = purchasePrice.getUpdateTime();
			Date updateTime2 = entity.getUpdateTime();
			if(billDate.compareTo(billDate2)>=0){
				if(updateTime2.compareTo(updateTime)>=0){
					updateById(supplierId, goodsId, goodSpecId, deliveryPlace, accId, purchasePrice.getId());
					purchasePrice.setEnable(1);
					repository.save(purchasePrice);
				}else{
					entity.setEnable(1);
					repository.save(entity);
				}
				
			}else{
				updateById(entity.getSupplierId(), entity.getGoods().getFid(), entity.getGoodSpecId(), entity.getDeliveryPlace(), accId, entity.getId());
				entity.setEnable(1);
				repository.save(entity);
			}
			PurchasePriceVo vo2 = new PurchasePriceVo();
	    	vo2.setBillDate(entity.getBillDate());
	    	vo2.setSupplierId(entity.getSupplierId());
	    	vo2.setGoodsId(entity.getGoods()==null?"":entity.getGoods().getFid());
	    	vo2.setGoodSpecId(entity.getGoodSpecId());
	    	vo2.setDeliveryPlace(entity.getDeliveryPlace());
	    	//设置日状态有效标识(保存，删除操作调用)
	    	setEnableDate(vo2);
        }
        //保存base64格式的图片
        attachService.saveBase64Attach(vo.getBase64Str(), entity.getId());

        Goods goods = entity.getGoods();
        if(goods.getUseFlag()==Goods.USE_FLAG_UNUSED){
            goods.setUseFlag(Goods.USE_FLAG_USED);
            goodsService.save(goods);
        }
        User sender=SecurityUtil.getCurrentUser();
        List<User> receivers=userRepository.findListByTopOrgId(SecurityUtil.getCurrentOrgId());
        String title="新货品报价";
        Supplier supplier=supplierRepository.findOne(entity.getSupplierId());
        String supplierName=supplier.getName();
    	String content="";
    	content=content+DateUtil.format(entity.getBillDate(),"yyyy年MM月dd日")+" ";
    	content=content+supplierName+goods.getName()+"价";
    	if(entity.getFactoryPrice()!=null){
    		content=content+entity.getFactoryPrice()+"元";
    	}else{
    		content=content+entity.getDeliveryPrice()+"元";
    	}
    	if(entity.getTaxPoint()!=null){
    		content+=",税点"+entity.getTaxPoint()+"%";
    	}
    	if(entity.getAfterTaxPrice()!=null){
    		content+=",税后价"+entity.getAfterTaxPrice()+"元";
    	}
    	if(entity.getPickUpCharge()!=null){
    		content+=",(提单费:"+entity.getPickUpCharge()+")";
    	}
    	content=content+",报价人:"+sender.getUserName();
        for(User receiver:receivers){
        	messageService.sendNormalMsg(sender,receiver,title,content,SecurityUtil.getFiscalAccount(),Message.TRIGGER_TYPE_NORMAL_PRICE_NOTIFY);
        }
        return buildSuccessRequestResult(getVo(entity));
    }

    public List<PurchasePriceVo> vagueSearch(PurchasePriceVo vo) {
        String userId = SecurityUtil.getCurrentUserId();

//        String inputType = userAttrService.getInputType(userId);

        List<PurchasePrice> entities = repository.vagueSearch(SecurityUtil.getCurrentOrgId(),
                userId, vo.getSearchKey(), vo.getSearchSize());
        return getVos(entities);
    }
    
	/**
	 * 根据发货地查询记录
	 * @param fid	发货地id
	 * @return
	 */
	public Long queryByDeliveryPlaceCount(String fid){
		return repository.queryByDeliveryPlaceCount(fid);
	};
	/**
	 * 保存时检索相同供应商、货品ID、货品属性ID、发货地且状态=有效的记录
	 * @param supplierId
	 * @param goodsId
	 * @param deliveryPlace
	 * @param accId
	 * @return
	 */
	public PurchasePrice queryBySupplier(String supplierId,String goodsId,String deliveryPlace,String accId){
		String fiscalAccountId = SecurityUtil.getFiscalAccountId();
		return repository.queryBySupplier(supplierId, goodsId, deliveryPlace, fiscalAccountId);
	}
	/**
	 * 保存时检索相同供应商、货品ID、货品属性ID、发货地且状态=有效的记录
	 * @param supplierId
	 * @param goodsId
	 * @param goodSpecId
	 * @param deliveryPlace
	 * @param accId
	 * @return
	 */
	public PurchasePrice queryBySupplier(String supplierId,String goodsId,String goodSpecId,String deliveryPlace,String accId){
		String fiscalAccountId = SecurityUtil.getFiscalAccountId();
		return repository.queryBySupplier(supplierId, goodsId,goodSpecId, deliveryPlace, fiscalAccountId);
	}
	/**
	 * 设置状态为失效，判断条件不为id的数据
	 * @param id
	 */
	@Transactional
	public void updateById(String supplierId,String goodsId,String goodSpecId,String deliveryPlace,String accId,String id){
		if(Strings.isNullOrEmpty(goodSpecId)){
			repository.updateById(id, accId, supplierId, goodsId, deliveryPlace);
		}else{
			repository.updateById(id, accId, supplierId, goodsId, goodSpecId, deliveryPlace);
		}
	}
	/**
	 * 设置日状态有效标识(保存，删除操作调用)
	 * @param vo（只需设置billDate）
	 */
	@Transactional
	public void setEnableDate(PurchasePriceVo vo){
		String accId = SecurityUtil.getFiscalAccountId();
        Sort sort = new Sort(Sort.Direction.DESC, "billDate","updateTime");
        PageRequest request = getPageRequest(new PageParamater(1,Integer.MAX_VALUE,0),sort);
        String creatorId = SecurityUtil.getCurrentUserId();
        Page<PurchasePrice> page = repository.findPageBy(accId, creatorId, vo, request);

        List<PurchasePrice> list = page.getContent();
        PurchasePrice temp = list.get(0);
        temp.setDayEnable(1);
        //设置当前单据日期日有效状态标识
        repository.save(temp);
        String billDate = DateUtils.getStringByFormat(temp.getBillDate(), "yyyy-MM-dd");
        String supplierId = temp.getSupplierId();
        String goodsId = temp.getGoods().getFid();
        String goodSpecId = temp.getGoodSpecId();
        String deliveryPlace = temp.getDeliveryPlace();
//        //设置日状态有效标识为失效，判断条件不为id的数据
        updateEnableDate(accId, temp.getId(),billDate,supplierId,goodsId, goodSpecId,deliveryPlace);
	}
	/**
	 * 设置日状体为失效，判断条件不为id的数据
	 * @param accId
	 * @param fid
	 * @param billDate
	 * @param supplierId
	 * @param goodsId
	 * @param goodSpecId
	 * @param deliveryPlace
	 */
	public void updateEnableDate(String accId,String fid,String billDate,String supplierId,String goodsId,String goodSpecId,String deliveryPlace){
		if(Strings.isNullOrEmpty(goodSpecId)){
			repository.updateEnableDate(accId, fid, billDate, supplierId, goodsId, deliveryPlace);
		}else{
			repository.updateEnableDate(accId, fid, billDate, supplierId, goodsId, goodSpecId, deliveryPlace);
		}
	}
	/**
	 * 每天零时找出过期的报价
	 */
	@Transactional
	public void checkExpiredPurchasePrice(){
		Date date=DateUtil.getSimpleDate(new Date());
		Date yesterday=DateUtil.getYesterday(date);
		List<PurchasePrice> list=repository.findByEffectiveDate(yesterday,date);
		String title="货品价格报价过期";
		User sender=null;
		//String receiver=null;
		String content=null;
		for(PurchasePrice pur:list){
			sender=userRepository.findOne(pur.getCreatorId());
			List<User> receivers=userRepository.findListByTopOrgId(sender.getTopOrg().getFid());
			content=sender.getUserName()+"的"+pur.getGoods().getName();
			if(pur.getGoodSpecId()!=null){
				content=content+"、"+specRepository.findOne(pur.getGoodSpecId()).getName();
			}
			content=content+"报价已过期，请尽快报价。单号:"+pur.getCode();
			for(User receiver:receivers){
				FiscalAccount acc=fiscalAccountService.findOne(pur.getAccId());
				messageService.sendNormalMsg(null,receiver,title,content,acc,Message.TRIGGER_TYPE_NORMAL_PRICE_NOTIFY);
			}			
		}
	}
}
