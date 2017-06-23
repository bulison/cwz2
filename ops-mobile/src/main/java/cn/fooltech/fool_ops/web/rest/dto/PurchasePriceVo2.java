//package cn.fooltech.fool_ops.web.rest.dto;
//
//import cn.fooltech.fool_ops.config.Constants;
//import cn.fooltech.fool_ops.domain.basedata.entity.*;
//import cn.fooltech.fool_ops.domain.basedata.vo.PurchasePriceVo;
//import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
//
//
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//@ToString
//@Setter
//@NoArgsConstructor
//public class PurchasePriceVo2 extends PurchasePriceVo {
//    Map deliveryPlaceAddress = new HashMap();
//    private Map supplier = new HashMap();
//    private Map goods = new HashMap();
//    private Map goodsUnit = new HashMap();
//    private Map goodsSpec = new HashMap();
//
//    public Map getDeliveryPlaceAddress() {
//if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(this.getDeliveryPlace()))){
//    deliveryPlaceAddress.put("deliveryPlace",this.getDeliveryPlace());
//    deliveryPlaceAddress.put("deliveryPlaceName",this.getDeliveryPlaceName());
//}
//
//        return deliveryPlaceAddress;
//    }
//
//    public Map getSupplier() {
//
//        if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(this.getSupplierId()))){
//            supplier.put("supplierId",this.getSupplierId());
//            supplier.put("supplierName",this.getSupplierName());
//        }
//
//        return supplier;
//    }
//
//    public Map getGoods() {
//        if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(this.getGoodsId()))){
//            goods.put("goodsId",this.getGoodsId());
//            goods.put("goodsName",this.getGoodsName());
//        }
//
//        return goods;
//    }
//
//    public Map getGoodsUnit() {
//        if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(this.getUnitId()))){
//            goodsUnit.put("unitId",this.getUnitId());
//            goodsUnit.put("unitName",this.getUnitName());
//        }
//
//        return goodsUnit;
//    }
//
//    public Map getGoodsSpec() {
//        if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(this.getGoodSpecId()))){
//            goodsUnit.put("goodSpecId",this.getGoodSpecId());
//            goodsUnit.put("specName",this.getSpecName());
//        }
//        return goodsSpec;
//    }
//}
