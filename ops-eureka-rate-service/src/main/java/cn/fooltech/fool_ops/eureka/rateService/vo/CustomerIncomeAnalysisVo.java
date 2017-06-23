package cn.fooltech.fool_ops.eureka.rateService.vo;

import java.math.BigDecimal;

/**
 * 客户收益分析VO
 * @author hjr
 *2017-4-7
 */
public class CustomerIncomeAnalysisVo {
	private String customerId; 		//客户ID
	private String code;			//客户编号
	private String name;			//客户名称
	private String area;			//地区
	private String areaName;		//地区名
	private String category;		//客户类别
	private String categoryName;		//客户类别名
	private BigDecimal salesAmount;	//销售金额
	private BigDecimal goodsCost;	//货品成本
	private BigDecimal cost;		//费用
	private BigDecimal profit;		//利润
	private BigDecimal currentDebt;	//当前欠收
	private BigDecimal credit;		//信用度
	private BigDecimal percentageAmount; //提成 
	public String getCustomerId() {
		return customerId;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getArea() {
		return area;
	}
	public String getCategory() {
		return category;
	}
	public BigDecimal getSalesAmount() {
		return salesAmount;
	}
	public BigDecimal getGoodsCost() {
		return goodsCost;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public BigDecimal getProfit() {
		return profit;
	}
	public BigDecimal getCurrentDebt() {
		return currentDebt;
	}
	public BigDecimal getCredit() {
		return credit;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setSalesAmount(BigDecimal salesAmount) {
		this.salesAmount = salesAmount;
	}
	public void setGoodsCost(BigDecimal goodsCost) {
		this.goodsCost = goodsCost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	public void setCurrentDebt(BigDecimal currentDebt) {
		this.currentDebt = currentDebt;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public BigDecimal getPercentageAmount() {
		return percentageAmount;
	}
	public void setPercentageAmount(BigDecimal percentageAmount) {
		this.percentageAmount = percentageAmount;
	}
	
}
