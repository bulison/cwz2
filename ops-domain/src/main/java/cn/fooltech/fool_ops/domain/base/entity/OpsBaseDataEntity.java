/**
 *
 */
package cn.fooltech.fool_ops.domain.base.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * ops基础数据实体基类
 * 基础数据指记录数较固定，字段少，主要字段有编码、名称，经常用于下拉选项
 *
 * @author ljb
 * @version 1.0
 * @date 2014年5月9日
 */
@MappedSuperclass
public class OpsBaseDataEntity extends OpsEntity {

    private static final long serialVersionUID = -58063436590912903L;

    /**
     * 编码，相当于助记符 一般控制有效的记录中编码不可重复
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String describe;

    /**
     * 获取编码
     *
     * @return
     */
    @Column(name = "FCODE", length = 50, nullable = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置编码
     *
     * @param theCode
     */
    public void setCode(String theCode) {
        code = theCode;
    }

    /**
     * 获取名称
     *
     * @return
     */
    @Column(name = "FNAME", length = 100, nullable = false)
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param theName
     */
    public void setName(String theName) {
        name = theName;
    }

    /**
     * 获取描述
     *
     * @return
     */
    @Column(name = "FDESCRIBE", length = 200)
    public String getDescribe() {
        return describe;
    }

    /**
     * 设置描述
     *
     * @param describe
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

}
