package cn.fooltech.fool_ops.eureka.rateService.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Splitter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.eureka.rateService.service.CustomerIncomeAnalysisService;
import cn.fooltech.fool_ops.eureka.rateService.vo.CustomerIncomeAnalysisVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.WarehouseBillTemVo;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/customerIncomeAnalysisResource")
public class CustomerIncomeAnalysisResource {
	@Autowired
	private CustomerIncomeAnalysisService service;
	/**
	 * 客户收益分析
	 * @param endDate
	 * @param startDate
	 * @param orgId
	 * @param accId
	 * @param page
	 * @param rows
	 * @return
	 */
	@ApiOperation("客户收益率分析")
	@GetMapping("/customerIncomeAnalysisProcessing")
	@HystrixCommand(fallbackMethod = "fallBack")
	public PageJson customerIncomeAnalysisProcessing(String endDate,String startDate,String orgId,String accId,String customerId,String category,String area,Integer page,Integer rows){
		PageRequest pageRequest = new PageRequest(page-1, rows);
		List<CustomerIncomeAnalysisVo> list=service.customerIncomeAnalysisProcessing(endDate, startDate, orgId, accId,customerId,category,area,page,rows);
		PageImpl<CustomerIncomeAnalysisVo> ph=new PageImpl<CustomerIncomeAnalysisVo>(list,pageRequest,list.size());
		
		return new PageJson(ph);
	}
	@ApiOperation("客户收益率明细")
	@GetMapping("/customerIncomeAnalysisDetail")
	@HystrixCommand(fallbackMethod = "fallBack")
	public PageJson customerIncomeAnalysisDetail(String customerId,String orgId,String accId,String startDate,String endDate,Integer page,Integer rows){
		PageRequest pageRequest = new PageRequest(page-1, rows);
		Page ph1 = PageHelper.startPage(page, rows);
		List<WarehouseBillTemVo> list=service.customerIncomeAnalysisDetail(customerId,orgId,accId,startDate,endDate);
		PageImpl<WarehouseBillTemVo> ph=new PageImpl<WarehouseBillTemVo>(list,pageRequest,ph1.size());
		return new PageJson(ph);
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
