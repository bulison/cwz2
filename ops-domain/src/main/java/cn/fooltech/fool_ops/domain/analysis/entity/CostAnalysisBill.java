package cn.fooltech.fool_ops.domain.analysis.entity;

import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 成本分析
 *
 * @author cwz
 */
@ApiModel("成本分析")
@Getter
@Setter
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_cost_analysis_bill")
public class CostAnalysisBill {

    private static final long serialVersionUID = 1L;

    // 主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    // 单据日期
    @ApiModelProperty(value = "单据日期")
    @Column(name = "FBILL_DATE")
    private Date billDate;

    // 线路路径
    @ApiModelProperty(value = "线路路径")
    @Column(name = "FROUTE")
    private String route;

    // 采购公司 关联供应商
    @ApiModelProperty(value = "采购公司  关联供应商")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUPPLIER_ID")
    private Supplier supplier;

    // 货品ID
    @ApiModelProperty(value = "货品ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_ID")
    private Goods goods;

    // 货品属性ID
    @ApiModelProperty(value = "货品属性ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_SPEC_ID")
    private GoodsSpec goodsSpec;

    // 货品单位ID 货品记账单位
    @ApiModelProperty(value = "货品单位ID  货品记账单位")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FGOODS_UINT_ID")
    private Unit unit;

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

    // 出厂价
    @ApiModelProperty(value = "出厂价")
    @Column(name = "FFACTORY_PRICE")
    private BigDecimal factoryPrice = BigDecimal.ZERO;

    // 调整出厂价
    @ApiModelProperty(value = "调整出厂价")
    @Column(name = "FPUBLISH_FACTORY_PRICE")
    private BigDecimal publishFactoryPrice = BigDecimal.ZERO;

    // 运输费用
    @ApiModelProperty(value = "运输费用")
    @Column(name = "FFREIGHT_PRICE")
    private BigDecimal freightPrice = BigDecimal.ZERO;

    // 调整运输费用
    @ApiModelProperty(value = "调整运输费用")
    @Column(name = "FPUBLISH_FREIGHT_PRICE")
    private BigDecimal publishFreightPrice = BigDecimal.ZERO;

    // 成本总价
    @ApiModelProperty(value = "成本总价")
    @Column(name = "FTOTAL_PRICE")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    // 调整成本总价
    @ApiModelProperty(value = "调整成本总价")
    @Column(name = "FPUBLISH_TOTAL_PRICE")
    private BigDecimal publishTotalPrice = BigDecimal.ZERO;

    // 可执行标识(1-可执行 2-难执行 3-无法执行)
    @ApiModelProperty(value = "可执行标识(1-可执行 2-难执行 3-无法执行)")
    @Column(name = "FEXECUTE_SIGN")
    private Integer executeSign = 1;

    // 预计天数
    @ApiModelProperty(value = "预计天数")
    @Column(name = "FEXPECTED_DAYS")
    private Integer expectedDays = 1;

    // 备注
    @ApiModelProperty(value = "备注")
    @Column(name = "FREMARK")
    private String remark;

    // 发布 0-不发布 1-发布
    @ApiModelProperty(value = "发布 0-不发布 1-发布")
    @Column(name = "FPUBLISH")
    private Integer publish = 0;

    // 是否采购 0-仓库【运输报价】 1-采购【成本分析】
    @ApiModelProperty(value = "是否采购 0-仓库【运输报价】 1-采购【成本分析】")
    @Column(name = "FPURCHASE")
    private Integer purchase = 0;

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

    // 销售客户ID  关联客户，根据收货地得出
    @ApiModelProperty(value = "客户ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCUSTOMER_ID")
    private Customer customer;

    // 货品报价单ID 关联货品价格报价表
    @ApiModelProperty(value = "货品报价单ID 关联货品价格报价表")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPURCHASE_ID")
    private PurchasePrice purchasePrice;
    
    @ApiModelProperty(value = "损耗(元)")
    @Column(name="FLOSS")
    private BigDecimal loss;
}