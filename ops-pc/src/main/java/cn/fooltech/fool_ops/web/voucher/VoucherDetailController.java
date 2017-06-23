package cn.fooltech.fool_ops.web.voucher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherDetailService;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherDetailVo;


/**
 * <p>凭证明细网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2015年11月23日
 */
@Controller
@RequestMapping("/voucherdetail")
public class VoucherDetailController {
	
	/**
	 * 凭证明细网页服务类
	 */
	@Autowired
	private VoucherDetailService detailService;
	
	/**
	 * 获取凭证明细
	 * @param voucherId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDetails")
	public List<VoucherDetailVo> getDetails(String voucherId){
		return detailService.getByVoucher(voucherId);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(VoucherDetailVo vo){
		return detailService.save(vo);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String id){
		return detailService.delete(id);
	}
	
}
