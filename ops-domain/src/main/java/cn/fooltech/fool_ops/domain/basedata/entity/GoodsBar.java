package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * 货品条码
 *
 * @author xjh
 */
@Entity
@Table(name = "tsb_goods_barcode")
public class GoodsBar extends OpsOrgEntity {

    private static final long serialVersionUID = 6585587910752511097L;

    /**
     * 货品条码
     */
    private String barCode;

    /**
     * 货品
     */
    private Goods goods;

    /**
     * 属性
     */
    private GoodsSpec spec;

    /**
     * 单位
     */
    private Unit unit;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间戳
     */
    private Date updateTime;

    /**
     * 获取条码
     *
     * @return
     */
    @Column(name = "FBARCODE")
    public String getBarCode() {
        return barCode;
    }

    /**
     * 设置条码
     *
     * @param barCode
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * 获取货品
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "FGOODS_ID")
    public Goods getGoods() {
        return goods;
    }

    /**
     * 设置货品
     *
     * @param goods
     */
    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    /**
     * 获取属性
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "FGOODS_SPEC_ID")
    public GoodsSpec getSpec() {
        return spec;
    }

    /**
     * 设置属性
     *
     * @param spec
     */
    public void setSpec(GoodsSpec spec) {
        this.spec = spec;
    }

    /**
     * 获取单位
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "FUINT_ID")
    public Unit getUnit() {
        return unit;
    }

    /**
     * 设置单位
     *
     * @param unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * 获取创建人
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间戳
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置创建时间戳
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
