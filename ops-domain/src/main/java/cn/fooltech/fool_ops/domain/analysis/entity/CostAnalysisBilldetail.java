package cn.fooltech.fool_ops.domain.analysis.entity;

import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPrice;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
@ApiModel("成本分析明细")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_cost_analysis_billdetail")
public class CostAnalysisBilldetail {

    private static final long serialVersionUID = 1L;

    // 主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    // 单据ID
    @ApiModelProperty(value = "单据ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FBILL_ID")
    private CostAnalysisBill bill;

    // 序号
    @ApiModelProperty(value = "序号")
    @Column(name = "FNO")
    private Integer no;

    // 运输报价ID
    @ApiModelProperty(value = "运输报价ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_BILL_ID")
    private TransportPrice transportBill;

    //报价日期
    @ApiModelProperty(value = "报价日期")
    @Column(name = "FBILL_DATE")
    private Date billDate;

    // 运输公司  关联供应商
    @ApiModelProperty(value = "运输公司  关联供应商")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUPPLIER_ID")
    private Supplier supplier;

    // 发货地ID 关联场地表
    @ApiModelProperty(value = "发货地ID  关联场地表")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDELIVERY_PLACE")
    private FreightAddress deliveryPlace;

    // 收货地ID 关联场地表
    @ApiModelProperty(value = "收货地ID  关联场地表")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FRECEIPT_PLACE")
    private FreightAddress receiptPlace;

    // 运输方式ID(关联辅助属性运输方式)
    @ApiModelProperty(value = "运输方式ID(关联辅助属性运输方式)")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_TYPE_ID")
    private AuxiliaryAttr transportType;

    // 装运方式ID(关联辅助属性装运方式)
    @ApiModelProperty(value = "装运方式ID(关联辅助属性装运方式)")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSHIPMENT_TYPE_ID")
    private AuxiliaryAttr shipmentType;

    //运输计价单位ID  关联辅助属性运输费计价单位
    @ApiModelProperty(value = "运输计价单位ID  关联辅助属性运输费计价单位")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_UNIT_ID")
    private AuxiliaryAttr transportUnit;

    // 运输费用
    @ApiModelProperty(value = "运输费用")
    @Column(name = "FFREIGHT_PRICE")
    private BigDecimal freightPrice = BigDecimal.ZERO;

    // 调整运输费用
    @ApiModelProperty(value = "调整运输费用")
    @Column(name = "FPUBLISH_FREIGHT_PRICE")
    private BigDecimal publishFreightPrice;

    // 换算关系  运输单位与货品基本单位的换算关系
    @ApiModelProperty(value = "换算关系  运输单位与货品基本单位的换算关系")
    @Column(name = "FCONVERSION_RATE")
    private BigDecimal conversionRate;

    // 折算运输单价
    @ApiModelProperty(value = "折算运输单价")
    @Column(name = "FBASE_PRICE")
    private BigDecimal basePrice = BigDecimal.ZERO;

    // 折算运输单价
    @ApiModelProperty(value = "调整折算运输单价")
    @Column(name = "FPUBLISH_BASE_PRICE")
    private BigDecimal publishBasePrice;

    // 可执行标识(1-可执行 2-难执行 3-无法执行)
    @ApiModelProperty(value = "可执行标识(1-可执行 2-难执行 3-无法执行)")
    @Column(name = "FEXECUTE_SIGN")
    private Integer executeSign = 1;

    // 预计天数
    @ApiModelProperty(value = "预计天数")
    @Column(name = "FEXPECTED_DAYS")
    private Integer expectedDays = 1;

    // 场地费用单价
    @ApiModelProperty(value = "场地费用单价")
    @Column(name = "FGROUND_COST_PRICE")
    private BigDecimal groundCostPrice = BigDecimal.ZERO;

    // 备注
    @ApiModelProperty(value = "备注")
    @Column(name = "FREMARK")
    private String remark;

    // 创建时间
    @ApiModelProperty(value = "创建时间")
    @Column(name = "FCREATE_TIME")
    private Date createTime;

    // 创建人
    @ApiModelProperty(value = "创建人")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    private User creator;

    // 修改时间戳,初始值为当前时间
    @ApiModelProperty(value = "修改时间戳,初始值为当前时间")
    @Column(name = "FUPDATE_TIME")
    private Date updateTime;

    // 组织ID,机构ID
    @ApiModelProperty(value = "组织ID,机构ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    private Organization org;

    // 账套ID
    @ApiModelProperty(value = "账套ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    private FiscalAccount fiscalAccount;

}