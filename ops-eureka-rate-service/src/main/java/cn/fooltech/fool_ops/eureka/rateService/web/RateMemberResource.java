package cn.fooltech.fool_ops.eureka.rateService.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Splitter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.eureka.rateService.dao.RateMemberDao;
import cn.fooltech.fool_ops.eureka.rateService.vo.RateMemberDetailVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.RateMemberSumVo;
import io.swagger.annotations.ApiOperation;

/**
 * 执行人效益controller
 * @author hjr
 * 2017-3-27
 */
@RestController
@RequestMapping("/api/RateMemberResource")
public class RateMemberResource {
	@Autowired
	private RateMemberDao dao;
	@ApiOperation("查询某个企业下所有员工的总效率")
	@GetMapping("/queryRateMemberSum")
	@HystrixCommand(fallbackMethod = "fallBack")
	public PageJson queryRateMemberSum(String orgId,String accId,String memberIds,String sidx,Integer page,Integer rows){
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		PageRequest pageRequest = new PageRequest(page-1, rows);
		Page ph1 = PageHelper.startPage(page, rows);

		if(memberIds==null||memberIds.equals("")){
			List<RateMemberSumVo> rateMemberSumVoList=dao.queryRateMemberSum(orgId,accId,memberIds,sidx);
			PageImpl<RateMemberSumVo> ph=new PageImpl<RateMemberSumVo>(rateMemberSumVoList,pageRequest,ph1.getTotal());
			 return new PageJson(ph);
		}else{
			List<String> memberIdList = splitter.splitToList(memberIds);
			List<RateMemberSumVo> rateMemberSumVoList=new ArrayList<RateMemberSumVo>();
			for(String memberId:memberIdList){
				rateMemberSumVoList.addAll(dao.queryRateMemberSum(orgId,accId,memberId,sidx));
			}
			PageImpl<RateMemberSumVo> ph=new PageImpl<RateMemberSumVo>(rateMemberSumVoList,pageRequest,ph1.getTotal());
			 return new PageJson(ph);
		}
	}
	/**
	 * 查找员工明细
	 */
	@ApiOperation("查询员工效率明细")
	@GetMapping("/queryRateMemberDetailByMemberId")
	@HystrixCommand(fallbackMethod = "fallBack")
	public PageJson queryRateMemberDetail(String orgId,String accId,String memberId,Integer page,Integer rows,String startDay,String endDay){
		Page ph1 = PageHelper.startPage(page, rows);
		PageRequest pageRequest = new PageRequest(page-1, rows);
		List<RateMemberDetailVo> list =dao.queryRateMemberDetailByMemberId(orgId,accId,memberId,startDay,endDay);
		PageImpl<RateMemberDetailVo> ph=new PageImpl<RateMemberDetailVo>(list,pageRequest,ph1.getTotal());

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
