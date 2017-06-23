package cn.fooltech.fool_ops.domain.voucher.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>凭证、单据关联</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年11月30日
 */
@Entity
@Table(name = "TBD_VOUCHER_BILL")
public class VoucherBill extends OpsOrgEntity {

    private static final long serialVersionUID = -2818463422974929782L;

    /**
     * 凭证
     */
    private Voucher voucher;

    /**
     * 单据类型
     */
    private Integer billType;

    /**
     * 单据ID
     */
    private String billId;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 获取凭证
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FVOUCHERID", nullable = false)
    public Voucher getVoucher() {
        return voucher;
    }

    /**
     * 设置凭证
     *
     * @param voucher
     */
    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    /**
     * 获取单据类型
     *
     * @return
     */
    @Column(name = "FBILL_TYPE", nullable = false)
    public Integer getBillType() {
        return billType;
    }

    /**
     * 设置单据类型
     *
     * @param billType
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    /**
     * 获取单据ID
     *
     * @return
     */
    @Column(name = "FBILL_ID", nullable = false, length = 32)
    public String getBillId() {
        return billId;
    }

    /**
     * 设置单据ID
     *
     * @param billId
     */
    public void setBillId(String billId) {
        this.billId = billId;
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
    @Column(name = "FCREATE_TIME")
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

}
