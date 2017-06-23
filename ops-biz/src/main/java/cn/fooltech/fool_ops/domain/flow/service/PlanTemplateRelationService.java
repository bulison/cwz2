package cn.fooltech.fool_ops.domain.flow.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBill;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBilldetail;
import cn.fooltech.fool_ops.domain.analysis.service.CostAnalysisBillService;
import cn.fooltech.fool_ops.domain.analysis.service.CostAnalysisBilldetailService;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.flow.entity.PlanGoodsDetail;
import cn.fooltech.fool_ops.domain.flow.vo.NoTemplate;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateVo;
import cn.fooltech.fool_ops.utils.ErrorCode;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerSupplierService;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplate;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplateRelation;
import cn.fooltech.fool_ops.domain.flow.repository.PlanTemplateRelationRepository;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateRelationVo;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * 计划模板关联 服务类
 */
@Service
public class PlanTemplateRelationService extends BaseService<PlanTemplateRelation, PlanTemplateRelationVo, String> {

	@Autowired
	private PlanTemplateRelationRepository repository;
	
	@Autowired
	private CostAnalysisBilldetailService detailService;

	@Autowired
	private CostAnalysisBillService costAnalysisBillService;


	@Autowired
	private CustomerSupplierService csvService;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public PlanTemplateRelationVo getVo(PlanTemplateRelation entity) {

		if(entity==null)return null;
		PlanTemplateRelationVo vo = VoFactory.createValue(PlanTemplateRelationVo.class, entity);
		CustomerSupplierView csv = entity.getCsv();
		if(csv!=null){
			vo.setCsvId(csv.getFid());
			vo.setCsvName(csv.getName());
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
		PlanTemplate planTemplate = entity.getPlanTemplate();
		if(planTemplate!=null){
			vo.setPlanTemplateId(planTemplate.getFid());
			vo.setPlanTemplateName(planTemplate.getName());
		}
		AuxiliaryAttr shipmentType = entity.getShipmentType();
		if(shipmentType!=null){
			vo.setShipmentTypeId(shipmentType.getFid());
			vo.setShipmentTypeName(shipmentType.getName());
		}
		AuxiliaryAttr transportType = entity.getTransportType();
		if(transportType!=null){
			vo.setTransportTypeId(transportType.getFid());
			vo.setTransportTypeName(transportType.getName());
		}
		
		return vo;
	}

	@Override
	public CrudRepository<PlanTemplateRelation, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<PlanTemplateRelationVo> query(PlanTemplateRelationVo vo, PageParamater paramater) {
		Sort sort = new Sort(Sort.Direction.DESC, "code");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<PlanTemplateRelation> page = repository.findPageBy(vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

	@Transactional
	public RequestResult saveWithCostAnalyzeBillId(String billId, int templateType , PlanTemplate template){

		CostAnalysisBill bill = costAnalysisBillService.get(billId);

		PlanTemplateRelation entity = new PlanTemplateRelation();
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		entity.setOrg(SecurityUtil.getCurrentOrg());
		entity.setPlanTemplate(template);
		entity.setTemplateType(templateType);

		entity.setDeliveryPlace(bill.getDeliveryPlace());
		entity.setReceiptPlace(bill.getReceiptPlace());

		if(templateType== PlanTemplateVo.TEMPLATE_TYPE_PURCHASE){
			Supplier supplier = bill.getSupplier();
			if(supplier!=null){
				CustomerSupplierView scv = csvService.findOne(supplier.getFid());
				entity.setCsv(scv);
			}
		}else{
			Customer customer = bill.getCustomer();
			if(customer!=null){
				CustomerSupplierView scv = csvService.findOne(customer.getFid());
				entity.setCsv(scv);
			}
		}

		repository.save(entity);
		return buildSuccessRequestResult();
	}

	@Transactional
	public RequestResult saveWithCostAnalyzeBillDetailId(String detailId, int templateType , PlanTemplate template){
		CostAnalysisBilldetail detail = detailService.get(detailId);
		if(detail==null)return buildFailRequestResult("数据有误");

		PlanTemplateRelation entity = new PlanTemplateRelation();
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		entity.setOrg(SecurityUtil.getCurrentOrg());
		entity.setPlanTemplate(template);
		entity.setTemplateType(templateType);

		entity.setDeliveryPlace(detail.getDeliveryPlace());
		entity.setReceiptPlace(detail.getReceiptPlace());
		entity.setShipmentType(detail.getShipmentType());
		entity.setTransportType(detail.getTransportType());

		Supplier supplier = detail.getSupplier();
		if(supplier!=null){
			CustomerSupplierView scv = csvService.findOne(supplier.getFid());
			entity.setCsv(scv);
		}
		repository.save(entity);
		return buildSuccessRequestResult();
	}

	@Transactional
	public RequestResult delete(String fid){
		try {
			repository.delete(fid);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除计划货品失败!");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 设置路径模板按钮：在计划关联模板表查找发货地、收货地、运输方式、装运方式与该路径相同的记录，
	 * 如果找到，取出计划模板ID，对其编辑，
	 * 如果找不到，新增计划模板，新增保存计划模板时把新增的模板ID写入计划关联模板表；
	 * @return
	 */
	public RequestResult settleRouteTemplate(String costBillDetailId){
		CostAnalysisBilldetail detail = detailService.findOne(costBillDetailId);

		if(detail==null)return buildFailRequestResult("数据不存在，可能已被重新生成，请刷新再试");

		String deliveryPlaceId = detail.getDeliveryPlace().getFid();
		String receiptPlaceId = detail.getReceiptPlace().getFid();
		String transportTypeId = detail.getTransportType().getFid();
		String shipmentTypeId = detail.getShipmentType().getFid();

		PlanTemplateRelation relation = repository.findBy(deliveryPlaceId, receiptPlaceId,
				transportTypeId, shipmentTypeId, PlanTemplateVo.TEMPLATE_TYPE_TRANSPORT);
		PlanTemplateRelationVo vo = getVo(relation);

		if(relation==null)return buildFailRequestResult(ErrorCode.FLOW_NOT_EXIST_TEMPLATE,"没有找到模板");
		return buildSuccessRequestResult(vo);
	}

	/**
	 * 判断是会否存在该模板
	 *  @return
	 */
	public PlanTemplateRelation existRouteTemplate(PlanGoodsDetail detail){
		String deliveryPlaceId = detail.getDeliveryPlace().getFid();
		String receiptPlaceId = detail.getReceiptPlace().getFid();
		String transportTypeId = detail.getTransportType().getFid();
		String shipmentTypeId = detail.getShipmentType().getFid();

		PlanTemplateRelation relation = repository.findBy(deliveryPlaceId, receiptPlaceId,
				transportTypeId, shipmentTypeId, PlanTemplateVo.TEMPLATE_TYPE_TRANSPORT);

		return relation;
	}


	/**
	 * 采购模板按钮：在计划关联模板表查找往来单位与采购单位一致的记录，
	 * 如果找到，取出计划模板ID，对其编辑，
	 * 如果找不到，新增计划模板，新增保存计划模板时把新增的模板ID写入计划关联模板表；
	 * @return
	 */
	public RequestResult settlePurchaseTemplate(String costBillId){
		CostAnalysisBill bill = costAnalysisBillService.findOne(costBillId);

		if(bill==null)return buildFailRequestResult("数据不存在，可能已被重新生成，请刷新再试");

		Supplier supplier = bill.getSupplier();
		if(supplier==null)return buildFailRequestResult(ErrorCode.FLOW_NOT_EXIST_SUPPLIER, "供应商不存在");

		String supplierId = supplier.getFid();

		PlanTemplateRelation relation = repository.findBy(supplierId, PlanTemplateVo.TEMPLATE_TYPE_PURCHASE);

		if(relation==null)return buildFailRequestResult(ErrorCode.FLOW_NOT_EXIST_TEMPLATE, "没有找到模板");

		PlanTemplateRelationVo vo = getVo(relation);
		return buildSuccessRequestResult(vo);
	}

	/**
	 * 销售模板按钮：从往来单位地址表查找与收货地相同的记录（可以直接读出主表的客户ID字段），
	 * 如果找不到记录（客户ID为空），提示用户收货地没有对应的客户，请去客户资料设置，
	 * 如果有记录，取出客户ID，在计划关联模板表查找往来单位与客户ID一致的记录，
	 * 		如果找到，取出计划模板ID，对其编辑，
	 * 		如果找不到，新增计划模板，新增保存计划模板时把新增的模板ID写入计划关联模板表；
	 * @return
	 */
	public RequestResult settleSaleTemplate(String costBillId){
		CostAnalysisBill bill = costAnalysisBillService.findOne(costBillId);

		if(bill==null)return buildFailRequestResult("数据不存在，可能已被重新生成，请刷新再试");

		Customer customer = bill.getCustomer();
		if(customer==null)return buildFailRequestResult(ErrorCode.FLOW_NOT_EXIST_CUSTOMER, "用户收货地没有对应的客户，请去客户资料设置");

		String customerId = customer.getFid();

		PlanTemplateRelation relation = repository.findBy(customerId, PlanTemplateVo.TEMPLATE_TYPE_SALE);

		if(relation==null)return buildFailRequestResult(ErrorCode.FLOW_NOT_EXIST_TEMPLATE, "没有找到模板");
		PlanTemplateRelationVo vo = getVo(relation);

		return buildSuccessRequestResult(vo);
	}

	/**
	 * 根据csvId查询采购模板
	 * @param csvId
	 * @return
	 */
	public PlanTemplateRelation findCsvTemplateRelation(String csvId, int templateType){
		return repository.findBy(csvId, templateType);
	}

	/**
	 * 循环判断每一条记录，是否每条记录都已设置采购、运输、销售模板
	 * @return
	 */
	public RequestResult settleAllTemplate(String ids){

		Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
		List<String> idList = splitter.splitToList(ids);

		Set<String> cache = Sets.newHashSet();

		//（1-采购，2-运输，3-销售）
		int purchase = 1,transport = 2, sale =3;

		List<NoTemplate> noTemplates = Lists.newArrayList();

		for(String id:idList){

			CostAnalysisBill bill = costAnalysisBillService.findOne(id);

			if(bill==null)throw new DataNotExistException();

			String goodsName = bill.getGoods().getName();
			String specName = bill.getGoodsSpec()==null?"":bill.getGoodsSpec().getName();
			String deliveryPlace = bill.getDeliveryPlace().getName();
			String receiptPlace = bill.getReceiptPlace().getName();
			String supplierId = bill.getSupplier()==null?"":bill.getSupplier().getFid();
			String customerId = bill.getCustomer()==null?"":bill.getCustomer().getFid();

			//判断采购模板
			RequestResult checkPurchase = this.settlePurchaseTemplate(id);
			if(!checkPurchase.isSuccess() && checkPurchase.getErrorCode()==ErrorCode.FLOW_NOT_EXIST_TEMPLATE){

				//采购key=供应商ID#1
				String key = supplierId+"#"+purchase;
				if(!cache.contains(key)){
					NoTemplate noTemplate = new NoTemplate(purchase, id, goodsName, specName, deliveryPlace, receiptPlace);
					noTemplates.add(noTemplate);
					cache.add(key);
				}

			}

			//判断运输模板
			List<CostAnalysisBilldetail> analysisBilldetails = detailService.findByBillId(id);

			for(CostAnalysisBilldetail detail:analysisBilldetails){
				RequestResult checkRoute = this.settleRouteTemplate(detail.getId());
				if(!checkRoute.isSuccess()){

					//运输key=发货地ID#收货地ID#运输方式ID#装运方式ID#2
					String key = detail.getDeliveryPlace().getFid()+"#"+
							detail.getReceiptPlace().getFid()+"#"+
							detail.getTransportType().getFid()+"#"+
							detail.getShipmentType().getFid()+"#"+transport;

					if(!cache.contains(key)) {
						String _deliveryPlace = detail.getDeliveryPlace().getName();
						String _receiptPlace = detail.getReceiptPlace().getName();

						NoTemplate noTemplate = new NoTemplate(transport, detail.getId(), goodsName, specName, _deliveryPlace, _receiptPlace);
						noTemplates.add(noTemplate);

						cache.add(key);
					}
				}
			}


			//判断销售模板
			RequestResult checkSale = this.settleSaleTemplate(id);
			if(!checkSale.isSuccess() && checkSale.getErrorCode()==ErrorCode.FLOW_NOT_EXIST_TEMPLATE){

				//运输key=客户ID#3
				String key = customerId+"#"+sale;
				if(!cache.contains(key)){
					NoTemplate noTemplate = new NoTemplate(sale, id, goodsName, specName, deliveryPlace, receiptPlace);
					noTemplates.add(noTemplate);

					cache.add(key);
				}
			}
		}

		if(noTemplates.size()>0){
			return buildFailRequestResult(ErrorCode.FLOW_NOT_EXIST_TEMPLATE,
					"模板不存在", noTemplates);
		}else{
			return buildSuccessRequestResult();
		}
	}

	/**
	 * 根据模板删除关联
	 * @param templateId
	 * @return
	 */
	public RequestResult deleteByTemplateId(String templateId){
		repository.deleteByTemplateId(templateId);
		return buildSuccessRequestResult();
	}
}
