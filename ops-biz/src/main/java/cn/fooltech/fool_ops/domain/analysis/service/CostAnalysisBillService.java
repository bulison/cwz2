package cn.fooltech.fool_ops.domain.analysis.service;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBill;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBilldetail;
import cn.fooltech.fool_ops.domain.analysis.repository.CostAnalysisBillRepository;
import cn.fooltech.fool_ops.domain.analysis.vo.*;
import cn.fooltech.fool_ops.domain.base.entity.StorageBaseEntity;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.basedata.service.*;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail2Vo;
import cn.fooltech.fool_ops.domain.flow.entity.*;
import cn.fooltech.fool_ops.domain.flow.service.PlanGoodsService;
import cn.fooltech.fool_ops.domain.flow.service.PlanService;
import cn.fooltech.fool_ops.domain.flow.service.PlanTemplateRelationService;
import cn.fooltech.fool_ops.domain.flow.service.TaskService;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateVo;
import cn.fooltech.fool_ops.domain.flow.vo.PlanVo;
import cn.fooltech.fool_ops.domain.flow.vo.TemplateData;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;
import cn.fooltech.fool_ops.domain.transport.entity.GoodsTransport;
import cn.fooltech.fool_ops.domain.transport.entity.TransportBilldetail;
import cn.fooltech.fool_ops.domain.transport.service.GoodsTransportService;
import cn.fooltech.fool_ops.utils.*;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务类
 */
@Service
public class CostAnalysisBillService extends BaseService<CostAnalysisBill, CostAnalysisBillVo, String> {

	@Autowired
	private CostAnalysisBillRepository repository;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsSpecService goodsSpecService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private FreightAddressService addressService;
	@Autowired
	private CostAnalysisBilldetailService billdetailService;

	@Autowired
	private PlanService planService;

	@Autowired
	private PlanGoodsService planGoodsService;

	@Autowired
	private PlanTemplateRelationService planTemplateRelationService;

	@Autowired
	private CustomerAddressService customerAddressService;

	@Autowired
	private TaskService taskService;

	@Autowired
	public TodayStorageDeliveryAnalysisService todayStorageDeliveryAnalysisService;

	@Autowired
	public TodayCostAnalysisService todayCostAnalysisService;

	@Autowired
    private AuxiliaryAttrService auxiliaryAttrService;

	@Autowired
	private GoodsTransportService goodsTransportService;

	@Autowired
	private TransportPriceDetail2Service transportBillDetail2Service;

