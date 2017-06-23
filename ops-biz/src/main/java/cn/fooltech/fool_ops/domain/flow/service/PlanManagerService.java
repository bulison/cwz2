package cn.fooltech.fool_ops.domain.flow.service;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.PageService;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportPriceDetail2Repository;
import cn.fooltech.fool_ops.domain.flow.dao.PlanManagerDao;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTransportFeeVo;
import cn.fooltech.fool_ops.domain.flow.vo.TransportFeeDetailVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>运输排程</p>
 *
 * @author c
 * @date 2017-03-01
 */

@Service
public class PlanManagerService implements PageService {

    @Autowired
    private PlanManagerDao planManagerDao;

    /**
     * 成本预计查询
     */
    @Transactional
    public PageJson queryPlanTransportFee(String planId, String deliveryPlace, String receiptPlace,
                                               String goodsId, String goodsSpecId, PageParamater page){
        Page pageHelper = PageHelper.startPage(page.getPage(), page.getRows(), true);

        InnerBean innerBean = queryAndWrap(planId, deliveryPlace, receiptPlace, goodsId, goodsSpecId);
        String[][] headerArgs = innerBean.getHeaderArgs();
        List<Object []> arrList = innerBean.getArrList();

        Map<String, String> header = Maps.newLinkedHashMap();
        for (int i=0; i<headerArgs[0].length; i++) {
            header.put(headerArgs[1][i], headerArgs[0][i]);
        }

        List<Map<String, Object>> dataList = Lists.newArrayListWithCapacity(arrList.size());
        for (Object [] entity: arrList) {
            Map<String, Object> dataMap = Maps.newLinkedHashMap();
            for (int j=0; j<headerArgs[0].length; j++) {
                dataMap.put(headerArgs[1][j], entity[j]);
            }
            dataList.add(dataMap);
        }

        PageJson pageJson = new PageJson();
        pageJson.setOther(header);
        pageJson.setRows(dataList);
        pageJson.setTotal(pageHelper.getTotal());
        return pageJson;
    }

    /**
     * 成本预计导出
     */
    @Transactional
    public RequestResult exportPlanTransportFee(String planId, String deliveryPlace, String receiptPlace,
                                                String goodsId, String goodsSpecId,HttpServletResponse response, PageParamater page){
        Page pageHelper = PageHelper.startPage(page.getPage(), page.getRows(), true);

        InnerBean innerBean = queryAndWrap(planId, deliveryPlace, receiptPlace, goodsId, goodsSpecId);
        String[][] headerArgs = innerBean.getHeaderArgs();
        List<Object []> arrList = innerBean.getArrList();

        try {
            ExcelUtils.exportExcel(headerArgs[0], arrList, response, "成本预计.xls");
        } catch (Exception e) {
            e.printStackTrace();
            return buildFailRequestResult("成本预计导出异常");
        }
        return buildSuccessRequestResult();
    }

    private InnerBean queryAndWrap(String planId, String deliveryPlace, String receiptPlace,
                                   String goodsId, String goodsSpecId){
        List<PlanTransportFeeVo> datas = planManagerDao.queryPlanTransportFee(planId, deliveryPlace,
                receiptPlace, goodsId, goodsSpecId);


        Map<String, String> allFeeTitle = Maps.newLinkedHashMap();

        for (PlanTransportFeeVo data: datas) {
            String transportBillId = data.getFeeId();//取出运输费报价单
            List<TransportFeeDetailVo> detailFee = planManagerDao.queryFeeByTransportBillId(transportBillId);

            Map<String, TransportFeeDetailVo> detailFeeMap = Maps.newLinkedHashMap();

            for(TransportFeeDetailVo detailVo:detailFee){
                allFeeTitle.put(detailVo.getFeeId(), detailVo.getFeeName());
                detailFeeMap.put(detailVo.getFeeId(), detailVo);
            }

            data.setFeeMap(detailFeeMap);
        }

        String[][] headerArgs = headerProcess(PlanTransportFeeVo.HEADER_TITLE, PlanTransportFeeVo.HEADER_KEY, allFeeTitle);
        List<Object []> arrList = dataProcess(headerArgs[0], headerArgs[1], datas);

        InnerBean innerBean = new InnerBean();
        innerBean.setHeaderArgs(headerArgs);
        innerBean.setArrList(arrList);
        return innerBean;
    }

    /**
     * 添加额外的表头
     * @param headerTitle 原表头
     * @param headerKey 原KEY
     * @param extTitle 额外的表头
     */
    private String[][] headerProcess(String [] headerTitle, String [] headerKey, Map<String, String> extTitle){
        String [] newHeaderTitle = new String[headerTitle.length + extTitle.size()];
        String [] newHeaderKey = new String[headerTitle.length + extTitle.size()];

        Set<String> keys = extTitle.keySet();
        Iterator<String> it = keys.iterator();
        for (int i=0; i<headerTitle.length + extTitle.size(); i++) {
            if (i <= headerTitle.length - 1) {
                newHeaderTitle[i] = headerTitle[i];
                newHeaderKey[i] = headerKey[i];
            } else{
                String iterkey = it.next();
                String iterval = extTitle.get(iterkey);
                newHeaderTitle[i] = iterval;
                newHeaderKey[i] = iterkey;
            }
        }

        return new String [][]{newHeaderTitle, newHeaderKey};
    }

    /**
     * 格式化数据为按表头排列的数组集合
     * @param headerTitle 表头
     * @param headerKey KEY值
     * @param vos 查询表单
     */
    @SuppressWarnings("unchecked")
    private List<Object []> dataProcess(String [] headerTitle, String [] headerKey,
                                        List<PlanTransportFeeVo> vos) {
        List<Object []> list = Lists.newArrayList();

        for (PlanTransportFeeVo vo : vos) {
            Map<String, Object> map = bean2Map(vo);
            Object[] row = new Object[headerTitle.length];

            for (int i=0; i<headerKey.length; i++) {
                String key = headerKey[i];
                Object obj = map.containsKey(key) ? map.get(key) : "";
                row[i] = (obj == null) ? "" : obj;

                Map<String, TransportFeeDetailVo> detailFee = vo.getFeeMap();

                //特殊处理动态部分
                if (detailFee.containsKey(key)) {
                    TransportFeeDetailVo detailVo = detailFee.get(key);
                    if(detailVo!=null){
                        row[i] = detailVo.getAmount();
                    }
                }
            }
            list.add(row);
        }
        return list;
    }

    /**
     * JAVA BEAN 转为 Map集合
     */
    private Map<String, Object> bean2Map(PlanTransportFeeVo vo){
        Map<String, Object> map = Maps.newLinkedHashMap();

        Class cls = vo.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(vo));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Data
    class InnerBean{
        String[][] headerArgs;
        List<Object []> arrList;
    }

}
