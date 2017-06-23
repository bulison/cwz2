package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * <p>财务科目模板类型</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年11月26日
 */
@Entity
@Table(name = "TBD_FISCAL_TEMPLATE_TYPE")
public class FiscalTemplateType extends OpsEntity {

    private static final long serialVersionUID = 6991224888197552223L;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 获取编码
     */
    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    /**
     * 设置编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取名称
     */
    @Column(name = "FNAME")
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     */
    public void setName(String name) {
        this.name = name;
    }
}
