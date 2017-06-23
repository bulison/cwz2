package cn.fooltech.fool_ops.web.voucher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.domain.voucher.entity.BillSubjectDetail;
import cn.fooltech.fool_ops.domain.voucher.service.BillSubjectDetailService;
import cn.fooltech.fool_ops.domain.voucher.vo.BillSubjectDetailVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>单据关联会计科目模板明细网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月17日
 */
@Controller
@RequestMapping("/billSubjectDetail")
public class BillSubjectDetailController extends BaseController{
	
	@Autowired
	private BillSubjectDetailService detailService;
	
	/**
	 * 获取模板明细
	 * @param templateId 模板ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getByTemplateId")
	public List<BillSubjectDetailVo> getByTemplateId(String templateId){
		List<BillSubjectDetail> datas = detailService.getByTemplateId(templateId);
		return detailService.getVos(datas);
	}
	
}
