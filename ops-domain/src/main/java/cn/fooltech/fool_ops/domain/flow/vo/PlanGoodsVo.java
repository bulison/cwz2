package cn.fooltech.fool_ops.domain.flow.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 计划货品VO
 * 
 * @author cwz
 *
 */
public class PlanGoodsVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 主键
	@ApiModelProperty(value = "主键")
	private String id;

	// 计划ID
	@ApiModelProperty(value = "计划ID")
	private String planId;
	private String planName;

	// 货品ID
	@ApiModelProperty(value = "货品ID")
	private String goodsId;
	private String goodsName;

	// 货品属性ID
	@ApiModelProperty(value = "货品属性ID")
	private String goodsSpecId;
	private String goodsSpecName;

	// 货品数量
	@ApiModelProperty(value = "货品数量")
	private BigDecimal goodsQuentity;

	// 销售金额
	@ApiModelProperty(value = "销售金额")
	private BigDecimal saleAmount;

	// 销售单价
	@ApiModelProperty(value = "销售单价")
	private BigDecimal salePrice;

	// 是否采购 0-仓库 1-采购
	@ApiModelProperty(value = "是否采购 0-仓库 1-采购")
	private Integer purchase;

	// 创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	// 创建人
	@ApiModelProperty(value = "创建人")
	private String creatorId;
	private String creatorName;

	// 修改时间戳(初始值为当前时间)
	@ApiModelProperty(value = "修改时间戳(初始值为当前时间)")
	private Date updateTime;

	// 组织ID(机构ID)
	@ApiModelProperty(value = "组织ID(机构ID)")
	private String orgId;
	private String orgName;

	// 账套ID
	@ApiModelProperty(value = "账套ID")
	private String fiscalAccountId;
	private String fiscalAccountName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")
	@ApiModelProperty(value = "开始日期，查询用")
	private String startDay;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")
	@ApiModelProperty(value = "结束日期，查询用")
	private String endDay;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")
	@ApiModelProperty(value = "运输日期")
	private Date transportDate;

	@ApiModelProperty(value = "成本分析主表ID")
	private String costAnalyzeBillId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsSpecId() {
		return goodsSpecId;
	}

	public void setGoodsSpecId(String goodsSpecId) {
		this.goodsSpecId = goodsSpecId;
	}

	public String getGoodsSpecName() {
		return goodsSpecName;
	}

	public void setGoodsSpecName(String goodsSpecName) {
		this.goodsSpecName = goodsSpecName;
	}

	public BigDecimal getGoodsQuentity() {
		return goodsQuentity;
	}

	public void setGoodsQuentity(BigDecimal goodsQuentity) {
		this.goodsQuentity = goodsQuentity;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getPurchase() {
		return purchase;
	}

	public void setPurchase(Integer purchase) {
		this.purchase = purchase;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getFiscalAccountId() {
		return fiscalAccountId;
	}

	public void setFiscalAccountId(String fiscalAccountId) {
		this.fiscalAccountId = fiscalAccountId;
	}

	public String getFiscalAccountName() {
		return fiscalAccountName;
	}

	public void setFiscalAccountName(String fiscalAccountName) {
		this.fiscalAccountName = fiscalAccountName;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public Date getTransportDate() {
		return transportDate;
	}

	public void setTransportDate(Date transportDate) {
		this.transportDate = transportDate;
	}

	public String getCostAnalyzeBillId() {
		return costAnalyzeBillId;
	}

	public void setCostAnalyzeBillId(String costAnalyzeBillId) {
		this.costAnalyzeBillId = costAnalyzeBillId;
	}
}