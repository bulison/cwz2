package cn.fooltech.fool_ops.domain.rate.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>表单传输对象- 央行贷款利率</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年6月14日
 */
public class LoanRateLogVo implements Serializable {

    private static final long serialVersionUID = -7526364555164788534L;

    @ApiModelProperty("主键")
    private String fid;


    @ApiModelProperty("贷款利率*100的值")
    @NotBlank(message = "利率必填")
    @Length(max = 10, message = "利率超过{max}位数")
    private String rate;

    @ApiModelProperty("增幅")
    @NotBlank(message = "增幅必填")
    @Length(max = 10, message = "增幅超过{max}位数")
    private String increase;

    // 创建时间
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    // 创建人
    @ApiModelProperty(value = "创建人id")
    private String creatorId;
    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    // 修改时间戳,初始值为当前时间
    @ApiModelProperty(value = "修改时间戳,初始值为当前时间")
    private String updateTime;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getIncrease() {
        return increase;
    }

    public void setIncrease(String increase) {
        this.increase = increase;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
