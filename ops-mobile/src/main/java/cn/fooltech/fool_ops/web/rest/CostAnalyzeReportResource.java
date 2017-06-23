package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.analysis.service.CostAnalysisBillService;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBilldetailVo;
import cn.fooltech.fool_ops.domain.analysis.vo.TransportSeperateDetailVo;
import cn.fooltech.fool_ops.domain.basedata.service.TodayCostAnalysisService;
import cn.fooltech.fool_ops.domain.report.dao.CostAnalyzeReportDao;
import cn.fooltech.fool_ops.domain.transport.service.GoodsTransportService;
import cn.fooltech.fool_ops.domain.transport.vo.GoodsTransportVo;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xjh on 2017/1/11.
 */
@RestController
@RequestMapping("/api/costAnalyzeReport")
public class CostAnalyzeReportResource extends AbstractBaseResource {

    private static final String NameSpace = "costAnalyzeReport";

    @Autowired
    private CostAnalyzeReportDao dao;

    @Autowired
    private CostAnalysisBillService service;

    @Autowired
    private GoodsTransportService goodsTransportService;

    @Autowired
    public TodayCostAnalysisService todayCostAnalysisService;

    @ApiOperation("查询成本分析报表")
    @GetMapping("/queryReport")
    public ResponseEntity queryReport(@RequestParam String customerId, String receiptPlaceId,
                                      String goodsId, PageParamater page){
        Page ph = PageHelper.startPage(page.getPage(), page.getRows());
        String accId= SecurityUtil.getFiscalAccountId();

        List<CostAnalysisBillVo> datas = dao.queryCostAnalyzeReport(accId, receiptPlaceId, customerId, goodsId);

        PageRequest pageRequest = getPageRequest(page);
        return pageReponse(datas, pageRequest, ph.getTotal());
    }

    @ApiOperation("根据主表ID查询成本分析报表明细记录")
    @GetMapping("/queryDetails")
    public ResponseEntity queryDetails(@RequestParam String billId){

        List<CostAnalysisBilldetailVo> datas = dao.queryCostAnalyzeDetail(billId);
        return listReponse(datas);
    }

    @ApiOperation("查询成本分析表记录及汇总的费用")
    @GetMapping("/queryAllFee")
    public ResponseEntity queryAllFee(CostAnalysisBillVo vo, PageParamater paramater){

        if(Strings.isNullOrEmpty(vo.getBillDate())){
            vo.setBillDate(DateUtils.getCurrentDate());
        }
        //查询成本分析表
        org.springframework.data.domain.Page<CostAnalysisBillVo> page = service.query(vo,paramater);

        List<CostAnalysisBillVo> list = page.getContent();
        RequestResult result = service.queryFeeReport(list);
        return reponse(result);
    }

    @ApiOperation("根据收货地查找根据中转站分列显示")
    @GetMapping("/querySeparateFee")
    public ResponseEntity querySeparateCostAnalyzeBill(CostAnalysisBillVo vo, PageParamater paramater) {
        PageJson page = service.querySeparateCostAnalyzeBill(vo, paramater);
        return ResponseEntity.ok().body(page);
    }

    @ApiOperation("根据根据运输费报价单查询费用明细")
    @GetMapping("/querySeparateDetail")
    public ResponseEntity querySeparateDetail(@RequestParam String transportIds) {
        if(Strings.isNullOrEmpty(transportIds)) return ResponseEntity.badRequest().body("");
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();

        List<String> transportIdList = splitter.splitToList(transportIds);
        if(transportIdList.size()==0)return ResponseEntity.badRequest().body("");

        List<List<TransportSeperateDetailVo>> datas = service.querySeparateDetail(transportIdList);
        return ResponseEntity.ok().body(datas);
    }

    @ApiOperation("根据货品ID+货品属性ID查询换算关系不为1的记录")
    @GetMapping("/queryConversionRate")
    public ResponseEntity querySeparateDetail(String goodsId, String specId) {
        List<GoodsTransportVo> datas = goodsTransportService.findByGoods(goodsId, specId);
        return ResponseEntity.ok().body(datas);
    }

    @ApiOperation("查询中转港口到客户收货地的路线成本分析")
    @GetMapping("/queryCustomerRouteAnalyze")
    public ResponseEntity queryCustomerRouteAnalyze(String billIds, String referAddressIds,
                                                    String customerId, String date) {
        RequestResult result = service.queryCustomerRouteAnalyze(billIds, referAddressIds, customerId, date);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation("重新生成当天成本分析")
    @GetMapping("/todayCostAnalysis")
    public ResponseEntity todayCostAnalysis(){
        todayCostAnalysisService.genRoute();

        String accId = SecurityUtil.getFiscalAccountId();
        todayCostAnalysisService.analysis(accId);
        return ResponseEntity.ok().body(RequestResult.ok());
    }
}
