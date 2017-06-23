package cn.fooltech.fool_ops.domain.capital.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

/**
 * 
 * @Description:资金计划关联单据 实体
 * @author cwz
 * @date 2017年2月28日 下午4:46:25
 */
@Entity
@Table(name = "tcapital_plan_bill")
public class CapitalPlanBill{

	private static final long serialVersionUID = 1L;

	// 主键
	private String id;

	// 明细表ID
	private CapitalPlanDetail detail;

	// 类型 1-单据金额，2-收付款金额
	private Integer bindType;

	// 关联类型，记录单据sign
	private Integer relationSign;

	// 关联ID 可关联仓库单据、收付款单、费用单
	private String relationId;

	// 单据金额
	private BigDecimal billAmount;

	// 绑定金额
	private BigDecimal bindAmount;

	// 创建时间
	private Date createTime;

	// 创建人
	private User creator;

	// 修改时间戳,初始值为当前时间
	private Date updateTime;

	// 组织ID，机构ID
	private Organization org;

	// 账套ID
	private FiscalAccount fiscalAccount;
    /**
     * 单号
     */
	private String code;
    /**
     * 收付款关联的客户或供应商
     */
    private CustomerSupplierView csv;
    
    @Column(name = "FCODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    @JoinColumn(name = "FCUSTOMER_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public CustomerSupplierView getCsv() {
		return csv;
	}

	public void setCsv(CustomerSupplierView csv) {
		this.csv = csv;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "FID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FDETAIL_ID")
	public CapitalPlanDetail getDetail() {
		return detail;
	}

	public void setDetail(CapitalPlanDetail detail) {
		this.detail = detail;
	}

	@Column(name = "FBIND_TYPE")
	public Integer getBindType() {
		return bindType;
	}

	public void setBindType(Integer bindType) {
		this.bindType = bindType;
	}

	@Column(name = "FRELATION_SIGN")
	public Integer getRelationSign() {
		return relationSign;
	}

	public void setRelationSign(Integer relationSign) {
		this.relationSign = relationSign;
	}

	@Column(name = "FRELATION_ID")
	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	@Column(name = "FBILL_AMOUNT")
	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	@Column(name = "FBIND_AMOUNT")
	public BigDecimal getBindAmount() {
		return bindAmount;
	}

	public void setBindAmount(BigDecimal bindAmount) {
		this.bindAmount = bindAmount;
	}

	@Column(name = "FCREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCREATOR_ID")
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@Column(name = "FUPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORG_ID")
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FACC_ID")
	public FiscalAccount getFiscalAccount() {
		return fiscalAccount;
	}

	public void setFiscalAccount(FiscalAccount fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}

}