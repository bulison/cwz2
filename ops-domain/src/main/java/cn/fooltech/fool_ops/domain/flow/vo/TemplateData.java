package cn.fooltech.fool_ops.domain.flow.vo;

import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.flow.entity.PlanTemplate;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.utils.NumberUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 模板生成的数据
 */
@NoArgsConstructor
@Getter
@Setter
public class TemplateData {
    private PlanTemplate template;//每个模板的模板ID
    private String lastTemplateId;//上一个模板ID
    private Integer flag;//模板标识（1-采购，2-运输，3-销售）
    private Date startDate;//计划开始时间
    private Date endDate;//计划完成时间
    private Integer days;
    private Goods goods;//货品
    private GoodsSpec spec;//货品属性
    private BigDecimal quentity;//货品数量
    private String accountUnitName;
    private BigDecimal transportQuentity;//运输数量
    private FreightAddress publishAddress;//发货地
    private FreightAddress receiveAddress;//收货地
    private AuxiliaryAttr transportType;//运输方式
    private AuxiliaryAttr shipmentType;//装运方式
    private Customer customer;//客户
    private Supplier supplier;//供应商
    private String routeIds;//计划货品表从表的运输路径ID(多个逗号隔开)
    private BigDecimal totalAmount;//该模板总金额
    private Boolean isTail = false;//是否队尾数据

    private List<TemplateData> mergeData = Lists.newArrayList();
    private TemplateData pre;
}
