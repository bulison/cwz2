package cn.fooltech.fool_ops.domain.basedata.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportLoss;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrTypeRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsSpecRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportLossRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportLossVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.repository.FreightAddressRepository;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * 损耗地址service类
 * @author hjr
 * @date 2017/03/13
 */
@Service
public class TransportLossService extends BaseService<TransportLoss,TransportLossVo,String>{
	@Autowired
	private TransportLossRepository transportLossRepository;
	@Autowired
	private AuxiliaryAttrRepository auxiliaryAttrRepository;
	@Autowired
	private GoodsSpecRepository goodsSpecRepository;
	@Autowired
	private GoodsRepository goodsRepository;
/**
 * 实体转换为VO
 */
	@Override
	public TransportLossVo getVo(TransportLoss entity) {
		TransportLossVo vo=new TransportLossVo();
		vo.setFid(entity.getFid());
		vo.setCreateTime(DateUtils.getStringByFormat(entity.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
		if(Strings.isNullOrEmpty(entity.getRemark())){
			vo.setRemark(entity.getRemark());
		}
		vo.setPaymentAmonut(entity.getPaymentAmonut().setScale(2,BigDecimal.ROUND_DOWN));
		//损耗收货地址
		AuxiliaryAttr attr1=entity.getDelivery();
		if(attr1!=null){
			vo.setDelivery(attr1.getName());
			vo.setDeliveryId(attr1.getFid());
		}
		//损耗发货地址
		AuxiliaryAttr attr2=entity.getReceipt();
		if(attr2!=null){
			vo.setReceipt(attr2.getName());
			vo.setReceiptId(attr2.getFid());
		}
		AuxiliaryAttr attr3=entity.getShipment();
		if(attr3!=null){
			vo.setShipment(attr3.getName());
			vo.setShipmentId(attr3.getFid());
		}
		User user=entity.getCreate();
		if(user!=null){
			vo.setCreate(user.getUserName());
			vo.setCreateId(user.getFid());
		}
		FiscalAccount faac=entity.getFiscalAccount();
		if(faac!=null){
			vo.setFiscalAccountId(faac.getFid());
		}
		Organization org=entity.getOrg();
		if(org!=null){
			vo.setOrgId(org.getFid());
		}
		Goods goods=entity.getGoods();
		if(goods!=null){
			vo.setGoods(goods.getName());
			vo.setGoodsId(goods.getFid());
		}
		GoodsSpec gsp=entity.getGoodsSpec();
		if(gsp!=null){
			vo.setGoodsSpec(gsp.getName());
			vo.setGoodsSpecId(gsp.getFid());
		}
		return vo;
	}

	@Override
	public CrudRepository<TransportLoss, String> getRepository() {
		return transportLossRepository;
	}
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<TransportLossVo> query(TransportLossVo vo,PageParamater paramater){
		Sort sort = new Sort(Sort.Direction.ASC, "createTime");
		PageRequest request = getPageRequest(paramater, sort);
		Page<TransportLoss> page=transportLossRepository.findPageBy(vo, request);
		Page<TransportLossVo> pageVos=getPageVos(page,request);
		return pageVos;
		
	}
	/**
	 * 新增/编辑
	 */
	@Transactional
	public RequestResult save(TransportLossVo vo){
		TransportLoss entity=null;
		if(Strings.isNullOrEmpty(vo.getFid())){
			entity=new TransportLoss();
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setCreateTime(new Date());
			entity.setUpdateTime(new Date());
			entity.setCreate(SecurityUtil.getCurrentUser());
		}else{
			entity=transportLossRepository.findOne(vo.getFid());
		}
		if(vo.getPaymentAmonut()==null){
			return buildFailRequestResult("损耗百分比必填");
		}
		if(Strings.isNullOrEmpty(vo.getDeliveryId())){
			return buildFailRequestResult("发货地址必填");
		}
		if(Strings.isNullOrEmpty(vo.getReceiptId())){
			return buildFailRequestResult("收货地址必填");
		}
		if(Strings.isNullOrEmpty(vo.getShipmentId())){
			return buildFailRequestResult("装运方式必填");
		}
		if(Strings.isNullOrEmpty(vo.getGoodsId())){
			return buildFailRequestResult("货品必填");
		}
		BigDecimal hundred=new BigDecimal(100);
		if(!(vo.getPaymentAmonut().abs().compareTo(hundred)==-1)){
			return buildFailRequestResult("损耗百分比不能超过一百");
		}
		entity.setDelivery(auxiliaryAttrRepository.findOne(vo.getDeliveryId()));
		entity.setReceipt(auxiliaryAttrRepository.findOne(vo.getReceiptId()));
		entity.setShipment(auxiliaryAttrRepository.findOne(vo.getShipmentId()));
		entity.setGoods(goodsRepository.findOne(vo.getGoodsId()));	
		if(!Strings.isNullOrEmpty(vo.getRemark())){
			entity.setRemark(vo.getRemark());
		}
		if(!Strings.isNullOrEmpty(vo.getGoodsSpecId())){
			GoodsSpec goodsSpec=goodsSpecRepository.findOne(vo.getGoodsSpecId());
			if(goodsSpec!=null){
				if(goodsSpec.getFlag()==1){
					entity.setGoodsSpec(goodsSpec);
				}else{
					return buildFailRequestResult("货品属性不能选择根节点");
				}
				
			}
		}else if(entity.getGoods().getGoodsSpec()==null){
			entity.setGoodsSpec(null);
		}
		else{
			return buildFailRequestResult("货品有货品属性时,货品属性必填");
		}
//		Goods g=goodsRepository.findOne(vo.getGoodsId());
		
		entity.setPaymentAmonut(vo.getPaymentAmonut());
		String deliveryId=entity.getDelivery().getFid();
		String receiptId=entity.getReceipt().getFid();
		String shipmentId=entity.getShipment().getFid();
		String goodsId=entity.getGoods().getFid();
		String goodsSpec=null;
		if(entity.getGoodsSpec()!=null){
			goodsSpec=entity.getGoodsSpec().getFid();
		}
		TransportLoss loss=null;
		if(!Strings.isNullOrEmpty(vo.getFid())){
			if(goodsSpec!=null){
				loss=transportLossRepository.findLossByCostBillandId(SecurityUtil.getFiscalAccount().getFid(), deliveryId,receiptId, shipmentId, goodsId,goodsSpec,vo.getFid());
			}else{
				loss=transportLossRepository.findLossByCostBillandId(SecurityUtil.getFiscalAccount().getFid(), deliveryId,receiptId, shipmentId, goodsId,vo.getFid());
			}
		}else{
			if(goodsSpec!=null){
				loss=transportLossRepository.findLossByCostBill(SecurityUtil.getFiscalAccount().getFid(), deliveryId,receiptId, shipmentId, goodsId,goodsSpec);
			}else{
				loss=transportLossRepository.findLossByCostBill(SecurityUtil.getFiscalAccount().getFid(), deliveryId,receiptId, shipmentId, goodsId);
			}		
		}

		if(loss!=null){
			if(!Strings.isNullOrEmpty(vo.getFid())){
				if(loss.getFid()!=vo.getFid()){
					return buildFailRequestResult("已经有损耗记录");
				}
			}else{
				return buildFailRequestResult("已经有损耗记录");
			}
			
		}
		transportLossRepository.save(entity);
		return buildSuccessRequestResult(getVo(entity));
	}
	
	
/*	*//**
	 * 修复旧数据(没有损耗地址的数据)
	 * 不需要使用这个接口,只在系统上线需要修复数据的时候使用
	 * @return
	 *//*
	public void repair(){
		List<Organization> list=organizationRepository.queryRootOrganization();
    	for(Organization o:list){
    		List<AuxiliaryAttrType> list1=auxiliaryAttrTypeRepository.queryNotLoss(o.getFid());
    		if(list1.size()==0){
    			AuxiliaryAttrType entity=new AuxiliaryAttrType();
    			entity.setOrg(o);
    			entity.setCode("023");
    			entity.setName("损耗地址");
    			entity.setDescribe("损耗地址");
    			entity.setCreateTime(new Date());
    			entity.setCreator(o.getCreateUser());
    			entity.setEnable((short)1);
    			entity.setTreeFlag((short)0);
    			auxiliaryAttrTypeRepository.save(entity);
    		}
    	}
	}*/
}
