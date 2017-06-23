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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.vo.CustomerVo;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.utils.WebUtils;

/**
 * <p>客户网页控制器类</p>
 * @author lxf
 * @version 1.0
 * @date 2014年12月23日
 * @update rqh 2015-09-14
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerController {
	
	/**
	 * 客户网页服务类
	 */
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ExcelExceptionService excelExceptionService;
	
	/**
	 * 客户弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/basedata/customer/customerWindow";
	}
	
	/**
	 * 客户信息管理页面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/basedata/customer/customerManage";
	}
	
	/**
	 * 客户列表信息(JSON)
	 * @param vo
	 * @param page 当前页数
	 * @param rows 页面大小
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageJson list(CustomerVo vo,PageParamater pageParamater) throws Exception{
		Page<CustomerVo> page = customerService.query(vo, pageParamater);
		PageJson pageJson = new PageJson(page);
		return pageJson;
	}
	
	/**
	 * 获取客户详细信息
	 * @param id 客户ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(String id, Model model){
		CustomerVo customer = customerService.getById(id);
		model.addAttribute("customer", customer);
		return "/basedata/customer/customerEdit";
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(){
		return "/basedata/customer/customerEdit";
	}
	
	/**
	 * 编辑页面
	 * @param id 客户ID
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(String id, Model model){
		CustomerVo customer = customerService.getById(id);
		model.addAttribute("customer", customer);
		return "/basedata/customer/customerEdit";
	}

	/**
	 * 客户收益分析页面
	 */
	@RequestMapping("/customerIncomeAnalysis")
	public String customerIncomeAnalysis(Model model){
		return "/rate/customerIncomeAnalysis/manage";
	}

	/**
	 * 客户收益详情页面
	 */
	@RequestMapping("/customerIncomeDetail")
	public String customerIncomeDetail(Model model){
		return "/rate/customerIncomeAnalysis/detail";
	}

	/**
	 * 新增、编辑
	 * @param vo
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(CustomerVo vo){
		return customerService.save(vo);
	}
	
	/**
	 * 删除客户信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(String id){
		return customerService.delete(id);
	}

	/**
	 * 判断编号是否有效
	 * @param vo
	 * @return
	 */
	@RequestMapping("/isCodeValid")
	@ResponseBody
	public RequestResult isCodeValid(CustomerVo vo){
		return customerService.isCodeValid(vo);
	}
	
	/**
	 * 导出
	 * @param vo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/export")
	public void export(CustomerVo vo,HttpServletResponse response) throws Exception{
		String searchKey = vo.getSearchKey();
		searchKey = URLDecoder.decode(searchKey,"utf-8");
		vo.setSearchKey(searchKey);
		PageParamater pageParamater = new PageParamater(1, Integer.MAX_VALUE, 0);
		Page<CustomerVo> page = customerService.query(vo,pageParamater); 
		List<CustomerVo> vos = page.getContent();
		ExcelUtils.exportExcel(CustomerVo.class, vos, "客户.xls", response);
	}
	
	@RequestMapping(value="/tmpAddExcelTitle")
	public void tmpAddExcelTitle()throws Exception{
		//String[] clas = new String[]{""};
		/*String cla = "com.gever.ops.web.report.vo.CashBankAccountVo";
		String[] fields = new String[]{"code","voucherCode","income","expend","balanceAmount","remark","abstName"};
		String[] cnNames = new String[]{"编号","凭证号","收入","支出","结余金额","备注","摘要"};
		int type = 31;
		
		ExcelMapService excelMapService = SpringUtils.getBean(ExcelMapService.class);
		ExcelMap map = null;
		
		for (int i=0;i<fields.length;i++) {
			map = new ExcelMap();
			map.setClazz(cla);
			map.setField(fields[i]);
			map.setCnName(cnNames[i]);
			map.setSequence(i+2);
			map.setType(type);
			map.setFimport(true);
			map.setFexport(true);
			map.setProcessor(null);
			map.setValidation(true);
			excelMapService.save(map);
		}*/
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
		RequestResult result = ExcelUtils.importExcel(CustomerVo.class, request, ImportType.SEQUNENCE, voBeans);
		
		int success = 0,fail = 0;
		
		//判断是否转换成功
		if(result.getReturnCode() == RequestResult.RETURN_SUCCESS){
			
			//逐个vo保存，统计成功、失败个数
			for(ImportVoBean voBean:voBeans){
				if(voBean.getVaild()){
					RequestResult cur = customerService.save((CustomerVo)voBean.getVo(),"import");
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
			String code = DateUtil.format(Calendar.getInstance().getTime(), DateUtil.dateTime);
			excelExceptionService.save(voBeans, code);
			
			ExcelUtils.processResultVos(voBeans, (Workbook) result.getData(), code);
			
			String successTag = "导入成功%d个；导入失败%d个；";
			successTag = String.format(successTag, success, fail);

            int returnCode = fail>0?RequestResult.RETURN_FAILURE:RequestResult.RETURN_SUCCESS;

            result = new RequestResult(returnCode, successTag, code);
		}
		WebUtils.writeJsonToHtml(response, result);
	}

	/**
	 * 模糊查询(根据客户编号、客户名称)
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/vagueSearch")
	public List<CustomerVo> vagueSearch(CustomerVo vo){
		return customerService.vagueSearch(vo);
	}
	
}
