package cn.fooltech.fool_ops.domain.flow.service;

import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateVo;
import cn.fooltech.fool_ops.domain.flow.vo.TemplateData;
import cn.fooltech.fool_ops.utils.NumberUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 模板生成的数据服务类
 */
@Service
public class TemplateDataService {

    private static final String format1 = "从【%s】采购";
    private static final String format2 = "从【%s】【%s】【%s】到【%s】，";
    private static final String format3 = "销售给【%s】";

    private static final String amount = "，金额：【%s】元";
    private static final String goodsStr = "【货品】【货品属性】，数量：【数量】【货品单位】";

    /**
     * 获取数据的描述
     * @return
     */
    @Transactional(readOnly = true)
    public String getAllDescribe(TemplateData entity){

        String describe = "";
        //模板标识（1-采购，2-运输，3-销售）
        if(entity.getFlag()== PlanTemplateVo.TEMPLATE_TYPE_PURCHASE){
            describe = String.format(format1, entity.getSupplier().getName());
        }else if(entity.getFlag()==PlanTemplateVo.TEMPLATE_TYPE_TRANSPORT){
            describe = String.format(format2, entity.getPublishAddress().getName(), entity.getShipmentType().getName(),
                    entity.getTransportType().getName(), entity.getReceiveAddress().getName());
        }else{
            describe = String.format(format3, entity.getCustomer().getName());
        }
        describe = describe+getGoodsDescribeAll(entity);
        describe = describe+String.format(amount, NumberUtil.bigDecimalToStr(entity.getTotalAmount(),2));
        return describe;
    }

    /**
     * 获取多个货品的描述
     * @return
     */
    private String getGoodsDescribeAll(TemplateData entity){

        if(entity.getMergeData().size()>0){
            List<String> strs = Lists.newArrayList();
            for(TemplateData templateData:entity.getMergeData()){
                strs.add(goodDescribe(templateData));
            }

            Joiner joiner = Joiner.on("，");
            return joiner.join(strs);
        }else{
            return goodDescribe(entity);
        }
    }

    /**
     * 获得货品描述
     * @param templateData
     * @return
     */
    private String goodDescribe(TemplateData templateData){
        //【货品】【货品属性】，数量：【数量】【货品单位】
        StringBuffer buffer = new StringBuffer();
        buffer.append("【");
        buffer.append(templateData.getGoods().getName());
        buffer.append("】");
        if(templateData.getSpec()!=null){
            buffer.append("【");
            buffer.append(templateData.getSpec().getName());
            buffer.append("】");
        }
        buffer.append("，数量：【");
        buffer.append(NumberUtil.bigDecimalToStr(templateData.getQuentity()));
        buffer.append("】【");
        buffer.append(templateData.getAccountUnitName());
        buffer.append("】");

        return buffer.toString();
    }
}
