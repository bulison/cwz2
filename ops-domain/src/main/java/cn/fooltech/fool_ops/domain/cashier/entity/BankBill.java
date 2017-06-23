package cn.fooltech.fool_ops.domain.cashier.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 银行单据记录-实体类
 *
 * @author xjh
 */

@Entity
@Table(name = "tbd_bank_bill")
public class BankBill extends OpsOrgEntity {

    public final static int TYPE_COMPANY = 1;//期初企业未到账
    public final static int TYPE_BANK = 2;//期初银行未到账
    public final static int TYPE_CREDIT = 3;//银行日记账
    public final static int TYPE_STATEMENT = 4;//银行对账单
    public final static int TYPE_CASH = 5;//现金日记账
    public final static short CHECKED = 1;//已勾兑
    public final static short UN_CHECKED = 0;//未勾兑
    private static final long serialVersionUID = 1386628540640910107L;
    /**
     * 科目
     */
    private FiscalAccountingSubject subject;

    /**
     * 类型
     * 1--期初企业未到账
     * 2--期初银行未到账
     * 3--银行日记账
     * 4--银行对账单
     * 5--现金日记账
     */
    private Integer type;

    /**
     * 结算日期
     */
    private Date settlementDate;

    /**
     * 当天顺序号
     */
    private Integer orderno;

    /**
     * 结算方式
     */
    private AuxiliaryAttr settlementType;

    /**
     * 结算号
     */
    private String settlementNo;

    /**
     * 借方金额
     */
    private BigDecimal debit;

    /**
     * 贷方金额
     */
    private BigDecimal credit;

    /**
     * 经手人
     */
    private Member member;

    /**
     * 凭证日期
     */
    private Date voucherDate;

    /**
     * 摘要
     */
    private String resume;

    /**
     * 勾对标识：0--未勾对 1--已勾对
     */
    private Short checked = UN_CHECKED;

    /**
     * 勾对记录
     */
    private BankBill checkBill;

    /**
     * 勾对日期
     */
    private Date checkDate;

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

    /**
     * 金额方向
     */
    private Integer direction;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 来源标识
     */
    private String source;

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

    @Column(name = "FSOURCE")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Column(name = "FTYPE")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "FSTTLEMENT_DATE")
    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    @Column(name = "FORDERNO")
    public Integer getOrderno() {
        return orderno;
    }

    public void setOrderno(Integer orderno) {
        this.orderno = orderno;
    }

    @JoinColumn(name = "FSTTLEMENT_TYPE")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public AuxiliaryAttr getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(AuxiliaryAttr settlementType) {
        this.settlementType = settlementType;
    }

    @Column(name = "FSTTLEMENT_NO")
    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    @Column(name = "FDEBIT")
    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    @Column(name = "FCREDIT")
    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    @JoinColumn(name = "FMEMBER")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FVOUCHER_DATE")
    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    @Column(name = "FRESUME")
    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    @Column(name = "FCHECKED")
    public Short getChecked() {
        return checked;
    }

    public void setChecked(Short checked) {
        this.checked = checked;
    }

    @JoinColumn(name = "FCHECK_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public BankBill getCheckBill() {
        return checkBill;
    }

    public void setCheckBill(BankBill checkBill) {
        this.checkBill = checkBill;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCHECK_DATE")
    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    /**
     * 获取科目
     */
    @JoinColumn(name = "FFISCAL_SUBJECT_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public FiscalAccountingSubject getSubject() {
        return subject;
    }

    public void setSubject(FiscalAccountingSubject subject) {
        this.subject = subject;
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

    @Transient
    public Integer getDirection() {
        if (credit.compareTo(BigDecimal.ZERO) > 0) {
            direction = -1;
        } else {
            direction = 1;
        }
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }
}
