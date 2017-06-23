package cn.fooltech.fool_ops.domain.capital.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * 
 * @Description: 资金计划关联单据VO
 * @author cwz
 * @date 2017年2月28日 下午5:05:59
 */
public class CapitalPlanBillVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 主键
	private String id;

	// 明细表ID
	private String detailId;
	private String detailName;

	// 类型 1-单据金额，2-收付款金额
	private Integer bindType;

	// 关联类型，记录单据sign
	private Integer relationSign;

	// 关联ID 可关联仓库单据、收付款单、费用单
	private String relationId;
	
	//单据日期
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date billDate;

	// 单据金额
	private BigDecimal billAmount;

	// 绑定金额
	private BigDecimal bindAmount;

	// 创建时间
	private Date createTime;

	// 创建人
	private String creatorId;
	private String creatorName;

	// 修改时间戳,初始值为当前时间
	private Date updateTime;

	// 组织ID，机构ID
	private String orgId;
	private String orgName;

	// 账套ID
	private String fiscalAccountId;
	private String fiscalAccountName;
	private Date startDay;
    /**
     * 单号
     */
	private String code;
    /**
     * 收付款关联的客户或供应商
     */
    private String csvId;

    /**
     * 收付款关联的客户或供应商名称
     */
    private String csvName;
	/**
	 * 填写金额
	 */
	private BigDecimal inputAmount;
	/**勾对金额*/
	private BigDecimal checkAmount;
	
	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCsvId() {
		return csvId;
	}

	public void setCsvId(String csvId) {
		this.csvId = csvId;
	}

	public String getCsvName() {
		return csvName;
	}

	public void setCsvName(String csvName) {
		this.csvName = csvName;
	}

	public BigDecimal getCheckAmount() {
		return checkAmount;
	}

	public void setCheckAmount(BigDecimal checkAmount) {
		this.checkAmount = checkAmount;
	}

	private Date endDay;

	public BigDecimal getInputAmount() {
		return inputAmount;
	}

	public void setInputAmount(BigDecimal inputAmount) {
		this.inputAmount = inputAmount;
	}

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

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public Integer getBindType() {
		return bindType;
	}

	public void setBindType(Integer bindType) {
		this.bindType = bindType;
	}

	public Integer getRelationSign() {
		return relationSign;
	}

	public void setRelationSign(Integer relationSign) {
		this.relationSign = relationSign;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public BigDecimal getBindAmount() {
		return bindAmount;
	}

	public void setBindAmount(BigDecimal bindAmount) {
		this.bindAmount = bindAmount;
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
	
	

}