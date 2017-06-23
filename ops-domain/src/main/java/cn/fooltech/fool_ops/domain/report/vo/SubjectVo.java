package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;

/**
 * 科目查询辅助
 *
 * @author xjh
 */
public class SubjectVo implements Serializable {
    private static final long serialVersionUID = 2703783042572880373L;
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
