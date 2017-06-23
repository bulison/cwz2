package cn.fooltech.fool_ops.domain.report.vo;

/**
 * <p>表单传输对象- 明细分类账报表</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年2月3日
 */
public class DetailCategoryAccountVo {

    /**
     * 当前科目ID
     */
    private String curSubjectId;

    /**
     * 科目开始编号
     */
    private String subjectStartCode;

    /**
     * 科目结束编号
     */
    private String subjectEndCode;

    /**
     * 科目层级
     */
    private Integer subjectLevel = 1;

    /**
     * 标识<br>
     * -1   上一个科目   1 下一个科目  0 当前科目
     */
    private Integer operationFlag = 0;

    public String getCurSubjectId() {
        return curSubjectId;
    }

    public void setCurSubjectId(String curSubjectId) {
        this.curSubjectId = curSubjectId;
    }

    public String getSubjectStartCode() {
        return subjectStartCode;
    }

    public void setSubjectStartCode(String subjectStartCode) {
        this.subjectStartCode = subjectStartCode;
    }

    public String getSubjectEndCode() {
        return subjectEndCode;
    }

    public void setSubjectEndCode(String subjectEndCode) {
        this.subjectEndCode = subjectEndCode;
    }

    public Integer getSubjectLevel() {
        return subjectLevel;
    }

    public void setSubjectLevel(Integer subjectLevel) {
        this.subjectLevel = subjectLevel;
    }

    public Integer getOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(Integer operationFlag) {
        this.operationFlag = operationFlag;
    }

}
