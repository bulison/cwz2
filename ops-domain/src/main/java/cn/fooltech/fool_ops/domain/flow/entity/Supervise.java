package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;


/**
 * <p>监督人</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年5月17日
 */
@Entity
@Table(name = "tflow_supervise")
public class Supervise extends OpsOrgEntity {

    /**
     * 部门监督人
     */
    public static final int TYPE_DEPT = 1;
    /**
     * 机构监督人
     */
    public static final int TYPE_ORG = 2;
    private static final long serialVersionUID = 3429967860919428092L;
    /**
     * 消息预警设置
     */
    private MsgWarnSetting warnSetting;

    /**
     * 监督人
     */
    private User supervise;

    /**
     * 类型1:部门监督人;2:机构监督人
     */
    private Integer type;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWARN_SETTING_ID", nullable = false)
    public MsgWarnSetting getWarnSetting() {
        return warnSetting;
    }

    public void setWarnSetting(MsgWarnSetting warnSetting) {
        this.warnSetting = warnSetting;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUPERVISE_ID", nullable = false)
    public User getSupervise() {
        return supervise;
    }

    public void setSupervise(User supervise) {
        this.supervise = supervise;
    }

    @Column(name = "FTYPE", nullable = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
