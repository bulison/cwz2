package cn.fooltech.fool_ops.web.basedata;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.basedata.dao.PeerQuoteReportDao;
import cn.fooltech.fool_ops.domain.basedata.vo.PeerQuoteVo;
import cn.fooltech.fool_ops.domain.basedata.vo.SimplePeerQuoteVo;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 同行报价分析
 * Created by xjh on 2017/1/3.
 */
@RestController
@RequestMapping("/api/peerQuoteReport")
public class PeerQuoteReportApiController {

    @Autowired
    private PeerQuoteReportDao dao;
    
    @ApiOperation("查询报表")
    @GetMapping("/queryReport")
    public PageJson queryReport(PeerQuoteVo vo, PageParamater page){
        Page pageHelper = PageHelper.startPage(page.getPage(), page.getRows());
        if(vo.getStartDay()!=null){
            vo.setStartDay(DateUtilTools.changeDateTime(vo.getStartDay(), 0, 0, 0, 0, 0));
        }
        if(vo.getEndDay()!=null){
            vo.setEndDay(DateUtilTools.changeDateTime(vo.getEndDay(), 0, 0, 23, 59, 59));
        }
        PageJson pageJson = new PageJson();
        vo.setAccId(SecurityUtil.getFiscalAccountId());
        List<PeerQuoteVo> datas = dao.queryReport(vo);
        pageJson.setTotal(pageHelper.getTotal());
        pageJson.setRows(datas);

        return pageJson;
    }

    @ApiOperation("查询曲线图数据")
    @GetMapping("/queryLineData")
    public LineChartVo queryLineData(PeerQuoteVo vo){
        if(vo.getStartDay()!=null){
            vo.setStartDay(DateUtilTools.changeDateTime(vo.getStartDay(), 0, 0, 0, 0, 0));
        }
        if(vo.getEndDay()!=null){
            vo.setEndDay(DateUtilTools.changeDateTime(vo.getEndDay(), 0, 0, 23, 59, 59));
        }
        vo.setAccId(SecurityUtil.getFiscalAccountId());
        List<SimplePeerQuoteVo> datas = dao.queryPriceTrend(vo);
        List<SimplePeerQuoteVo> costs = dao.queryCostPriceTrend(vo);
        datas.addAll(costs);

        Map<String, List<SimplePeerQuoteVo>> supplierMap = Maps.newLinkedHashMap();
        for(SimplePeerQuoteVo svo:datas){
            if(Strings.isNullOrEmpty(svo.getSupplier()))continue;
            String key = svo.getSupplier().trim();
            if(supplierMap.containsKey(key)){
                List<SimplePeerQuoteVo> existData = supplierMap.get(key);
                existData.add(svo);
            }else{
                List<SimplePeerQuoteVo> existData = Lists.newArrayList();
                existData.add(svo);
                supplierMap.put(key, existData);
            }
        }
        List<LineDataVo> lines = Lists.newArrayList();
        String xAxis[] = null;
        for(String keyIter:supplierMap.keySet()){
            List<SimplePeerQuoteVo> simpleVos = supplierMap.get(keyIter);
            Map<String, BigDecimal> fillDatas = autoFillData(vo.getStartDay(), vo.getEndDay(), simpleVos);
            LineDataVo lineDataVo = new LineDataVo();
            lineDataVo.setName(keyIter);
            BigDecimal bdata[] = {};
            lineDataVo.setData(fillDatas.values().toArray(bdata));
            lines.add(lineDataVo);
            String xAxisDatas[] = {};
            if(xAxis==null)xAxis = fillDatas.keySet().toArray(xAxisDatas);
        }


        LineChartVo chartVo = new LineChartVo();
        chartVo.setCharTitle("同行报价分析");
        String lineTitles[] = {};
        chartVo.setLineTitle(supplierMap.keySet().toArray(lineTitles));
        chartVo.setLineDataVoList(lines);
        chartVo.setXAxis(xAxis);
        return chartVo;
    }

    /**
     * 按时间填充每一天数据
     * @param startDay
     * @param endDay
     * @param data
     */
    private Map<String, BigDecimal> autoFillData(Date startDay, Date endDay, List<SimplePeerQuoteVo> data){
        Map<String, BigDecimal> daydata = Maps.newLinkedHashMap();

        Date iter = DateUtilTools.changeDateTime(startDay, 0, 0, 0, 0,0);
        endDay = DateUtilTools.changeDateTime(endDay, 0, 0, 0, 0,0);

        SimplePeerQuoteVo first = data.get(0);
        BigDecimal lastVal = NumberUtil.multiply(first.getUnitScale(), first.getDeliveryPrice());
        int i = 0;

        do{
            while(i<data.size()){
                SimplePeerQuoteVo simple = data.get(i);
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
}
