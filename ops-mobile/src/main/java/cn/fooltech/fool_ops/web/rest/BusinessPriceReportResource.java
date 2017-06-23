package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBilldetailVo;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.common.vo.Base64Img;
import cn.fooltech.fool_ops.domain.report.dao.BusinessPriceReportDao;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
@RequestMapping("/api/businessPriceReport")
public class BusinessPriceReportResource extends AbstractBaseResource {

    @Autowired
    private BusinessPriceReportDao dao;
    
    @Autowired
    private AttachService attachService;

    @ApiOperation("查询业务报价报表")
    @GetMapping("/queryReport")
    public ResponseEntity queryReport(@RequestParam String customerId, String receiptPlaceId,
                                      String goodsId, PageParamater page){

        Page ph = PageHelper.startPage(page.getPage(), page.getRows());
        String accId= SecurityUtil.getFiscalAccountId();
        List<CostAnalysisBillVo> datas = dao.queryBusinessPriceReport(accId, customerId, receiptPlaceId, goodsId);

        PageRequest pageRequest = getPageRequest(page);
        return pageReponse(datas, pageRequest, ph.getTotal());
    }

    @ApiOperation("根据主表ID查询业务报价明细记录")
    @GetMapping("/queryDetails")
    public ResponseEntity queryDetails(@RequestParam String billId){
        List<CostAnalysisBilldetailVo> datas = dao.queryBusinessPriceDetail(billId);
        return listReponse(datas);
    }
    
    @ApiOperation("根据主表ID查询业务报价附件")
    @GetMapping("/queryAttach")
    public ResponseEntity queryAttach(@RequestParam String busId ){
    	List<Base64Img> imgs = attachService.getImgs(busId);
    	return listReponse(imgs);
    }
}
