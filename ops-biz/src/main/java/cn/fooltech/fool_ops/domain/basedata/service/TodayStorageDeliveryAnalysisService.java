package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBill;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBilldetail;
import cn.fooltech.fool_ops.domain.analysis.entity.TransportRoute;
import cn.fooltech.fool_ops.domain.analysis.repository.CostAnalysisBillRepository;
import cn.fooltech.fool_ops.domain.analysis.repository.CostAnalysisBilldetailRepository;
import cn.fooltech.fool_ops.domain.analysis.repository.TransportRouteRepository;
import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.basedata.repository.*;
import cn.fooltech.fool_ops.domain.basedata.vo.RouteVo;
import cn.fooltech.fool_ops.domain.flow.entity.PlanGoods;
import cn.fooltech.fool_ops.domain.flow.repository.PlanGoodsRepository;
import cn.fooltech.fool_ops.domain.flow.service.PlanGoodsService;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.period.repository.StockPeriodRepository;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.transport.entity.GoodsTransport;
import cn.fooltech.fool_ops.domain.transport.repository.GoodsTransportRepository;
import cn.fooltech.fool_ops.domain.warehouse.entity.StockStore;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.repository.PeriodStockAmountRepository;
import cn.fooltech.fool_ops.domain.warehouse.repository.StockStoreRepository;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillDetailRepository;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/12.
 */

@Service
public class TodayStorageDeliveryAnalysisService {

    private static final int TRANSPORT_UNIT_SAFE = 0;
    private static final int TRANSPORT_UNIT_UNSAFE = 1;
    private static final String TRANSPORT_UNIT_UNSAFE_STR = "换算关系可疑";

    private Logger logger = LoggerFactory.getLogger(TodayStorageDeliveryAnalysisService.class);
	@Autowired
	private TransportLossRepository transportLossRepository;
    @Autowired
    private StockStoreRepository storeRepository;

    @Autowired
    private WarehouseBillDetailRepository billDetailRepository;

    @Autowired
    private TransportRouteRepository routeRepository;

    @Autowired
    private FreightAddressService addressService;

    @Autowired
    private PlanGoodsRepository planGoodsRepository;

    @Autowired
    private CostAnalysisBillRepository costBillRepository;

    @Autowired
    private CostAnalysisBilldetailRepository costBilldetailRepository;

    @Autowired
    private GoodsTransportRepository goodsTransportRepository;

    @Autowired
    private TransportPriceRepository transportPriceRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransportPriceDetail2Repository tpd2Repository;

    /**
     * 删除当天旧数据
     */
    @Transactional
    public void deleteOldData(String receiptPlaceId){
        Date today = DateUtilTools.getToday();
        costBillRepository.deleteByParam(PlanGoods.WAREHOUSE, receiptPlaceId, today);
    }

