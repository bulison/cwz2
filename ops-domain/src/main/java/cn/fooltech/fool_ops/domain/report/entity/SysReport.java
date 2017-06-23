package cn.fooltech.fool_ops.domain.report.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>数据分析报表</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月12日
 */
@Entity
@Table(name = "rep_sys_report")
public class SysReport extends OpsEntity {
    private static final long serialVersionUID = -2621556228667467678L;

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

    /**
     * 上级报表
     */
    private SysReport parent;

    /**
     * 下级报表
     */
    private Set<SysReport> childs = new HashSet<SysReport>(0);

    public SysReport() {
    }

    public SysReport(String fid) {
        this.fid = fid;
    }

    /**
     * 获取报表名称
     */
    @Column(name = "FREPORTNAME", length = 50)
    public String getReportName() {
        return reportName;
    }

    /**
     * 设置报表名称
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * 获取汇总查询方式编号
     *
     * @return
     */
    @Column(name = "FCODE")
    public Integer getCode() {
        return code;
    }

    /**
     * 设置汇总查询方式编号
     *
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取列表标题
     *
     * @return
     */
    @Column(name = "FHEADERS", length = 500)
    public String getHeaders() {
        return headers;
    }

    /**
     * 设置列表标题
     *
     * @param headers
     */
    public void setHeaders(String headers) {
        this.headers = headers;
    }

    /**
     * 获取统计配置信息
     *
     * @return
     */
    @Column(name = "FCOUNT_INFO", length = 200)
    public String getCountInfo() {
        return countInfo;
    }

    /**
     * 设置统计配置信息
     *
     * @param countInfo
     */
    public void setCountInfo(String countInfo) {
        this.countInfo = countInfo;
    }

    /**
     * 获取上级报表
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPARENT_ID")
    public SysReport getParent() {
        return parent;
    }

    /**
     * 设置上级报表
     *
     * @param parent
     */
    public void setParent(SysReport parent) {
        this.parent = parent;
    }

    /**
     * 获取下级报表
     *
     * @return
     */
    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "parent")
    public Set<SysReport> getChilds() {
        return childs;
    }

    /**
     * 设置下级报表
     *
     * @param childs
     */
    public void setChilds(Set<SysReport> childs) {
        this.childs = childs;
    }

    @Column(name = "FSHOW_PAGE")
    public Short getShowPage() {
        return showPage;
    }

    public void setShowPage(Short showPage) {
        this.showPage = showPage;
    }

    @Column(name = "FJAVA_SCRIPT")
    public String getJavaScript() {
        return javaScript;
    }

    public void setJavaScript(String javaScript) {
        this.javaScript = javaScript;
    }

	/*@Column(name = "FSUM_FLAG")
    public Integer getSumFlag() {
		return sumFlag;
	}

	public void setSumFlag(Integer sumFlag) {
		this.sumFlag = sumFlag;
	}*/

}
