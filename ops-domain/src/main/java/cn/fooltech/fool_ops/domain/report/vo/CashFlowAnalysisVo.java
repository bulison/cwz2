package cn.fooltech.fool_ops.domain.report.vo;

/** 
* @author cwz 
* @version 创建时间：2017年3月21日 上午9:16:02 
* 
*/
public class CashFlowAnalysisVo {
	/** 
	*
	* @author cwz 
	* @version 创建时间：2017年3月21日 上午9:16:02 
	* 
	*/
	//预计收付日期
	private String paymentDate;
	//预警颜色
	private String colour;
	//预计收入
	private String income;
	//预计支出
	private String expenditure;
	//结余金额
	private String amount;
    /**
     * 页面搜索关键字- 开始日期
     */
    private String startDay;

    /**
     * 页面搜索关键字- 结束日期
     */
    private String endDay;
    
    /**
     * 资金池预警额度
     */
    private String warningQuota;
    /**
     * 分页标识，默认分页<br>
     * 0 不分页  1 分页
     */
    private int flag = 1;
    
    public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	/**
     * 资金池预警额度
     * @return
     */
	public String getWarningQuota() {
		return warningQuota;
	}
	public void setWarningQuota(String warningQuota) {
		this.warningQuota = warningQuota;
	}
	/**
	 * 预计收付日期
	 * @return
	 */
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	/**
	 * 预警颜色
	 * @return
	 */
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	/**
	 * 预计收入
	 * @return
	 */
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	/**
	 * 预计支出
	 * @return
	 */
	public String getExpenditure() {
		return expenditure;
	}
	public void setExpenditure(String expenditure) {
		this.expenditure = expenditure;
	}
	/**
	 * 结余金额
	 * @return
	 */
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * 页面搜索关键字- 开始日期
	 * @return
	 */
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	/**
	 * 页面搜索关键字- 结束日期
	 * @return
	 */
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	
	

}
