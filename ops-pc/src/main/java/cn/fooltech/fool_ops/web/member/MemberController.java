package cn.fooltech.fool_ops.web.member;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.member.vo.MemberVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.utils.WebUtils;

/**
 * <p>人员网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-09-08 13:55:54
 */
@Controller
@RequestMapping(value = "/member")
public class MemberController{
	
	/**
	 * 人员网页服务类
	 */
	@Autowired
	private MemberService memberWebService;
	
	@Autowired
	private AuxiliaryAttrService auxiliaryAttrWebService;
	
	@Autowired
	private ExcelExceptionService exExceptionWebService;
	
	/**
	 * 人员弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/member/memberWindow";
	}
	
	@RequestMapping(value="/memberManager")
	public String memberManager(){
		return "/member/member";
	}
	
	@RequestMapping(value="/addMember")
	public String addMember(){
		return "/member/addMember";
	}
	
	@RequestMapping(value="/editMember")
	public String editMember(String fid,HttpServletRequest request){
		MemberVo entity=memberWebService.getByFid(fid);
		request.setAttribute("entity", entity);
		return "/member/addMember";
	}
	
	@RequestMapping(value="/list")
	@ResponseBody
	public PageJson query(MemberVo vo,PageParamater pageParamater){
		Page<MemberVo> page = memberWebService.query(vo, pageParamater);
		PageJson pageJson = new PageJson(page);
		return pageJson;
	}
	
	@RequestMapping(value="/save")
	@ResponseBody
	public RequestResult save(MemberVo vo){
		return memberWebService.save(vo);
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public RequestResult delete(MemberVo vo){
		return memberWebService.delete(vo.getFid());
		 
	}
	
	@RequestMapping(value="/eduList")
	@ResponseBody
	public List<CommonTreeVo> eduList(MemberVo vo){
		 return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("006");
	}
	
	@RequestMapping(value="/jobStatusList")
	@ResponseBody
	public List<CommonTreeVo> jobStatusList(MemberVo vo){
		 return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("005");
	}
	
	
	/**
	 * 根据部门ID查人员
	 * add by xjh
	 */
	@RequestMapping(value="/queryTreeByDept")
	@ResponseBody
	public List<CommonTreeVo> queryTreeByDept(String deptId){
		return memberWebService.queryTreeByDept(deptId);
	}
	
	/**
	 * 自动填充异步查询
	 * add by xjh
	 */
	@RequestMapping(value="/queryAutoComplete")
	@ResponseBody
	public List<MemberVo> queryAutoComplete(String filterKey, String filterVal){
		return memberWebService.queryForAutoComplete(filterKey, filterVal);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		dateFormat.setLenient(false);//严格限制日期输入
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 导出例子
	 * @author xjh
	 * @throws Exception 
	 * @date 2015年9月23日
	 */
	@RequestMapping(value="/export")
	public void export(MemberVo vo, HttpServletResponse response) throws Exception{
		
		String searchKey = vo.getSearchKey();
		searchKey = URLDecoder.decode(searchKey,"utf-8");
		vo.setSearchKey(searchKey);
		PageParamater pageParamater = new PageParamater();
		pageParamater.setPage(1);//导出所有
		pageParamater.setRows(Integer.MAX_VALUE);
		Page<MemberVo> page = memberWebService.query(vo, pageParamater);
		List<MemberVo> vos = page.getContent();
		
		try {
			
			ExcelUtils.exportExcel(MemberVo.class, vos, "企业人员.xls", response);
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
		RequestResult result = ExcelUtils.importExcel(MemberVo.class, request, ImportType.SEQUNENCE, voBeans);
		
		int success = 0,fail = 0;
		
		//判断是否转换成功
		if(result.getReturnCode() == RequestResult.RETURN_SUCCESS){
			
			//逐个vo保存，统计成功、失败个数
			for(ImportVoBean voBean:voBeans){
				if(voBean.getVaild()){
					RequestResult cur = memberWebService.save((MemberVo)voBean.getVo());
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
			exExceptionWebService.save(voBeans, code);
			
			ExcelUtils.processResultVos(voBeans, (Workbook) result.getData(), code);
			
			String successTag = "导入成功%d个；导入失败%d个；";
			successTag = String.format(successTag, success, fail);

			int returnCode = fail>0?RequestResult.RETURN_FAILURE:RequestResult.RETURN_SUCCESS;

			result = new RequestResult(returnCode, successTag, code);
		}
		WebUtils.writeJsonToHtml(response, result);
	}
	
	/**
	 * 模糊查询(根据人员编号、人员名称)
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/vagueSearch")
	public List<MemberVo> vagueSearch(MemberVo vo){
		return memberWebService.vagueSearch(vo);
	}
	
}