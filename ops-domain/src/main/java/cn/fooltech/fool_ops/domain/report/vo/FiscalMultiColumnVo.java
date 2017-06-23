package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象- 多栏明细账设置</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年1月26日
 */
public class FiscalMultiColumnVo implements Serializable {

    private static final long serialVersionUID = -1883246104827344658L;

    /**
     * ID
     */
    private String fid;

    /**
     * 科目ID
     */
    private String subjectId;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 名称
     */
    private String name;

    /**
     * 部门
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 核算项目类别
     */
    private Integer auxiliaryType;

    /**
     * 多栏明细账设置明细(JSON数组字符串)
     */
    private String details;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getAuxiliaryType() {
        return auxiliaryType;
    }

    public void setAuxiliaryType(Integer auxiliaryType) {
        this.auxiliaryType = auxiliaryType;
    }

}
