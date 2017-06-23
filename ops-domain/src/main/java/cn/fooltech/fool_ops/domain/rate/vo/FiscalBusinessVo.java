package cn.fooltech.fool_ops.domain.rate.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class FiscalBusinessVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -411195537269187381L;
	private String fid;
	//会记期间Id
	private String periodId;
	//会记期间名
	private String periodName;
	//项目
	private String item;
	//行号
	private Integer number;
	//本期金额
	private BigDecimal value;
	//公式
	private String formula;
	//部门Id
	private String deptId;
	//部门姓名
	private String deptName;
	//更新时间
	private String updateTime;
	public String getFid() {
		return fid;
	}
	public String getPeriodId() {
		return periodId;
	}
	public String getPeriodName() {
		return periodName;
	}
	public String getItem() {
		return item;
	}
	public Integer getNumber() {
		return number;
	}
	public BigDecimal getValue() {
		return value;
	}
	public String getFormula() {
		return formula;
	}
	public String getDeptId() {
		return deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	
	
}
