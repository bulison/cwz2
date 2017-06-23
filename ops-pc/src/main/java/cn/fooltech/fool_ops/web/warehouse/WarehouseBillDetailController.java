package cn.fooltech.fool_ops.web.warehouse;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.common.entity.ResultObject;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillDetailService;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * <p>仓库单据记录明细网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月22日
 */
@Controller
@RequestMapping("/billdetail")
public class WarehouseBillDetailController extends BaseController {

	/**
	 * 仓库单据记录明细网页服务类
	 */
	@Autowired
	private WarehouseBillDetailService detailWebService;

	
	/**
	 * 单据明细信息
	 * @param id 仓库单据明细ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(String id, Model model){
		WarehouseBillDetailVo billDetail = detailWebService.getById(id);
		model.addAttribute("billDetail", billDetail);
		return "/warehourse/billdetail/detail";
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
//	@RequestMapping("/save")
//	@ResponseBody
//	public RequestResult save(WarehouseBillDetailVo vo){
//		return detailWebService.save(vo);
//	}
	
	/**
	 * 删除
	 * @param id 仓库单据明细ID
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id){
		return detailWebService.delete(id);
	}
	
	/**
	 * 获取单据明细
	 * @param billId 仓库单据ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getBillDetails")
	public List<WarehouseBillDetailVo> getBillDetailVos(String billId){
		return detailWebService.getBillDetailVos(billId);
	}
	/**
	 * 获取用户销售提成明细
	 * @param memberId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getPercentageDetails")
	public ResultObject getPercentageDetails(String memberId,PageParamater pageParamater){
		return detailWebService.getPercentageDetails(memberId,pageParamater);
	}
}
