package cn.fooltech.fool_ops.domain.transport.entity;

import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
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
 * 收/发货单从表2
 */
@ApiModel("收/发货单从表2")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_transport_billdetail")
public class TransportBilldetail {

    private static final long serialVersionUID = 1L;


    //主键
    @ApiModelProperty(value = "主键")
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;


    //单据表ID
    //@ApiModelProperty(hidden = true)
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTRANSPORT_BILL_ID")
    private WarehouseBill transportBill;


    //箱号
    @ApiModelProperty(value = "箱号")
    @Column(name = "FCONTAINER_NUMBER")
    private String containerNumber;


    //封号1
    @ApiModelProperty(value = "封号1")
    @Column(name = "FSEALING_NUMBER1")
    private String sealingNumber1;


    //封号2
    @ApiModelProperty(value = "封号2")
    @Column(name = "FSEALING_NUMBER2")
    private String sealingNumber2;


    //描述
    @ApiModelProperty(value = "描述")
    @Column(name = "FDESCRIBE")
    private String describe;


    //创建时间
    @ApiModelProperty(value = "创建时间")
    @Column(name = "FCREATE_TIME")
    private Date createTime;


    //创建人
    @ApiModelProperty(value = "创建人")
    @Column(name = "FCREATOR_ID")
    private String creatorId;


    //修改时间戳,初始值为当前时间
    @ApiModelProperty(value = "修改时间戳,初始值为当前时间")
    @Column(name = "FUPDATE_TIME")
    private Date updateTime;


    //组织ID,机构ID
    @ApiModelProperty(value = "组织ID,机构ID")
    @Column(name = "FORG_ID")
    private String orgId;


    //账套ID
    @ApiModelProperty(value = "账套ID")
    @Column(name = "FACC_ID")
    private String accId;


}