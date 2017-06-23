package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象- 多栏明细账报表</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年1月26日
 */
public class MultiColumnReportVo implements Serializable {

    private static final long serialVersionUID = 9071746621820226949L;

    /**
     * 开始财务会计期间ID
     */
    private String startFiscalPeriodId;

    /**
     * 结束财务会计期间ID
     */
    private String endFiscalPeriodId;

    /**
     * 栏目的ID
     */
    private String settingId;

    /**
     * 状态(多个逗号隔开)
     */
    private String status;

    /**
     * 分页标识，默认分页<br>
     * 0 不分页  1 分页
     */
    private int flag = 1;

    public String getStartFiscalPeriodId() {
        return startFiscalPeriodId;
    }

    public void setStartFiscalPeriodId(String startFiscalPeriodId) {
        this.startFiscalPeriodId = startFiscalPeriodId;
    }

    public String getEndFiscalPeriodId() {
        return endFiscalPeriodId;
    }

    public void setEndFiscalPeriodId(String endFiscalPeriodId) {
        this.endFiscalPeriodId = endFiscalPeriodId;
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
