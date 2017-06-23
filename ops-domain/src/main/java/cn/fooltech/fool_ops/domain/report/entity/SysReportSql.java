package cn.fooltech.fool_ops.domain.report.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;


/**
 * <p>系统报表查询SQL</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年10月12日
 */
@Entity
@Table(name = "REP_SYS_SQL")
public class SysReportSql extends OpsEntity {

    private static final long serialVersionUID = -5198332491504556573L;

    /**
     * 系统报表
     */
    private SysReport sysReport;

    /**
     * SQL语句
     */
    private String sql;

    /**
     * 获取系统报表
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSYS_REPORT_ID", nullable = false)
    public SysReport getSysReport() {
        return sysReport;
    }

    /**
     * 设置系统报表
     *
     * @param sysReport
     */
    public void setSysReport(SysReport sysReport) {
        this.sysReport = sysReport;
    }

    /**
     * 获取SQL语句
     *
     * @return
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "FSQL", columnDefinition = "TEXT", nullable = false)
    @NotBlank
    public String getSql() {
        return sql;
    }

    /**
     * 设置SQL语句
     *
     * @param sql
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

}
