package cn.fooltech.fool_ops.domain.analysis.entity;

import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPrice;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 运输路径表
 *
 * @author cwz
 */
@ApiModel("运输路径表")
@Getter
@Setter
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_transport_route")
public class TransportRoute {

    private static final long serialVersionUID = 1L;

    // 主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    // 运输报价ID 默认取值的运输报价
    @ApiModelProperty(value = "运输报价ID 默认取值的运输报价")
    @Column(name = "FTRANSPORT_BILL_ID")
    private String transportBill;

    // 线路路径 记录JSON
    @ApiModelProperty(value = "线路路径 记录JSON")
    @Column(name = "FROUTE")
    private String route;

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

    // 创建时间
    @ApiModelProperty(value = "创建时间")
    @Column(name = "FCREATE_TIME")
    private Date createTime;

    // 创建人
    @ApiModelProperty(value = "创建人")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    private User creator;

    // 修改时间戳(初始值为当前时间)
    @ApiModelProperty(value = "修改时间戳(初始值为当前时间)")
    @Column(name = "FUPDATE_TIME")
    private Date updateTime;

    // 机构ID
    @ApiModelProperty(value = "机构ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    private Organization org;

    // 账套ID
    @ApiModelProperty(value = "账套ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    private FiscalAccount fiscalAccount;

}