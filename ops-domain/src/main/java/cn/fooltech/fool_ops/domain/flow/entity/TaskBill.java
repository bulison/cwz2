package cn.fooltech.fool_ops.domain.flow.entity;

import java.io.Serializable;
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

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;

/**
 * 计划事件关联单据
 * 
 * @author cwz
 * @date 2017-2-8
 */
@Entity
@Table(name = "tflow_task_bill")
public class TaskBill implements Serializable{

	private static final long serialVersionUID = 1L;

	// 主键
	private String id;

	// 计划ID
	private Plan plan;

	// 事件ID
	private Task task;
	// 单据类型
	private Integer billSign;

	// 单据ID
	private String bill;

	// 创建时间
	private Date createTime;

	// 创建人
	private User creator;

	// 修改时间戳(初始值为当前时间)
	private Date updateTime;

	// 组织ID(机构ID)
	private Organization org;

	// 账套ID
	private FiscalAccount fiscalAccount;

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
	@JoinColumn(name = "FPLAN_ID")
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FTASK_ID")
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	@Column(name = "FBILL_SIGN")
	public Integer getBillSign() {
		return billSign;
	}

	public void setBillSign(Integer billSign) {
		this.billSign = billSign;
	}

	@Column(name = "FBILL_ID")
	public String getBill() {
		return bill;
	}

	public void setBill(String bill) {
		this.bill = bill;
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