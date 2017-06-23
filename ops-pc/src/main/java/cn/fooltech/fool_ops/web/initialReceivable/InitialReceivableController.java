package cn.fooltech.fool_ops.web.initialReceivable;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.domain.initialReceivable.service.InitialReceivableService;
import cn.fooltech.fool_ops.domain.initialReceivable.vo.InitialReceivableVo;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.utils.WebUtils;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>期初应收管理控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年9月8日
 */
@Controller
@RequestMapping(value = "/initialReceivableController")
public class InitialReceivableController extends BaseController{
	@Autowired
	InitialReceivableService initialReceivableWebService;
	
	@Autowired
	private ExcelExceptionService exExceptionWebService;
	
	@RequestMapping(value="/initialReceivableManager")
	public String intialBankManager(ModelMap model){
		//是否允许修改删除：true：允许；false：不允许
		model.put("enableEditOrDelete", initialReceivableWebService.enableEditOrDelete());
		Integer state = initialReceivableWebService.getTheFristPeriod();
		model.put("state", state);
		return "/initial/initialReceivable";
	}
	
	@RequestMapping(value="/addInitialReceivable")
	public String addInitialReceivable(HttpServletRequest request){
		request.setAttribute("code", initialReceivableWebService.getNewCode());
		return "/initial/addInitialReceivable";
	}
	
	@RequestMapping(value="/editInitialReceivable")
	public String editInitialReceivable(InitialReceivableVo vo,HttpServletRequest request){
		InitialReceivableVo entity=initialReceivableWebService.getByFid(vo.getFid());
		request.setAttribute("entity", entity);
		return "/initial/addInitialReceivable"; 
	}
	
	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson query(InitialReceivableVo vo,PageParamater pageParamater){
		Page<WarehouseBill> page=initialReceivableWebService.query(vo, pageParamater);
		PageJson pageJson = new PageJson();
		pageJson.setRows(initialReceivableWebService.getVos(page.getContent()));
		pageJson.setTotal(page.getTotalElements());
		return pageJson;
	}
	
	@RequestMapping(value="/save")
	@ResponseBody
	public RequestResult save(InitialReceivableVo vo){
		return initialReceivableWebService.save(vo);
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public RequestResult delete(InitialReceivableVo vo){
		return initialReceivableWebService.delete(vo.getFid());
	}
	
	
	/**
	 * 导出例子
	 * @author xjh
	 * @throws Exception 
	 * @date 2015年9月23日
	 */
	@RequestMapping(value="/export")
	public void export(InitialReceivableVo vo,PageParamater pageParamater, HttpServletResponse response) throws Exception{
		
		String searchKey = vo.getSearchKey();
		searchKey = URLDecoder.decode(searchKey,"utf-8");
		vo.setSearchKey(searchKey);
		
		pageParamater.setPage(1);//导出所有
		pageParamater.setRows(Integer.MAX_VALUE);
		Page<WarehouseBill> page = initialReceivableWebService.query(vo, pageParamater);
		
		List<InitialReceivableVo> vos = initialReceivableWebService.getVos(page.getContent());
		
		try {
			
			ExcelUtils.exportExcel(InitialReceivableVo.class, vos, "期初应收.xls", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		RequestResult result = ExcelUtils.importExcel(InitialReceivableVo.class, request, ImportType.SEQUNENCE, voBeans);
		
		int success = 0,fail = 0;
		
		//判断是否转换成功
		if(result.getReturnCode() == RequestResult.RETURN_SUCCESS){
			
			Set<String> customerCodes = Sets.newHashSet();
			
			//逐个vo保存，统计成功、失败个数
			for(ImportVoBean voBean:voBeans){
				if(voBean.getVaild()){
					
					InitialReceivableVo ipv = (InitialReceivableVo)voBean.getVo();
					
					if(customerCodes.contains(ipv.getCustomerCode())){
						voBean.setMsg("重复的客户");
						voBean.setVaild(false);
						fail++;
					}else{
						customerCodes.add(ipv.getCustomerCode());
						ipv.setCode(initialReceivableWebService.getNewCode());
						ipv.setFreeAmount(BigDecimal.ZERO);
						RequestResult cur = initialReceivableWebService.save(ipv);
						if(cur.getReturnCode() == RequestResult.RETURN_SUCCESS){
							success++;
						}else{
							voBean.setMsg(cur.getMessage());
							voBean.setVaild(false);
							fail++;
						}
					}
				}else{
					fail++;
				}
			}
			
			//使用时间作为流水号，保存异常信息
			String code = DateUtil.format(Calendar.getInstance().getTime(), DateUtil.dateTime);
			exExceptionWebService.save(voBeans, code);
			
			ExcelUtils.processResultVos(voBeans, (Workbook) result.getData(), code);
			
			String successTag = "导入成功%d个；导入失败%d个；";
			successTag = String.format(successTag, success, fail);

            int returnCode = fail>0?RequestResult.RETURN_FAILURE:RequestResult.RETURN_SUCCESS;

            result = new RequestResult(returnCode, successTag, code);
		}
		WebUtils.writeJsonToHtml(response, result);
	}

}
