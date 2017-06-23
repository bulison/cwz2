package cn.fooltech.fool_ops.domain.flow.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBill;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBilldetail;
import cn.fooltech.fool_ops.domain.analysis.repository.CostAnalysisBilldetailRepository;
import cn.fooltech.fool_ops.domain.analysis.service.CostAnalysisBillService;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsSpecService;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.entity.PlanGoods;
import cn.fooltech.fool_ops.domain.flow.entity.PlanGoodsDetail;
import cn.fooltech.fool_ops.domain.flow.repository.PlanGoodsDetailRepository;
import cn.fooltech.fool_ops.domain.flow.repository.PlanGoodsRepository;
import cn.fooltech.fool_ops.domain.flow.vo.PlanGoodsVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 计划货品服务类
 */
@Service
public class PlanGoodsService extends BaseService<PlanGoods, PlanGoodsVo, String> {

	@Autowired
	private PlanGoodsRepository repository;

	@Autowired
	private PlanGoodsDetailRepository detailRepo;

	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private GoodsSpecService goodsSpecService;
	
	@Autowired
	private PlanService planService;
	
	@Autowired
	private AuxiliaryAttrService auxiliaryAttrService;

	@Autowired
	private CostAnalysisBillService costAnalysisBillService;

	@Autowired
	private CostAnalysisBilldetailRepository costAnalysisDetailRepo;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public PlanGoodsVo getVo(PlanGoods entity) {
		PlanGoodsVo vo = VoFactory.createValue(PlanGoodsVo.class, entity);
		Goods goods = entity.getGoods();
		if(goods!=null){
			vo.setGoodsId(goods.getFid());
			vo.setGoodsName(goods.getName());
		}
		GoodsSpec goodsSpec = entity.getGoodsSpec();
		if(goodsSpec!=null){
			vo.setGoodsSpecId(goodsSpec.getFid());
			vo.setGoodsSpecName(goodsSpec.getName());
		}
		Plan plan = entity.getPlan();
		if(plan!=null){
			vo.setPlanId(plan.getFid());
			vo.setPlanName(plan.getName());
		}

		return vo;
	}

	@Override
	public CrudRepository<PlanGoods, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<PlanGoodsVo> query(PlanGoodsVo vo, PageParamater paramater) {

		Sort sort = new Sort(Sort.Direction.DESC, "createTime");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<PlanGoods> page = repository.findPageBy(vo, pageRequest);
		return getPageVos(page, pageRequest);
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
	 * 根据json字符串保存数据
	 * @param planGoodsJson
	 * @return
	 */
	@Transactional
	public RequestResult save(String planGoodsJson, Plan plan){
		List<PlanGoodsVo> array = JSONArray.parseArray(planGoodsJson, PlanGoodsVo.class);

		List<PlanGoods> datas = Lists.newArrayList();

		for(PlanGoodsVo vo:array){

			String costAnalyzeBillId = vo.getCostAnalyzeBillId();
			CostAnalysisBill bill = costAnalysisBillService.findOne(costAnalyzeBillId);

			List<CostAnalysisBilldetail> details = costAnalysisDetailRepo.findByBillId(costAnalyzeBillId);

			PlanGoods entity = genPlanGoods(vo, bill, plan);

			repository.save(entity);

			List<PlanGoodsDetail> dataDetails = Lists.newArrayList();

			for(CostAnalysisBilldetail detail:details){
				PlanGoodsDetail planGoodsDetail = genPlanGoodsDetail(entity, detail);
				detailRepo.save(planGoodsDetail);

				dataDetails.add(planGoodsDetail);
			}
			entity.setDetails(dataDetails);
			datas.add(entity);
		}

		return buildSuccessRequestResult(datas);
	}

	/**
	 * 生成PlanGoods
	 * @param vo
	 * @param bill
	 * @return
	 */
	private PlanGoods genPlanGoods(PlanGoodsVo vo, CostAnalysisBill bill, Plan plan){
		PlanGoods entity = new PlanGoods();
		entity.setPlan(plan);
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setOrg(bill.getOrg());
		entity.setCreateTime(DateUtilTools.now());
		entity.setFiscalAccount(bill.getFiscalAccount());
		entity.setBillDate(bill.getBillDate());
		entity.setSupplier(bill.getSupplier());
		entity.setCustomer(bill.getCustomer());
		entity.setRoute(bill.getRoute());
		entity.setPurchasePrice(bill.getPurchasePrice());
		entity.setGoods(bill.getGoods());
		entity.setGoodsSpec(bill.getGoodsSpec());
		entity.setUnit(bill.getUnit());

		entity.setDeliveryPlace(bill.getDeliveryPlace());
		entity.setReceiptPlace(bill.getReceiptPlace());
		entity.setFactoryPrice(bill.getFactoryPrice());
		entity.setPublishFactoryPrice(bill.getPublishFactoryPrice());
		entity.setFreightPrice(bill.getFreightPrice());
		entity.setPublishFreightPrice(bill.getPublishFreightPrice());
		entity.setTotalPrice(bill.getTotalPrice());
		entity.setPublishTotalPrice(bill.getPublishTotalPrice());
		entity.setExecuteSign(bill.getExecuteSign());
		entity.setExpectedDays(bill.getExpectedDays());
		entity.setRemark(bill.getRemark());
		entity.setPublish(bill.getPublish());
		entity.setPurchase(bill.getPurchase());

		entity.setGoodsQuentity(vo.getGoodsQuentity());
		entity.setSaleAmount(vo.getSaleAmount());
		entity.setSalePrice(vo.getSalePrice());
		entity.setTransportDate(vo.getTransportDate());

		entity.setStatus(plan.getStatus());

		return entity;
	}

	/**
	 * 生成PlanGoodsDetail
	 * @param planGoods
	 * @param detail
	 * @return
	 */
	private PlanGoodsDetail genPlanGoodsDetail(PlanGoods planGoods, CostAnalysisBilldetail detail){
		PlanGoodsDetail entity = new PlanGoodsDetail();
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setFiscalAccount(detail.getFiscalAccount());
		entity.setOrg(detail.getOrg());
		entity.setCreateTime(DateUtilTools.now());
		entity.setBillDate(detail.getBillDate());
		entity.setSupplier(detail.getSupplier());
		entity.setBill(planGoods);
		entity.setNo(detail.getNo());
		entity.setTransportBill(detail.getTransportBill());

		entity.setDeliveryPlace(detail.getDeliveryPlace());
		entity.setReceiptPlace(detail.getReceiptPlace());
		entity.setTransportType(detail.getTransportType());
		entity.setShipmentType(detail.getShipmentType());
		entity.setTransportUnit(detail.getTransportUnit());

		entity.setFreightPrice(detail.getFreightPrice());
		entity.setPublishFreightPrice(detail.getPublishFreightPrice());
		entity.setConversionRate(detail.getConversionRate());

		entity.setBasePrice(detail.getBasePrice());
		entity.setPublishBasePrice(detail.getPublishBasePrice());

		entity.setExecuteSign(detail.getExecuteSign());
		entity.setExpectedDays(detail.getExpectedDays());
		entity.setRemark(detail.getRemark());

		entity.setGroundCostPrice(detail.getGroundCostPrice());

		return entity;
	}
}