    public void analysis(String receiptPlaceId) {

        deleteOldData(receiptPlaceId);

        //在途数量
        Map<String, StockStore> onWay = Maps.newLinkedHashMap();

        //即时分仓库存数量
        Map<String, StockStore> curStore = Maps.newLinkedHashMap();

        //仓库 - 分仓库存列表
        Map<String, List<StockStore>> warehouse_store = Maps.newLinkedHashMap();

        //换算关系缓存
        Map<String, GoodsTransport> cache = Maps.newHashMap();

        //1、从即时库存中取出每个分仓库存数M
        List<StockStore> stores = storeRepository.findAll();

        Joiner joiner = Joiner.on(";");

        for(StockStore stock:stores){


            String goodsId = stock.getGoods().getFid();
            String specId = stock.getGoodsSpec()==null?"":stock.getGoodsSpec().getFid();
            String warehouseId = stock.getWarehouse().getFid();
            String accId = stock.getFiscalAccount().getFid();

            String key = joiner.join(accId, warehouseId, goodsId, specId);
            curStore.put(key, stock);

        }

        //按仓库分别取出收货单数量S和发货单数量F；
        List<WarehouseBillDetail> shdDetails = billDetailRepository.
                findNotCompleteBy(WarehouseBuilderCodeHelper.shd);

        fillShdOnWayMap(shdDetails, onWay);

        List<WarehouseBillDetail> fhdDetails = billDetailRepository.
                findNotCompleteBy(WarehouseBuilderCodeHelper.shd);

        fillFhdOnWayMap(fhdDetails, onWay);

        //从计划货品表关联计划货品从表得出计划发货数量X
        List<PlanGoods> planfhDetails = planGoodsRepository.queryNotComplate(PlanGoods.WAREHOUSE);
        fillFhPlanGoodsOnWayMap(planfhDetails, onWay);

        //从计划货品表关联计划货品从表得出计划收货数量Y
        List<PlanGoods> planshDetails = planGoodsRepository.queryNotComplate(PlanGoods.PURCHSE);
        fillShPlanGoodsOnWayMap(planshDetails, onWay);


        //计算在途数量N
        for(String iterkey:onWay.keySet()){
            StockStore val = onWay.get(iterkey);
            if(val.getAccountQuentity().compareTo(BigDecimal.ZERO)<0){
                val.setAccountQuentity(BigDecimal.ZERO);
            }
        }

        //计算实际库存R
        for(String iterkey:curStore.keySet()){
            StockStore real = curStore.get(iterkey);
            StockStore val = onWay.get(iterkey);
            if(val!=null){
                //实际库存数量R = M – NA
                BigDecimal realQuentity = NumberUtil.subtract(real.getAccountQuentity(), val.getAccountQuentity());
                if(realQuentity.compareTo(BigDecimal.ZERO)<=0){
                    curStore.put(iterkey, null);
                }else{
                    real.setAccountQuentity(realQuentity);
                }
            }
            String warehouseId = real.getWarehouse().getFid();
            String accId = real.getFiscalAccount().getFid();
            String storeKey = joiner.join(accId, warehouseId);

            List<StockStore> storeList = warehouse_store.get(storeKey);
            if(storeList!=null){
                storeList.add(real);
            }else{
                warehouse_store.put(storeKey, Lists.newArrayList(real));
            }
        }

        process(warehouse_store, receiptPlaceId, cache);
    }

    /**
     * 填充发货单的在途数量
     */
    private void fillFhdOnWayMap(List<WarehouseBillDetail> fhdDetails, Map<String, StockStore> onWay){

        Joiner joiner = Joiner.on(";");
        for(WarehouseBillDetail fhd:fhdDetails){

            String goodsId = fhd.getGoods().getFid();
            String specId = fhd.getGoodsSpec()==null?"":fhd.getGoodsSpec().getFid();
            String warehouseId = fhd.getInWareHouse().getFid();
            String accId = fhd.getFiscalAccount().getFid();

            String key = joiner.join(accId, warehouseId, goodsId, specId);

            StockStore exist = onWay.get(key);
            if(exist!=null){
                BigDecimal existQuentity = NumberUtil.subtract(exist.getAccountQuentity(), fhd.getAccountQuentity());
                exist.setAccountQuentity(existQuentity);
            }else{
                BigDecimal existQuentity = NumberUtil.multiply(new BigDecimal(-1), fhd.getAccountQuentity());

                exist  = new StockStore();
                exist.setAccountQuentity(fhd.getAccountQuentity());
                exist.setAccountUnit(fhd.getAccountUint());
                exist.setFiscalAccount(fhd.getFiscalAccount());
                exist.setGoods(fhd.getGoods());
                exist.setGoodsSpec(fhd.getGoodsSpec());
                exist.setWarehouse(fhd.getInWareHouse());
                exist.setOrg(fhd.getOrg());
                exist.setAccountQuentity(existQuentity);

                onWay.put(key, exist);
            }
        }
    }


    /**
     * 填充计划发货的在途数量
     */
    private void fillFhPlanGoodsOnWayMap(List<PlanGoods> fhDetails, Map<String, StockStore> onWay){

        Joiner joiner = Joiner.on(";");
        for(PlanGoods fhd:fhDetails){

            String goodsId = fhd.getGoods().getFid();
            String specId = fhd.getGoodsSpec()==null?"":fhd.getGoodsSpec().getFid();
            FreightAddress deliveryPlace = fhd.getDeliveryPlace();
            if(deliveryPlace==null)continue;
            AuxiliaryAttr warehouse = deliveryPlace.getWarehouse();
            if(warehouse==null)continue;

            String warehouseId = warehouse.getFid();
            String accId = fhd.getFiscalAccount().getFid();

            String key = joiner.join(accId, warehouseId, goodsId, specId);

            StockStore exist = onWay.get(key);
            if(exist!=null){
                BigDecimal existQuentity = NumberUtil.subtract(exist.getAccountQuentity(), fhd.getGoodsQuentity());
                exist.setAccountQuentity(existQuentity);
            }else{
                BigDecimal existQuentity = NumberUtil.multiply(new BigDecimal(-1), fhd.getGoodsQuentity());

                exist  = new StockStore();
                exist.setAccountQuentity(fhd.getGoodsQuentity());
                exist.setAccountUnit(fhd.getUnit());
                exist.setFiscalAccount(fhd.getFiscalAccount());
                exist.setGoods(fhd.getGoods());
                exist.setGoodsSpec(fhd.getGoodsSpec());
                exist.setWarehouse(warehouse);
                exist.setOrg(fhd.getOrg());
                exist.setAccountQuentity(existQuentity);

                onWay.put(key, exist);
            }
        }
    }

