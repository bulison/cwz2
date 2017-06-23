package cn.fooltech.fool_ops.domain.basedata.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 场地费报价从表
 */
@ApiModel("场地费报价从表")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_ground_price_detail")
public class GroundPriceDetail {

    private static final long serialVersionUID = 1L;

    public static int COST_SIGN_Y = 1;
    public static int COST_SIGN_N = 0;


    //主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    //主表ID,关联场地费报价
    @ApiModelProperty(value = "主表ID,关联场地费报价")
    @Column(name = "FBILL_ID")
    private String billId;


    //运输费用ID,关联辅助属性运输费用
    @ApiModelProperty(value = "运输费用ID,关联辅助属性运输费用")
    @Column(name = "FTRANSPORT_COST_ID")
    private String transportCostId;


    //运输单位ID,关联辅助属性运输计价单位
    @ApiModelProperty(value = "运输单位ID,关联辅助属性运输计价单位")
    @Column(name = "FTRANSPORT_UNIT_ID")
    private String transportUnitId;


    //费用金额
    @ApiModelProperty(value = "费用金额")
    @Column(name = "FAMOUNT")
    private BigDecimal amount;


    //成本费用标识:0-否 1-是
    @ApiModelProperty(value = "成本费用标识:0-否 1-是")
    @Column(name = "FCOST_SIGN")
    private Integer costSign;


    //描述
    @ApiModelProperty(value = "描述")
    @Column(name = "FDESCRIBE")
    private String describe;


    //创建时间
    @ApiModelProperty(value = "创建时间")
    @Column(name = "FCREATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    //创建人
    @ApiModelProperty(value = "创建人")
    @Column(name = "FCREATOR_ID")
    private String creatorId;


    //修改时间戳
    @ApiModelProperty(value = "修改时间戳")
    @Column(name = "FUPDATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    //组织ID
    @ApiModelProperty(value = "组织ID")
    @Column(name = "FORG_ID")
    private String orgId;


    //账套ID
    @ApiModelProperty(value = "账套ID")
    @Column(name = "FACC_ID")
    private String accId;


}