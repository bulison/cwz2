/**
 *
 */
package cn.fooltech.fool_ops.domain.base.entity;

import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * <p>ops带企业属性的实体基类</p>
 *
 * @author ljb
 * @version 1.0
 * @date 2014年6月25日
 */
@MappedSuperclass
public class OpsOrgEntity extends OpsEntity {

    private static final long serialVersionUID = -8039585719072439576L;

    /**
     * 所属企业
     */
    protected Organization org;

    /**
     * 获取所属企业
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    @JsonIgnore
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
