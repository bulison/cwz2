package cn.fooltech.fool_ops.domain.warehouse.strategy;

import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.ReflectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 策略处理类
 * Created by xjh on 2016/12/22.
 */
public class CalMethodProcessor {

    /**
     * 根据策略set运算
     *
     * @param exist 目标字段值
     * @param detail 临时变量值
     * @param methods
     * @param <T>
     * @return
     */
    public static <T> T process(T exist, Object detail, Object total, List<CalMethod> methods) {
        for (CalMethod calMethod : methods) {

            //System.out.println("==========calMethod:"+calMethod);

            BigDecimal field1Val = BigDecimal.ZERO;
            BigDecimal field2Val = BigDecimal.ZERO;
            BigDecimal field3Val = BigDecimal.ZERO;

            if (calMethod.firstFieldRef() == 1) {
                field1Val = getVal(exist, calMethod.field1);
            } else {
                field1Val = getVal(detail, calMethod.field1);
            }
            if (calMethod.secondFieldRef() == 1) {
                field2Val = getVal(exist, calMethod.field2);
            } else {
                field2Val = getVal(detail, calMethod.field2);
            }
            if(calMethod.hasThirdRef()){
                if (calMethod.thirdFieldRef() == 1) {
                    field3Val = getVal(exist, calMethod.field3);
                }else if(calMethod.thirdFieldRef() == 2) {
                    field3Val = getVal(total, calMethod.field3);
                }else{
                    field3Val = getVal(detail, calMethod.field3);
                }
            }

            BigDecimal result = BigDecimal.ZERO;

            if(!calMethod.hasThirdRef()){
                if (calMethod.com1 == CalMethod.Compute.Add) {
                    result = NumberUtil.add(field1Val, field2Val);
                } else if (calMethod.com1 == CalMethod.Compute.Sub) {
                    result = NumberUtil.subtract(field1Val, field2Val);
                } else if (calMethod.com1 == CalMethod.Compute.Mul) {
                    result = NumberUtil.multiply(field1Val, field2Val);
                } else {
                    if(field2Val.compareTo(BigDecimal.ZERO)==0)continue;
                    result = NumberUtil.divide(field1Val, field2Val, 20);
                }
            } else {
                if (calMethod.com2 == CalMethod.Compute.Add) {
                    result = NumberUtil.add(field2Val, field3Val);
                } else if (calMethod.com2 == CalMethod.Compute.Sub) {
                    result = NumberUtil.subtract(field2Val, field3Val);
                } else if (calMethod.com2 == CalMethod.Compute.Mul) {
                    result = NumberUtil.multiply(field2Val, field3Val);
                } else {
                    if(field3Val.compareTo(BigDecimal.ZERO)==0)continue;
                    result = NumberUtil.divide(field2Val, field3Val, 20);
                }
                if (calMethod.com1 == CalMethod.Compute.Add) {
                    result = NumberUtil.add(field1Val, result);
                } else if (calMethod.com1 == CalMethod.Compute.Sub) {
                    result = NumberUtil.subtract(field1Val, result);
                } else if (calMethod.com1 == CalMethod.Compute.Mul) {
                    result = NumberUtil.multiply(field1Val, result);
                } else {
                    if(result.compareTo(BigDecimal.ZERO)==0)continue;
                    result = NumberUtil.divide(field1Val, result, 20);
                }
            }

            ReflectionUtils.setFieldValue(exist, calMethod.targetField, result);
        }
        return exist;
    }

    /**
     * 获得对象值
     * @param obj
     * @param fieldName
     * @return
     */
    private static BigDecimal getVal(Object obj, String fieldName){
        Object val = ReflectionUtils.getFieldValue(obj, fieldName);
        if(val==null)return BigDecimal.ZERO;
        return (BigDecimal)val;
    }
}
