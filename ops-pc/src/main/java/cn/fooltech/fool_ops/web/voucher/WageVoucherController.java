package cn.fooltech.fool_ops.web.voucher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.voucher.service.WageVoucherService;
import cn.fooltech.fool_ops.domain.voucher.vo.WageVoucherVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>工资凭证网页控制器，用于工资生成凭证</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月12日
 */
@Controller
@RequestMapping("/wageVoucher")
public class WageVoucherController extends BaseController{
	
	@Autowired
	private WageVoucherService wageVoucherService;
	
	/**
	 * 列表查询
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(PageParamater paramater){
		Page<WageVoucherVo> page = wageVoucherService.query(paramater);
		return new PageJson(page);
	}
	
	/**
	 * 新增
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(WageVoucherVo vo){
		return wageVoucherService.save(vo);
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid){
		return wageVoucherService.delete(fid);
	}
		
	/**
	 * 删除全部
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteAll")
	public RequestResult deleteAll(){
		return wageVoucherService.deleteAll();
	}
	
}
