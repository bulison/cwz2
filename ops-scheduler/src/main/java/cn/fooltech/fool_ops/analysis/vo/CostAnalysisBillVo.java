package cn.fooltech.fool_ops.analysis.vo;

import lombok.Data;

import java.util.Date;


@Data
public class CostAnalysisBillVo {


    //主键

    private String id;


    //单据日期

    private Date billDate;


    //线路路径

    private String route;


    //采购公司  关联供应商

    private String supplierId;


    //货品ID

    private String goodsId;


    //货品属性ID

    private String goodsSpecId;


    //货品单位ID  货品记账单位

    private String goodsUintId;


    //发货地ID  关联场地表

    private String deliveryPlace;


    //收货地ID  关联场地表

    private String receiptPlace;


    //出厂价

    private Float factoryPrice;


    //调整出厂价

    private Float publishFactoryPrice;


    //运输费用

    private Float freightPrice;


    //调整运输费用

    private Float publishFreightPrice;


    //成本总价

    private Float totalPrice;


    //调整成本总价

    private Float publishTotalPrice;


    //可执行标识(1-可执行 2-难执行 3-无法执行)

    private Integer executeSign;


    //预计天数

    private Integer expectedDays;


    //备注

    private String remark;


    //发布 0-不发布 1-发布

    private Integer publish;


    //是否采购 0-仓库 1-采购

    private Integer purchase;


    //创建时间

    private Date createTime;


    //创建人

    private String creatorId;


    //修改时间戳,初始值为当前时间

    private Date updateTime;


    //组织ID,机构ID

    private String orgId;


    //账套ID

    private String accId;
}
