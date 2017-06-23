package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象- 多栏明细账设置明细</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年1月26日
 */
public class FiscalMultiColumnDetailVo implements Serializable {

    private static final long serialVersionUID = -8472659381444615879L;

    /**
     * ID
     */
    private String fid;

    /**
     * 多栏明细ID
     */
    private String fiscalMultiColumnId;

    /**
     * 多栏明细名称
     */
    private String fiscalMultiColumnName;

    /**
     * 科目ID
     */
    private String subjectId;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 余额方向(借、贷)
     */
    private Integer direction;

    /**
     * 辅助核算类别
     */
    private Integer auxiliaryType;

    /**
     * 辅助核算ID
     */
    private String auxiliaryAttrId;

    /**
     * 辅助核算编号
     */
    private String auxiliaryAttrCode;

    /**
     * 辅助核算名称
     */
    private String auxiliaryAttrName;

    public String getFiscalMultiColumnId() {
        return fiscalMultiColumnId;
    }

    public void setFiscalMultiColumnId(String fiscalMultiColumnId) {
        this.fiscalMultiColumnId = fiscalMultiColumnId;
    }

    public String getFiscalMultiColumnName() {
        return fiscalMultiColumnName;
    }

    public void setFiscalMultiColumnName(String fiscalMultiColumnName) {
        this.fiscalMultiColumnName = fiscalMultiColumnName;
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

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getAuxiliaryType() {
        return auxiliaryType;
    }

    public void setAuxiliaryType(Integer auxiliaryType) {
        this.auxiliaryType = auxiliaryType;
    }

    public String getAuxiliaryAttrId() {
        return auxiliaryAttrId;
    }

    public void setAuxiliaryAttrId(String auxiliaryAttrId) {
        this.auxiliaryAttrId = auxiliaryAttrId;
    }

    public String getAuxiliaryAttrCode() {
        return auxiliaryAttrCode;
    }

    public void setAuxiliaryAttrCode(String auxiliaryAttrCode) {
        this.auxiliaryAttrCode = auxiliaryAttrCode;
    }

    public String getAuxiliaryAttrName() {
        return auxiliaryAttrName;
    }

    public void setAuxiliaryAttrName(String auxiliaryAttrName) {
        this.auxiliaryAttrName = auxiliaryAttrName;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

}
