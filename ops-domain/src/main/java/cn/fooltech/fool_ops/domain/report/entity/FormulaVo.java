package cn.fooltech.fool_ops.domain.report.entity;

/**
 * 公式项类
 *
 * @author xjh
 */
public class FormulaVo {

    protected String fh;//+-
    protected String name;//函数名称
    protected String fullName;//函数名称
    protected String paramater;//函数参数

    public FormulaVo(String fh, String name, String fullName, String paramater) {
        super();
        this.fh = fh;
        this.name = name;
        this.fullName = fullName;
        this.paramater = paramater;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParamater() {
        return paramater;
    }

    public void setParamater(String paramater) {
        this.paramater = paramater;
    }

    public String getFh() {
        return fh;
    }

    public void setFh(String fh) {
        this.fh = fh;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
