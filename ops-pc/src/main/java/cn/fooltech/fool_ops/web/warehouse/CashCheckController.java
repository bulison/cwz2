package cn.fooltech.fool_ops.web.warehouse;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.cashier.entity.BankCheckdate;
import cn.fooltech.fool_ops.domain.cashier.service.BankCheckdateService;
import cn.fooltech.fool_ops.domain.warehouse.entity.CashCheck;
import cn.fooltech.fool_ops.domain.warehouse.service.CashCheckService;
import cn.fooltech.fool_ops.domain.warehouse.vo.CashCheckVo;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>现金盘点单网页控制器基类</p>
 * @author cwz
 * @version 1.0
 * @date 2016年9月18日
 */
@Controller
@RequestMapping("/cashCheck")
public class CashCheckController extends BaseController{
	
	@Autowired
	private CashCheckService cashCheckService;
	
	@Autowired
	private BankCheckdateService bankCheckdateService;
	/**
	 * 现金盘点单分页信息
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(CashCheckVo vo, PageParamater paramater){
		Page<CashCheck> page = cashCheckService.query(vo, paramater);
		PageJson pageJson = new PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(cashCheckService.getVos(page.getContent()));
		return pageJson;
		
	}
	
	/**
	 * 新增/编辑 现金盘点单
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(CashCheckVo cashCheckVo){
		return cashCheckService.save(cashCheckVo);
	}
	
	/**
	 * 删除 现金盘点单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult deleteCashCheck(@RequestParam String fid){
		return cashCheckService.delete(fid);
	}
	/**
	 * 去现金盘点单列表信息页面<br>
	 */
	@RequestMapping("/listCashCheck")
	public String listCashCheck(ModelMap model){
		return "/cashier/cashCheck/manage";
	}
	 
	/**
	 * 新增/编辑现金盘点单页面
	 * @return
	 */
	@RequestMapping("/editCashCheck")
	public String editCashCheck(ModelMap model, String fid){
		if(StringUtils.isNotBlank(fid)){
			CashCheckVo assetVo = cashCheckService.getById(fid);
			assetVo.setStauts("1");
			//获取最大日期扎帐
			BankCheckdate checkdate = bankCheckdateService.getMaxCheckDate();
			if(checkdate!=null){
				String date = assetVo.getDate();
				//判断账单日期是否大于扎帐日期，小于的就不能修改
				if(DateUtils.getDateFromString(date).compareTo(checkdate.getCheckDate())<0){
					assetVo.setStauts("2");
				}
			}
			model.put("cash",assetVo);
		}
	    return "/cashier/cashCheck/edit";
	}
	
	/**
	 * 获取上一次盘点数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryLastNumber")
	public CashCheckVo queryLastNumber(@RequestParam String subjectId){
		return cashCheckService.queryLastNumber(subjectId);
		
	}
	/**
	 * 判断是否当天时间，如果不是当天时间不可以修改
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkDate")
	public RequestResult checkDate(@RequestParam String fid){
		return cashCheckService.checkDate(fid);
	}
	@ResponseBody
	@RequestMapping(value = "/totalFamount")
	public BigDecimal totalFamount(CashCheckVo vo){
		return CashCheckService.totalFamount(vo);
	}

}
