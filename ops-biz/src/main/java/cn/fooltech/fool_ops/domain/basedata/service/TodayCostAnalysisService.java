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
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.transport.entity.GoodsTransport;
import cn.fooltech.fool_ops.domain.transport.entity.TransportBilldetail;
import cn.fooltech.fool_ops.domain.transport.repository.GoodsTransportRepository;
import cn.fooltech.fool_ops.domain.warehouse.repository.StockStoreRepository;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillDetailRepository;
import cn.fooltech.fool_ops.utils.DateUtil;
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
import java.util.stream.Collectors;


@Service
public class TodayCostAnalysisService {

    private static final int TRANSPORT_UNIT_SAFE = 0;
    private static final int TRANSPORT_UNIT_UNSAFE = 1;
    private static final String TRANSPORT_UNIT_UNSAFE_STR = "换算关系可疑";

    private Logger logger = LoggerFactory.getLogger(TodayStorageDeliveryAnalysisService.class);

	@Autowired
	private TransportLossRepository transportLossRepository;
    @Autowired
    private TodayCostAnalysisRepository todayCostAnalysisRepository;

    @Autowired
    private TransportRouteRepository routeRepository;

    @Autowired
    private FreightAddressService addressService;


    @Autowired
    private CostAnalysisBillRepository costBillRepository;

    @Autowired
    private CostAnalysisBilldetailRepository costBilldetailRepository;

    @Autowired
    private GoodsTransportRepository goodsTransportRepository;

    @Autowired
    private TransportPriceRepository transportPriceRepository;

    @Autowired
    private PurchasePriceRepository purchasePriceRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private GoodsSpecRepository specRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private TransportPriceDetail2Repository tpd2Repository;

    /**
     * 生成路径
     */
    @Transactional(readOnly = false)
    public void genRoute(){
        todayCostAnalysisRepository.genRoute();
    }

    /**
     * 删除当天旧数据
     */
    @Transactional
    public void dropOldData(){
        todayCostAnalysisRepository.dropOldData(1);
    }

    /**
     * 删除当天旧数据
     */
    @Transactional
    public void dropOldData(String accId){
        todayCostAnalysisRepository.dropOldData(1, accId);
    }

    @Transactional
    public void analysis() {

        dropOldData();

        List<TransportRoute> routes = routeRepository.findAll();
        Map<String, GoodsTransport> cache = Maps.newHashMap();

        for(TransportRoute route:routes){

            FreightAddress deliveryPlace = addressService.get(route.getDeliveryPlace().getFid());

//            if(route.getFiscalAccount().getFid().equals("402881865995d67e01599726657f0000")){
//                System.out.println("===========");
//            }

            if(deliveryPlace==null)continue;
            List<FreightAddress> deliveryAddress = null;
            if(deliveryPlace.getFlag()==FreightAddress.LEFA_PARENT){
                deliveryAddress = addressService.findAllChildren(deliveryPlace);
                deliveryAddress.add(deliveryPlace);
            }else{
                deliveryAddress = Lists.newArrayList(deliveryPlace);
            }

            for(FreightAddress deliverPlace:deliveryAddress){
                List<PurchasePrice> purchasePrices = purchasePriceRepository.findByDeliveryPlace(deliverPlace.getFid());

                if(purchasePrices==null || purchasePrices.size()==0)continue;

                FreightAddress receiptPlace = addressService.get(route.getReceiptPlace().getFid());

                if(receiptPlace==null)continue;
                if(receiptPlace.getEnable()==FreightAddress.ENABLE_N)continue;

                if(receiptPlace.getFlag()==FreightAddress.LEFA_PARENT){
                    List<FreightAddress> childAddress = addressService.findAllChildren(receiptPlace);
                    childAddress.add(receiptPlace);
                    for(FreightAddress child:childAddress){
                        if(child.getEnable()==FreightAddress.ENABLE_Y
                                && child.getRecipientSign()==FreightAddress.RECEIPT_Y){
                            insertCostAnalyzeBill(route, purchasePrices, child, deliverPlace, cache);
                        }
                    }
                }else{
                    if(receiptPlace.getRecipientSign()==FreightAddress.RECEIPT_N)continue;
                    insertCostAnalyzeBill(route, purchasePrices, receiptPlace, deliverPlace, cache);
                }
            }

        }
    }


