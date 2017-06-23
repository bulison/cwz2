package cn.fooltech.fool_ops.domain.rate.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>表单传输对象 - 收益率方案</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-06-14 08:57:07
 */
public class RateProgrammeVo implements Serializable {

    private static final long serialVersionUID = 8126665455576218640L;
    @NotBlank(message = "方案名称必填")
    @Length(max = 32, message = "方案名称长度超过{max}个字符")
    private String name;//方案名称
    private Date createTime;//创建时间
    private String creatorId;//创建人ID
    private String creatorName;//创建人名称
    @Min(value = 1, message = "周期不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "周期不能大于{value}")
    private BigDecimal cycle;//周期
    private String fid;
    private String details;//明细json数据
    private String searchKey;//列表搜索关键字
    //利润
    private BigDecimal profit;
    //利润率
    private BigDecimal profitRate;
    //周期收益率
    private BigDecimal cycleProfitRate;
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * 获取前台表单传过来的明细集合
     */
    @SuppressWarnings("rawtypes")
    @JsonIgnore
    public List<RateProgrammeRecordVo> getDetailList() {
        List<RateProgrammeRecordVo> list = new ArrayList<RateProgrammeRecordVo>();
        if (StringUtils.isNotBlank(this.details)) {
            JSONArray array = JSONArray.fromObject(this.details);
            List details = (List) JSONArray.toCollection(array, RateProgrammeRecordVo.class);
            Iterator iterator = details.iterator();
            while (iterator.hasNext()) {
                RateProgrammeRecordVo detail = (RateProgrammeRecordVo) iterator.next();
                list.add(detail);
            }
        }
        return list;
    }

    public BigDecimal getCycle() {
        return cycle;
    }

    public void setCycle(BigDecimal cycle) {
        this.cycle = cycle;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

	public BigDecimal getProfit() {
		return profit;
	}

	public BigDecimal getProfitRate() {
		return profitRate;
	}

	public BigDecimal getCycleProfitRate() {
		return cycleProfitRate;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public void setProfitRate(BigDecimal profitRate) {
		this.profitRate = profitRate;
	}

	public void setCycleProfitRate(BigDecimal cycleProfitRate) {
		this.cycleProfitRate = cycleProfitRate;
	}

}
