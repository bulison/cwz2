package cn.fooltech.fool_ops.domain.fiscal.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象 - 财务-科目模板类型</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-11-26 11:39:12
 */
public class FiscalTemplateTypeVo implements Serializable {

    private static final long serialVersionUID = -3075528800766454268L;
    private String code;
    private String name;
    private String fid;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
