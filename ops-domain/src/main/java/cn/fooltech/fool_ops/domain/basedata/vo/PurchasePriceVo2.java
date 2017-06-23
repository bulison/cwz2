//package cn.fooltech.fool_ops.domain.basedata.vo;
//
//import cn.fooltech.fool_ops.config.Constants;
//import cn.fooltech.fool_ops.domain.basedata.entity.*;
//import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//
//import java.util.Date;
//
///**
// * Created by Administrator on 2017/1/4.
// */
//@Data
//@ToString
//@NoArgsConstructor
//public class PurchasePriceVo2 extends PurchasePrice {
//
//    private Supplier supplier=new Supplier();
//
//    //    @ApiModelProperty(value = "供应商名称")
//    private String supplierName;
//        private Goods goods=new Goods() ;
////    @ApiModelProperty(value = "货品名称")
//    private String goodsName;
//
//        private Unit goodsUnit=new Unit();
////    @ApiModelProperty(value = "单位名称")
//    private String unitName;
//
//        private GoodsSpec goodsSpec=new GoodsSpec() ;
////    @ApiModelProperty(value = "属性名称")
//    private String specName;
//    //    @ApiModelProperty(value = "发货地名称")
//    FreightAddress freightAddress= new FreightAddress() ;
////    @ApiModelProperty(value = "发货地名称")
//    private  String deliveryPlaceName;
//
//
//
//    //    @ApiModelProperty(value = "开始日期，查询用")
//    private Date startDay;
//
//    //    @ApiModelProperty(value = "结束日期，查询用")
//    private Date endDay;
//
//    /**
//     * 记录状态
//     */
////    @ApiModelProperty(value = "记录状态")
//    private String recordStatus = VehicleInformation.STATUS_SAC;
//    /**
//     * 搜索关键字
//     */
////    @ApiModelProperty(value = "搜索关键字")
//    private String searchKey;
//    /**
//     * 模糊搜索结果集大小
//     */
////    @ApiModelProperty(value = "模糊搜索结果集大小")
//    private Integer searchSize = Constants.VAGUE_SEARCH_SIZE;
//}
