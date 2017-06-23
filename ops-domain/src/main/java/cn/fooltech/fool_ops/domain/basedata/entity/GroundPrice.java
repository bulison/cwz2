package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.AbstractEntity;
import com.alibaba.fastjson.annotation.JSONField;
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
 * 场地费报价
 */
@ApiModel("场地费报价")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_ground_price")
public class GroundPrice extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    public static int ENABLE_Y = 1;
    public static int ENABLE_N = 0;

    //主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    //单号
    @ApiModelProperty(value = "单号")
    @Column(name = "FCODE")
    private String code;


    //单据日期
    @ApiModelProperty(value = "单据日期")
    @Column(name = "FBILL_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date billDate;


    //地址ID
    @ApiModelProperty(value = "地址ID")
    @Column(name = "FADDRESS_ID")
    private String addressId;


    //有效日期
    @ApiModelProperty(value = "有效日期")
    @Column(name = "FEFFECTIVE_DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date effectiveDate;


    //描述
    @ApiModelProperty(value = "描述")
    @Column(name = "FDESCRIBE")
    private String describe;


    //创建时间
    @ApiModelProperty(value = "创建时间")
    @Column(name = "FCREATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    //创建人
    @ApiModelProperty(value = "创建人")
    @Column(name = "FCREATOR_ID")
    private String creatorId;


    @Column(name = "FUPDATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    //组织ID
    @ApiModelProperty(value = "组织ID")
    @Column(name = "FORG_ID")
    private String orgId;


    //账套ID
    @ApiModelProperty(value = "账套ID")
    @Column(name = "FACC_ID")
    private String accId;

    //成本费用金额,由从表成本费用标识为1的记录汇总得出
    @ApiModelProperty(value = "成本费用金额,由从表成本费用标识为1的记录汇总得出")
    @Column(name = "FCOST_AMOUNT")
    private BigDecimal costAmount;

    //状态0-失效;1-有效
    @ApiModelProperty(value = "状态0-失效;1-有效")
    @Column(name = "FENABLE")
    private Integer enable;

    //状态,0-失效 1-有效
    @ApiModelProperty(value = "日状态,0-失效 1-有效（每天只要一条有效数据）")
    @Column(name = "FENABLE_DATE")
    private Integer dayEnable;
}