	@Autowired
	private TransportPriceService transportPriceService;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public CostAnalysisBillVo getVo(CostAnalysisBill entity) {
		if(entity==null) return null;
		CostAnalysisBillVo vo = VoFactory.createValue(CostAnalysisBillVo.class, entity);
		vo.setBillDate(DateUtils.getStringByFormat(entity.getBillDate(), "yyyy-MM-dd HH:mm:ss"));
		vo.setUpdateTime(DateUtils.getStringByFormat(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
		vo.setFactoryPrice(NumberUtil.scale(entity.getFactoryPrice(), 2));
		vo.setPublishFactoryPrice(NumberUtil.scale(entity.getPublishFactoryPrice(), 2));
		vo.setFreightPrice(NumberUtil.scale(entity.getFreightPrice(), 2));
		vo.setPublishFreightPrice(NumberUtil.scale(entity.getPublishFreightPrice(), 2));
		vo.setTotalPrice(NumberUtil.scale(entity.getTotalPrice(), 2));
		vo.setPublishTotalPrice(NumberUtil.scale(entity.getPublishTotalPrice(), 2));
		vo.setLoss(NumberUtil.scale(entity.getLoss(),2));
		FreightAddress deliveryPlace = entity.getDeliveryPlace();
		if(deliveryPlace!=null){
			vo.setDeliveryPlaceId(deliveryPlace.getFid());
			vo.setDeliveryPlaceName(deliveryPlace.getName());
		}
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
		FreightAddress receiptPlace = entity.getReceiptPlace();
		if(receiptPlace!=null){
			vo.setReceiptPlaceId(receiptPlace.getFid());
			vo.setReceiptPlaceName(receiptPlace.getName());
		}
		Supplier supplier = entity.getSupplier();
		if(supplier!=null){
			vo.setSupplierId(supplier.getFid());
			vo.setSupplierName(supplier.getName());
		}
		Unit unit = entity.getUnit();
		if(unit!=null){
			vo.setUnitId(unit.getFid());
			vo.setUnitName(unit.getName());
		}
		Customer customer = entity.getCustomer();
		if(customer!=null){
			String customerId = customer.getFid();
			vo.setCustomerId(customerId);

			//查找客户默认收货地址
			FreightAddressVo defaultAddress = customerAddressService.
					getFreightAddressByCsvId(customerId);
			if(defaultAddress!=null){
				vo.setDefaultAddressId(defaultAddress.getFid());
			}
		}
		PurchasePrice purchasePrice = entity.getPurchasePrice();
		if(purchasePrice!=null){
			vo.setPurchaseDate(DateUtilTools.date2String(purchasePrice.getBillDate()));
			vo.setPurchaseId(purchasePrice.getId());
		}

		return vo;
	}

	@Override
	public CrudRepository<CostAnalysisBill, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@Transactional
	public Page<CostAnalysisBillVo> query(CostAnalysisBillVo vo, PageParamater paramater) {

		if(vo.getPurchase()!=null && vo.getPurchase()==0 && !Strings.isNullOrEmpty(vo.getReceiptPlaceId())){
			todayCostAnalysisService.genRoute();
			todayStorageDeliveryAnalysisService.analysis(vo.getReceiptPlaceId());
		}

		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Sort.Direction.ASC, "goods.name","goodsSpec.name","receiptPlace.name","totalPrice");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<CostAnalysisBill> page = repository.findPageBy(accId,vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

	/**
	 * 修改或新增
	 * 对外出厂价、对外运费、预计时间、备注等信息进行修改保存；
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(CostAnalysisBillVo vo) {
        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
        }
		CostAnalysisBill entity = null;
		if (Strings.isNullOrEmpty(vo.getId())) {
			entity = new CostAnalysisBill();
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
//			if(date.compareTo(billDate)!=0){
//				return buildFailRequestResult("只能修改当天的有效数据!");
//			}
			entity.setUpdateTime(new Date());
		}
//		entity.setRoute(vo.getRoute());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setBillDate(DateUtils.getDateFromString(vo.getBillDate()));
		if(!Strings.isNullOrEmpty(vo.getSupplierId())){
			entity.setSupplier(supplierService.findOne(vo.getSupplierId()));
		}
		if(!Strings.isNullOrEmpty(vo.getGoodsId())){
			entity.setGoods(goodsService.findOne(vo.getGoodsId()));
		}
		
		if(!Strings.isNullOrEmpty(vo.getGoodsSpecId())){
			entity.setGoodsSpec(goodsSpecService.findOne(vo.getGoodsSpecId()));
		}
		if(!Strings.isNullOrEmpty(vo.getUnitId())){
			entity.setUnit(unitService.findOne(vo.getUnitId()));
		}
		if(!Strings.isNullOrEmpty(vo.getDeliveryPlaceId())){
			entity.setDeliveryPlace(addressService.findOne(vo.getDeliveryPlaceId()));
		}
		if(!Strings.isNullOrEmpty(vo.getReceiptPlaceId())){
			entity.setReceiptPlace(addressService.findOne(vo.getReceiptPlaceId()));
		}
		//出厂价
		BigDecimal publishFactoryPrice = vo.getPublishFactoryPrice()==null?BigDecimal.ZERO:vo.getPublishFactoryPrice();
		entity.setFactoryPrice(vo.getFactoryPrice());
		entity.setPublishFactoryPrice(publishFactoryPrice);
		//运输费用
		entity.setFreightPrice(vo.getFreightPrice());
		BigDecimal publishFreightPrice =vo.getPublishFreightPrice()==null?BigDecimal.ZERO:vo.getPublishFreightPrice();
		entity.setPublishFreightPrice(publishFreightPrice);
		entity.setTotalPrice(vo.getTotalPrice());
		//对外总价=对外出厂价+对外运费
		entity.setPublishTotalPrice(publishFactoryPrice.add(publishFreightPrice));
		entity.setExecuteSign(vo.getExecuteSign());
		//预计时间
		entity.setExpectedDays(vo.getExpectedDays());
		//备注
		entity.setRemark(vo.getRemark());
		entity.setPurchase(vo.getPurchase());
		entity.setPublish(vo.getPublish()==null?0:vo.getPublish());
		repository.save(entity);

		return buildSuccessRequestResult();
	}

	/**
	 * 导出成本分析数据
	 * @param costAnalysisBillVoList	主表集合
	 * @param response http对象
	 */
	public RequestResult export(List<CostAnalysisBillVo> costAnalysisBillVoList, HttpServletResponse response){
        //需要导出的数据集
        Map<String, List<Object[]>> voFeeData = Maps.newHashMap();
		Map<String, Map<String, BigDecimal>> voFeeMap = Maps.newHashMap();
		//柜重【列】(动态拼凑)，根据主表查询明细表，取明细表中最大值。
        Map<String, String> gzMap = Maps.newHashMap();

		String cateName = "运输费用";
        List<AuxiliaryAttr> attrList = auxiliaryAttrService.getAuxiliaryAttrByCate(
                cateName, SecurityUtil.getCurrentOrgId(), SecurityUtil.getFiscalAccountId());
        List<String> feeName = attrList.stream().map(StorageBaseEntity::getName).collect(Collectors.toList());
        String[] feeNameArray = feeName.toArray(new String[feeName.size()]);

		for (CostAnalysisBillVo costAnalysisBillVo: costAnalysisBillVoList) {
			//通过成本分析表id查询明细
			String sql = "select CONCAT(c.FNAME, IFNULL(s.FNAME,\"\")) '货品名称', a.FFACTORY_PRICE '出厂价', a.FFREIGHT_PRICE '运输费用单价', a.FLOSS '损耗', a.FTOTAL_PRICE '成本总价' \n" +
                    "from tsb_cost_analysis_bill a\n" +
                    "INNER JOIN tsb_cost_analysis_billdetail b on a.FID=b.FBILL_ID\n" +
                    "INNER JOIN tbd_goods c on a.FGOODS_ID=c.FID \n" +
                    "LEFT JOIN tbd_good_spec s on a.FGOODS_SPEC_ID=s.FID\n" +
                    "where  FBILL_ID= :billId\n" +
                    " GROUP BY a.FID";

			//查询运费报价
            String priceSql = "select a.FNAME, d2.FAMOUNT, G.FCONVERSION_RATE\n" +
					"from \n" +
					"tsb_cost_analysis_bill b,\n" +
					"tsb_transport_price_detail2 d2, \n" +
					"tsb_cost_analysis_billdetail c, \n" +
					"tbd_auxiliary_attr a,\n" +
					"tbd_goods_transport g\n" +
					"where b.fid = :billId\n" +
					"AND c.FTRANSPORT_BILL_ID = d2.FBILL_ID\n" +
					"AND a.FID = d2.FTRANSPORT_COST_ID\n" +
					"AND c.FBILL_ID = b.fid\n" +
					"AND g.FSHIPMENT_TYPE_ID = c.FSHIPMENT_TYPE_ID\n" +
					"AND g.FTRANSPORT_UNIT_ID = d2.FTRANSPORT_UNIT_ID\n" +
					"AND g.FGOODS_ID = b.FGOODS_ID\n" +
					"AND ((g.FGOOD_SPEC_ID = b.FGOODS_SPEC_ID) or (g.FGOOD_SPEC_ID is null and b.FGOODS_SPEC_ID is null));";

            List<Object[]> queryAll = billdetailService.queryAll(costAnalysisBillVo.getId(), sql);
			List<Object[]> priceList = billdetailService.queryAll(costAnalysisBillVo.getId(), priceSql);

			Map<String,BigDecimal> feeMap = Maps.newHashMap();
			for (Object[] price : priceList) {
				BigDecimal b = new BigDecimal(String.valueOf(price[1]));
				BigDecimal fee = new BigDecimal(price[1].toString()).divide(new BigDecimal(price[2].toString()), 2, BigDecimal.ROUND_HALF_UP);
				if (feeMap.containsKey(price[0].toString())) {
					feeMap.put(price[0].toString(), feeMap.get(price[0].toString()).add(fee));
				} else {
					feeMap.put(price[0].toString(), fee);
				}
			}

			for (Object[] obj : queryAll) {
				obj[1] = NumberUtil.scale(new BigDecimal(obj[1].toString()), 2) + "";
				obj[2] = NumberUtil.scale(new BigDecimal(obj[2].toString()), 2) + "";
				obj[3] = NumberUtil.scale(new BigDecimal(obj[3].toString()), 2) + "";
				obj[4] = NumberUtil.scale(new BigDecimal(obj[4].toString()), 2) + "";
			}
			voFeeMap.put(costAnalysisBillVo.getId(), feeMap);
            voFeeData.put(costAnalysisBillVo.getId(), queryAll);

            //柜重：取最大的换算关系当作柜重费用(取从表最大值) 保留2位有效小数
            BigDecimal conversionRate = BigDecimal.ZERO;
            List<CostAnalysisBilldetailVo> billDetailVoList = billdetailService.query(costAnalysisBillVo.getId());
            for (CostAnalysisBilldetailVo detail: billDetailVoList) {
                if (conversionRate.compareTo(detail.getConversionRate()) < 0) conversionRate = detail.getConversionRate();
            }
            gzMap.put(costAnalysisBillVo.getId(), NumberUtil.scale(conversionRate, 2) + "");
		}

        List<Object[]> exportDataList = Lists.newArrayList();
		for (CostAnalysisBillVo vo: costAnalysisBillVoList) {
            Object[] extRow = new Object[feeNameArray.length];
            Map<String, BigDecimal> fee = voFeeMap.get(vo.getId());
            if (fee.size() > 0) {
                for (int i=0; i< extRow.length; i++) {
                    extRow[i] = (fee.get(feeNameArray[i]) == null)? "" : NumberUtil.scale(fee.get(feeNameArray[i]), 2) + "";
                }
            }
            for (Object[] mainRow : voFeeData.get(vo.getId())) {
                // 固定列数据 + 动态列数据
				Object[] row = new Object[mainRow.length + feeNameArray.length + 1];
				System.arraycopy(mainRow, 0, row, 0, 2);
				System.arraycopy(mainRow, 2, row, feeNameArray.length + 3, 3);
				System.arraycopy(extRow, 0, row, 2, extRow.length);
				row[extRow.length + 2]  = gzMap.get(vo.getId());
                exportDataList.add(row);
            }
        }

        String[] comTitle = {"货品名称", "出厂价"};
		String[] comTitle2 = {"柜重", "运输费用单价", "损耗", "成本总价"};
        String[] exportTitle = Arrays.copyOf(comTitle, comTitle.length + feeNameArray.length + comTitle2.length);
        System.arraycopy(feeNameArray, 0, exportTitle, comTitle.length, feeNameArray.length);
        System.arraycopy(comTitle2, 0, exportTitle, comTitle.length + feeNameArray.length, comTitle2.length);

		try {
			ExcelUtils.exportExcel(exportTitle, exportDataList, response, "成本分析.xls");
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("成本分析导出异常");
		}
		return buildSuccessRequestResult();

	}

	/**
	 * 成本分析数据报表
	 * @param costAnalysisBillVoList	主表集合
	 */
	public RequestResult queryFeeReport(List<CostAnalysisBillVo> costAnalysisBillVoList){
		//需要导出的数据集
		Map<String, List<Object[]>> voFeeData = Maps.newHashMap();
		Map<String, Map<String, BigDecimal>> voFeeMap = Maps.newHashMap();
		//柜重【列】(动态拼凑)，根据主表查询明细表，取明细表中最大值。
		Map<String, String> gzMap = Maps.newHashMap();

		String cateName = "运输费用";
		List<AuxiliaryAttr> attrList = auxiliaryAttrService.getAuxiliaryAttrByCate(
				cateName, SecurityUtil.getCurrentOrgId(), SecurityUtil.getFiscalAccountId());
		List<String> feeName = attrList.stream().map(StorageBaseEntity::getName).collect(Collectors.toList());
		String[] feeNameArray = feeName.toArray(new String[feeName.size()]);

		for (CostAnalysisBillVo costAnalysisBillVo: costAnalysisBillVoList) {
			//通过成本分析表id查询明细
			String sql = "select CONCAT(c.FNAME, IFNULL(s.FNAME,\"\")) '货品名称', a.FFACTORY_PRICE '出厂价', a.FFREIGHT_PRICE '运输费用单价', a.FLOSS '损耗', a.FTOTAL_PRICE '成本总价', a.FPURCHASE_ID '货品报价单ID'\n" +
					"from tsb_cost_analysis_bill a\n" +
					"INNER JOIN tsb_cost_analysis_billdetail b on a.FID=b.FBILL_ID\n" +
					"INNER JOIN tbd_goods c on a.FGOODS_ID=c.FID \n" +
					"LEFT JOIN tbd_good_spec s on a.FGOODS_SPEC_ID=s.FID\n" +
					"where  FBILL_ID= :billId\n" +
					" GROUP BY a.FID";

			//查询运费报价
			String priceSql = "select a.FNAME, d2.FAMOUNT, G.FCONVERSION_RATE\n" +
					"from \n" +
					"tsb_cost_analysis_bill b,\n" +
					"tsb_transport_price_detail2 d2, \n" +
					"tsb_cost_analysis_billdetail c, \n" +
					"tbd_auxiliary_attr a,\n" +
					"tbd_goods_transport g\n" +
					"where b.fid = :billId\n" +
					"AND c.FTRANSPORT_BILL_ID = d2.FBILL_ID\n" +
					"AND a.FID = d2.FTRANSPORT_COST_ID\n" +
					"AND c.FBILL_ID = b.fid\n" +
					"AND g.FSHIPMENT_TYPE_ID = c.FSHIPMENT_TYPE_ID\n" +
					"AND g.FTRANSPORT_UNIT_ID = d2.FTRANSPORT_UNIT_ID\n" +
					"AND g.FGOODS_ID = b.FGOODS_ID\n" +
					"AND ((g.FGOOD_SPEC_ID = b.FGOODS_SPEC_ID) or (g.FGOOD_SPEC_ID is null and b.FGOODS_SPEC_ID is null));";

			List<Object[]> queryAll = billdetailService.queryAll(costAnalysisBillVo.getId(), sql);
			List<Object[]> priceList = billdetailService.queryAll(costAnalysisBillVo.getId(), priceSql);

			Map<String,BigDecimal> feeMap = Maps.newHashMap();
			for (Object[] price : priceList) {
				BigDecimal b = new BigDecimal(String.valueOf(price[1]));
				BigDecimal fee = new BigDecimal(price[1].toString()).divide(new BigDecimal(price[2].toString()), 2, BigDecimal.ROUND_HALF_UP);
				if (feeMap.containsKey(price[0].toString())) {
					feeMap.put(price[0].toString(), feeMap.get(price[0].toString()).add(fee));
				} else {
					feeMap.put(price[0].toString(), fee);
				}
			}

			for (Object[] obj : queryAll) {
				obj[1] = NumberUtil.scale(new BigDecimal(obj[1].toString()), 2) + "";
				obj[2] = NumberUtil.scale(new BigDecimal(obj[2].toString()), 2) + "";
				obj[3] = NumberUtil.scale(new BigDecimal(obj[3].toString()), 2) + "";
				obj[4] = NumberUtil.scale(new BigDecimal(obj[4].toString()), 2) + "";
			}
			voFeeMap.put(costAnalysisBillVo.getId(), feeMap);
			voFeeData.put(costAnalysisBillVo.getId(), queryAll);

			//柜重：取最大的换算关系当作柜重费用(取从表最大值) 保留2位有效小数
			BigDecimal conversionRate = BigDecimal.ZERO;
			List<CostAnalysisBilldetailVo> billDetailVoList = billdetailService.query(costAnalysisBillVo.getId());
			for (CostAnalysisBilldetailVo detail: billDetailVoList) {
				if (conversionRate.compareTo(detail.getConversionRate()) < 0) conversionRate = detail.getConversionRate();
			}
			gzMap.put(costAnalysisBillVo.getId(), NumberUtil.scale(conversionRate, 2) + "");
		}

		List<Object[]> exportDataList = Lists.newArrayList();
		for (CostAnalysisBillVo vo: costAnalysisBillVoList) {
			Object[] extRow = new Object[feeNameArray.length];
			Map<String, BigDecimal> fee = voFeeMap.get(vo.getId());
			if (fee.size() > 0) {
				for (int i=0; i< extRow.length; i++) {
					extRow[i] = (fee.get(feeNameArray[i]) == null)? "" : NumberUtil.scale(fee.get(feeNameArray[i]), 2) + "";
				}
			}
			for (Object[] mainRow : voFeeData.get(vo.getId())) {
				// 固定列数据 + 动态列数据
				Object[] row = new Object[mainRow.length + feeNameArray.length + 1];
				System.arraycopy(mainRow, 0, row, 0, 2);
				System.arraycopy(mainRow, 2, row, feeNameArray.length + 3, 4);
				System.arraycopy(extRow, 0, row, 2, extRow.length);
				row[extRow.length + 2]  = gzMap.get(vo.getId());
				exportDataList.add(row);
			}
		}

		String[] comTitle = {"货品名称", "出厂价"};
		String[] comTitle2 = {"柜重", "运输费用单价", "损耗", "成本总价", "货品报价单ID"};
		String[] exportTitle = Arrays.copyOf(comTitle, comTitle.length + feeNameArray.length + comTitle2.length);
		System.arraycopy(feeNameArray, 0, exportTitle, comTitle.length, feeNameArray.length);
		System.arraycopy(comTitle2, 0, exportTitle, comTitle.length + feeNameArray.length, comTitle2.length);

		Map<String, Object> mapExt = Maps.newHashMap();
		mapExt.put("title", exportTitle);
		mapExt.put("data", exportDataList);
		return buildSuccessRequestResult(mapExt);

	}

	/**
	 * 发布
	 * @param fids 主表ids
	 */
	@Transactional
	public RequestResult issue(String fids){
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();

		List<String> fidList = splitter.splitToList(fids);

		try {
			for(String fid:fidList){
				repository.issue(fid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("发布异常!");
		}
		return buildSuccessRequestResult();
	};
	/**
	 * 根据id查询单据信息
	 * @param id
	 * @return	
	 */
	public CostAnalysisBillVo findById(String id){
		CostAnalysisBill findOne = repository.findOne(id);
		if(findOne==null)throw new DataNotExistException();
		CostAnalysisBillVo vo = getVo(findOne);
		List<CostAnalysisBilldetailVo> details = billdetailService.findVosByBillId(id);
		vo.setDetails(JSONArray.fromObject(details).toString());
		return vo;
		
	}
	/**
	 * 折线图
	 * @param vo
	 * @return
	 */
	public  List<CostAnalysisBillVo> findChart(CostAnalysisBillVo vo) {
		String accountId = SecurityUtil.getFiscalAccountId();
		List<CostAnalysisBill> list = repository.findChart(accountId, vo);
		List<CostAnalysisBillVo> vos = getVos(list);
		return vos;
	}

	/**
	 * 生成流程
	 * @param planVo
	 * @param planGoodsJson
	 * @return
	 */
	@Transactional
	public RequestResult genFlow(PlanVo planVo, String planGoodsJson){

		//1、保存计划和计划货品
		RequestResult save = savePlanAndGoods(planVo, planGoodsJson);

		Map<String, Object> saveMap = save.getDataExt();

		String planId = "";

		if(save.isSuccess()){
			Plan plan = (Plan)saveMap.get("plan");
			//2、根据模板生成数据
			//(a)采购模板，按主表生成
			List<PlanGoods> planGoods = (List<PlanGoods>)save.getData();

			List<TemplateData> purchaseDatas = Lists.newArrayList();
			List<TemplateData> transportDatas = Lists.newArrayList();
			List<TemplateData> saleDatas = Lists.newArrayList();


			for(PlanGoods pg:planGoods){

				fillPurchaseData(planVo, pg, purchaseDatas);
				TemplateData tail = fillTransportData(planVo, pg, transportDatas);
				fillSaleData(planVo, pg, saleDatas, tail);
			}

			Map<String, TemplateData> mergePurchaseDatas = mergeData(purchaseDatas, 1);
			Map<String, TemplateData> mergeTransportDatas = mergeTransprotData(transportDatas);
			Map<String, TemplateData> mergeSaleDatas = mergeData(saleDatas, 2);

			Task rootTask = null;
			//生成根事件
			if(Strings.isNullOrEmpty(planVo.getFid())){
				rootTask = taskService.saveRootTask(plan);
			}else{
				rootTask = taskService.getRootTask(planVo.getFid());
			}

			taskService.addTaskByTemplateData(rootTask, plan, mergePurchaseDatas.values(), "1-CG");
			taskService.addTaskByTemplateData(rootTask, plan, mergeTransportDatas.values(), "2-YS");
			taskService.addTaskByTemplateData(rootTask, plan, mergeSaleDatas.values(), "3-XS");

			planId = plan.getFid();
		}else{
			save.setDataExt(null);
			save.setData(null);
			return save;
		}

		return buildSuccessRequestResult(planId);
	}

	/**
	 * 保存计划和货品计划
	 * @param planVo
	 * @param planGoodsJson
	 * @return
	 */
	@Transactional
	public RequestResult savePlanAndGoods(PlanVo planVo, String planGoodsJson){
		RequestResult save = planService.save(planVo, false);

		Map map = Maps.newHashMap();

		if(save.isSuccess()){
			String planId = (String)save.getData();
			Plan plan = planService.findOne(planId);
			map.put("plan", plan);
			save = planGoodsService.save(planGoodsJson, plan);
		}

		save.setDataExt(map);
		return save;
	}

	/**
	 * 生成采购模板数据
	 * @param planVo
	 * @param planGoods
	 * @param templateDatas
	 */
	private void fillPurchaseData(PlanVo planVo, PlanGoods planGoods, List<TemplateData> templateDatas){
			Supplier supplier = planGoods.getSupplier();
			if(supplier==null)return;

			PlanTemplateRelation ptr = planTemplateRelationService.findCsvTemplateRelation(supplier.getFid(),
					PlanTemplateVo.TEMPLATE_TYPE_PURCHASE);
			PlanTemplate template = ptr.getPlanTemplate();

			if(ptr==null)return;

			//记录每个模板的模板ID、上一个模板ID、模板标识（1-采购，2-运输，3-销售）、
			// 计划开始时间、计划完成时间、货品ID、货品属性ID、货品数量、运输数量、发货地ID、
			// 收货地ID、运输方式ID、装运方式ID、承运单位ID（供应商或客户）、
			// 计划货品表从表的运输路径ID、以及计算该模板总金额；
			TemplateData data = new TemplateData();

			data.setTemplate(template);
			data.setDays(template.getDays().intValue());
			data.setLastTemplateId(null);
			data.setFlag(PlanTemplateVo.TEMPLATE_TYPE_PURCHASE);

			Date startDate = DateUtilTools.string2Date(planVo.getAntipateStartTime());
			Date endDate = DateUtilTools.changeDateTime(startDate, 0 ,template.getDays().intValue()-1,
					0, 0,0);

			data.setStartDate(startDate);
			data.setEndDate(endDate);

			data.setGoods(planGoods.getGoods());
			data.setSpec(planGoods.getGoodsSpec());
			data.setQuentity(planGoods.getGoodsQuentity());
			data.setTransportQuentity(BigDecimal.ZERO);
			data.setReceiveAddress(planGoods.getReceiptPlace());
			data.setPublishAddress(planGoods.getDeliveryPlace());
			data.setSupplier(supplier);
			data.setAccountUnitName(planGoods.getUnit().getName());

			BigDecimal totalAmount = NumberUtil.multiply(new BigDecimal(-1), planGoods.getGoodsQuentity());
			totalAmount = NumberUtil.multiply(totalAmount, planGoods.getFactoryPrice());
			data.setTotalAmount(totalAmount);

			templateDatas.add(data);
	}

	/**
	 * 生成运输路径模板数据
	 * @param planVo
	 * @param planGoods
	 * @param templateDatas
	 */
	private TemplateData fillTransportData(PlanVo planVo, PlanGoods planGoods,
								   List<TemplateData> templateDatas){
		List<PlanGoodsDetail> pgDetails = planGoods.getDetails();

		String lastTemplateId = null;

		Date startDate = planGoods.getTransportDate();
		for(PlanGoodsDetail pgDetail:pgDetails){
			Supplier supplier = pgDetail.getSupplier();
			if(supplier==null)continue;

			PlanTemplateRelation ptr = planTemplateRelationService.existRouteTemplate(pgDetail);
			if(ptr==null)continue;

			PlanTemplate template = ptr.getPlanTemplate();
			//记录每个模板的模板ID、上一个模板ID、模板标识（1-采购，2-运输，3-销售）、
			// 计划开始时间、计划完成时间、货品ID、货品属性ID、货品数量、运输数量、发货地ID、
			// 收货地ID、运输方式ID、装运方式ID、承运单位ID（供应商或客户）、
			// 计划货品表从表的运输路径ID、以及计算该模板总金额；
			TemplateData data = new TemplateData();
			data.setTemplate(template);
			data.setDays(template.getDays().intValue());
			data.setLastTemplateId(lastTemplateId);

			data.setFlag(PlanTemplateVo.TEMPLATE_TYPE_TRANSPORT);

			Date endDate = DateUtilTools.changeDateTime(startDate, 0 ,template.getDays().intValue()-1,
					0, 0,0);

			data.setStartDate(startDate);
			data.setEndDate(endDate);

			data.setGoods(planGoods.getGoods());
			data.setSpec(planGoods.getGoodsSpec());

			data.setQuentity(planGoods.getGoodsQuentity());

			String goodsId = planGoods.getGoods().getFid();
			String specId = planGoods.getGoodsSpec()==null?"":planGoods.getGoodsSpec().getFid();
			String transportUnitId = pgDetail.getTransportUnit().getFid();

			GoodsTransport gt = goodsTransportService.findGoodsTransport(goodsId, specId, transportUnitId);

			//运输数量=采购数量 / 货品对应运输单位换算关系（有小数则取整+1）
			BigDecimal transprtQuentity = null;

			if(pgDetail.getConversionRate().compareTo(BigDecimal.ONE)==0){
				transprtQuentity = planGoods.getGoodsQuentity();
			}else{
				transprtQuentity = NumberUtil.divide(planGoods.getGoodsQuentity(),
						gt.getConversionRate(), 2);
				transprtQuentity = transprtQuentity.setScale(0, BigDecimal.ROUND_UP);
			}

			data.setTransportQuentity(transprtQuentity);
			data.setAccountUnitName(planGoods.getUnit().getName());

			data.setReceiveAddress(pgDetail.getReceiptPlace());
			data.setPublishAddress(pgDetail.getDeliveryPlace());
			data.setShipmentType(pgDetail.getShipmentType());
			data.setTransportType(pgDetail.getTransportType());

			data.setSupplier(supplier);

			data.setRouteIds(pgDetail.getTransportBill().getId());

			//运输金额 = -1 * （运输数量 * 成本分析表从表的基本运费 * 货品对应运输单位换算关系）
			BigDecimal totalAmount = NumberUtil.multiply(new BigDecimal(-1), data.getTransportQuentity());
			totalAmount = NumberUtil.multiply(totalAmount, pgDetail.getBasePrice());
			totalAmount = NumberUtil.multiply(totalAmount, gt.getConversionRate());
			data.setTotalAmount(totalAmount);

			templateDatas.add(data);

			lastTemplateId = data.getTemplate().getFid();
			startDate = DateUtilTools.changeDateTime(endDate, 0, 1, 0, 0, 0);
		}

		//设置队尾数据
		TemplateData tail = templateDatas.get(templateDatas.size()-1);
		tail.setIsTail(true);

		return tail;
	}


	/**
	 * 生成销售模板数据
	 * @param planVo
	 * @param planGoods
	 * @param templateDatas
	 */
	private void fillSaleData(PlanVo planVo, PlanGoods planGoods, List<TemplateData> templateDatas,
							  TemplateData tail){
		Customer customer = planGoods.getCustomer();
		if(customer==null)return;

		PlanTemplateRelation ptr = planTemplateRelationService.findCsvTemplateRelation(customer.getFid()
			 , PlanTemplateVo.TEMPLATE_TYPE_SALE);
		PlanTemplate template = ptr.getPlanTemplate();

		if(ptr==null)return;

		//记录每个模板的模板ID、上一个模板ID、模板标识（1-采购，2-运输，3-销售）、
		// 计划开始时间、计划完成时间、货品ID、货品属性ID、货品数量、运输数量、发货地ID、
		// 收货地ID、运输方式ID、装运方式ID、承运单位ID（供应商或客户）、
		// 计划货品表从表的运输路径ID、以及计算该模板总金额；
		TemplateData data = new TemplateData();

		data.setTemplate(template);
		data.setPre(tail);
		data.setDays(template.getDays().intValue());
		Date startDate = new Date();
		if(tail!=null){
			data.setLastTemplateId(tail.getTemplate().getFid());
			startDate = tail.getEndDate();
		}

		data.setFlag(PlanTemplateVo.TEMPLATE_TYPE_SALE);

		startDate = DateUtilTools.changeDateTime(startDate, 0, 1, 0, 0, 0);
		Date endDate = DateUtilTools.changeDateTime(startDate, 0 ,template.getDays().intValue()-1,
				0, 0,0);

		data.setStartDate(startDate);
		data.setEndDate(endDate);

		data.setGoods(planGoods.getGoods());
		data.setSpec(planGoods.getGoodsSpec());

		data.setQuentity(planGoods.getGoodsQuentity());
		data.setAccountUnitName(planGoods.getUnit().getName());
		data.setTransportQuentity(BigDecimal.ZERO);

		data.setReceiveAddress(planGoods.getReceiptPlace());
		data.setPublishAddress(planGoods.getDeliveryPlace());

		data.setCustomer(customer);

		BigDecimal totalAmount = NumberUtil.multiply(planGoods.getSalePrice(), planGoods.getGoodsQuentity());
		data.setTotalAmount(totalAmount);

		templateDatas.add(data);
	}



	/**
	 * 合并运输模板数据
	 * @param transportDatas
	 */
	public Map<String, TemplateData> mergeTransprotData(List<TemplateData> transportDatas){

		Map<String, TemplateData> map = Maps.newLinkedHashMap();
		if(transportDatas.size()<1)return map;

		Joiner joiner = Joiner.on("#").skipNulls();

		Date maxEndDate = transportDatas.get(transportDatas.size()-1).getEndDate();

		//汇总条件为供应商+发货地+收货地+运输方式+装运方式
		for(TemplateData data:transportDatas){

			String supplierId = data.getSupplier().getFid();
			String deliveryId = data.getPublishAddress().getFid();
			String receiveId = data.getReceiveAddress().getFid();
			String transportTypeId = data.getTransportType().getFid();
			String shipmentTypeId = data.getShipmentType().getFid();

			String key = joiner.join(supplierId, deliveryId, receiveId, transportTypeId, shipmentTypeId);

			TemplateData find = map.get(key);
			if(find==null){
				map.put(key, data);
			}else{
				//看有没有已汇总的数据，没有则需要本节点添加到汇总
				TemplateData parent = find;

				if(find.getMergeData().size()>0){
					find.getMergeData().add(data);
				}else{
					parent = VoFactory.createValue(TemplateData.class, find);

					parent.setCustomer(find.getCustomer());
					parent.setSupplier(find.getSupplier());
					parent.setTransportType(find.getTransportType());
					parent.setGoods(find.getGoods());
					parent.setPublishAddress(find.getPublishAddress());
					parent.setReceiveAddress(find.getReceiveAddress());
					parent.setShipmentType(find.getShipmentType());
					parent.setTemplate(find.getTemplate());
					parent.setSpec(find.getSpec());

					parent.getMergeData().add(find);
					parent.getMergeData().add(data);

					map.put(key, parent);
				}

				//记录累加金额和FTRANSPORT_ID，FTRANSPORT_ID用逗号隔开
				String routeIds = joiner.join(parent.getRouteIds(), data.getRouteIds());
				parent.setRouteIds(routeIds);
				parent.setTotalAmount(NumberUtil.add(parent.getTotalAmount(), data.getTotalAmount()));

				Date max = DateUtilTools.max(parent.getEndDate(), data.getEndDate());
				parent.setEndDate(max);
				for(TemplateData td:parent.getMergeData()){
					td.setEndDate(max);
				}
			}

			if(data.getIsTail()!=null&&data.getIsTail()){
				maxEndDate = DateUtilTools.max(maxEndDate, data.getEndDate());
			}
		}

		Date tailDate = maxEndDate;

		//反向计算开始日期
		for(int i=transportDatas.size()-1;i>=0;i--){
			TemplateData td = transportDatas.get(i);
			if(td.getIsTail()!=null && td.getIsTail()){
				Date startDate = DateUtilTools.changeDateTime(maxEndDate, 0, -td.getDays()+1, 0, 0, 0);
				td.setStartDate(startDate);
				td.setEndDate(maxEndDate);
				tailDate = DateUtilTools.getYesterday(startDate);
			}else{
				td.setEndDate(tailDate);
				Date startDate = DateUtilTools.changeDateTime(tailDate, 0, -td.getDays()+1, 0, 0, 0);
				td.setStartDate(startDate);
				tailDate = DateUtilTools.getYesterday(startDate);
			}
		}

		//计算合并的开始日期
		for(TemplateData mergeData:map.values()){
			if(mergeData.getMergeData().size()>0){
				Date min = findMinDate(mergeData.getMergeData());
				mergeData.setStartDate(min);
			}
		}

		return map;
	}

	/**
	 * 合并采购模板数据,汇总条件是：供应商和发货地
	 * 合并销售模板数据,汇总条件是：客户和收货地
	 * 计划开始时间取合并模板的最小值的计划开始时间，计划完成时间取最大值的计划完成时间
	 * @param pruchaseDatas
	 * @param flag flag=1采购 flag=2销售
	 */
	public Map<String, TemplateData> mergeData(List<TemplateData> pruchaseDatas, int flag) {
		Map<String, TemplateData> map = Maps.newLinkedHashMap();

		Joiner joiner = Joiner.on("#").skipNulls();

		for (TemplateData data : pruchaseDatas) {

			//处理因为合并导致最后运输日期改变的数据
			if(data.getPre()!=null){
				TemplateData pre = data.getPre();
				Date startDate = DateUtilTools.changeDateTime(pre.getEndDate(), 0, 1, 0, 0, 0);
				Date endDate = DateUtilTools.changeDateTime(startDate, 0 ,data.getDays().intValue()-1,
						0, 0,0);
				data.setStartDate(startDate);
				data.setEndDate(endDate);
			}

			String key = null;
			if(flag==1){
				String supplierId = data.getSupplier().getFid();
				String deliveryId = data.getPublishAddress().getFid();
				key = joiner.join(supplierId, deliveryId);
			}else{
				String customerId = data.getCustomer().getFid();
				String receivedId = data.getReceiveAddress().getFid();
				key = joiner.join(customerId, receivedId);
			}


			TemplateData find = map.get(key);
			if(find==null){
				map.put(key, data);
			}else{
				//看有没有已汇总的数据，没有则需要本节点添加到汇总
				TemplateData parent = find;

				if(find.getMergeData().size()>0){
					find.getMergeData().add(data);
				}else{
					parent = VoFactory.createValue(TemplateData.class, find);
					parent.setCustomer(find.getCustomer());
					parent.setSupplier(find.getSupplier());
					parent.setTransportType(find.getTransportType());
					parent.setGoods(find.getGoods());
					parent.setPublishAddress(find.getPublishAddress());
					parent.setReceiveAddress(find.getReceiveAddress());
					parent.setShipmentType(find.getShipmentType());
					parent.setTemplate(find.getTemplate());
					parent.setSpec(find.getSpec());

					parent.getMergeData().add(find);
					parent.getMergeData().add(data);

					map.put(key, parent);

				}

				//记录累加金额和FTRANSPORT_ID，FTRANSPORT_ID用逗号隔开
				String routeIds = joiner.join(parent.getRouteIds(), data.getRouteIds());
				parent.setRouteIds(routeIds);
				parent.setTotalAmount(NumberUtil.add(parent.getTotalAmount(), data.getTotalAmount()));

				Date max = DateUtilTools.max(parent.getEndDate(), data.getEndDate());
				parent.setEndDate(max);
				for(TemplateData td:parent.getMergeData()){
					td.setEndDate(max);
				}

				Date min = DateUtilTools.min(parent.getStartDate(), data.getStartDate());
				parent.setStartDate(min);
				for(TemplateData td:parent.getMergeData()){
					td.setStartDate(min);
				}

			}
		}

		return map;
	}

	/**
	 * 找到最小的日期
	 * @param datas
	 * @return
	 */
	private Date findMinDate(List<TemplateData> datas){
		Date min = datas.get(0).getStartDate();
		for(TemplateData td:datas){
			min = DateUtilTools.min(td.getStartDate(), min);
		}
		return min;
	}

	/**
	 * 根据收货地查找根据中转站分列显示的
	 * 		在成本分析表查询出目的地为目的港的记录；
	 再分解每一段路径，汇总第段路径的费用，提取收货地为中转地址的所有地址，并根据中转地址生成表头；
	 注意，中转地址上面名称为中转地址的第一级名称，下面名称为中转地址名称，如果中转地址为第一级，则只显示一层名称；
	 从第一条线路开始到最后一条线路的循环（循环1）；

	 初始累计费用X=0、累计费用T=0、报价ID=空字符串；

	 分解线路的每一段路径，从第一条路径开始到目的港开始循环（循环2）；
	 累计费用X=累计费用X+该路径费用、累计费用T=累计费用T+该路径费用、报价ID=报价ID+该路径的报价ID+逗号；

	 判断收货地是否目的港，如果是目的港，费用写入累计费用X，
	 目的港金额写入累计费用T+税后价，如果不是目的港，判断收货地址是否为中转地址，
	 如果是中转地址，费用写入累计费用X，并记录报价ID，然后把累计费用X=0，报价ID＝空字符串，
	 中转地址写入累计费用T，继续判断下一段路径；
	 */
	public PageJson querySeparateCostAnalyzeBill(CostAnalysisBillVo vo, PageParamater paramater){

		Sort sort = new Sort(Sort.Direction.ASC, "goods.name","goodsSpec.name","receiptPlace.name","totalPrice");
		PageRequest pageRequest = getPageRequest(paramater, sort);

		String accId = SecurityUtil.getFiscalAccountId();
		Page<CostAnalysisBill> bills = repository.findPageBy(accId, vo, pageRequest);
		Page<CostAnalysisBillVo> billVos = getPageVos(bills, pageRequest);

		Joiner joiner = Joiner.on(",").skipNulls();

		List<List<BillTitle>> separateTitles = Lists.newArrayList();

		for(CostAnalysisBillVo bill:billVos.getContent()){


			List<CostAnalysisBilldetail> details = billdetailService.findByBillId(bill.getId());

			BigDecimal feex = BigDecimal.ZERO, feet = BigDecimal.ZERO;
			String reportIds = "";

			List<BillTitle> listData = Lists.newArrayList();

			for(int index=0;index<details.size();index++) {

				CostAnalysisBilldetail detail = details.get(index);
				//累计费用X=累计费用X+该路径费用、累计费用T=累计费用T+该路径费用、报价ID=报价ID+逗号+该路径的报价ID；
				feex = NumberUtil.add(feex, detail.getBasePrice());
				feet = NumberUtil.add(feet, detail.getBasePrice());
				reportIds = joiner.join(reportIds, detail.getTransportBill().getId());

				FreightAddress receipt = detail.getReceiptPlace();
				Short istransfer = receipt.getTransfer();

				//如果是目的港，费用写入累计费用X，目的港金额写入累计费用T+税后价
				if (receipt.getFid().equals(vo.getReceiptPlaceId())){

					BillValue value = new BillValue();
					value.setPlaceId(receipt.getFid());
					value.setPlaceName(receipt.getName());
					value.setAmount(NumberUtil.add(feet, bill.getFactoryPrice()));
					value.setFee(feex);
					value.setTransportIds(reportIds);

					bill.getSeparateValues().add(value);

					BillTitle title = new BillTitle();
					title.setPlaceId(receipt.getFid());
					title.setPlaceName(receipt.getName());
					listData.add(title);

				//如果是中转地址，费用写入累计费用X，并记录报价ID，然后把累计费用X=0，报价ID＝空字符串，中转地址写入累计费用T
				}else if(istransfer!=null && istransfer == FreightAddress.TRANSFER_Y){

					BillTitle title = new BillTitle();
					title.setPlaceId(receipt.getFid());
					title.setPlaceName(receipt.getName());

					if(receipt.getParent()!=null){
						title.setParentPlaceId(receipt.getParent().getFid());
						title.setParentPlaceName(receipt.getParent().getName());
					}
					listData.add(title);

					BillValue value = new BillValue();
					if(receipt.getParent()!=null){
						value.setParentPlaceId(receipt.getParent().getFid());
					}
					value.setTransportIds(reportIds);
					value.setPlaceId(receipt.getFid());
					value.setPlaceName(receipt.getName());
					value.setFee(feex);
					value.setAmount(NumberUtil.add(feet, bill.getFactoryPrice()));

					bill.getSeparateValues().add(value);

					feex = BigDecimal.ZERO;
					reportIds = "";
				}else{
					if(index==details.size()-1){

						BillValue value = new BillValue();
						value.setPlaceId(bill.getReceiptPlaceId());
						value.setPlaceName(bill.getReceiptPlaceName());
						value.setAmount(NumberUtil.add(feet, bill.getFactoryPrice()));
						value.setFee(feex);
						value.setTransportIds(reportIds);

						bill.getSeparateValues().add(value);

						BillTitle title = new BillTitle();
						title.setPlaceId(bill.getReceiptPlaceId());
						title.setPlaceName(bill.getReceiptPlaceName());
						listData.add(title);
					}
				}
			}

			separateTitles.add(listData);
		}

		List<BillTitle> sortTitles = sortBillTitles(separateTitles);
		autoFillEmptyBillVal(sortTitles, billVos);

		PageJson pageJson = new PageJson(billVos);
		pageJson.setOther(sortTitles);

		return pageJson;
	}

	/**
	 * 自动填充没值的表
	 * @param titles
	 * @param billVos
	 */
	private void autoFillEmptyBillVal(List<BillTitle> titles, Page<CostAnalysisBillVo> billVos){
		for(CostAnalysisBillVo vo:billVos.getContent()){
			Map<String, BillValue> cache = Maps.newLinkedHashMap();
			Set<String> readayProcessKeys = Sets.newHashSet();
			Set<String> parentKeys = Sets.newHashSet();

			for(BillValue val:vo.getSeparateValues()){

				if(!Strings.isNullOrEmpty(val.getParentPlaceId())){
					parentKeys.add(val.getParentPlaceId());
				}
				cache.put(val.getPlaceId(), val);
			}
			List<BillValue> datas = Lists.newArrayList();
			for(BillTitle title:titles){
				if(cache.containsKey(title.getPlaceId())){
					datas.add(cache.get(title.getPlaceId()));
				}else{
					if(Strings.isNullOrEmpty(title.getParentPlaceId())){
						datas.add(genEmptyVal());
					} else if(!readayProcessKeys.contains(title.getParentPlaceId())
							&& !parentKeys.contains(title.getParentPlaceId())){
						datas.add(genEmptyVal());
						readayProcessKeys.add(title.getParentPlaceId());
					}

				}
			}
			vo.setSeparateValues(datas);
		}
	}

	/**
	 * 排序并去重复
	 * @return
	 */
	private List<BillTitle> sortBillTitles(List<List<BillTitle>> separateTitles){

		List<BillTitle> sortResult = Lists.newArrayList();
		Map<String, BillTitle> cache = Maps.newLinkedHashMap();

		for(int i=0;i<separateTitles.size();i++) {
			List<BillTitle> temp = separateTitles.get(i);

			int index = 1;
			for(BillTitle iter:temp){

				boolean find = false;
				int lastVal = 0;
				for(BillTitle sort:sortResult){

					if(find){
						int tempVal = sort.getSort();
						sort.setSort(sort.getSort()+index+lastVal);
						lastVal = tempVal;
						continue;
					}
					if(sort.getPlaceId().equals(iter.getPlaceId())){
						if(index>sort.getSort()){
							find = true;
							lastVal = sort.getSort();
							sort.setSort(index);
						}else{
							index = sort.getSort();
						}
					}else if(!Strings.isNullOrEmpty(sort.getParentPlaceId()) &&
							!Strings.isNullOrEmpty(iter.getParentPlaceId()) &&
							sort.getParentPlaceId().equals(iter.getParentPlaceId())){
						if(index>sort.getSort()){
							find = true;
							lastVal = sort.getSort();
							sort.setSort(index);
						}else{
							index = sort.getSort();
						}
					}
				}

				iter.setSort(index);
				index++;
			}

			cache.clear();

			for(BillTitle iter:temp){
				cache.put(iter.getPlaceId(), iter);
			}
			for(BillTitle iter:sortResult){
				cache.put(iter.getPlaceId(), iter);
			}

			sortResult = Lists.newArrayList(cache.values());
			Collections.sort(sortResult, new Comparator<BillTitle>(){
				@Override
				public int compare(BillTitle o1, BillTitle o2) {
					if(o1.getSort().compareTo(o2.getSort())==0){
						return o1.getPlaceId().compareTo(o2.getPlaceId());
					}
					return o1.getSort().compareTo(o2.getSort());
				}
			});
		}

		return sortResult;
	}


	/**
	 * 根据运输费报价ID查询明细
	 * @param transportIds
	 * @return
	 */
	public List<List<TransportSeperateDetailVo>> querySeparateDetail(List<String> transportIds){

		List<List<TransportSeperateDetailVo>> datas = Lists.newArrayList();

		for(String transportId:transportIds){
			List<TransportPriceDetail2> detail2s = transportBillDetail2Service.findByBillId(transportId);

			List<TransportSeperateDetailVo> vos = Lists.newArrayList();

			TransportPrice bill = transportPriceService.get(transportId);

			String transportType = bill.getTransportType().getName();
			String shipmentType = bill.getShipmentType().getName();
			String deliveryPlace = bill.getDeliveryPlace().getName();
			String receiptPlace = bill.getReceiptPlace().getName();

			for(TransportPriceDetail2 detail2:detail2s){

				TransportSeperateDetailVo vo = new TransportSeperateDetailVo();
				vo.setAmount(detail2.getAmount());
				vo.setBillDate(DateUtilTools.date2String(bill.getBillDate()));
				vo.setDeliveryPlace(deliveryPlace);
				vo.setReceiptPlace(receiptPlace);
				vo.setShipmentType(shipmentType);
				vo.setTransportType(transportType);
				vo.setTransportCostName(detail2.getTransportCost().getName());
				vo.setTransportCompany(bill.getSupplier().getName());
				vos.add(vo);

			}
			datas.add(vos);
		}

		return datas;
	}


	/**
	 * 查询中转港口到客户收货地的路线成本分析
	 * @param billIds
	 * @param referAddressIds
	 * @param customerId
	 */
	public RequestResult queryCustomerRouteAnalyze(String billIds, String referAddressIds,
		String customerId, String date){

		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		Joiner joiner = Joiner.on(",").skipNulls();

		List<String> billIdList = splitter.splitToList(billIds);
		if(billIdList.size()==0)return buildFailRequestResult("billIds参数解析错误");

		FreightAddressVo defaultAddess = customerAddressService.getFreightAddressByCsvId(customerId);
		if(defaultAddess==null) return buildFailRequestResult("客户没有默认地址");

		String billOneId = billIdList.get(0);
		CostAnalysisBill billOne = repository.findOne(billOneId);
		FreightAddress oneDeliveryPlace = billOne.getReceiptPlace();
		String receiptPlaceId = defaultAddess.getFid();
		if(oneDeliveryPlace.getFid().equals(receiptPlaceId)){
			return buildFailRequestResult("你选择的地址已是客户收货地址，不能再选择");
		}

		List<CostAnalysisBillVo> results = Lists.newArrayList();
		Map<String, BillTitle> separateTitles = Maps.newLinkedHashMap();

		if(Strings.isNullOrEmpty(date)){
			date = DateUtilTools.date2String(DateUtilTools.getToday());
		}

		for(String billId:billIdList){
			CostAnalysisBill bill = repository.findOne(billId);
			if(bill==null)continue;

			CostAnalysisBillVo billVo = getVo(bill);
			List<CostAnalysisBilldetail> voBilldetails = billdetailService.findByBillId(bill.getId());

			CostAnalysisBilldetail end = voBilldetails.get(voBilldetails.size()-1);

			//查出完成路径
			List<CostAnalysisBill> routes = null;

			String specId = billVo.getGoodsSpecId();
			String goodsId = billVo.getGoodsId();

			if(Strings.isNullOrEmpty(specId)){
				routes = repository.findByRoutes(receiptPlaceId, billId, date, goodsId);
			}else{
				routes = repository.findByRoutes(receiptPlaceId, billId, date, goodsId, specId);
			}

			for(CostAnalysisBill route:routes){
				CostAnalysisBillVo vo = new CostAnalysisBillVo();
				vo.setId(route.getId());
				vo.setFactoryPrice(billVo.getTotalPrice());
				vo.setGoodsId(billVo.getGoodsId());
				vo.setGoodsName(billVo.getGoodsName());
				vo.setGoodsSpecId(billVo.getGoodsSpecId());
				vo.setGoodsSpecName(billVo.getGoodsSpecName());
				vo.setDeliveryPlaceName(billVo.getReceiptPlaceName());
				vo.setDeliveryPlaceId(end.getReceiptPlace().getFid());
				vo.setReceiptPlaceName(route.getReceiptPlace().getName());
				vo.setReceiptPlaceId(route.getReceiptPlace().getFid());
				vo.setTotalPrice(route.getTotalPrice());
				vo.setLoss(route.getLoss());

				if(vo.getSeparateValues()==null)vo.setSeparateValues(Lists.newArrayList());

				List<CostAnalysisBilldetail> billdetails = billdetailService.findByBillId(route.getId());

				String reportIds = "";
				BillValue value = new BillValue();
				BillTitle title = new BillTitle();

				boolean start = false;
				for(CostAnalysisBilldetail detail:billdetails){

					if(start){

						FreightAddress address = detail.getDeliveryPlace();

						value.setPlaceId(address.getFid());
						value.setPlaceName(address.getName());
						value.setAmount(NumberUtil.subtract(route.getTotalPrice(), bill.getTotalPrice()));

						title.setPlaceId(address.getFid());
						title.setPlaceName(address.getName());
						separateTitles.put(address.getFid(), title);

						if(detail.getTransportBill()!=null){
							reportIds = joiner.join(reportIds, detail.getTransportBill().getId());
						}

					}else if(!start && isSameAddress(detail.getReceiptPlace(), bill.getReceiptPlace(), true)){
						start = true;
					}
				}

				if(!start){

					value.setPlaceId(oneDeliveryPlace.getFid());
					value.setPlaceName(oneDeliveryPlace.getName());
					value.setAmount(NumberUtil.subtract(route.getTotalPrice(), bill.getTotalPrice()));

					title.setPlaceId(oneDeliveryPlace.getFid());
					title.setPlaceName(oneDeliveryPlace.getName());
					separateTitles.put(oneDeliveryPlace.getFid(), title);
				}

				value.setTransportIds(reportIds);
				vo.getSeparateValues().add(value);

				results.add(vo);
			}
		}

		String originId = oneDeliveryPlace.getFid();
		processReferAddress(referAddressIds, separateTitles, results, defaultAddess.getFid(),
				originId, date);

		RequestResult requestResult = buildSuccessRequestResult();
		requestResult.setData(separateTitles.values());

		Map<String, Object> datas = Maps.newHashMap();
		datas.put("routeData", results);
		requestResult.setDataExt(datas);
		return requestResult;
	}

	/**
	 * 判断是否同一个地址
	 * @param address1
	 * @param address2
	 * @param compareParent
	 * @return
	 */
	private boolean isSameAddress(FreightAddress address1, FreightAddress address2, boolean compareParent){
		if(address1.getFid().equals(address2.getFid()))return true;
		if(compareParent){
			FreightAddress paddress1 = address1.getParent();
			FreightAddress paddress2 = address2.getParent();

			if(paddress1==null)return false;
			if(paddress2==null)return false;

			if(paddress1.getFid().equals(address2.getFid()))return true;
			if(paddress2.getFid().equals(address1.getFid()))return true;
		}

		return false;
	}


	/**
	 * 添加参考地址的数据
	 * @param referAddressIds
	 * @param separateTitles
	 * @param results
	 * @param receiptPlaceId
	 * @param orginId
	 */
	private void processReferAddress(String referAddressIds,
									 Map<String, BillTitle> separateTitles,
									 List<CostAnalysisBillVo> results,
									 String receiptPlaceId, String orginId, String date){

		if(Strings.isNullOrEmpty(referAddressIds))return;
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> referAddressList = splitter.splitToList(referAddressIds);
		if(referAddressList.size()==0)return;

		Joiner joiner = Joiner.on(",").skipNulls();
		for(String deliveryPlaceId:referAddressList){

			Date dated = DateUtilTools.string2Date(date);

			List<CostAnalysisBill> bills = repository.findByDeliveryAndRecept(deliveryPlaceId,
					receiptPlaceId, dated);

			for(CostAnalysisBill bill:bills){

				CostAnalysisBillVo billVo = getVo(bill);
				String reportIds = "";

				if(billVo.getSeparateValues()==null)billVo.setSeparateValues(Lists.newArrayList());

				List<CostAnalysisBilldetail> billdetails = billdetailService.findByBillId(bill.getId());

				boolean done = false;
				BillValue value = new BillValue();
				BillTitle title = new BillTitle();

				for(int i=0;i<billdetails.size();i++) {

					CostAnalysisBilldetail billdetail = billdetails.get(i);

					if(!done){
						FreightAddress detailDelivery = billdetail.getDeliveryPlace();
						FreightAddress detailReceipt = billdetail.getReceiptPlace();

						if(separateTitles.containsKey(detailDelivery.getFid())){

							title.setPlaceId(detailDelivery.getFid());
							title.setPlaceName(detailDelivery.getName());
							separateTitles.put(detailDelivery.getFid(), title);

							value.setPlaceId(detailDelivery.getFid());
							value.setPlaceName(detailDelivery.getName());
							value.setAmount(billVo.getFreightPrice());

							done = true;
						}else if(separateTitles.containsKey(detailReceipt.getFid())){
							title.setPlaceId(detailReceipt.getFid());
							title.setPlaceName(detailReceipt.getName());
							separateTitles.put(detailReceipt.getFid(), title);

							value.setPlaceId(detailReceipt.getFid());
							value.setPlaceName(detailReceipt.getName());
							value.setAmount(billVo.getFreightPrice());
							done = true;
						}
					}

					if(billdetail.getTransportBill()!=null){
						reportIds = joiner.join(reportIds, billdetail.getTransportBill().getId());
					}

				}

				if(!done){
					for(int i=0;i<billdetails.size();i++){
						CostAnalysisBilldetail billdetail = billdetails.get(i);

						if(!done){
							FreightAddress detailDelivery = billdetail.getDeliveryPlace();
							FreightAddress detailReceipt = billdetail.getReceiptPlace();

							if(i==0 &&
									((detailDelivery.getParent()!=null &&
											detailDelivery.getParent().getFid().equals(orginId))
											||
											detailDelivery.getFid().equals(orginId))){

								title.setPlaceId(detailDelivery.getFid());
								title.setPlaceName(detailDelivery.getName());
								separateTitles.put(detailDelivery.getFid(), title);

								value.setPlaceId(detailDelivery.getFid());
								value.setPlaceName(detailDelivery.getName());
								value.setAmount(billVo.getFreightPrice());

								done = true;
							}else if((detailReceipt.getParent()!=null &&
									detailReceipt.getParent().getFid().equals(orginId))
									||detailReceipt.getFid().equals(orginId)){

								title.setPlaceId(detailReceipt.getFid());
								title.setPlaceName(detailReceipt.getName());
								separateTitles.put(detailReceipt.getFid(), title);

								value.setPlaceId(detailReceipt.getFid());
								value.setPlaceName(detailReceipt.getName());
								value.setAmount(billVo.getFreightPrice());

								done = true;
							}
						}
					}
				}

				if(!done){
					String extendFee = "feeExtend";
					value.setPlaceId(extendFee);
					value.setPlaceName("费用");
					value.setAmount(billVo.getFreightPrice());

					title.setPlaceId(extendFee);
					title.setPlaceName("费用");
					separateTitles.put(extendFee, title);
				}

				value.setTransportIds(reportIds);

				billVo.getSeparateValues().add(value);
				results.add(billVo);
			}
		}
	}

	/**
	 * 生成一个空的数据，方便前端处理界面
	 * @return
	 */
	private BillValue genEmptyVal(){
		BillValue value = new BillValue();

		value.setPlaceId("");
		value.setPlaceName("");
		return value;
	}

	/**
	 * 根据收货地、发货地查询运输费用明细
	 * @param costAnalyzeId
	 * @param deliveryPlaceId
	 * @param receiptPlaceId
	 * @return
	 */
	public List<List<TransportSeperateDetailVo>> querySeparateDetail(String costAnalyzeId,
																	 String deliveryPlaceId, String receiptPlaceId){
		List<CostAnalysisBilldetail> billdetails = billdetailService.findByBillId(costAnalyzeId);

		if(billdetails.size()==0)return Collections.EMPTY_LIST;

		List<String> transportIds = Lists.newArrayList();

		if(Strings.isNullOrEmpty(deliveryPlaceId)){
			deliveryPlaceId = billdetails.get(0).getDeliveryPlace().getFid();
		}
		if(Strings.isNullOrEmpty(receiptPlaceId)){
			receiptPlaceId = billdetails.get(billdetails.size()-1).getReceiptPlace().getFid();
		}

		CostAnalysisBill bill = repository.getOne(costAnalyzeId);

		if(bill.getDeliveryPlace().getFid().equals(deliveryPlaceId)){
			for(CostAnalysisBilldetail detail:billdetails){
				if(detail.getTransportBill()!=null) {
					transportIds.add(detail.getTransportBill().getId());
				}
				if(detail.getReceiptPlace().getFid().equals(receiptPlaceId)){
					break;
				}
			}
		}else if(bill.getReceiptPlace().getFid().equals(receiptPlaceId)){
			boolean start = false;
			for(CostAnalysisBilldetail detail:billdetails){
				if(start){
					if(detail.getTransportBill()!=null) {
						transportIds.add(detail.getTransportBill().getId());
					}
					continue;
				}
				if(detail.getReceiptPlace().getFid().equals(receiptPlaceId)){
					start = true;
					if(detail.getTransportBill()!=null) {
						transportIds.add(detail.getTransportBill().getId());
					}
				}
			}
		}else{
			boolean start = false;
			for(CostAnalysisBilldetail detail:billdetails){
				if(start){
					if(detail.getTransportBill()!=null){
						transportIds.add(detail.getTransportBill().getId());
					}
					if(detail.getReceiptPlace().getFid().equals(receiptPlaceId)){
						break;
					}
				}
				if(detail.getReceiptPlace().getFid().equals(deliveryPlaceId)){
					start = true;
				}
			}
		}

		return querySeparateDetail(transportIds);
	}
}
