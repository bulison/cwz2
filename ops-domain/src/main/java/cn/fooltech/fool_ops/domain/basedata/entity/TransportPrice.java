package cn.fooltech.fool_ops.domain.basedata.entity;

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
 *
 */
@ApiModel("运输费报价")

@Data
@NoArgsConstructor
@Entity
@Table(name = "tsb_transport_price")
public class TransportPrice {

    private static final long serialVersionUID = 1L;


    //主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    //单号,从单据单号生成规则生成单号
    @ApiModelProperty(value = "单号,从单据单号生成规则生成单号")
    @Column(name = "FCODE")
    private String code;


    //单据日期,取当前时间
    @ApiModelProperty(value = "单据日期,取当前时间")
    @Column(name = "FBILL_DATE")
    private Date billDate;


    //承运单位,关联供应商
    //@ApiModelProperty(value = "承运单位,关联供应商")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUPPLIER_ID")
    private Supplier supplier;
    
    //报价单位,关联供应商
    //@ApiModelProperty(value = "报价单位,关联供应商")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPRICE_UNIT_ID")
    private Supplier priceUnit;

    //发货地ID,关联场地表
    //@ApiModelProperty(value = "发货地ID,关联场地表")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDELIVERY_PLACE")
    private FreightAddress deliveryPlace;


    //收货地ID,关联场地表
    //@ApiModelProperty(value = "收货地ID,关联场地表")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FRECEIPT_PLACE")
    private FreightAddress receiptPlace;


    //运输方式ID,关联辅助属性运输方式
    //@ApiModelProperty(value = "运输方式ID,关联辅助属性运输方式")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_TYPE_ID")
    private AuxiliaryAttr transportType;


    //装运方式ID,关联辅助属性装运方式
    //@ApiModelProperty(value = "装运方式ID,关联辅助属性装运方式")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSHIPMENT_TYPE_ID")
    private AuxiliaryAttr shipmentType;
    
    //运输单位ID,关联辅助属性运输计价单位
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_UNIT_ID")
    private AuxiliaryAttr transportUnit;


    //有效日期
    @ApiModelProperty(value = "有效日期")
    @Column(name = "FEFFECTIVE_DATE")
    private Date effectiveDate;


    //金额
    @ApiModelProperty(value = "金额")
    @Column(name = "FAMOUNT")
    private BigDecimal amount;


    //可执行标识,1-可执行 2-难执行 3-无法执行
    @ApiModelProperty(value = "可执行标识,1-可执行 2-难执行 3-无法执行")
    @Column(name = "FEXECUTE_SIGN")
    private Integer executeSign;


    //预计天数
    @ApiModelProperty(value = "预计天数")
    @Column(name = "FEXPECTED_DAYS")
    private Integer expectedDays;


    //描述
    @ApiModelProperty(value = "描述")
    @Column(name = "FDESCRIBE")
    private String describe;


    //状态,0-失效 1-有效
    @ApiModelProperty(value = "状态,0-失效 1-有效")
    @Column(name = "FENABLE")
    private Integer enable;


    //创建时间
    @ApiModelProperty(value = "创建时间")
    @Column(name = "FCREATE_TIME")
    private Date createTime;


    //创建人
    //@ApiModelProperty(value = "创建人")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    private User creator;


    //修改时间戳,初始值为当前时间
    @ApiModelProperty(value = "修改时间戳,初始值为当前时间")
    @Column(name = "FUPDATE_TIME")
    private Date updateTime;


    //组织ID,机构ID
    //@ApiModelProperty(value = "组织ID,机构ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    private Organization org;


    //账套ID
    //@ApiModelProperty(value = "账套ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    private FiscalAccount fiscalAccount;

    //状态,0-失效 1-有效
    @ApiModelProperty(value = "日状态,0-失效 1-有效（每天只要一条有效数据）")
    @Column(name = "FENABLE_DATE")
    private Integer dayEnable;
}