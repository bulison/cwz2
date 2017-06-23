package cn.fooltech.fool_ops.domain.report.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;

import javax.persistence.*;
import java.util.Date;


/**
 * 资产负债抽象类
 *
 * @author xjh
 */
@MappedSuperclass
public class SheetFormula extends OpsOrgEntity {

    private static final long serialVersionUID = -1652303843913541347L;

    /**
     * 资产项目
     */
    private String assetItem;

    /**
     * 资产行号
     */
    private Integer assetNumber;

    /**
     * 资产公式
     */
    private String assetFormula;

    /**
     * 负债项目
     */
    private String debitItem;

    /**
     * 负债行号
     */
    private Integer debitNumber;

    /**
     * 负债公式
     */
    private String debitFormula;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 更新时间戳
     */
    private Date updateTime;

    @Column(name = "FASSET_ITEM")
    public String getAssetItem() {
        return assetItem;
    }

    public void setAssetItem(String assetItem) {
        this.assetItem = assetItem;
    }

    @Column(name = "FASSET_NUMBER")
    public Integer getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(Integer assetNumber) {
        this.assetNumber = assetNumber;
    }

    @Column(name = "FASSET_FORMULA")
    public String getAssetFormula() {
        return assetFormula;
    }

    public void setAssetFormula(String assetFormula) {
        this.assetFormula = assetFormula;
    }

    @Column(name = "FDEBT_ITEM")
    public String getDebitItem() {
        return debitItem;
    }

    public void setDebitItem(String debitItem) {
        this.debitItem = debitItem;
    }

    @Column(name = "FDEBT_NUMBER")
    public Integer getDebitNumber() {
        return debitNumber;
    }

    public void setDebitNumber(Integer debitNumber) {
        this.debitNumber = debitNumber;
    }

    @Column(name = "FDEBT_FORMULA")
    public String getDebitFormula() {
        return debitFormula;
    }

    public void setDebitFormula(String debitFormula) {
        this.debitFormula = debitFormula;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID", nullable = false)
    public Organization getDept() {
        return dept;
    }

    public void setDept(Organization dept) {
        this.dept = dept;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