    /**
     * 根据账套ID处理
     * @param accId
     */
    @Transactional
    public void analysis(String accId) {

        dropOldData(accId);

        List<TransportRoute> routes = routeRepository.findByAccId(accId);
        Map<String, GoodsTransport> cache = Maps.newHashMap();

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

            for(FreightAddress deliverPlace:deliveryAddress){
                List<PurchasePrice> purchasePrices = purchasePriceRepository.findByDeliveryPlace(deliverPlace.getFid());

                if(purchasePrices==null || purchasePrices.size()==0)continue;

                FreightAddress receiptPlace = addressService.get(route.getReceiptPlace().getFid());

                if(receiptPlace==null)continue;
                if(receiptPlace.getEnable()==FreightAddress.ENABLE_N)continue;

                if(receiptPlace.getFlag()==FreightAddress.LEFA_PARENT){
                    List<FreightAddress> childAddress = addressService.findAllChildren(receiptPlace);
                    childAddress.add(receiptPlace);
                    for(FreightAddress child:childAddress){
                        if(child.getEnable()==FreightAddress.ENABLE_Y
                                && child.getRecipientSign()==FreightAddress.RECEIPT_Y){
                            insertCostAnalyzeBill(route, purchasePrices, child, deliverPlace, cache);
                        }
                    }
                }else{
                    if(receiptPlace.getRecipientSign()==FreightAddress.RECEIPT_N)continue;
                    insertCostAnalyzeBill(route, purchasePrices, receiptPlace, deliverPlace, cache);
                }
            }

        }
    }


    /**
     * 插入成本分析表
     成本分析表字段	数据来源
     FID	系统自动生成
     FBILL_DATE	当前日期
     FROUTE	运输路径表的线路路径
     FSUPPLIER_ID	货品报价表的供应商
     FGOODS_ID	货品报价表的货品
     FGOODS_SPEC_ID	货品报价表的货品属性
     FGOODS_UINT_ID	货品资料的基本单位
     FDELIVERY_PLACE	运输路径表的发货地
     FRECEIPT_PLACE	运输路径表的收货地
     FFACTORY_PRICE	货品报价表的交货总价换算货品基体单位价格
     FPURCHASE	1
     */
    @Transactional
    private void insertCostAnalyzeBill(TransportRoute route, List<PurchasePrice> purchasePrices,
            FreightAddress receiptAddress, FreightAddress deliveryPlace, Map<String, GoodsTransport> cache){

        for(PurchasePrice pp:purchasePrices) {

            try{
                Goods goods = pp.getGoods();
                String specId = pp.getGoodSpecId() == null ? null : pp.getGoodSpecId();
                String unitGroupId = goods.getUnitGroup().getFid();
                Unit accountUnit = unitRepository.findTopByGroupId(unitGroupId);
                Unit unit = unitRepository.findOne(pp.getUnitId());
                GoodsSpec spec = null;
                if (!Strings.isNullOrEmpty(specId)) {
                    spec = specRepository.findOne(specId);
                }
                BigDecimal accountUnitePrice = NumberUtil.divide(pp.getDeliveryPrice(), unit.getScale(), 2);

                CostAnalysisBill bill = new CostAnalysisBill();
                bill.setPurchasePrice(pp);
                bill.setBillDate(new Date());
                bill.setRoute(route.getRoute());
                bill.setSupplier(supplierRepository.findOne(pp.getSupplierId()));
                bill.setGoods(goods);
                bill.setGoodsSpec(spec);
                bill.setUnit(accountUnit);
                bill.setDeliveryPlace(deliveryPlace);
                bill.setReceiptPlace(receiptAddress);
                bill.setFactoryPrice(accountUnitePrice);
                bill.setPurchase(1);
                bill.setOrg(route.getOrg());
                bill.setFiscalAccount(route.getFiscalAccount());
                bill.setCreateTime(new Date());
                bill.setUpdateTime(new Date());

                //-----------------


                CustomerAddress ca = customerAddressRepository.queryDefaultByDeliveryPlace(receiptAddress.getFid());
                if (ca != null) {
                    bill.setCustomer(customerRepository.findOne(ca.getCustomerId()));
                }

                costBillRepository.save(bill);

                //插入明细表并判断换算关系是否可疑
                int transportUnitSafe = insertCostAnalyzeBillDetail(route, bill, cache);
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
                Goods good=bill.getGoods();
                if(deliveryPlaceLoss!=null&&receiptPlaceLoss!=null&&shipmentType!=null&&good!=null){

                    String deliveryId=deliveryPlaceLoss.getFid();
                    String receiptId=receiptPlaceLoss.getFid();
                    String shipmentId=shipmentType.getFid();
                    String goodsId=good.getFid();

                    if(goodsSpec==null){
                        transportLoss=transportLossRepository.findLossByCostBill(route.getFiscalAccount().getFid(), deliveryId, receiptId, shipmentId, goodsId);
                    }else{
                        transportLoss=transportLossRepository.findLossByCostBill(route.getFiscalAccount().getFid(), deliveryId, receiptId, shipmentId, goodsId,goodsSpec.getFid());
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

                if(transportUnitSafe==TRANSPORT_UNIT_UNSAFE){
                    bill.setRemark(TRANSPORT_UNIT_UNSAFE_STR);
                }
                bill.setTotalPrice(bill.getTotalPrice().add(bill.getLoss()));
                costBillRepository.save(bill);
            }catch (Exception e){
                e.printStackTrace();
            }

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
    public Integer insertCostAnalyzeBillDetail(TransportRoute route, CostAnalysisBill bill, Map<String, GoodsTransport> cache){

        String routeLine = "["+route.getRoute()+"]";

        List<RouteVo> routeVos = JSONArray.parseArray(routeLine, RouteVo.class);
        String transportBillIds = route.getTransportBill();
        Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
        List<String> transportBills = splitter.splitToList(transportBillIds);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal oldtotal = BigDecimal.ZERO;
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

            String accId = bill.getFiscalAccount().getFid();

            //查出运输费报价
            TransportPrice price = transportPriceRepository.findOne(transportBills.get(index));
            //上一日有效的运输报价
            Date yesterday = DateUtilTools.getYesterday(price.getBillDate());
            TransportPrice oldprice = transportPriceRepository.findYesterdayRecord(yesterday, accId,
                    price.getReceiptPlace().getFid(),
                    price.getDeliveryPlace().getFid(),
                    price.getTransportType().getFid(),
                    price.getShipmentType().getFid());
            if(oldprice!=null){
                detail.setPublishFreightPrice(oldprice.getAmount());
                oldtotal = NumberUtil.add(oldprice.getAmount(), oldtotal);
            }else{
                detail.setPublishFreightPrice(BigDecimal.ZERO);
            }

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
            //detail.setPublishBasePrice(NumberUtil.divide(detail.getPublishFreightPrice(), detail.getConversionRate(), 2));
            detail.setPublishBasePrice(BigDecimal.ZERO);


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
        bill.setPublishFreightPrice(oldtotal);
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
    public GoodsTransport insertDefaultGoodsTransport(Organization org, Goods goods, GoodsSpec spec, Unit accountUnit,
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
