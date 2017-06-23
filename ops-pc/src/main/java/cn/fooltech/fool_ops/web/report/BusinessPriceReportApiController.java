package cn.fooltech.fool_ops.web.report;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import cn.fooltech.fool_ops.domain.basedata.vo.PeerQuoteVo;
import cn.fooltech.fool_ops.domain.report.dao.BusinessPriceReportDao;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xjh on 2017/1/11.
 */
@RestController
@RequestMapping("/api/businessPriceReport")
public class BusinessPriceReportApiController {

    @Autowired
    private BusinessPriceReportDao dao;

    @ApiOperation("查询业务报价报表")
    @GetMapping("/queryReport")
    public PageJson queryReport(@RequestParam String customerId, String receiptPlaceId,
                                String goodsId, PageParamater page){

        Page pageHelper = PageHelper.startPage(page.getPage(), page.getRows());
        PageJson pageJson = new PageJson();
        String accId= SecurityUtil.getFiscalAccountId();
        List<CostAnalysisBillVo> datas = dao.queryBusinessPriceReport(accId, customerId, receiptPlaceId, goodsId);
        pageJson.setTotal(pageHelper.getTotal());
        pageJson.setRows(datas);

        return pageJson;
    }
}
