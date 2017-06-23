package cn.fooltech.fool_ops.domain.capital.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;


/** 
 * 绑定单据VO
* @author cwz 
* @version 创建时间：2017年3月7日 下午2:14:48 
* 
*/
public class BindingBillVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*计划事件关联单据主键*/
	private String id;
	/*单据日期*/
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date billDate;
	/*单据编号*/
	private String code;
	/*单据类型*/
	private Integer billType;
	/*来往单位：客户id，供应商id*/
	private String unitId;
	/*来往单位：客户名称，供应商名称*/
	private String unitName;
	/*单据金额*/
	private BigDecimal amount;
	/*勾对金额*/
	private BigDecimal checkAmount;
	/**计划id**/
	private String planId;
	
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getCheckAmount() {
		return checkAmount;
	}
	public void setCheckAmount(BigDecimal checkAmount) {
		this.checkAmount = checkAmount;
	}	
	
	public static BindingBillVo toObject(Map<String, Object> map) {
		BindingBillVo billVo = new BindingBillVo();
		billVo.setAmount(map.get("amount")==null?null:(BigDecimal) map.get("amount"));
		billVo.setBillDate(map.get("billDate")==null?null:(Date)map.get("billDate"));
		billVo.setBillType(map.get("billType")==null?null:(Integer)map.get("billType"));
		billVo.setCheckAmount(map.get("checkAmount")==null?null:(BigDecimal) map.get("checkAmount"));
		billVo.setCode(map.get("code")==null?null:(String)map.get("code"));
		billVo.setUnitId(map.get("unitId")==null?null:(String)map.get("unitId"));
		billVo.setUnitName(map.get("unitName")==null?null:(String)map.get("unitName"));
		billVo.setId(map.get("fid")==null?null:(String)map.get("fid"));
		return billVo;
	}
	
	public static List<BindingBillVo> toObject(List<Map<String, Object>> lists){
		List<BindingBillVo> billVos = Lists.newArrayList();
		for (Map<String, Object> map : lists) {
			BindingBillVo billVo = BindingBillVo.toObject(map);
			if (billVo!=null) {
				billVos.add(billVo);
			}
		}
		return billVos;
	}
	
	
	
}
