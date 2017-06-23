package cn.fooltech.fool_ops.domain.analysis.vo;

import java.math.BigDecimal;

/**
 * Created by Derek on 2017/4/26.
 */
public class BillValue {

    private String placeId;
    private String parentPlaceId;
    private String placeName;
    private BigDecimal amount;
    private BigDecimal fee;
    private String transportIds;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getParentPlaceId() {
        return parentPlaceId;
    }

    public void setParentPlaceId(String parentPlaceId) {
        this.parentPlaceId = parentPlaceId;
    }

    public String getTransportIds() {
        return transportIds;
    }

    public void setTransportIds(String transportIds) {
        this.transportIds = transportIds;
    }
}
