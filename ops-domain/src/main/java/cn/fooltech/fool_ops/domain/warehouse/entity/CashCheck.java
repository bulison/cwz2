package cn.fooltech.fool_ops.domain.warehouse.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>现金盘点单</p>
 *
 * @author cwz
 * @version 1.0
 * @date 2016-9-18
 */
@Entity
@Table(name = "TBD_CASH_CHECK")
public class CashCheck extends OpsOrgEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 科目
     */
    private FiscalAccountingSubject subject;
    /**
     * 盘点日期
     */
    private Date date;
    /**
     * 盘点金额
     */
    private BigDecimal amount = BigDecimal.ZERO;
    /**
     * 100元(记录张数)
     */
    private Integer f100;
    /**
     * 50元
     */
    private Integer f050;
    /**
     * 20元
     */
    private Integer f020;
    /**
     * 10元
     */
    private Integer f010;
    /**
     * 5元
     */
    private Integer f005;
    /**
     * 2元
     */
    private Integer f002;
    /**
     * 1元
     */
    private Integer f001;
    /**
     * 5角
     */
    private Integer f_50;
    /**
     * 2角
     */
    private Integer f_20;
    /**
     * 1角
     */
    private Integer f_10;
    /**
     * 5分
     */
    private Integer f_05;
    /**
     * 2分
     */
    private Integer f_02;
    /**
     * 1分
     */
    private Integer f_01;
    /**
     * 备注
     */
    private String resume;
    /**
     * 创建人
     */
    private User creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间戳(初始值为当前时间)
     */
    private Date updateTime;
    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 获取科目
     */
    @JoinColumn(name = "FFISCAL_SUBJECTID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public FiscalAccountingSubject getSubject() {
        return subject;
    }

    /**
     * 设置科目
     *
     * @param subject
     */
    public void setSubject(FiscalAccountingSubject subject) {
        this.subject = subject;
    }

    /**
     * 获取盘点日期
     *
     * @return
     */
    @Column(name = "FDATE")
    public Date getDate() {
        return date;
    }

    /**
     * 设置盘点日期
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 获取盘点金额
     *
     * @return
     */
    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置盘点金额
     *
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 100元(记录张数)
     *
     * @return
     */
    @Column(name = "F100")
    public Integer getF100() {
        return f100;
    }

    public void setF100(Integer f100) {
        this.f100 = f100;
    }

    /**
     * 50元(记录张数)
     *
     * @return
     */
    @Column(name = "F050")
    public Integer getF050() {
        return f050;
    }

    public void setF050(Integer f050) {
        this.f050 = f050;
    }

    /**
     * 20元(记录张数)
     *
     * @return
     */
    @Column(name = "F020")
    public Integer getF020() {
        return f020;
    }

    public void setF020(Integer f020) {
        this.f020 = f020;
    }

    /**
     * 10元(记录张数)
     *
     * @return
     */
    @Column(name = "F010")
    public Integer getF010() {
        return f010;
    }

    public void setF010(Integer f010) {
        this.f010 = f010;
    }

    /**
     * 5元(记录张数)
     *
     * @return
     */
    @Column(name = "F005")
    public Integer getF005() {
        return f005;
    }

    public void setF005(Integer f005) {
        this.f005 = f005;
    }

    /**
     * 2元(记录张数)
     *
     * @return
     */
    @Column(name = "F002")
    public Integer getF002() {
        return f002;
    }

    public void setF002(Integer f002) {
        this.f002 = f002;
    }

    /**
     * 1元(记录张数)
     *
     * @return
     */
    @Column(name = "F001")
    public Integer getF001() {
        return f001;
    }

    public void setF001(Integer f001) {
        this.f001 = f001;
    }

    /**
     * 5角(记录张数)
     *
     * @return
     */
    @Column(name = "F_50")
    public Integer getF_50() {
        return f_50;
    }

    public void setF_50(Integer f_50) {
        this.f_50 = f_50;
    }

    /**
     * 2角(记录张数)
     *
     * @return
     */
    @Column(name = "F_20")
    public Integer getF_20() {
        return f_20;
    }

    public void setF_20(Integer f_20) {
        this.f_20 = f_20;
    }

    /**
     * 1角(记录张数)
     *
     * @return
     */
    @Column(name = "F_10")
    public Integer getF_10() {
        return f_10;
    }

    public void setF_10(Integer f_10) {
        this.f_10 = f_10;
    }

    /**
     * 5分(记录张数)
     *
     * @return
     */
    @Column(name = "F_05")
    public Integer getF_05() {
        return f_05;
    }

    public void setF_05(Integer f_05) {
        this.f_05 = f_05;
    }

    /**
     * 2分(记录张数)
     *
     * @return
     */
    @Column(name = "F_02")
    public Integer getF_02() {
        return f_02;
    }

    public void setF_02(Integer f_02) {
        this.f_02 = f_02;
    }

    /**
     * 1分(记录张数)
     *
     * @return
     */
    @Column(name = "F_01")
    public Integer getF_01() {
        return f_01;
    }

    public void setF_01(Integer f_01) {
        this.f_01 = f_01;
    }

    /**
     * 获取备注
     *
     * @return
     */
    @Column(name = "FRESUME")
    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
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

    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return
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
     * 获取修改时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
}
