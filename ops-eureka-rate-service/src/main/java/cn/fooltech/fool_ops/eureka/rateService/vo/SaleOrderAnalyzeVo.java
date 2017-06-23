package cn.fooltech.fool_ops.eureka.rateService.vo;

/**
 * 演示Vo
 * Created by xjh on 2017/3/16.
 */
public class SaleOrderAnalyzeVo {

    private String goodsId;//货品ID
    private String goodsName;//货品名称
    private Double rate;//收益率

    public SaleOrderAnalyzeVo(String goodsId, String goodsName, Double rate) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.rate = rate;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
