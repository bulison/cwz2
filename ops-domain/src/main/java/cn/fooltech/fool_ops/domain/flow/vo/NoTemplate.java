package cn.fooltech.fool_ops.domain.flow.vo;

import lombok.*;

/**
 * 没找到模板VO
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class NoTemplate {

    private int type;//模板类型 （1-采购、2-运输、3-销售）
    private String dataId;//数据id
    private String goodsName;//货品名称
    private String specName;//属性名称
    private String deliveryPlace;// 发货地
    private String receiptPlace;// 收货地

}
