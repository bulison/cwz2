package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
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
import java.util.Date;

/**
 *
 */
@ApiModel("运输费报价模板从2表")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_transport_template_detail2")
public class TransportTemplateDetail2 {

    private static final long serialVersionUID = 1L;


    //主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    //从1ID,关联运输费报价模板从1表ID
    //@ApiModelProperty(value = "从1ID,关联运输费报价模板从1表ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDETAIL1_ID")
    private TransportTemplateDetail1 detail1;


    //模板ID,关联运输费报价模板
    //@ApiModelProperty(value = "模板ID,关联运输费报价模板")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTEMPLATE_ID")
    private TransportTemplate template;


    //运输费用ID,关联辅助属性运输费用
    //@ApiModelProperty(value = "运输费用ID,关联辅助属性运输费用")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_COST_ID")
    private AuxiliaryAttr transportCost;


    //运输单位ID,关联辅助属性运输计价单位
    //@ApiModelProperty(value = "运输单位ID,关联辅助属性运输计价单位")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_UNIT_ID")
    private AuxiliaryAttr transportUnit;


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


    //修改时间戳,	初始值为当前时间
    @ApiModelProperty(value = "修改时间戳,	初始值为当前时间")
    @Column(name = "FUPDATE_TIME")
    private Date updateTime;


    //组织ID,机构ID
    @ApiModelProperty(value = "组织ID,机构ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    private Organization org;


    //账套ID
    //@ApiModelProperty(value = "账套ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    private FiscalAccount fiscalAccount;

    //从2表编号（用于记录从1表编号）
    @ApiModelProperty(value = "从2表编号（用于记录从1表编号）")
    @Column(name = "FCODE")
    private String code;

}