    /**
     * 填充计划收货的在途数量
     */
    private void fillShPlanGoodsOnWayMap(List<PlanGoods> shDetails, Map<String, StockStore> onWay){

        Joiner joiner = Joiner.on(";");
        for(PlanGoods fhd:shDetails){

            String goodsId = fhd.getGoods().getFid();
            String specId = fhd.getGoodsSpec()==null?"":fhd.getGoodsSpec().getFid();
            FreightAddress receiptPlace = fhd.getReceiptPlace();
            if(receiptPlace==null)continue;
            AuxiliaryAttr warehouse = receiptPlace.getWarehouse();
            if(warehouse==null)continue;

            String warehouseId = warehouse.getFid();
            String accId = fhd.getFiscalAccount().getFid();

            String key = joiner.join(accId, warehouseId, goodsId, specId);

            StockStore exist = onWay.get(key);
            if(exist!=null){
                BigDecimal existQuentity = NumberUtil.add(exist.getAccountQuentity(), fhd.getGoodsQuentity());
                exist.setAccountQuentity(existQuentity);
            }else{
                BigDecimal existQuentity = fhd.getGoodsQuentity();

                exist  = new StockStore();
                exist.setAccountQuentity(fhd.getGoodsQuentity());
                exist.setAccountUnit(fhd.getUnit());
                exist.setFiscalAccount(fhd.getFiscalAccount());
                exist.setGoods(fhd.getGoods());
                exist.setGoodsSpec(fhd.getGoodsSpec());
                exist.setWarehouse(warehouse);
                exist.setOrg(fhd.getOrg());
                exist.setAccountQuentity(existQuentity);

                onWay.put(key, exist);
            }
        }
    }

    /**
     * 填充收货单的在途数量
     */
    private void fillShdOnWayMap(List<WarehouseBillDetail> shdDetails, Map<String, StockStore> onWay){

        Joiner joiner = Joiner.on(";");
        for(WarehouseBillDetail shd:shdDetails){

            String goodsId = shd.getGoods().getFid();
            String specId = shd.getGoodsSpec()==null?"":shd.getGoodsSpec().getFid();
            String warehouseId = shd.getInWareHouse().getFid();
            String accId = shd.getFiscalAccount().getFid();

            String key = joiner.join(accId, warehouseId, goodsId, specId);

            StockStore store  = new StockStore();
            store.setAccountQuentity(shd.getAccountQuentity());
            store.setAccountUnit(shd.getAccountUint());
            store.setFiscalAccount(shd.getFiscalAccount());
            store.setGoods(shd.getGoods());
            store.setGoodsSpec(shd.getGoodsSpec());
            store.setWarehouse(shd.getInWareHouse());
            store.setOrg(shd.getOrg());

            onWay.put(key, store);
        }
    }

