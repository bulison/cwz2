package cn.fooltech.fool_ops.web.rate;

/**
 * Created by xjh on 2017/3/22.
 */

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.rate.repository.RateMemberRepository;
import cn.fooltech.fool_ops.domain.rate.service.LoanRateService;
import cn.fooltech.fool_ops.domain.rate.service.RateMemberService;
import cn.fooltech.fool_ops.utils.SecurityUtil;

import java.util.Date;

import cn.fooltech.fool_ops.utils.SecurityUtil;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Strings;

/**
 * restful  收益率Controller
 */
@RestController
@RequestMapping(value = "/api/rate")
public class RateApiController {

    private final Logger logger = Logger.getLogger(getClass());

    @Value("${eureka.api-gateway.rateService.defaultZone}")
    private String eurekaGateway;

    @Autowired
    private RestTemplate restTemplate;
	 @Autowired
	 private RateMemberService  repository;

    @Autowired
    private LoanRateService loanRateService;

    /**
     * 查询某个企业下所有员工的总效率
     * @param memberIds
     * @param sidx
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/queryRateMemberSum")
    public PageJson queryRateMemberSum(String memberIds,String sidx,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "rows", defaultValue = "10")Integer rows){
    	
    	repository.rateMemberDataPreprocess();
    	String orgId=SecurityUtil.getCurrentOrgId();
    	String accId=SecurityUtil.getFiscalAccountId();
        String url = eurekaGateway + "RateMemberResource/queryRateMemberSum";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("orgId", orgId)
                .queryParam("accId", accId)
                .queryParam("memberIds", memberIds)
                .queryParam("sidx", sidx)
                .queryParam("page", page)
                .queryParam("rows", rows);
        return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();

    }
    /**
     * 查询员工效率明细
     * @param memberId
     * @param page
     * @param rows
     * @param startDay
     * @param endDay
     * @return
     */
    @GetMapping(value = "/queryRateMemberDetailByMemberId")
    public PageJson queryRateMemberByMemberId(String memberId,
    		@RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "rows", defaultValue = "10")Integer rows,           
            String startDay,
           String endDay) {
    	String orgId=SecurityUtil.getCurrentOrgId();
    	String accId=SecurityUtil.getFiscalAccountId();
        String url = eurekaGateway + "RateMemberResource/queryRateMemberDetailByMemberId";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("orgId", orgId)
                .queryParam("accId", accId)
                .queryParam("memberId", memberId)
                .queryParam("page",page)
                .queryParam("rows", rows)
                .queryParam("startDay", startDay)
                .queryParam("endDay", endDay);

        return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();

    }

    /**
     * 货品利润分析
     * @param startDate
     * @param endDate
     * @param goodsId
     * @param specId
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/goodsProfitAnalysis")
    public PageJson goodsProfitAnalysis(String startDate, String endDate, String goodsId, String specId,
                                     @RequestParam(name = "page", defaultValue = "1") Integer page,
                                     @RequestParam(name = "rows", defaultValue = "10")Integer rows) {

        String url = eurekaGateway + "goodsProfitAnalysis/query";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("accId", SecurityUtil.getFiscalAccountId())
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("goodsId", goodsId)
                .queryParam("specId", specId)
                .queryParam("page", page)
                .queryParam("rows", rows);
        return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();
    }

    /**
     * 货品利润明细分析
     * @param startDate
     * @param endDate
     * @param goodsId
     * @param specId
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/goodsProfitAnalysisDetail")
    public PageJson goodsProfitAnalysisDetail(String startDate, String endDate, String goodsId, String specId,
                                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                                        @RequestParam(name = "rows", defaultValue = "10")Integer rows) {

        String url = eurekaGateway + "goodsProfitAnalysis/queryDetail";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("accId", SecurityUtil.getFiscalAccountId())
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("goodsId", goodsId)
                .queryParam("specId", specId)
                .queryParam("page", page)
                .queryParam("rows", rows);
        return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();
    }

    /**
     * 销售订单分析
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/saleOrder/orderAnalyze")
    public PageJson saleOrderAnalyze(String startDate, String endDate, String customerId,
                                     String saleId, String saleCode,
                                     @RequestParam(name = "page", defaultValue = "1") Integer page,
                                     @RequestParam(name = "rows", defaultValue = "10")Integer rows) {

        String url = eurekaGateway + "saleOrder/orderAnalyze";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("customerId", customerId)
                .queryParam("saleId", saleId)
                .queryParam("saleCode", saleCode)
                .queryParam("accId", SecurityUtil.getFiscalAccountId())
                .queryParam("page", page)
                .queryParam("rows", rows);

        //System.out.println("url:"+builder.build().encode().toUri());

        return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();
    }

    /**
     * 销售订单明细分析
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param goodsId
     * @param goodsSpecId
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/saleOrder/detailAnalyze")
    public PageJson saleOrderDetailAnalyze(String startDate, String endDate, String customerId,
                                           String saleId, String saleCode, String goodsId,
                                           String goodsSpecId,
                                           @RequestParam(name = "page", defaultValue = "1") Integer page,
                                           @RequestParam(name = "rows", defaultValue = "10")Integer rows) {

        String url = eurekaGateway + "saleOrder/detailAnalyze";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("customerId", customerId)
                .queryParam("saleId", saleId)
                .queryParam("saleCode", saleCode)
                .queryParam("goodsId", goodsId)
                .queryParam("specId", goodsSpecId)
                .queryParam("accId", SecurityUtil.getFiscalAccountId())
                .queryParam("page", page)
                .queryParam("rows", rows);

        return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();
    }
    /**
     * 客户收益分析
     * @param endDate
     * @param startDate
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/customerIncomeAnalysisProcessing")
    public PageJson customerIncomeAnalysisProcessing(String endDate,String startDate,String customerId,String category,String area,
    												@RequestParam(name = "page", defaultValue = "1") Integer page,
    												@RequestParam(name = "rows", defaultValue = "10")Integer rows){
    	
    	String url = eurekaGateway + "customerIncomeAnalysisResource/customerIncomeAnalysisProcessing";
    	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
    			.queryParam("endDate", endDate)
    			.queryParam("startDate", startDate)
    			.queryParam("orgId", SecurityUtil.getCurrentOrgId())
    			.queryParam("accId", SecurityUtil.getFiscalAccountId())
    			.queryParam("customerId", customerId)
    			.queryParam("category", category)
    			.queryParam("area", area)
    			.queryParam("page", page)
    			.queryParam("rows", rows);
    	return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();
    }
    /**
     * 客户收益明细
     * @param customerId
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/customerIncomeAnalysisDetail")
    public PageJson customerIncomeAnalysisDetail(String customerId,String startDate,String endDate,
    													@RequestParam(name = "page", defaultValue = "1") Integer page,
    													@RequestParam(name = "rows", defaultValue = "10")Integer rows){
    	String url = eurekaGateway + "customerIncomeAnalysisResource/customerIncomeAnalysisDetail";
    	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
    			.queryParam("customerId", customerId)
    			.queryParam("orgId", SecurityUtil.getCurrentOrgId())
    			.queryParam("accId", SecurityUtil.getFiscalAccountId())
    			.queryParam("startDate", startDate)
    			.queryParam("endDate", endDate)
    			.queryParam("page", page)
    			.queryParam("rows", rows);
    	return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();

    }

    /**
     * 销售出库单分析
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/saleOutAnalyze")
    public PageJson saleOutAnalyze(String startDate,
                                   String endDate,
                                   String customerId,
                                   String saleId,
                                   String saleCode,
                                   @RequestParam(name = "page", defaultValue = "1") Integer page,
                                   @RequestParam(name = "rows", defaultValue = "10")Integer rows){
        String url = eurekaGateway + "saleOut/saleOutAnalyze";

        String orgId = SecurityUtil.getCurrentOrgId();


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("customerId", customerId)
                .queryParam("saleId", saleId)
                .queryParam("saleCode", saleCode)
                .queryParam("dayFundLoseRate", loanRateService.getBankRate(orgId))
                .queryParam("orgId", orgId)
                .queryParam("accId", SecurityUtil.getFiscalAccountId())
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("page", page)
                .queryParam("rows", rows);
        return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();

    }


    /**
     * 销售出库单关联分析
     * @param saleOutId
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/saleOutRelation")
    public PageJson saleOutRelation(@RequestParam String saleOutId,
                                   @RequestParam(name = "page", defaultValue = "1") Integer page,
                                   @RequestParam(name = "rows", defaultValue = "10")Integer rows){
        String url = eurekaGateway + "saleOut/saleOutRelation";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("saleOutId", saleOutId)
                .queryParam("page", page)
                .queryParam("rows", rows);
        return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();

    }


    /**
     * 销售出库单分析
     * @param startDate
     * @param endDate
     * @param customerId
     * @param saleId
     * @param saleCode
     * @param page
     * @param rows
     * @return
     */
    @GetMapping(value = "/saleOutDetailAnalyze")
    public PageJson saleOutDetailAnalyze(String startDate,
                                   String endDate,
                                   String customerId,
                                   String saleId,
                                   String saleCode,
                                   String goodId,
                                   String goodSpecId,
                                   @RequestParam(name = "page", defaultValue = "1") Integer page,
                                   @RequestParam(name = "rows", defaultValue = "10")Integer rows){
        String url = eurekaGateway + "saleOut/saleOutDetailAnalyze";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("customerId", customerId)
                .queryParam("saleId", saleId)
                .queryParam("saleCode", saleCode)
                .queryParam("goodId", goodId)
                .queryParam("goodSpecId", goodSpecId)
                .queryParam("orgId", SecurityUtil.getCurrentOrgId())
                .queryParam("accId", SecurityUtil.getFiscalAccountId())
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("page", page)
                .queryParam("rows", rows);
        return restTemplate.getForEntity(builder.build().encode().toUri(), PageJson.class).getBody();

    }
}
