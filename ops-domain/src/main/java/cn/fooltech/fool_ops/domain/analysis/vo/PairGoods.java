package cn.fooltech.fool_ops.domain.analysis.vo;

/**
 * Created by Derek on 2017/5/9.
 */
public class PairGoods{
    private String goodsId;
    private String sepcId;

    public PairGoods(String goodsId, String sepcId) {
        this.goodsId = goodsId;
        this.sepcId = sepcId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getSepcId() {
        return sepcId;
    }

    public void setSepcId(String sepcId) {
        this.sepcId = sepcId;
    }
}
