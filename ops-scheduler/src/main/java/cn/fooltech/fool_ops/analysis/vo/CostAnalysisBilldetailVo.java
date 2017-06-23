package cn.fooltech.fool_ops.analysis.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CostAnalysisBilldetailVo {

    //主键

    private String id;


    //单据ID

    private String billId;


    //序号

    private Integer no;


    //运输报价ID

    private String transportBillId;


    //报价日期

    private Date billDate;


    //运输公司  关联供应商

    private String supplierId;


    //发货地ID，关联场地表

    private String deliveryPlace;


    //收货地ID，关联场地表

    private String receiptPlace;


    //运输方式ID，关联辅助属性运输方式

    private String transportTypeId;


    //装运方式ID，关联辅助属性装运方式

    private String shipmentTypeId;


    //运输计价单位ID  关联辅助属性运输费计价单位

    private String transportUnitId;


    //运输费用

    private Float freightPrice;


    //调整运输费用

    private Float publishFreightPrice;


    //换算关系  运输单位与货品基本单位的换算关系

    private Float conversionRate;


    //折算运输单价

    private Float basePrice;


    //调整折算运输单价

    private Float publishBasePrice;


    //可执行标识：1-可执行 2-难执行 3-无法执行

    private Integer executeSign;


    //预计天数

    private Integer expectedDays;


    //场地费用单价

    private Float groundCostPrice;


    //备注

    private String remark;


    //创建时间

    private Date createTime;


    //创建人

    private String creatorId;


    //修改时间戳，初始值为当前时间

    private Date updateTime;


    //组织ID,机构ID

    private String orgId;


    //账套ID

    private String accId;
}
