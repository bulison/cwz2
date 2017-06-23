package cn.fooltech.fool_ops.domain.report.vo;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;

import java.io.Serializable;


/**
 * <p>表单传输对象- 辅助属性简单信息</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年1月27日
 */
public class AuxiliaryAttrSimpleVo implements Serializable {

    private static final long serialVersionUID = -1630673550814013891L;

    /**
     * 科目ID
     */
    private String subjectId;

    /**
     * 辅助属性ID
     */
    private String auxiliaryAttrId;

    /**
     * 辅助属性编号
     */
    private String auxiliaryAttrCode;

    /**
     * 辅助属性名称
     */
    private String auxiliaryAttrName;

    /**
     * 余额方向
     */
    private Integer direction;

    /**
     * 余额方向名称
     */
    private String directionName;

    public AuxiliaryAttrSimpleVo() {

    }

    public AuxiliaryAttrSimpleVo(String subjectId, String auxiliaryAttrId, String auxiliaryAttrCode, String auxiliaryAttrName, Integer direction) {
        this.subjectId = subjectId;
        this.auxiliaryAttrId = auxiliaryAttrId;
        this.auxiliaryAttrCode = auxiliaryAttrCode;
        this.auxiliaryAttrName = auxiliaryAttrName;
        this.direction = direction;
        //余额方向
        if (this.direction == FiscalAccountingSubject.DIRECTION_BORROW) {
            this.directionName = "借";
        } else {
            this.directionName = "贷";
        }
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
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

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

}
