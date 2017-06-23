package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;

/**
 * <p>表单传输VO--系统报表</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月12日
 */
public class SysReportVo implements Serializable {

    private static final long serialVersionUID = -7216321131508477589L;

    /**
     * 主键
     */
    private String fid;

    /**
     * 报表名称
     */
    private String reportName;

    /**
     * 汇总查询方式编号
     */
    private Integer code;

    /**
     * 列表标题
     */
    private String headers;

    /**
     * 统计配置信息(列的序号，多个用逗号分隔)
     */
    private String countInfo;

    /**
     * 上级报表ID
     */
    private String parentId;

    /**
     * 上级报表名称
     */
    private String parentName;

    /**
     * 合计标识
     * 0 不合计 1合计
     */
    //private Integer sumFlag;

    /**
     * 1/NULL为显示分页
     * 0为不显示
     */
    private Short showPage;

    /**
     * 脚本
     */
    private String javaScript;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getCountInfo() {
        return countInfo;
    }

    public void setCountInfo(String countInfo) {
        this.countInfo = countInfo;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Short getShowPage() {
        return showPage;
    }

    public void setShowPage(Short showPage) {
        this.showPage = showPage;
    }

    public String getJavaScript() {
        return javaScript;
    }

    public void setJavaScript(String javaScript) {
        this.javaScript = javaScript;
    }

	/*public Integer getSumFlag() {
        return sumFlag;
	}

	public void setSumFlag(Integer sumFlag) {
		this.sumFlag = sumFlag;
	}*/

}
