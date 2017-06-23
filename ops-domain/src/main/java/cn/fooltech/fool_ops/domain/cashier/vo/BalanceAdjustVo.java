package cn.fooltech.fool_ops.domain.cashier.vo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>余额调节表单传输对象</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年1月6日
 */
public class BalanceAdjustVo implements Serializable {

    private static final long serialVersionUID = -3419976753587715420L;

    /**
     * 银行日记账余额
     */
    private String journalAmount;

    /**
     * 银行已收企业未收金额
     */
    private String enterpriseUnReceiveAmount;

    /**
     * 银行已付企业未付金额
     */
    private String enterpriseUnPayAmount;

    /**
     * 银行对账单余额
     */
    private String statementAmount;

    /**
     * 企业已收银行未收金额
     */
    private String bankUnReceiveAmount;

    /**
     * 企业已付银行未付金额
     */
    private String bankUnPayAmount;

    /**
     * 单据信息
     */
    private List<BankBillSimpleVo> infos;

    public String getJournalAmount() {
        return journalAmount;
    }

    public void setJournalAmount(String journalAmount) {
        this.journalAmount = journalAmount;
    }

    public String getEnterpriseUnReceiveAmount() {
        return enterpriseUnReceiveAmount;
    }

    public void setEnterpriseUnReceiveAmount(String enterpriseUnReceiveAmount) {
        this.enterpriseUnReceiveAmount = enterpriseUnReceiveAmount;
    }

    public String getEnterpriseUnPayAmount() {
        return enterpriseUnPayAmount;
    }

    public void setEnterpriseUnPayAmount(String enterpriseUnPayAmount) {
        this.enterpriseUnPayAmount = enterpriseUnPayAmount;
    }

    public String getStatementAmount() {
        return statementAmount;
    }

    public void setStatementAmount(String statementAmount) {
        this.statementAmount = statementAmount;
    }

    public String getBankUnReceiveAmount() {
        return bankUnReceiveAmount;
    }

    public void setBankUnReceiveAmount(String bankUnReceiveAmount) {
        this.bankUnReceiveAmount = bankUnReceiveAmount;
    }

    public String getBankUnPayAmount() {
        return bankUnPayAmount;
    }

    public void setBankUnPayAmount(String bankUnPayAmount) {
        this.bankUnPayAmount = bankUnPayAmount;
    }

    public List<BankBillSimpleVo> getInfos() {
        return infos;
    }

    public void setInfos(List<BankBillSimpleVo> infos) {
        this.infos = infos;
    }

}
