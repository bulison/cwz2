package cn.fooltech.fool_ops.domain.basedata.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 货品提成表
 * </p>
 * @author cwz
 * @date 2017年6月19日
 */
public class GoodsPercentageVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "开始日期，查询用")
	private Date startDay;

	@ApiModelProperty(value = "结束日期，查询用")
	private Date endDay;
	
    /**
     * 主键
     */
	private String fid;
	
	// 货品ID
	@ApiModelProperty(value = "货品ID")
	private String goodsId;
	private String goodsName;

	// 提成点数配【0-100之间】
	@ApiModelProperty(value = "提成点数配【0-100之间】")
	private BigDecimal percentage;

	// 是否最后录入
	@ApiModelProperty(value = "是否最后录入")
	private Integer isLast;

	// 修改时间戳【初始值为当前时间】
	@ApiModelProperty(value = "修改时间戳【初始值为当前时间】")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	// 账套ID
	@ApiModelProperty(value = "账套ID")
	private String fiscalAccountId;
	
	private String fiscalAccountName;
    /**
     * 所属企业
     */
	private String orgId;
	
	private String orgName;

	public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}

	public Date getEndDay() {
		return endDay;
	}

	public void setEndDay(Date endDay) {
		this.endDay = endDay;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
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

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public Integer getIsLast() {
		return isLast;
	}

	public void setIsLast(Integer isLast) {
		this.isLast = isLast;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	
}