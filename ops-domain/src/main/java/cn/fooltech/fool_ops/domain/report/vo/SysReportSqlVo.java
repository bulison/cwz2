package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象- 系统报表查询SQL</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年10月12日
 */
public class SysReportSqlVo implements Serializable {

    private static final long serialVersionUID = -3197953609642240151L;

    /**
     * ID
     */
    private String fid;

    /**
     * 系统报表ID
     */
    private String sysReportId;

    /**
     * 系统报表名称
     */
    private String sysReportName;

    /**
     * SQL语句
     */
    private String sql;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getSysReportId() {
        return sysReportId;
    }

    public void setSysReportId(String sysReportId) {
        this.sysReportId = sysReportId;
    }

    public String getSysReportName() {
        return sysReportName;
    }

    public void setSysReportName(String sysReportName) {
        this.sysReportName = sysReportName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
