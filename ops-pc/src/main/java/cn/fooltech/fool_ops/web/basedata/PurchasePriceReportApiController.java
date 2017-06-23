package cn.fooltech.fool_ops.web.basedata;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.basedata.dao.PurchasePriceReportDao;
import cn.fooltech.fool_ops.domain.basedata.vo.PeerQuoteVo;
import cn.fooltech.fool_ops.domain.basedata.vo.PurchasePriceVo;
import cn.fooltech.fool_ops.domain.basedata.vo.SimplePeerQuoteVo;
import cn.fooltech.fool_ops.domain.basedata.vo.SimplePurchasePriceVo;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.common.vo.Base64Img;
import cn.fooltech.fool_ops.domain.line.LineChartVo;
import cn.fooltech.fool_ops.domain.line.LineDataVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 货品采购价格分析
 * Created by xjh on 2017/1/3.
 */
@RestController
@RequestMapping("/api/purchasePrice")
public class PurchasePriceReportApiController {

    @Autowired
    private PurchasePriceReportDao dao;
    
    @Autowired
    private AttachService attachService;
    
    @ApiOperation("查询货品采购价格报表")
    @GetMapping("/queryReport")
    public PageJson queryReport(PurchasePriceVo vo, PageParamater page){
        Page pageHelper = PageHelper.startPage(page.getPage(), page.getRows(), true);
        if(vo.getStartDay()!=null){
            vo.setStartDay(DateUtilTools.changeDateTime(vo.getStartDay(), 0, 0, 0, 0, 0));
        }
        if(vo.getEndDay()!=null){
            vo.setEndDay(DateUtilTools.changeDateTime(vo.getEndDay(), 0, 0, 23, 59, 59));
        }

        PageJson pageJson = new PageJson();
        vo.setAccId(SecurityUtil.getFiscalAccountId());
        List<PurchasePriceVo> datas = dao.queryReport(vo);
        pageJson.setRows(datas);
        pageJson.setTotal(pageHelper.getTotal());

        return pageJson;
    }

    @ApiOperation("查询货品采购价格曲线图数据")
    @GetMapping("/queryLineData")
    public LineChartVo queryLineData(PurchasePriceVo vo){
        if(vo.getStartDay()!=null){
            vo.setStartDay(DateUtilTools.changeDateTime(vo.getStartDay(), 0, 0, 0, 0, 0));
        }
        if(vo.getEndDay()!=null){
            vo.setEndDay(DateUtilTools.changeDateTime(vo.getEndDay(), 0, 0, 23, 59, 59));
        }
        vo.setAccId(SecurityUtil.getFiscalAccountId());
        List<SimplePurchasePriceVo> datas = dao.queryPriceTrend(vo);
        Map<String, List<SimplePurchasePriceVo>> supplierMap = Maps.newLinkedHashMap();
        for(SimplePurchasePriceVo svo:datas){
            if(Strings.isNullOrEmpty(svo.getSupplierName()))continue;
            String key = svo.getSupplierName().trim();
            if(supplierMap.containsKey(key)){
                List<SimplePurchasePriceVo> existData = supplierMap.get(key);
                existData.add(svo);
            }else{
                List<SimplePurchasePriceVo> existData = Lists.newArrayList();
                existData.add(svo);
                supplierMap.put(key, existData);
            }
        }
        List<LineDataVo> lines = Lists.newArrayList();
        String xAxis[] = null;
        for(String keyIter:supplierMap.keySet()){
            List<SimplePurchasePriceVo> simpleVos = supplierMap.get(keyIter);
            Map<String, BigDecimal> fillDatas = fillData(vo.getStartDay(), vo.getEndDay(), simpleVos);
            LineDataVo lineDataVo = new LineDataVo();
            lineDataVo.setName(keyIter);
            BigDecimal bdata[] = {};
            lineDataVo.setData(fillDatas.values().toArray(bdata));
            lines.add(lineDataVo);
            String xAxisDatas[] = {};
            if(xAxis==null)xAxis = fillDatas.keySet().toArray(xAxisDatas);
        }


        LineChartVo chartVo = new LineChartVo();
        chartVo.setCharTitle("货品采购价格分析");
        String lineTitles[] = {};
        chartVo.setLineTitle(supplierMap.keySet().toArray(lineTitles));
        chartVo.setLineDataVoList(lines);
        chartVo.setXAxis(xAxis);
        return chartVo;
    }

    /**
     * 按时间填充每一天数据（没有数据不填充）
     * @param startDay
     * @param endDay
     * @param data
     */
    private Map<String, BigDecimal> fillData(Date startDay, Date endDay, List<SimplePurchasePriceVo> data){
        Map<String, BigDecimal> daydata = Maps.newLinkedHashMap();

        Date iter = DateUtilTools.changeDateTime(startDay, 0, 0, 0, 0,0);
        endDay = DateUtilTools.changeDateTime(endDay, 0, 0, 0, 0,0);

        do {

            String dateStr = DateUtilTools.date2String(iter);
            daydata.put(dateStr, null);
            iter = DateUtilTools.changeDateTime(iter, 0, 1, 0, 0, 0);
        }while(iter.compareTo(endDay)<=0);

        for(SimplePurchasePriceVo simple:data){
            Date sdate = simple.getBillDate();
            String dateStr = DateUtilTools.date2String(sdate);
            BigDecimal val = NumberUtil.multiply(simple.getUnitScale(), simple.getDeliveryPrice());
            daydata.put(dateStr, val);
        }

        return daydata;
    }

    /**
     * 按时间填充每一天数据（没有数据自动填充）
     * @param startDay
     * @param endDay
     * @param data
     */
    private Map<String, BigDecimal> autoFillData(Date startDay, Date endDay, List<SimplePurchasePriceVo> data){
        Map<String, BigDecimal> daydata = Maps.newLinkedHashMap();

        Date iter = DateUtilTools.changeDateTime(startDay, 0, 0, 0, 0,0);
        endDay = DateUtilTools.changeDateTime(endDay, 0, 0, 0, 0,0);

        SimplePurchasePriceVo first = data.get(0);
        BigDecimal lastVal = NumberUtil.multiply(first.getUnitScale(), first.getDeliveryPrice());
        int i = 0;

        do{
            while(i<data.size()){
                SimplePurchasePriceVo simple = data.get(i);
                Date sdate = simple.getBillDate();
                sdate = DateUtilTools.changeDateTime(sdate, 0, 0, 0, 0,0);
                if(sdate.compareTo(iter)>0){
                    break;
                }else if(sdate.compareTo(iter)==0){
                    lastVal = NumberUtil.multiply(simple.getUnitScale(), simple.getDeliveryPrice());
                    i++;
                }else{
                    i++;
                }
            }

            String dateStr = DateUtilTools.date2String(iter);
            daydata.put(dateStr, lastVal);
            iter = DateUtilTools.changeDateTime(iter, 0, 1, 0, 0,0);
        }while(iter.compareTo(endDay)<=0);
        return daydata;
    }
    
    @ApiOperation("根据主表ID查询货品采购价格分析附件")
    @GetMapping("/queryAttach")
    public List<Base64Img> queryAttach(@RequestParam String busId ){
    	List<Base64Img> imgs = attachService.getImgs(busId);
    	return imgs;
    }
}
