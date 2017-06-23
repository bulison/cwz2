package cn.fooltech.fool_ops.domain.asset.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 固定资产卡片-实体类
 *
 * @author xjh
 */

@Entity
@Table(name = "tbd_asset_detail")
public class AssetDetail extends OpsOrgEntity {


    public static final int TYPE_BUY = 1;//资产购入
    public static final int TYPE_DEPRECIATION = 2;//资产折旧
    public static final int TYPE_CLEAR = 3;//资产清算
    public static final short TYPE_SASSET_BUY = 1; //资产购入
    public static final short TYPE_SASSET_DEPRECIATION = 2; //资产折旧
    public static final short TYPE_SASSET_CLEAR = 3; //资产清算
    private static final long serialVersionUID = -6788054232413065574L;
    /**
     * 固定资产
     */
    private Asset asset;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 日期
     */
    private Date date;
    /**
     * 金额
     */
    private BigDecimal amount = BigDecimal.ZERO;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private User creator;
    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;
    /**
     * 修改时间戳
     */
    private Date updateTime;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FASSET_ID")
    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    @Column(name = "FTYPE")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "FDATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "FREMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取账套
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    /**
     * 获取修改时间戳
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
