package cn.fooltech.fool_ops.web.basedata;

import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.ThrowException;
import cn.fooltech.fool_ops.domain.basedata.service.BankService;
import cn.fooltech.fool_ops.domain.basedata.vo.BankVo;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.domain.initialBank.service.InitialBankService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.utils.WebResponseUtils;


/**
 * <p>银行信息管理控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年9月27日
 */
@Controller
@RequestMapping(value = "/bankController")
public class BankController {

	@Autowired
	private BankService bankService;
	
	@Autowired
	private ExcelExceptionService exExceptionService;
	@Autowired
	private InitialBankService initialBankService;
	/**
	 * 客户弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/bank/bankWindow";
	}
	
	/**
	 * 自动补全帮助方法
	 * @param vo
	 * @author tjr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/keyhandler")
	public List<BankVo> keyHandler(BankVo vo)throws Exception{
		return bankService.keyHandler(vo);
	}
	
	/**
	 * 银行管理页面
	 * @return
	 */
	@RequestMapping(value="/bankManager")
	public String periodManager(){
		return "/bank/bank";
	}
	
	@RequestMapping(value="/addBank")
	public String addBank(){
		return "/bank/addBank";
	}
	
	@RequestMapping(value="/editBank")
	public String editBank(BankVo vo,HttpServletRequest request){
		BankVo entity=bankService.getById(vo.getFid(), ThrowException.Throw);
		request.setAttribute("entity", entity);
		return "/bank/addBank";
	}
	
	@RequestMapping(value="/save")
	@ResponseBody
    public RequestResult save(HttpServletRequest request,BankVo vo)throws Exception{
    	return bankService.save(vo);
    }
	@RequestMapping(value="/delete")
	@ResponseBody
    public RequestResult delete(HttpServletRequest request,BankVo vo)throws Exception{
		if(initialBankService.countByBankId(vo.getFid())==0){
			return bankService.delete(vo.getFid());
		}else{
			  return new RequestResult(RequestResult.RETURN_FAILURE, "此银行信息已被引用,无法删除");
		}
    	
    }
	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson query(PageParamater paramater, BankVo vo){
		
		Page<BankVo> page = bankService.query(vo, paramater);
		return new PageJson(page);
	}
	
	@RequestMapping(value="/comboboxData")
	@ResponseBody
	public List<BankVo> comboboxData(BankVo vo){
		return bankService.findAll(vo);
	}
	
	/**
	 * 导出
	 * @param vo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/export")
	public void export(BankVo vo,HttpServletResponse response) throws Exception{
		
		String keyWord = vo.getKeyWord();
		keyWord = URLDecoder.decode(keyWord,"utf-8");
		vo.setKeyWord(keyWord);
		PageParamater paramater = new PageParamater();
		paramater.setRows(Integer.MAX_VALUE);
		Page<BankVo> result = bankService.query(vo, paramater); 
		ExcelUtils.exportExcel(BankVo.class, result.getContent(), "银行信息.xls", response);
	}
	
	/**
	 * 上传文件并导入
	 * @author xjh
	 * @date 2015年9月23日
	 */
	@RequestMapping(value="/import", method=RequestMethod.POST)
	@ResponseBody
	public void importExcel(HttpServletRequest request, HttpServletResponse response){
		
		List<ImportVoBean> voBeans = Lists.newArrayList();
		
		//把excel转换成VO对象
		RequestResult result = ExcelUtils.importExcel(BankVo.class, request, ImportType.SEQUNENCE, voBeans);
		
		int success = 0,fail = 0;
		
		//判断是否转换成功
		if(result.isSuccess()){
			
			//逐个vo保存，统计成功、失败个数
			for(ImportVoBean voBean:voBeans){
				if(voBean.getVaild()){
					RequestResult cur = bankService.save((BankVo)voBean.getVo());
					if(cur.getReturnCode() == RequestResult.RETURN_SUCCESS){
						success++;
					}else{
						voBean.setMsg(cur.getMessage());
						voBean.setVaild(false);
						fail++;
					}
				}else{
					fail++;
				}
			}
			
			//使用时间作为流水号，保存异常信息
			String code = DateUtilTools.date2String(Calendar.getInstance().getTime(), 
					DateUtilTools.DATE_PATTERN_YYYYMMDDHHMMssSSS);
			exExceptionService.save(voBeans, code);
			
			Object workbook = result.getData();
			ExcelUtils.processResultVos(voBeans, (Workbook) workbook, code);
			
			String successTag = "导入成功%d个；导入失败%d个；";
            successTag = String.format(successTag, success, fail);

            int returnCode = fail>0?RequestResult.RETURN_FAILURE:RequestResult.RETURN_SUCCESS;

            result = new RequestResult(returnCode, successTag, code);
		}
		WebResponseUtils.writeJsonToHtml(response, result);
	}
}
