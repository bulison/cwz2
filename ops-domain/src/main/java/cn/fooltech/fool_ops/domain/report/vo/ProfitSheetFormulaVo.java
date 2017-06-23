package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象 - 利润公式</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-02-18 16:28:10
 */
public class ProfitSheetFormulaVo implements Serializable {

    private static final long serialVersionUID = 641115058574743542L;
    private String item;//项目
    private Integer number;//行号
    private String formula;//公式
    private String updateTime;//修改时间戳
    private String fid;

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getNumber() {
        return this.number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
