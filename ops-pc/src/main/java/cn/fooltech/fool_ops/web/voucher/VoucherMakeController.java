package cn.fooltech.fool_ops.web.voucher;

import java.util.List;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherMakeService;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherService;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>凭证制作网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2015年11月30日
 */
@Controller
@RequestMapping("/vouchermake")
public class VoucherMakeController extends BaseController{
	
	/**
	 * 凭证制作网页服务类
	 */
	@Autowired
	private VoucherMakeService voucherMakeService;
	
	/**
	 * 凭证服务类
	 */
	@Autowired
	private VoucherService voucherService;
	
	/**
	 * 制作凭证主界面
	 * @return
	 */
	@RequestMapping("/manage")
	public String manage(){
		return "/fiscal/makeVoucher/manage";
	}
	
	/**
	 * 科目设置界面
	 * @return
	 */
	@RequestMapping("/config")
	public String config(){
		return "/fiscal/makeVoucher/config";
	}
	
	/**
	 * 待摊费用生成凭证界面
	 * @return
	 */
	@RequestMapping("/prepaidvoucher")
	public String prepaidvoucher(){
		return "/fiscal/prepaidExpenses/create";
	}
	
	/**
	 * 固定资产生产凭证主界面
	 * @return
	 */
	@RequestMapping("/assetvoucher")
	public String assetvoucher(){
		return "/asset/assetVoucher/create";
	}
	/**
	 * 工资生成凭证主界面
	 * @return
	 */
	@RequestMapping("/create")
	public String creat(){
		return "/wage/makeVoucher/create";
	}
	
	/**
	 * 单据分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryBill")
	public PageJson queryBill(VoucherMakeVo vo, PageParamater paramater){
		Page<VoucherMakeVo> page = voucherMakeService.queryBill(vo, paramater);
		return new PageJson(page);
	}
	
	/**
	 * 查询所有单据
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryAllBill")
	public List<VoucherMakeVo> queryAllBill(VoucherMakeVo vo, PageParamater paramater){
		paramater.setRows(Integer.MAX_VALUE);
		Page<VoucherMakeVo> page = voucherMakeService.queryBill(vo, paramater);
		return page.getContent();
	}
	
	/**
	 * 获取单据明细
	 * @param billId 单据ID
	 * @param billType 单据类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getBillDetail")
	public List<WarehouseBillDetailVo> getBillDetail(String billId, int billType){
		return voucherMakeService.getBillDetail(billId, billType);
	}
	
	/**
	 * 制作凭证
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/makeVoucher")
	public RequestResult makeVoucher(VoucherMakeVo vo){
		RequestResult requestResult = voucherMakeService.makeVoucher(vo);
		if(requestResult.getReturnCode() == RequestResult.RETURN_SUCCESS){
			//合并凭证明细
			String voucherId = (String) requestResult.getData();
			if(!Strings.isNullOrEmpty(voucherId)){
				voucherService.mergeDetailBySubject(voucherId);
				voucherService.updateSumAmount(voucherId);
			}
		}
		return requestResult;
	}
	
}
