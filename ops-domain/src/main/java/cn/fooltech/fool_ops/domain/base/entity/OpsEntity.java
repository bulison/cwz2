/**
 *
 */
package cn.fooltech.fool_ops.domain.base.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * ops实体基类
 * <p>
 * 1.隔离gdp层
 * 2.提供扩展
 *
 * @author ljb
 * @version 1.0
 * @date 2014年5月9日
 */
@MappedSuperclass
public class OpsEntity extends BasePO implements Serializable, Cloneable {

    public static final String STATUS_SNU = "SNU";//无效数据
    public static final String STATUS_SAC = "SAC";//有效数据
    private static final long serialVersionUID = -1425111764061702334L;
    /**
     * 主键
     */
    protected String fid;

    /**
     * 获取主键
     */
    @Override
    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getFid() {
        return fid;
    }

    /**
     * 设置主键
     */
    @Override
    public void setFid(String fid) {
        this.fid = fid;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        OpsEntity entity = (OpsEntity) clone;
        entity.setFid(null);
        return entity;
    }
}
