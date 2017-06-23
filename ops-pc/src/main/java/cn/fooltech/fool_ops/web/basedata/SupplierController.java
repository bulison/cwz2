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
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.basedata.vo.SupplierVo;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.utils.WebUtils;

/**
 * <p>供应商网页控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年4月23日
 */
@Controller
@RequestMapping(value = "/supplier")
public class SupplierController {
	
	/**
	 *供应商服务类
	 */
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private ExcelExceptionService exExceptionService;
	
	/**
	 * 供应商弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/basedata/supplier/supplierWindow";
	}
	
	/**
	 * 供应商信息管理页面
	 * @param vo 
	 * @param page 当前页数
	 * @param rows 页面大小
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(SupplierVo vo,PageParamater pageParamater, Model model){
//		PageJson result = supplierService.queryJson(vo, page, rows);

		Page<SupplierVo> page = supplierService.query(vo,pageParamater);
		PageJson pageJson = new PageJson(page);
		model.addAttribute("result", pageJson);
		return "/basedata/supplier/supplierManagement";
	}
	
	/**
	 * 供应商列表信息(JSON)
	 * @param vo
	 * @param page 当前页数
	 * @param rows 页面大小
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public @ResponseBody PageJson list(SupplierVo vo,PageParamater pageParamater, Model model){
//		Page<SupplierVo> query = supplierService.query(vo, pageParamater);
		return supplierService.queryJson(vo,pageParamater);
	}
	
	/**
	 * 获取供应商详细信息
	 * @param id 供应商ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(String id, Model model){
		SupplierVo supplier = supplierService.getById(id);
		model.addAttribute("vo", supplier);
		return "/basedata/supplier/detailSupplier";
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(){
		return "/basedata/supplier/addSupplier";
	}
	
	/**
	 * 编辑页面
	 * @param id 供应商ID
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(String id, Model model){
		SupplierVo supplier = supplierService.getById(id);
		model.addAttribute("vo", supplier);
		return "/basedata/supplier/updateSupplier";
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(SupplierVo vo){
		return supplierService.save(vo);
	}
	
	/**
	 * 删除供应商信息
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(String id){
		return supplierService.delete(id);
	}

	/**
	 * 判断编号是否有效
	 * @param vo
	 * @return
	 */
	@RequestMapping("/isCodeValid")
	@ResponseBody
	public RequestResult isCodeValid(SupplierVo vo){
		return supplierService.isCodeValid(vo);
	}
	
	/**
	 * 导出
	 * @param vo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/export")
	public void export(SupplierVo vo,HttpServletResponse response) throws Exception{
		String searchKey = vo.getSearchKey();
		searchKey = URLDecoder.decode(searchKey,"utf-8");
		vo.setSearchKey(searchKey);
		PageParamater pageParamater = new PageParamater(1, Integer.MAX_VALUE, 0);
		Page<SupplierVo> page = supplierService.query(vo,pageParamater);  
		List<SupplierVo> vos= page.getContent();
		ExcelUtils.exportExcel(SupplierVo.class, vos, "供应商.xls", response);
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
		RequestResult result = ExcelUtils.importExcel(SupplierVo.class, request, ImportType.SEQUNENCE, voBeans);
		
		int success = 0,fail = 0;
		
		//判断是否转换成功
		if(result.getReturnCode() == RequestResult.RETURN_SUCCESS){
			
			//逐个vo保存，统计成功、失败个数
			for(ImportVoBean voBean:voBeans){
				if(voBean.getVaild()){
					RequestResult cur = supplierService.save((SupplierVo)voBean.getVo(),"import");
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
			exExceptionService.save(voBeans, code);
			
			ExcelUtils.processResultVos(voBeans, (Workbook) result.getData(), code);
			
			String successTag = "导入成功%d个；导入失败%d个；";
			successTag = String.format(successTag, success, fail);

            int returnCode = fail>0?RequestResult.RETURN_FAILURE:RequestResult.RETURN_SUCCESS;

            result = new RequestResult(returnCode, successTag, code);
		}
		WebUtils.writeJsonToHtml(response, result);
	}
	
	/**
	 * 模糊查询(根据供应商编号、供应商名称)
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/vagueSearch")
	public List<SupplierVo> vagueSearch(SupplierVo vo){
		return supplierService.vagueSearch(vo);
	}
	
}
