
package cn.fooltech.fool_ops.web.basedata;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.TransportPriceDetail1Service;
import cn.fooltech.fool_ops.domain.basedata.service.TransportPriceDetail2Service;
import cn.fooltech.fool_ops.domain.basedata.service.TransportPriceService;
import cn.fooltech.fool_ops.domain.basedata.vo.SimplePeerQuoteVo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail1Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail2Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceVo;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.common.vo.Base64Img;
import cn.fooltech.fool_ops.domain.line.LineChartVo;
import cn.fooltech.fool_ops.domain.line.LineDataVo;
import cn.fooltech.fool_ops.domain.transport.service.GoodsTransportService;
import cn.fooltech.fool_ops.domain.transport.vo.GoodsTransportVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.web.base.BaseController;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 运输费报价网页控制器类
 * </p>
 * 
 * @author cwz
 * @date 2016-12-30
 */
@Controller
@RequestMapping("/transportPrice")
public class TransportPriceController extends BaseController {
	private Logger logger = Logger.getLogger(TransportPriceController.class);
	/**
	 * 运输费报价服务类
	 */
	@Autowired
	private TransportPriceService priceService;
	
	/**
	 * 运输费报价(从1表)服务类
	 */
	@Autowired
	private TransportPriceDetail1Service detail1Service;
	
    @Autowired
    private AttachService attachService;

	/**
	 * 运输费报价管理页面
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/report/shippingQuote/manage";
	}
	/**
	 * 运输费报价明细管理页面
	 */
	@RequestMapping(value = "/detal")
	public String manage2(){
		return "/report/shippingQuote/detal";
	}
	/**
	 * 运输费报价曲线图
	 * @return
	 */
	@RequestMapping(value = "/chart")
	public String chart(TransportPriceVo vo, PageParamater pageParamater, Model model){
		return "/report/shippingQuote/chart";
	}
	@RequestMapping(value="/queryLineData")
	@PostMapping
	@ResponseBody
    public LineChartVo queryLineData(TransportPriceVo vo){
		PageParamater pageParamater = new PageParamater(1, Integer.MAX_VALUE, 0);
        List<LineDataVo> lines = Lists.newArrayList();
        vo.setDayEnable(1);
        Sort sort = new Sort(Sort.Direction.ASC, "billDate");
		Page<TransportPriceVo> query = priceService.queryByReport(vo,pageParamater,sort);
		List<TransportPriceVo> list = query.getContent();
		List<String> billDates = Lists.newArrayList();
		Map<String, List<TransportPriceVo>> supplierMap = Maps.newLinkedHashMap();
//		Map<String, String> billDatesMap = Maps.newLinkedHashMap();

        for (TransportPriceVo vos : list) {
        	//添加横坐标数据
        	String billDate = vos.getBillDate();
			billDates.add(billDate);
			String key = vos.getSupplierName();
            if(supplierMap.containsKey(key)){
                List<TransportPriceVo> existData = supplierMap.get(key);
                existData.add(vos);
            }else{
                List<TransportPriceVo> existData = Lists.newArrayList();
                existData.add(vos);
                supplierMap.put(key, existData);
            }
		}
//        //去除横坐标重复数据
//        for (String string : billDates) {
//        	 if(!billDatesMap.containsKey(string)){
//        		 billDatesMap.put(string, string);
//        	 }
//		}
        String xAxis[] = null;
        for(String keyIter:supplierMap.keySet()){
            List<TransportPriceVo> simpleVos = supplierMap.get(keyIter);
            Date startDay = DateUtils.getDateFromString(vo.getStartDay())==null?new Date():DateUtils.getDateFromString(vo.getStartDay());
            Date endDay = DateUtils.getDateFromString(vo.getEndDay())==null?new Date():DateUtils.getDateFromString(vo.getEndDay());
            Map<String, BigDecimal> fillDatas = autoFillData(startDay,endDay,simpleVos);
            LineDataVo lineDataVo = new LineDataVo();
            lineDataVo.setName(keyIter);
            BigDecimal bdata[] = {};
            lineDataVo.setData(fillDatas.values().toArray(bdata));
            lines.add(lineDataVo);
            String xAxisDatas[] = {};
            if(xAxis==null)xAxis = fillDatas.keySet().toArray(xAxisDatas);
        }
        LineChartVo chartVo = new LineChartVo();
        chartVo.setCharTitle("运输报价分析");
        String lineTitles[] = {};
        String[] LineTitle = supplierMap.keySet().toArray(lineTitles);
        chartVo.setLineTitle(LineTitle);
        chartVo.setLineDataVoList(lines);
        chartVo.setXAxis(xAxis);
        return chartVo;
    }

