package cn.fooltech.fool_ops.domain.asset.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 固定资产卡片-实体类
 *
 * @author xjh
 */

@Entity
@Table(name = "tbd_asset")
public class Asset extends OpsOrgEntity {

    public static final short STATUS_UNAUDIT = 0;//未审核
    public static final short STATUS_AUDIT = 1;//审核
    public static final short STATUS_ACCRUED = 2;//计提
    public static final short STATUS_ACCRUED_COMPLETE = 3;//计提完成
    public static final short STATUS_CLEAR = 4;//已清算
    private static final long serialVersionUID = -6767229717740802775L;
    /**
     * 资产编号
     */
    private String assetCode;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 资产类型
     */
    private AuxiliaryAttr assetType;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 数量
     */
    private BigDecimal quentity = new BigDecimal("1");

    /**
     * 资产原值
     */
    private BigDecimal initialValue = BigDecimal.ZERO;

    /**
     * 折旧年限
     */
    private Integer discountYear = 5;

    /**
     * 残值率
     */
    private BigDecimal residualRate = new BigDecimal("0.05");

    /**
     * 资产净值
     */
    private BigDecimal residualValue = BigDecimal.ZERO;

    /**
     * 来源
     */
    private String supplier;

    /**
     * 等级
     */
    private String grade;

    /**
     * 产地及厂家
     */
    private String manufactor;

    /**
     * 规格型号
     */
    private String model;

    /**
     * 使用日期
     */
    private Date useDate;

    /**
     * 购买日期
     */
    private Date buyDate;

    /**
     * 计提日期
     */
    private Date shareDate;

    /**
     * 固定资产科目
     */
    private FiscalAccountingSubject assetSubject;

    /**
     * 折旧科目
     */
    private FiscalAccountingSubject depreciationSubject;

    /**
     * 支付科目
     */
    private FiscalAccountingSubject paymentSubject;

    /**
     * 清算科目
     */
    private FiscalAccountingSubject clearSubject;

    /**
     * 费用科目
     */
    private FiscalAccountingSubject expenseSubject;

    /**
     * 状态
     * 0--未审核
     * 1--审核
     * 2--计提中
     * 3--计提完成
     * 4--已清算
     */
    private Short recordStatus = STATUS_UNAUDIT;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 审核人
     */
    private User auditor;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 修改时间戳
     */
    private Date updateTime;

    /**
     * 权限过滤的部门
     */
    private Organization dep;

    /**
     * 详情
     */
    private List<AssetDetail> details = Lists.newArrayList();

    @Column(name = "FASSET_CODE")
    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    @Column(name = "FASSET_NAME")
    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FASSET_TYPE")
    public AuxiliaryAttr getAssetType() {
        return assetType;
    }

    public void setAssetType(AuxiliaryAttr assetType) {
        this.assetType = assetType;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    public void setDept(Organization dept) {
        this.dept = dept;
    }

    @Column(name = "FQUANTITY")
    public BigDecimal getQuentity() {
        return quentity;
    }

    public void setQuentity(BigDecimal quentity) {
        this.quentity = quentity;
    }

    @Column(name = "FINITIAL_VALUE")
    public BigDecimal getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    @Column(name = "FDISCOUNT_YEAR")
    public Integer getDiscountYear() {
        return discountYear;
    }

    public void setDiscountYear(Integer discountYear) {
        this.discountYear = discountYear;
    }

    @Column(name = "FRESIDUAL_RATE")
    public BigDecimal getResidualRate() {
        return residualRate;
    }

    public void setResidualRate(BigDecimal residualRate) {
        this.residualRate = residualRate;
    }

    @Column(name = "FRESIDUAL_VALUE")
    public BigDecimal getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(BigDecimal residualValue) {
        this.residualValue = residualValue;
    }

    @Column(name = "FSUPPLIER")
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Column(name = "FGRADE")
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Column(name = "FMANUFACTOR")
    public String getManufactor() {
        return manufactor;
    }

    public void setManufactor(String manufactor) {
        this.manufactor = manufactor;
    }

    @Column(name = "FMODEL")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "FUSE_DATE")
    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    @Column(name = "FBUY_DATE")
    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    @Column(name = "FSHARE_DATE")
    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }

    @JoinColumn(name = "FASSET_SUBJECT")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public FiscalAccountingSubject getAssetSubject() {
        return assetSubject;
    }

    public void setAssetSubject(FiscalAccountingSubject assetSubject) {
        this.assetSubject = assetSubject;
    }

    @JoinColumn(name = "FDEPRECIATION_SUBJECT")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public FiscalAccountingSubject getDepreciationSubject() {
        return depreciationSubject;
    }

    public void setDepreciationSubject(FiscalAccountingSubject depreciationSubject) {
        this.depreciationSubject = depreciationSubject;
    }

    @JoinColumn(name = "FPAYMENT_SUBJECT")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public FiscalAccountingSubject getPaymentSubject() {
        return paymentSubject;
    }

    public void setPaymentSubject(FiscalAccountingSubject paymentSubject) {
        this.paymentSubject = paymentSubject;
    }

    @JoinColumn(name = "FCLEAR_SUBJECT")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public FiscalAccountingSubject getClearSubject() {
        return clearSubject;
    }

    public void setClearSubject(FiscalAccountingSubject clearSubject) {
        this.clearSubject = clearSubject;
    }

    @Column(name = "RECORD_STATUS")
    public Short getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Short recordStatus) {
        this.recordStatus = recordStatus;
    }

    /**
     * 获取费用科目
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FEXPENSE_SUBJECT")
    public FiscalAccountingSubject getExpenseSubject() {
        return expenseSubject;
    }

    /**
     * 设置费用科目
     *
     * @param expenseSubject
     */
    public void setExpenseSubject(FiscalAccountingSubject expenseSubject) {
        this.expenseSubject = expenseSubject;
    }

    /**
     * 获取创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
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
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @OneToMany(mappedBy = "asset", cascade = {}, fetch = FetchType.LAZY)
    public List<AssetDetail> getDetails() {
        return details;
    }

    public void setDetails(List<AssetDetail> details) {
        this.details = details;
    }

    /**
     * 获取审核人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FAUDITOR")
    public User getAuditor() {
        return auditor;
    }

    public void setAuditor(User auditor) {
        this.auditor = auditor;
    }

    /**
     * 获取创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FAUDIT_TIME")
    public Date getAuditTime() {
        return auditTime;
    }


    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEP_ID")
    public Organization getDep() {
        return dep;
    }

    public void setDep(Organization dep) {
        this.dep = dep;
    }
}