    /**
     * 生成成本分析表记录
     * @param stores
     * @param receiptPlaceId
     */
    private void process(Map<String, List<StockStore>> stores, String receiptPlaceId,
                         Map<String, GoodsTransport> cache){
        List<TransportRoute> routes = routeRepository.findAll();

        for(TransportRoute route:routes){

            FreightAddress deliveryPlace = addressService.get(route.getDeliveryPlace().getFid());

            if(deliveryPlace==null)continue;
            List<FreightAddress> deliveryAddress = null;
            if(deliveryPlace.getFlag()==FreightAddress.LEFA_PARENT){
                deliveryAddress = addressService.findAllChildren(deliveryPlace);
                deliveryAddress.add(deliveryPlace);
            }else{
                deliveryAddress = Lists.newArrayList(deliveryPlace);
            }

            List<StockStore> storeList = null;
            for(FreightAddress deliveryIter:deliveryAddress){

                if(deliveryIter.getWarehouse()==null)continue;
                String warehouseId = deliveryIter.getWarehouse().getFid();
                String accId = route.getFiscalAccount().getFid();

                storeList = stores.get(accId+";"+warehouseId);
                if(storeList!=null && storeList.size()>0){
                    FreightAddress receiptPlace = addressService.get(route.getReceiptPlace().getFid());
                    if(receiptPlace==null)continue;

                    //不是条件的收货地，不处理
                    if(!receiptPlace.getFid().equals(receiptPlaceId))continue;

                    if(receiptPlace.getEnable()==FreightAddress.ENABLE_N)continue;

                    if(receiptPlace.getFlag()==FreightAddress.LEFA_PARENT){
                        List<FreightAddress> childAddress = addressService.findAllChildren(receiptPlace);
                        childAddress.add(receiptPlace);

                        for(FreightAddress child:childAddress){
                            if(child.getEnable()==FreightAddress.ENABLE_Y
                                    && child.getRecipientSign()==FreightAddress.RECEIPT_Y){
                                insertCostAnalyzeBill(route, storeList, child, deliveryIter, cache);
                            }
                        }
                    }else{
                        if(receiptPlace.getRecipientSign()==FreightAddress.RECEIPT_N)continue;
                        insertCostAnalyzeBill(route, storeList, receiptPlace, deliveryIter, cache);
                    }
                }
            }


        }
    }

    /**
     * 插入成本分析表
     FID	系统自动生成
     FBILL_DATE	当前日期
     FROUTE	运输路径表的线路路径
     FSUPPLIER_ID	Null
     FGOODS_ID	实际库存表的货品
     FGOODS_SPEC_ID	实际库存表的货品属性
     FGOODS_UINT_ID	货品资料的基本单位
     FDELIVERY_PLACE	运输路径表的发货地
     FRECEIPT_PLACE	运输路径表的收货地
     FFACTORY_PRICE	按该仓库最近一次进价，之后成本核算时会修改
     FPURCHASE	0
     */
    @Transactional(readOnly = false)
    private void insertCostAnalyzeBill(TransportRoute route, List<StockStore> storeList,
                                       FreightAddress receiptAddress, FreightAddress deliveryPlace,
        Map<String, GoodsTransport> cache){

        for(StockStore store:storeList){

            CostAnalysisBill bill = new CostAnalysisBill();
            bill.setBillDate(new Date());
            bill.setRoute(route.getRoute());
            bill.setSupplier(null);
            bill.setGoods(store.getGoods());
            bill.setGoodsSpec(store.getGoodsSpec());
            bill.setUnit(store.getAccountUnit());
            bill.setDeliveryPlace(deliveryPlace);
            bill.setReceiptPlace(receiptAddress);

            bill.setFactoryPrice(store.getAccountPrice());
            bill.setPurchase(0);
            bill.setOrg(store.getOrg());
            bill.setFiscalAccount(store.getFiscalAccount());
            bill.setCreateTime(new Date());
            bill.setUpdateTime(new Date());
            
            //---------------

            CustomerAddress ca = customerAddressRepository.queryDefaultByDeliveryPlace(receiptAddress.getFid());
            if(ca!=null){
                bill.setCustomer(customerRepository.findOne(ca.getCustomerId()));
            }

            costBillRepository.save(bill);
            int transportUnitSafe = insertCostAnalyzeBillDetail(route, bill, cache);

            //插入明细表并判断换算关系是否可疑
            if(transportUnitSafe==TRANSPORT_UNIT_UNSAFE){
                bill.setRemark(TRANSPORT_UNIT_UNSAFE_STR);
            }
            
            AuxiliaryAttr deliveryPlaceLoss=bill.getDeliveryPlace().getTransportLoss();
            AuxiliaryAttr receiptPlaceLoss=bill.getReceiptPlace().getTransportLoss();
            GoodsSpec goodsSpec=bill.getGoodsSpec();
            AuxiliaryAttr shipmentType=null;
            TransportLoss transportLoss=null;
            List<CostAnalysisBilldetail> list=costBilldetailRepository.findByBillIdOrderFno(bill.getId());
            CostAnalysisBilldetail costAnalysisBilldetail=null;
            if(list!=null&&list.size()>0){
            	//costAnalysisBilldetail=new CostAnalysisBilldetail();
            	costAnalysisBilldetail=list.get(0);
            }
            if(costAnalysisBilldetail!=null){
            	if(costAnalysisBilldetail.getShipmentType()!=null){
            		shipmentType=costAnalysisBilldetail.getShipmentType();
            	}
            	//shipmentType=new AuxiliaryAttr();
            }
            Goods goods=bill.getGoods();
            if(deliveryPlaceLoss!=null&&receiptPlaceLoss!=null&&shipmentType!=null&&goods!=null){
           
            	String deliveryId=deliveryPlaceLoss.getFid();
            	String receiptId=receiptPlaceLoss.getFid();
            	String shipmentId=shipmentType.getFid();
            	String goodsId=goods.getFid();
            	
                	if(goodsSpec==null){
                    	transportLoss=transportLossRepository.findLossByCostBill(SecurityUtil.getFiscalAccount().getFid(), deliveryId, receiptId, shipmentId, goodsId);
                   }else{
                   	transportLoss=transportLossRepository.findLossByCostBill(SecurityUtil.getFiscalAccount().getFid(), deliveryId, receiptId, shipmentId, goodsId,goodsSpec.getFid());
                   }
            }
            if(transportLoss!=null){
            	BigDecimal hundred=new BigDecimal(100);
                BigDecimal loss= NumberUtil.multiply(transportLoss.getPaymentAmonut(), bill.getTotalPrice());
                loss=NumberUtil.divide(loss, hundred, 8);
                bill.setLoss(loss);
            }else{
            	bill.setLoss(BigDecimal.ZERO);
            }
        //    BigDecimal loss= NumberUtil.multiply(transportLoss.getPaymentAmonut(), bill.getFactoryPrice());
            
         //   bill.setLoss(loss);
            bill.setTotalPrice(bill.getTotalPrice().add(bill.getLoss()));
            costBillRepository.save(bill);

        
        }
    }



