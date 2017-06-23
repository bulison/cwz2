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
@ApiModel("运输费报价模板")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_transport_template")
public class TransportTemplate {

    private static final long serialVersionUID = 1L;


    //主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    //编号
    @ApiModelProperty(value = "编号")
    @Column(name = "FCODE")
    private String code;


    //名称
    @ApiModelProperty(value = "名称")
    @Column(name = "FNAME")
    private String name;


    //发货地ID	关联场地表
    //@ApiModelProperty(value = "发货地ID	关联场地表", hidden = true)
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDELIVERY_PLACE")
    @JsonIgnore
    private FreightAddress deliveryPlace;


    //收货地ID	关联场地表
    //@ApiModelProperty(value = "收货地ID	关联场地表", hidden = true)
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FRECEIPT_PLACE")
    @JsonIgnore
    private FreightAddress receiptPlace;


    //运输方式ID	关联辅助属性运输方式
    //@ApiModelProperty(value = "运输方式ID	关联辅助属性运输方式", hidden = true)
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_TYPE_ID")
    @JsonIgnore
    private AuxiliaryAttr transportType;


    //装运方式ID	关联辅助属性装运方式
    //@ApiModelProperty(value = "装运方式ID	关联辅助属性装运方式", hidden = true)
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSHIPMENT_TYPE_ID")
    @JsonIgnore
    private AuxiliaryAttr shipmentType;


    //预计天数
    @ApiModelProperty(value = "预计天数")
    @Column(name = "FEXPECTED_DAYS")
    private Integer expectedDays;


    //状态0--停用 1--启用
    @ApiModelProperty(value = "状态0--停用 1--启用")
    @Column(name = "ENABLE")
    private Integer enable;


    //描述
    @ApiModelProperty(value = "描述")
    @Column(name = "FDESCRIBE")
    private String describe;


    //创建时间
    @ApiModelProperty(value = "创建时间")
    @Column(name = "FCREATE_TIME")
    private Date createTime;


    //创建人
    //@ApiModelProperty(value = "创建人", hidden = true)
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    @JsonIgnore
    private User creator;


    //修改时间戳	初始值为当前时间
    @ApiModelProperty(value = "修改时间戳	初始值为当前时间")
    @Column(name = "FUPDATE_TIME")
    private Date updateTime;


    //组织ID	机构ID
    //@ApiModelProperty(value = "组织ID	机构ID", hidden = true)
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    @JsonIgnore
    private Organization org;


    //账套ID
    //@ApiModelProperty(value = "账套ID", hidden = true)
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    @JsonIgnore
    private FiscalAccount fiscalAccount;

}