	private Map<String, BigDecimal> autoFillData(List<TransportPriceVo> data) {
		Map<String, BigDecimal> daydata = Maps.newLinkedHashMap();
		int i = 0;
		while (i < data.size()) {
			TransportPriceVo simple = data.get(i);
			BigDecimal amount = simple.getAmount();
			String dateStr = simple.getBillDate();
			daydata.put(dateStr, amount);
			i++;
		}
		return daydata;
	}
    /**
     * 按时间填充每一天数据
     * @param startDay
     * @param endDay
     * @param data
     */
    private Map<String, BigDecimal> autoFillData(Date startDay, Date endDay, List<TransportPriceVo> data){
        Map<String, BigDecimal> daydata = Maps.newLinkedHashMap();

        Date iter = DateUtilTools.changeDateTime(startDay, 0, 0, 0, 0,0);
        endDay = DateUtilTools.changeDateTime(endDay, 0, 0, 0, 0,0);

        TransportPriceVo first = data.get(0);
        BigDecimal lastVal = first.getAmount();
        int i = 0;

        do{
            while(i<data.size()){
            	TransportPriceVo simple = data.get(i);
                Date sdate = DateUtils.getDateFromString(simple.getBillDate());
                sdate = DateUtilTools.changeDateTime(sdate, 0, 0, 0, 0,0);
                if(sdate.compareTo(iter)>0){
                    break;
                }else if(sdate.compareTo(iter)==0){
                    lastVal = simple.getAmount();
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
	/**
	 * 运输费报价表信息
	 * @param vo
	 * @param pageParamater
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public @ResponseBody PageJson list(TransportPriceVo vo, PageParamater pageParamater, Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, "billDate");
		Page<TransportPriceVo> query = priceService.queryByReport(vo, pageParamater,sort);
		return priceService.getPageJson(query.getContent(), query.getTotalElements());
	}
	@RequestMapping("/queryByDetai1")
	public @ResponseBody PageJson queryByDetai1(String billId, PageParamater pageParamater, Model model) {
//		Page<TransportPriceVo> query = priceService.queryByReport(vo, pageParamater);
//		return priceService.getPageJson(query.getContent(), query.getTotalElements());
		List<TransportPriceDetail1Vo> list = detail1Service.query(billId, pageParamater);
		PageJson pageJson = detail1Service.getPageJson(list, list.size());
		return pageJson;
	}

	/**
	 * 新增、编辑
	 * 
	 * @param vo
	 */

	@RequestMapping("/save")
	public RequestResult save(TransportPriceVo vo) {
		return priceService.save(vo);
	}

	/**
	 * 删除运输费报价模板信息
	 * 
	 * @param vo
	 * @return
	 */
	
	@RequestMapping("/delete")
	public RequestResult delete(String id) {
		return priceService.delete(id);
	}
	/**
	 * 查询运输公司在有效期内的报价记录
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表	
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @param supplierId		承运id,关联供应商
	 * @param transportUnitId	运输单位id(关联辅助属性运输计价单位)
	 * @return
	 */
	@RequestMapping("/findByCompany")
	@ResponseBody
	public TransportPriceVo findByCompany(String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId, String supplierId,String priceUnitId,String transportUnitId){
		TransportPriceVo vo = priceService.findByCompany(deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId,supplierId,priceUnitId,transportUnitId);
		return vo;
	}
	/**
	 * 导出
	 * @param vo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/export")
	public void export(TransportPriceVo vo,HttpServletResponse response) {
		priceService.export(vo, response);
	}
	
//    @ApiOperation("根据主表ID查询业务报价附件")
    @RequestMapping("/queryAttach")
    public List<Base64Img> queryAttach(@RequestParam String busId ){
    	List<Base64Img> imgs = attachService.getImgs(busId);
    	return imgs;
    }
    /**
     * 获取基本运费
     * @param billId			运输报价ID
	 * @param goodsId			货品ID
	 * @param goodSpecId		货品属性ID
	 * @param transportUnitId	运输计价单位ID
	 * @param shipmentTypeId	装运方式ID
	 * @return
     */
	@RequestMapping("/queryByAmount")
	@ResponseBody
    public BigDecimal queryByAmount(String billId,String goodsId,String goodSpecId,String transportUnitId, String shipmentTypeId){
		BigDecimal amount = priceService.queryByAmount(billId, goodsId, goodSpecId, transportUnitId, shipmentTypeId);
		return amount;
	}
	
}