    /**
     * 把第2点插入的记录根据线路路径，解释详细路径，插入相应记录到成本分析表从表
     * FID	系统自动生成
     FBILL_ID	主表ID
     FNO	自动加1
     FDELIVERY_PLACE	解释路径得出
     FRECEIPT_PLACE	解释路径得出
     FTRANSPORT_TYPE_ID	解释路径得出
     FSHIPMENT_TYPE_ID	解释路径得出
     */
    private Integer insertCostAnalyzeBillDetail(TransportRoute route, CostAnalysisBill bill, Map<String, GoodsTransport> cache){

        String routeLine = "["+route.getRoute()+"]";

        List<RouteVo> routeVos = JSONArray.parseArray(routeLine, RouteVo.class);
        String transportBillIds = route.getTransportBill();
        Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
        List<String> transportBills = splitter.splitToList(transportBillIds);

        BigDecimal total = BigDecimal.ZERO;
        int maxValid = 0;
        int expectedDay = 0;
        int transportUnit = TRANSPORT_UNIT_SAFE;

        for(int index=0;index<transportBills.size();index++){
            RouteVo routeVo = routeVos.get(index);
            CostAnalysisBilldetail detail = new CostAnalysisBilldetail();
            detail.setBill(bill);

            detail.setOrg(bill.getOrg());
            detail.setFiscalAccount(bill.getFiscalAccount());
            detail.setCreateTime(new Date());
            detail.setUpdateTime(new Date());
            detail.setNo(index+1);

            //查出运输费报价
            TransportPrice price = transportPriceRepository.findOne(transportBills.get(index));

            detail.setReceiptPlace(price.getReceiptPlace());
            detail.setDeliveryPlace(price.getDeliveryPlace());
            detail.setTransportType(price.getTransportType());
            detail.setShipmentType(price.getShipmentType());

            if(price==null)continue;
            if(price.getTransportUnit()==null)continue;

            detail.setBillDate(price.getBillDate());
            GoodsTransport gt = findOrCreateGoodsTransport(bill.getOrg(), bill.getGoods(), bill.getGoodsSpec(), bill.getUnit(),
                    price.getTransportUnit(), detail.getShipmentType(), cache);
            detail.setConversionRate(gt.getConversionRate());

            //需要重新计算basePrice
            List<TransportPriceDetail2> tbillDetail2s = tpd2Repository.queryDetail(price.getId());
            BigDecimal detail2Total = BigDecimal.ZERO;
            for(TransportPriceDetail2 detail2:tbillDetail2s){

                //取出从表1的属性
                TransportPriceDetail1 detail1 = detail2.getDetail1();

                GoodsTransport detail1Gt = findOrCreateGoodsTransport(bill.getOrg(), bill.getGoods(), bill.getGoodsSpec(), bill.getUnit(),
                        detail2.getTransportUnit(), detail1.getShipmentType(), cache);

                if(detail1Gt.getSysSign()==GoodsTransport.SYS_SIGN_GEN){
                    transportUnit = TRANSPORT_UNIT_UNSAFE;
                }

                BigDecimal val = NumberUtil.divide(detail2.getAmount(), detail1Gt.getConversionRate(), 20);
                detail2Total = NumberUtil.add(detail2Total, val);
            }
            detail2Total = detail2Total.setScale(2, BigDecimal.ROUND_HALF_UP);

            //basePrice=合计（从表2金额/真正的换算关系）
            detail.setBasePrice(detail2Total);
            detail.setPublishBasePrice(NumberUtil.divide(detail.getPublishFreightPrice(), detail.getConversionRate(), 2));


            detail.setTransportUnit(price.getTransportUnit());
            detail.setSupplier(price.getSupplier());
            detail.setFreightPrice(price.getAmount());
            detail.setExecuteSign(price.getExecuteSign());
            detail.setTransportBill(price);
            detail.setExpectedDays(price.getExpectedDays());

            total = NumberUtil.add(detail.getBasePrice(), total);
            expectedDay += detail.getExpectedDays();
            maxValid = Math.max(maxValid, price.getExecuteSign());

            costBilldetailRepository.save(detail);
        }

        bill.setFreightPrice(total);
        bill.setExecuteSign(maxValid);
        bill.setExpectedDays(expectedDay);
        bill.setTotalPrice(NumberUtil.add(bill.getFactoryPrice(), bill.getFreightPrice()));
        bill.setPublishTotalPrice(NumberUtil.add(bill.getPublishFactoryPrice(), bill.getPublishFreightPrice()));


        costBillRepository.save(bill);

        return transportUnit;
    }


