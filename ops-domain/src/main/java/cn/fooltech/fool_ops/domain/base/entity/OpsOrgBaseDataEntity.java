/**
 *
 */
package cn.fooltech.fool_ops.domain.base.entity;

import cn.fooltech.fool_ops.domain.sysman.entity.Organization;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;


/**
 * ops带企业属性的基础数据实体基类
 *
 * @author ljb
 * @version 1.0
 * @date 2014年6月11日
 */
@MappedSuperclass
public class OpsOrgBaseDataEntity extends OpsBaseDataEntity {

    private static final long serialVersionUID = -6490215063651827242L;

    /**
     * 所属企业
     */
    private Organization org;

    /**
     * 获取所属企业
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置所属企业
     *
     * @param org
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

}
