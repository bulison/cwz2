package cn.fooltech.fool_ops.domain.capital.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.Length;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import io.swagger.annotations.ApiModelProperty;

public class CapitalPlanChangeLogVo {
	/**
	 * ID 主键
	 */
	
	@ApiModelProperty(value = "主键")
	private String id;
	/**
	 * 明细表ID
	 */
	
	@ApiModelProperty(value = "明细表ID")
	@Length(max = 32, message = "ID长度超过{max}个字符")
	private String detailId;
	/**
	 * 变更类型 1-修改日期，2-修改金额
	 */
	@ApiModelProperty(value = " 变更类型 1-修改日期，2-修改金额")
	private Integer changeType;
	/**
	 * 上次预计收付日期
	 */
	 
	@ApiModelProperty(value = "上次预计收付日期")
	private String prePaymentDate;
	/**
	 * 本次预计收付款日期
	 */
	
	@ApiModelProperty(value = "本次预计收付款日期")
	private String paymentDate;
	/**
	 * 上次预计收付金额
	 */
	
	@ApiModelProperty(value = "上次预计收付金额")
	private BigDecimal prePaymentAmount;
	/**
	 * 本次预计收付金额
	 */

	@ApiModelProperty(value = "本次预计收付金额")
	private BigDecimal paymentAmount;
	/**
	 * 	备注
	 */ 

	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	@Length(max = 32, message = "ID长度超过{max}个字符")
	private String createId;
	/**
	 * 修改时间戳 初始值为当前时间
	 */
	@ApiModelProperty(value = "修改时间戳 初始值为当前时间")
	private Timestamp updateTime;
	/**
	 * 组织ID 机构ID
	 */
	@ApiModelProperty(value = "组织ID 机构ID")
	private String orgId;
	/**
	 * 账套ID
	 */
	@ApiModelProperty(value = "账套ID")
	private String accId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDetailId() {
		return detailId;
	}
	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	public Integer getChangeType() {
		return changeType;
	}
	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
	public String getPrePaymentDate() {
		return prePaymentDate;
	}
	public void setPrePaymentDate(String prePaymentDate) {
		this.prePaymentDate = prePaymentDate;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public BigDecimal getPrePaymentAmount() {
		return prePaymentAmount;
	}
	public void setPrePaymentAmount(BigDecimal prePaymentAmount) {
		this.prePaymentAmount = prePaymentAmount;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	
}
