package cn.fooltech.fool_ops.domain.rate.vo;

import java.io.Serializable;

/**
 * 
 * <p>流程收益率分析【报表】</p>  
 * @author cwz
 * @date 2017年4月17日
 */
public class PlanYieldRateVo implements Serializable {

    private static final long serialVersionUID = -7526364555164785212L;

    /**
     * 流程计划ID
     */
    private String fid;
    /**
     * 流程编号
     */
    private String planCode;
    /**
     * 流程名称
     */
    private String planName;
    /**
     * 支出金额
     */
    private String outAmount;
    /**
     * 收入金额
     */
    private String inAmount;
    /**
     * 利润
     */
    private String profit;
    /**
     * 预计收益率
     */
    private String estimatedYieldrate;
    /**
     * 实际收益率
     */
    private String effectiveYieldrate;
    /**
     * 参考收益率
     */
    private String referenceYieldrate;
    /**
     * 当前预计收益率
     */
    private String currentYieldRate;
    /**
     * 计划开始日期
     */
    private String plantStartDate;	
    /**
     * 计划结束日期
     */
    private String fantipateEndTime;
    /**
     * 实际完成日期
     */
    private String factualEndTime;	
    /**
     * 延期天数
     */
    private String fextensionDays;
    /**
     * 延期次数
     */
    private String fextensionCount;
    /**
     * 承办效率
     */
    private String contractorsEfficiency;
    /**
     * 分页标识，默认分页<br>
     * 0 不分页  1 分页
     */
    private int flag = 1;
    /**
     * 页面搜索关键字- 开始日期
     */
    private String startDay;

    /**
     * 页面搜索关键字- 结束日期
     */
    private String endDay;
    /**
     * 发起人
     */
    private String initiate;
    /**
     * 承办人
     */
    private String principal;
    /**
     * 流程状态
     */
    private String status;

    /**
     * 排序：流程编号(1)、流程名称(2)、支出金额(3)、收入金额(4)、利润(5)、预计收益率(6)、实际收益率(7)、当前预计收益率(8)	
     */
    private Integer sidx;
    /**
     * 排序方向：0-升序，1-降序
     */
    private Integer sord;
    
    
    public Integer getSidx() {
		return sidx;
	}
	public void setSidx(Integer sidx) {
		this.sidx = sidx;
	}
	public Integer getSord() {
		return sord;
	}
	public void setSord(Integer sord) {
		this.sord = sord;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getInitiate() {
		return initiate;
	}
	public void setInitiate(String initiate) {
		this.initiate = initiate;
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	/**
	 * 流程编号
	 * @return
	 */
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	/**
	 * 流程名称
	 * @return
	 */
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	/**
	 * 支出金额	
	 * @return
	 */
	public String getOutAmount() {
		return outAmount;
	}
	/**
	 * 支出金额	
	 * @param outAmount
	 */
	public void setOutAmount(String outAmount) {
		this.outAmount = outAmount;
	}
	/**
	 * 收入金额
	 * @return
	 */
	public String getInAmount() {
		return inAmount;
	}
	/**
	 * 收入金额
	 * @param inAmount
	 */
	public void setInAmount(String inAmount) {
		this.inAmount = inAmount;
	}
	/**
	 * 利润
	 * @return
	 */
	public String getProfit() {
		return profit;
	}
	/**
	 * 利润
	 * @param profit
	 */
	public void setProfit(String profit) {
		this.profit = profit;
	}
	/**
	 * 预计收益率
	 * @return
	 */
	public String getEstimatedYieldrate() {
		return estimatedYieldrate;
	}
	/**
	 * 预计收益率
	 * @param estimatedYieldrate
	 */
	public void setEstimatedYieldrate(String estimatedYieldrate) {
		this.estimatedYieldrate = estimatedYieldrate;
	}
	/**
	 * 实际收益率
	 * @return
	 */
	public String getEffectiveYieldrate() {
		return effectiveYieldrate;
	}
	/**
	 * 实际收益率
	 * @param effectiveYieldrate
	 */
	public void setEffectiveYieldrate(String effectiveYieldrate) {
		this.effectiveYieldrate = effectiveYieldrate;
	}
	/**
	 * 参考收益率
	 * @return
	 */
	public String getReferenceYieldrate() {
		return referenceYieldrate;
	}
	/**
	 * 参考收益率
	 * @param referenceYieldrate
	 */
	public void setReferenceYieldrate(String referenceYieldrate) {
		this.referenceYieldrate = referenceYieldrate;
	}
	/**
	 * 当前预计收益率
	 * @return
	 */
	public String getCurrentYieldRate() {
		return currentYieldRate;
	}
	/**
	 * 当前预计收益率
	 * @param currentYieldRate
	 */
	public void setCurrentYieldRate(String currentYieldRate) {
		this.currentYieldRate = currentYieldRate;
	}
	/**
	 * 计划开始日期		
	 * @return
	 */
	public String getPlantStartDate() {
		return plantStartDate;
	}
	/**
	 * 计划开始日期		
	 * @param plantStartDate
	 */
	public void setPlantStartDate(String plantStartDate) {
		this.plantStartDate = plantStartDate;
	}
	/**
	 * 计划结束日期
	 * @return
	 */
	public String getFantipateEndTime() {
		return fantipateEndTime;
	}
	/**
	 * 计划结束日期
	 * @param fantipateEndTime
	 */
	public void setFantipateEndTime(String fantipateEndTime) {
		this.fantipateEndTime = fantipateEndTime;
	}
	/**
	 * 实际完成日期	
	 * @return
	 */
	public String getFactualEndTime() {
		return factualEndTime;
	}
	/**
	 * 实际完成日期	
	 * @param factualEndTime
	 */
	public void setFactualEndTime(String factualEndTime) {
		this.factualEndTime = factualEndTime;
	}
	/**
	 * 延期天数
	 * @return
	 */
	public String getFextensionDays() {
		return fextensionDays;
	}
	/**
	 * 延期天数
	 * @param fextensionDays
	 */
	public void setFextensionDays(String fextensionDays) {
		this.fextensionDays = fextensionDays;
	}
	/**
	 * 延期次数
	 * @return
	 */
	public String getFextensionCount() {
		return fextensionCount;
	}
	/**
	 * 延期次数
	 * @param fextensionCount
	 */
	public void setFextensionCount(String fextensionCount) {
		this.fextensionCount = fextensionCount;
	}
	/**
	 * 承办效率
	 * @return
	 */
	public String getContractorsEfficiency() {
		return contractorsEfficiency;
	}
	/**
	 * 承办效率
	 * @param contractorsEfficiency
	 */
	public void setContractorsEfficiency(String contractorsEfficiency) {
		this.contractorsEfficiency = contractorsEfficiency;
	}
    
    

}
