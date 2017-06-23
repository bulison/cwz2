package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportLoss;
import cn.fooltech.fool_ops.domain.basedata.service.TransportLossService;
import cn.fooltech.fool_ops.domain.basedata.service.TransportTemplateService;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportLossVo;
/**
 * 
 * @author hjr
 * @date 2017/3/14
 */
@Controller
@RequestMapping("/transportLoss")
public class TransportLossController {
	@Autowired
	TransportLossService transportLossService;
	
	
	/**
	 * 损耗地址表信息(JSON)
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(TransportLossVo vo,PageParamater pageParamater){
		Page<TransportLossVo> page=transportLossService.query(vo, pageParamater);
		return new PageJson(page);
	}

	/**
	 * 新增/编辑损耗地址
	 */
	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(TransportLossVo vo){
		return transportLossService.save(vo);
	}
	/**
	 * 删除损耗地址
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id){
		
		return transportLossService.delete(id);
	}
	/**
	 * 弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/basedata/transportLoss/manage";
	}
/*	*//**
	 * 修复旧数据(没有损耗地址的数据)
	 * 不需要使用这个接口,只在某些特定情况下使用
	 *//*
	 @GetMapping("repair")
	    public void repair(){
		 transportLossService.repair();
	 }*/
	
}
