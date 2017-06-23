package cn.fooltech.fool_ops.domain.rate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
/**
 * 收益率公式抽象类
 * @author 
 *
 */
@MappedSuperclass
public class BusinessFormula extends OpsOrgEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8905597293215621209L;
	/**
	 * 项目
	 */
	private String item;
	/**
	 * 行号
	 */
	private Integer number;
	/**
	 * 公式
	 */
	private String formula;
	/**
	 * 部门
	 */
	private Organization dept;
	/**
	 * 账套ID
	 */
	private FiscalAccount fiscalAccount;
	/**
	 * 修改时间戳
	 */
	private Date updateTime;
	@Column(name="FITEM")
	public String getItem() {
		return item;
	}
	@Column(name="FNUMBER")
	public Integer getNumber() {
		return number;
	}
	@Column(name="FFORMULA")
	public String getFormula() {
		return formula;
	}
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPTID", nullable = false)
    public Organization getDept() {
		return dept;
	}
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
	public FiscalAccount getFiscalAccount() {
		return fiscalAccount;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public void setFiscalAccount(FiscalAccount fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public void setDept(Organization dept) {
		this.dept = dept;
	}
}
