package cn.fooltech.fool_ops.web.report;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.report.service.CashBankAccountService;
import cn.fooltech.fool_ops.domain.report.vo.CashBankAccountVo;
import cn.fooltech.fool_ops.utils.ExcelUtils;


/**
 * 现金银行账
 * 
 * @author tjr
 */
@Controller
@RequestMapping("/report/cashbankaccount/")
public class CashBankAccountController{

	@Autowired
	CashBankAccountService cashBankAccountWebService;
	
	@RequestMapping(value = "/manage")
	public String manage() {
		return "/report/cashbankaccount/manage";
	}

	@RequestMapping("/list")
	@ResponseBody
	public PageJson list(CashBankAccountVo vo, PageParamater paramater) throws Exception {
		PageJson page =  cashBankAccountWebService.getPage(vo, paramater);
		return page;
	}
	
	@RequestMapping("/getList")
	public List<CashBankAccountVo> getList(CashBankAccountVo vo, PageParamater paramater) throws Exception {
		List<CashBankAccountVo> list = cashBankAccountWebService.getList(vo, paramater);
		return list;
	}
	
	
	/**
	 * 导出，需要在cfg_excel_map表配置对应字段
	 * @param vo
	 * @param paramater
	 * @param buildCode
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/export")
	public void export(CashBankAccountVo vo, PageParamater paramater,HttpServletResponse response) throws Exception{
		paramater.setRows(Integer.MAX_VALUE);
		List<CashBankAccountVo> list = getList(vo, paramater);
		ExcelUtils.exportExcel(CashBankAccountVo.class,list,"export.xls",response);
	}

}
