package cn.fooltech.fool_ops.web.voucher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubject;
import cn.fooltech.fool_ops.domain.voucher.service.BillSubjectService;
import cn.fooltech.fool_ops.domain.voucher.vo.BillSubjectVo;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>单据、会计科目关联模板网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2015年12月2日
 */
@Controller
@RequestMapping("/billsubject")
public class BillSubjectController extends BaseController{
	
	/**
	 * 单据、会计科目关联网页服务类
	 */
	@Autowired
	private BillSubjectService billSubjectService;
	
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(BillSubjectVo vo, PageParamater paramater){
		Page<BillSubjectVo> page = billSubjectService.query(vo, paramater);
		return new PageJson(page);
	}
	
	/**
	 * 获取模板信息
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getById")
	public BillSubjectVo getById(String fid){
		BillSubject entity = billSubjectService.get(fid);
		return billSubjectService.getVo(entity, true);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(BillSubjectVo vo){
		return billSubjectService.save(vo);
	}
	
	/**
	 * 删除
	 * @param id 模板ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid){
		return billSubjectService.delete(fid);
	}
	
}
