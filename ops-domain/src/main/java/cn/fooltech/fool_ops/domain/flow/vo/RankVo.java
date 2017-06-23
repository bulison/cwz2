package cn.fooltech.fool_ops.domain.flow.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 评分</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-06-08 10:02:29
 */
public class RankVo implements Serializable {

    private static final long serialVersionUID = -3050402443131252439L;

    @NotNull(message = "类型必填")
    private Short type;//类型:0：计划；1：事件

    @NotBlank(message = "业务ID必填")
    private String businessId;//业务ID

    @NotNull(message = "评分必填")
    private BigDecimal rank;//评分
    private String createTime;//创建时间/评分时间
    private String creatorName;//创建人/评分人
    private String fid;
    private String comment;//评论

    public Short getType() {
        return this.type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public BigDecimal getRank() {
        return this.rank;
    }

    public void setRank(BigDecimal rank) {
        this.rank = rank;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
