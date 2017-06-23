package cn.fooltech.fool_ops.eureka.rateService.web;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.eureka.rateService.dao.GoodsProfitDao;
import cn.fooltech.fool_ops.eureka.rateService.dao.GoodsProfitDetailDao;
import cn.fooltech.fool_ops.eureka.rateService.utils.DateUtilTools;
import cn.fooltech.fool_ops.eureka.rateService.vo.GoodsProfitDetailVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.GoodsProfitVo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/goodsProfitAnalysis")
public class GoodsProfitAnalysis {
    @Autowired
    private GoodsProfitDao goodsProfitDao;
    @Autowired
    GoodsProfitDetailDao detailDao;

    /**
     * 查询货品利润分析
     * @param startDate
     * @param endDate
     * @param goodsId
     * @param specId
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallBack")
    @GetMapping("/query")
    public PageJson query(String accId, String startDate, String endDate, String goodsId, String specId,Integer page,Integer rows){
        List<GoodsProfitVo> goodsProfits=goodsProfitDao.findProfitAnalysis(accId,DateUtilTools.string2Date(startDate), DateUtilTools.string2Date(endDate),goodsId,specId,page,rows);
        PageJson pageJson=new PageJson();
        pageJson.setTotal(new Long(goodsProfits.size()));
        pageJson.setRows(goodsProfits);
        pageJson.setResult(PageJson.ERROR_CODE_SUCCESS);
        return pageJson;
    }

    /**
     * 查询货品利润分析详细表
     * @param startDate
     * @param endDate
     * @param goodsId
     * @param specId
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallBack")
    @GetMapping("/queryDetail")
    public PageJson queryDetail(String accId, String startDate, String endDate, String goodsId, String specId,Integer page,Integer rows){
        List<GoodsProfitDetailVo> goodsProfits=detailDao.findProfitAnalysisDetail(accId,DateUtilTools.string2Date(startDate), DateUtilTools.string2Date(endDate),goodsId,specId,page,rows);
        PageJson pageJson=new PageJson();
        pageJson.setTotal(new Long(goodsProfits.size()));
        pageJson.setRows(goodsProfits);
        pageJson.setResult(PageJson.ERROR_CODE_SUCCESS);
        return pageJson;
    }

    /**
     * 失败后的短路回调函数
     * @return
     */
    public PageJson fallBack(){
        PageJson pageJson = new PageJson(PageJson.ERROR_CODE_FAIL);
        return pageJson;
    }
}
