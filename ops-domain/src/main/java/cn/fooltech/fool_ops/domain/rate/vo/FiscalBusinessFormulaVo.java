package cn.fooltech.fool_ops.domain.rate.vo;

import java.io.Serializable;

public class FiscalBusinessFormulaVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5469987912989288792L;
	private String fid;
	//项目
	private String item;
	//行号
	private Integer number;
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
	public String getItem() {
		return item;
	}
	public Integer getNumber() {
		return number;
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
	public void setItem(String item) {
		this.item = item;
	}
	public void setNumber(Integer number) {
		this.number = number;
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
