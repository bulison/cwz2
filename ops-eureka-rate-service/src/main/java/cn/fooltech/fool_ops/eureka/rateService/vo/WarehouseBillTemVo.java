package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 仓库单据临时VO
 * @author hjr
 *2017-4-7	
 */
public class WarehouseBillTemVo {
	private BigDecimal freeAmount;//免单金额
	private BigDecimal totalPayAmount;//已收款金额
	private BigDecimal totalAmount;//总金额
	private String fid;// fid
	private BigDecimal ta;// 实际天数
	private Date billDate; //单据日期
	private BigDecimal tb;//预计天数
	private BigDecimal ty;//延迟天数
	private List<CapitalPlanDetailTemVo> planDetailList;//外键资金计划明细表
	private int tn;//延迟次数
	private String code;//单号
	private BigDecimal costAmount; //货品成本
	private BigDecimal cost;//费用
	private BigDecimal profit;//利润
	private BigDecimal noPayAmount;//未收款金额
	public BigDecimal getFreeAmount() {
		return freeAmount;
	}
	public BigDecimal getTotalPayAmount() {
		return totalPayAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public String getFid() {
		return fid;
	}
	public BigDecimal getTa() {
		return ta;
	}
	public Date getBillDate() {
		return billDate;
	}
	public BigDecimal getTb() {
		return tb;
	}
	public BigDecimal getTy() {
		return ty;
	}
	public List<CapitalPlanDetailTemVo> getPlanDetailList() {
		return planDetailList;
	}
	public int getTn() {
		return tn;
	}
	public void setFreeAmount(BigDecimal freeAmount) {
		this.freeAmount = freeAmount;
	}
	public void setTotalPayAmount(BigDecimal totalPayAmount) {
		this.totalPayAmount = totalPayAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public void setTa(BigDecimal ta) {
		this.ta = ta;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public void setTb(BigDecimal tb) {
		this.tb = tb;
	}
	public void setTy(BigDecimal ty) {
		this.ty = ty;
	}
	public void setPlanDetailList(List<CapitalPlanDetailTemVo> planDetailList) {
		this.planDetailList = planDetailList;
	}
	public void setTn(int tn) {
		this.tn = tn;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public BigDecimal getProfit() {
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	public BigDecimal getNoPayAmount() {
		return noPayAmount;
	}
	public void setNoPayAmount(BigDecimal noPayAmount) {
		this.noPayAmount = noPayAmount;
	}
	
	
}
