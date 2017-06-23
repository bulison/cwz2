package cn.fooltech.fool_ops.domain.flow.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>表单传输对象 - 监督人</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-05-17 15:09:31
 */
public class SuperviseVo implements Serializable {

    private static final long serialVersionUID = -6403372922903806580L;

    @NotBlank(message = "监督人ID必填")
    private String superviseId;//监督人
    private String superviseName;

    @NotBlank(message = "消息预警配置ID必填")
    private String warnSettingId;//消息预警配置ID

    @NotNull(message = "类型必填")
    private Integer type;//类型1:部门监督人; 2:机构监督人
    private String fid;

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getSuperviseId() {
        return superviseId;
    }

    public void setSuperviseId(String superviseId) {
        this.superviseId = superviseId;
    }

    public String getWarnSettingId() {
        return warnSettingId;
    }

    public void setWarnSettingId(String warnSettingId) {
        this.warnSettingId = warnSettingId;
    }

    public String getSuperviseName() {
        return superviseName;
    }

    public void setSuperviseName(String superviseName) {
        this.superviseName = superviseName;
    }
}
