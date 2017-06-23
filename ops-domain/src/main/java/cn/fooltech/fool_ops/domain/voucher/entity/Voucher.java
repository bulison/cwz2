package cn.fooltech.fool_ops.domain.voucher.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <p>凭证</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年11月23日
 */
@Entity
@Table(name = "TBD_VOUCHER")
public class Voucher extends OpsOrgEntity {

    /**
     * 状态- 未审核
     */
    public static final int STATUS_UNAUDITED = 0;
    /**
     * 状态- 已审核
     */
    public static final int STATUS_AUDITED = 1;
    /**
     * 状态- 已作废
     */
    public static final int STATUS_CANCELED = 2;
    /**
     * 状态- 已过账
     */
    public static final int STATUS_POSTED = 3;
    private static final long serialVersionUID = 2335969700786869123L;
    /**
     * 凭证字
     */
    private AuxiliaryAttr voucherWord;
    /**
     * 凭证号
     */
    private Integer voucherNumber = 0;
    /**
     * 凭证字号
     */
    private String voucherWordNumber;
    /**
     * 附件数
     */
    private Integer accessoryNumber = 0;
    /**
     * 顺序号
     */
    private Integer number = 0;
    /**
     * 凭证主管
     */
    private User supervisor;
    /**
     * 记账人
     */
    private User postPeople;
    /**
     * 凭证日期
     */
    private Date voucherDate;
    /**
     * 财务会计期间
     */
    private FiscalPeriod fiscalPeriod;
    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;
    /**
     * 审核时间
     */
    private Date auditDate;
    /**
     * 审核人
     */
    private User auditor;
    /**
     * 作废时间
     */
    private Date cancelDate;
    /**
     * 作废人
     */
    private User cancelor;
    /**
     * 创建人
     */
    private User creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 状态
     */
    private Integer recordStatus = STATUS_UNAUDITED;

    /**
     * 合计借方金额
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;
    /**
     * 部门
     */
    private Organization dept;
    /**
     * 明细
     */
    private List<VoucherDetail> details = new ArrayList<VoucherDetail>(0);

    /**
     * 获取凭证字
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FVOUCHER_WORD", nullable = false)
    public AuxiliaryAttr getVoucherWord() {
        return voucherWord;
    }

    /**
     * 设置凭证字
     *
     * @param voucherWord
     */
    public void setVoucherWord(AuxiliaryAttr voucherWord) {
        this.voucherWord = voucherWord;
    }

    /**
     * 获取凭证号
     *
     * @return
     */
    @Column(name = "FVOUCHER_NUMBER", nullable = false)
    public Integer getVoucherNumber() {
        return voucherNumber;
    }

    /**
     * 设置凭证号
     *
     * @param voucherNumber
     */
    public void setVoucherNumber(Integer voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    /**
     * 获取凭证字号
     *
     * @return
     */
    @Column(name = "FVOUCHER_WORD_NUMBER", length = 50)
    public String getVoucherWordNumber() {
        return voucherWordNumber;
    }

    /**
     * 设置凭证字号
     *
     * @param voucherWordNumber
     */
    public void setVoucherWordNumber(String voucherWordNumber) {
        this.voucherWordNumber = voucherWordNumber;
    }

    /**
     * 获取附件数
     *
     * @return
     */
    @Column(name = "FACCESSORY_NUMBER")
    public Integer getAccessoryNumber() {
        return accessoryNumber;
    }

    /**
     * 设置附件数
     *
     * @param accessoryNumber
     */
    public void setAccessoryNumber(Integer accessoryNumber) {
        this.accessoryNumber = accessoryNumber;
    }

    /**
     * 获取顺序号
     *
     * @return
     */
    @Column(name = "FNUMBER")
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置顺序号
     *
     * @param number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 获取凭证主管
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUPERVISOR")
    public User getSupervisor() {
        return supervisor;
    }

    /**
     * 设置凭证主管
     *
     * @param supervisor
     */
    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * 获取记账人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPOST_PEOPLE")
    public User getPostPeople() {
        return postPeople;
    }

    /**
     * 设置记账人
     *
     * @param postPeople
     */
    public void setPostPeople(User postPeople) {
        this.postPeople = postPeople;
    }

    /**
     * 获取凭证日期
     *
     * @return
     */
    @Column(name = "FVOUCHER_DATE")
    @Temporal(TemporalType.DATE)
    public Date getVoucherDate() {
        return voucherDate;
    }

    /**
     * 设置凭证日期
     *
     * @param voucherDate
     */
    public void setVoucherDate(Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    /**
     * 获取财务会计期间
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FVOUCHER_PERIOD_ID", nullable = false)
    public FiscalPeriod getFiscalPeriod() {
        return fiscalPeriod;
    }

    /**
     * 设置财务会计期间
     *
     * @param fiscalPeriod
     */
    public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
        this.fiscalPeriod = fiscalPeriod;
    }

    /**
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置财务账套
     *
     * @param fiscalAccount
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    /**
     * 获取审核日期
     *
     * @return
     */
    @Column(name = "FAUDIT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getAuditDate() {
        return auditDate;
    }

    /**
     * 设置审核日期
     *
     * @param auditDate
     */
    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    /**
     * 获取审核人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FAUDITOR")
    public User getAuditor() {
        return auditor;
    }

    /**
     * 设置审核人
     *
     * @param auditor
     */
    public void setAuditor(User auditor) {
        this.auditor = auditor;
    }

    /**
     * 获取作废时间
     *
     * @return
     */
    @Column(name = "FCANCEL_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCancelDate() {
        return cancelDate;
    }

    /**
     * 设置作废时间
     *
     * @param cancelDate
     */
    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    /**
     * 获取作废人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCANCELOR")
    public User getCancelor() {
        return cancelor;
    }

    /**
     * 设置作废人
     *
     * @param cancelor
     */
    public void setCancelor(User cancelor) {
        this.cancelor = cancelor;
    }

    /**
     * 获取创建人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", nullable = false)
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME", nullable = false)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取状态
     *
     * @return
     */
    @Column(name = "RECORD_STATUS", nullable = false)
    public Integer getRecordStatus() {
        return recordStatus;
    }

    /**
     * 设置状态
     *
     * @param recordStatus
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    /**
     * 获取明细
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "voucher")
    public List<VoucherDetail> getDetails() {
        return details;
    }

    /**
     * 设置明细
     *
     * @param details
     */
    public void setDetails(List<VoucherDetail> details) {
        this.details = details;
    }

    /**
     * 获取所属部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    /**
     * 设置部门
     *
     * @param dept
     */
    public void setDept(Organization dept) {
        this.dept = dept;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        Voucher voucher = (Voucher) clone;
        voucher.setFid(null);
        voucher.setDetails(null);
        return voucher;
    }

    @Column(name = "FTOTAL_AMOUNT")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    //枚举
    public enum RecordStatus {
        unaudited(0, "未审核"),
        audited(1, "已审核"),
        canceled(2, "已作废"),
        posted(3, "已过账");

        int code;
        String name;

        RecordStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getName(int code) {
            for (RecordStatus c : RecordStatus.values()) {
                if (c.getCode() == code) {
                    return c.name;
                }
            }
            return "";
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
