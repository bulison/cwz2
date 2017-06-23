package cn.fooltech.fool_ops.domain.payment.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;

import javax.persistence.*;
import java.math.BigDecimal;


/**
 * <p>单据返利表</p>
 *
 * @author lgk
 * @version V1.0
 * @date 2016年4月6日上午09:14:38
 */
@Entity
@Table(name = "tsb_warehouse_return")
public class WarehouseReturn extends OpsOrgEntity {

    /**
     *
     */
    private static final long serialVersionUID = -7825900125502812874L;
    /**
     * 返利单
     */
    private PaymentBill paymentBill;
    /**
     * 单据主表
     */
    private WarehouseBill warehouseBill;
    /**
     * 关联单据明细表
     */
    private WarehouseBillDetail warehouseBillDetail;
    /**
     * 返利率%
     */
    private BigDecimal rates;
    /**
     * 返利金额
     */
    private BigDecimal amount;
    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 获取关联的收付款单
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPAYMENT_BILL_ID")
    public PaymentBill getPaymentBill() {
        return paymentBill;
    }

    public void setPaymentBill(PaymentBill paymentBill) {
        this.paymentBill = paymentBill;
    }

    /**
     * 获取关联的收付款单
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAREHOUSE_BILL_ID")
    public WarehouseBill getWarehouseBill() {
        return warehouseBill;
    }

    public void setWarehouseBill(WarehouseBill warehouseBill) {
        this.warehouseBill = warehouseBill;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAREHOUSE_DETAIL_ID")
    public WarehouseBillDetail getWarehouseBillDetail() {
        return warehouseBillDetail;
    }

    public void setWarehouseBillDetail(WarehouseBillDetail warehouseBillDetail) {
        this.warehouseBillDetail = warehouseBillDetail;
    }

    @Column(name = "FRATES")
    public BigDecimal getRates() {
        return rates;
    }

    public void setRates(BigDecimal rates) {
        this.rates = rates;
    }

    @Column(name = "FAMOUNT")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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


}
