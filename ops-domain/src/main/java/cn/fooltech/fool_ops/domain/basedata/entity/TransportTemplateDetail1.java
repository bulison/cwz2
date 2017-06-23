package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 *
 */
@ApiModel("运输费报价模板从1表")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_transport_template_detail1")
public class TransportTemplateDetail1 {

    private static final long serialVersionUID = 1L;


    //主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    //模板ID,关联运输费报价模板
    //@ApiModelProperty(value = "模板ID,关联运输费报价模板")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTEMPLATE_ID")
    private TransportTemplate template;


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


    //预计天数
    @ApiModelProperty(value = "预计天数")
    @Column(name = "FEXPECTED_DAYS")
    private Integer expectedDays;


    //描述
    @ApiModelProperty(value = "描述")
    @Column(name = "FDESCRIBE")
    private String describe;


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


    //组织ID
    //@ApiModelProperty(value = "组织ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    private Organization org;


    //账套ID
    //@ApiModelProperty(value = "账套ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    private FiscalAccount fiscalAccount;

    //装运方式ID	关联辅助属性装运方式
    //@ApiModelProperty(value = "装运方式ID	关联辅助属性装运方式", hidden = true)
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSHIPMENT_TYPE_ID")
    @JsonIgnore
    private AuxiliaryAttr shipmentType;

    //从1表编号（用于添加修改从2表）
    @ApiModelProperty(value = "从1表编号（用于添加修改从2表）")
    @Column(name = "FCODE")
    private String code;
}