package cn.fooltech.fool_ops.domain.analysis.vo;

import java.math.BigDecimal;


public class TransportSeperateDetailVo {

    private String transportCostName;//运输费用名称
    private BigDecimal amount;//金额
    private String deliveryPlace;//收货地
    private String receiptPlace;//收货地
    private String billDate;//报价日期
    private String transportCompany;//运输公司
    private String transportType;//运输方式
    private String shipmentType;//装运方式

    public String getTransportCostName() {
        return transportCostName;
    }

    public void setTransportCostName(String transportCostName) {
        this.transportCostName = transportCostName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getTransportCompany() {
        return transportCompany;
    }

    public void setTransportCompany(String transportCompany) {
        this.transportCompany = transportCompany;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public String getReceiptPlace() {
        return receiptPlace;
    }

    public void setReceiptPlace(String receiptPlace) {
        this.receiptPlace = receiptPlace;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }
}
