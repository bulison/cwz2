package cn.fooltech.fool_ops.domain.rate.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 收益率方案明细</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-06-14 08:58:29
 */
public class RateProgrammeRecordVo implements Serializable {

    private static final long serialVersionUID = -6261398579546329123L;
    @NotBlank(message = "交易日期必填")
    @Length(min = 10, max = 10, message = "交易日期格式有误")
    private String tradeDate;//交易日期
    //private String realDate;//实际交易日期
    //private Short tradeStatus;//交易状态  0：未开始；1：进行中；2：已完成
    @NotNull(message = "交易类型必填")
    private Short type;//交易类型 0：收入；1：支出
    @NotNull(message = "交易金额必填")
    @Min(value = 1, message = "交易金额不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "交易金额不能大于{value}")
    private BigDecimal amount;//交易金额
    private String rateProgrammeId;//方案ID
    private String fid;

    public String getTradeDate() {
        return this.tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    /*public Short getTradeStatus(){
        return this.tradeStatus;
    }

    public void setTradeStatus(Short tradeStatus){
        this.tradeStatus = tradeStatus;
    }*/
    public Short getType() {
        return this.type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getRateProgrammeId() {
        return rateProgrammeId;
    }

    public void setRateProgrammeId(String rateProgrammeId) {
        this.rateProgrammeId = rateProgrammeId;
    }

	/*public String getRealDate() {
        return realDate;
	}

	public void setRealDate(String realDate) {
		this.realDate = realDate;
	}*/
}
