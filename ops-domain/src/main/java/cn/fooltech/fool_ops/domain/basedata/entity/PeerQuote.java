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
 *
 */
@ApiModel("同行报价")
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_peer_quote")
public class PeerQuote extends AbstractEntity {

    private static final long serialVersionUID = 1L;


    //主键
    private String id;


    //单号,从单据单号生成规则生成单号
    private String code;


    //单据日期,取当前时间
    private Date billDate;


    //供应商,直接输入名字
    private String supplier;


    //客户,关联客户表
    private String customerId;


    //货品,关联货品表
    private Goods goods;


    //货品属性ID,关联属性表
    private String goodSpecId;


    //货品单位,关联单位表
    private String unitId;


    //交货总价
    private BigDecimal deliveryPrice;


    //收货地ID,关联场地表
    private String receiptPlace;


    //描述
    private String describe;


    //创建时间
    private Date createTime;


    //创建人
    private String creatorId;


    //修改时间戳,初始值为当前时间
    private Date updateTime;


    //组织ID
    private String orgId;


    //账套ID
    private String accId;

    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "FBILL_DATE")
    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    @Column(name = "FSUPPLIER")
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Column(name = "FCUSTOMER_ID")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @JoinColumn(name = "FGOODS_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Column(name = "FGOOD_SPEC_ID")
    public String getGoodSpecId() {
        return goodSpecId;
    }

    public void setGoodSpecId(String goodSpecId) {
        this.goodSpecId = goodSpecId;
    }

    @Column(name = "FUNIT_ID")
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @Column(name = "FDELIVERY_PRICE")
    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    @Column(name = "FRECEIPT_PLACE")
    public String getReceiptPlace() {
        return receiptPlace;
    }

    public void setReceiptPlace(String receiptPlace) {
        this.receiptPlace = receiptPlace;
    }

    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "FCREATOR_ID")
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "FORG_ID")
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Column(name = "FACC_ID")
    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }
}