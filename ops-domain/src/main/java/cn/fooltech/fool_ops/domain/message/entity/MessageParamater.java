/**
 *
 */
package cn.fooltech.fool_ops.domain.message.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.*;


/**
 * <p>消息参数</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年5月19日
 */
@Entity
@Table(name = "tmc_message_paramater")
public class MessageParamater extends OpsEntity {

    private static final long serialVersionUID = 4489825526541054711L;

    /**
     * 关联消息
     */
    private Message message;

    /**
     * 业务数据
     */
    private String busData;

    /**
     * 业务类
     */
    private String busClass;

    /**
     * 业务场景
     */
    private String busScene;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FMESSAGE_ID")
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Column(name = "FBUS_DATA")
    public String getBusData() {
        return busData;
    }

    public void setBusData(String busData) {
        this.busData = busData;
    }

    @Column(name = "FBUS_CLASS")
    public String getBusClass() {
        return busClass;
    }

    public void setBusClass(String busClass) {
        this.busClass = busClass;
    }

    @Column(name = "FBUS_SCENE")
    public String getBusScene() {
        return busScene;
    }

    public void setBusScene(String busScene) {
        this.busScene = busScene;
    }
}
