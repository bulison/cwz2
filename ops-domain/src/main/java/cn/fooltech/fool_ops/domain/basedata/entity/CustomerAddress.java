package cn.fooltech.fool_ops.domain.basedata.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;

/**
 * 客户/供应商默认收/发货地址
 */
@ApiModel("客户/供应商默认收/发货地址")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_customer_address")
public class CustomerAddress {

    public static final Integer DEFAULT = 1;
    public static final Integer NOT_DEFAULT = 0;
    private static final long serialVersionUID = 1L;
    //主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    //往来单位ID,关联供应商表或销售商表ID
    @ApiModelProperty(value = "往来单位ID,关联供应商表或销售商表ID")
    @Column(name = "FCUSTOMER_ID")
    private String customerId;


    //地址ID,关联货运地址表
    @ApiModelProperty(value = "地址ID,关联货运地址表")
    @Column(name = "FADDRESS_ID")
    private String addressId;


    //默认地址
    @ApiModelProperty(value = "默认地址:0-否 1-是")
    @Column(name = "FDEFAULT")
    private Integer fdefault;


    //描述
    @ApiModelProperty(value = "描述")
    @Column(name = "FDESCRIBE")
    private String describe;


    //创建时间
    @ApiModelProperty(value = "创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "FCREATE_TIME")
    private Date createTime;


    //创建人
    @ApiModelProperty(value = "创建人")
    @Column(name = "FCREATOR_ID")
    private String creatorId;


    //修改时间戳
    @ApiModelProperty(value = "修改时间戳")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "FUPDATE_TIME")
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