    /**
     * 查找换算关系，没有则插入默认换算关系1
     * @param org
     * @param goods
     * @param spec
     * @param accountUnit
     * @param transportUnit
     * @param shipmentType
     * @return
     */
    private GoodsTransport findOrCreateGoodsTransport(Organization org, Goods goods, GoodsSpec spec, Unit accountUnit,
                                                      AuxiliaryAttr transportUnit, AuxiliaryAttr shipmentType,
                                                      Map<String, GoodsTransport> cache){

        GoodsTransport gt = null;

        String goodsId = goods.getFid();
        String specId = spec==null?null:spec.getFid();
        String transportUnitId = transportUnit.getFid();
        String shipmentTypeId = shipmentType.getFid();

        String key = Joiner.on("#").skipNulls().join(transportUnitId, shipmentTypeId, goodsId, specId);

        GoodsTransport exist = cache.get(key);

        if(exist==null){
            if(Strings.isNullOrEmpty(specId)){
                gt = goodsTransportRepository.findTopBySpecIsNull(goodsId, transportUnitId, shipmentTypeId);
            }else{
                gt = goodsTransportRepository.findTopBySpecNotNull(goodsId, specId, transportUnitId, shipmentTypeId);
            }
            if(gt==null){
                //找不到换算关系,插入换算关系=1的默认记录
                gt = insertDefaultGoodsTransport(org, goods, spec, accountUnit,
                        transportUnit, shipmentType);
            }

            cache.put(key, gt);
            return gt;
        }else{
            return exist;
        }
    }

    /**
     * 插入默认换算关系记录
     * @param org
     * @param goods
     * @param spec
     * @param accountUnit
     * @param transportUnit
     * @param shipmentType
     * @return
     */
    private GoodsTransport insertDefaultGoodsTransport(Organization org, Goods goods, GoodsSpec spec, Unit accountUnit,
                                                       AuxiliaryAttr transportUnit, AuxiliaryAttr shipmentType){
        GoodsTransport gt = new GoodsTransport();
        gt.setConversionRate(BigDecimal.ONE);
        gt.setCreateTime(new Date());
        gt.setOrg(org);
        gt.setSysSign(GoodsTransport.SYS_SIGN_GEN);
        gt.setGoods(goods);
        gt.setGoodSpec(spec);
        gt.setTransportUnit(transportUnit);
        gt.setShipmentType(shipmentType);
        gt.setUnit(accountUnit);
        gt.setUpdateTime(new Date());
        goodsTransportRepository.save(gt);
        return gt;
    }
}
