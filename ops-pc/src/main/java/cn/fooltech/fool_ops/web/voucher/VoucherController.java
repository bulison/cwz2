package cn.fooltech.fool_ops.web.voucher;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalConfig;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalConfigService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalPeriodVo;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherService;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherVo;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>凭证网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2015年11月23日
 */
@Controller
@RequestMapping("/voucher")
public class VoucherController extends BaseController{
	
	/**
	 * 凭证网页服务类
	 */
	@Autowired
	private VoucherService voucherService;
	
	/**
	 * 会计期间网页服务类
	 */
	@Autowired
	private FiscalPeriodService periodService;
	
	/**
	 * 财务参数设置服务类
	 */
	@Autowired
	private FiscalConfigService configService;
	
	/**
	 * 机构网页服务类
	 */
	@Autowired
	private OrgService orgService;
	
	/**
	 * 凭证管理界面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(Model model){
		FiscalPeriod fiscalPeriod = periodService.getFirstNotCheck();
		if(fiscalPeriod != null){
			model.addAttribute("endVoucherDate", fiscalPeriod.getEndDate());
			model.addAttribute("startVoucherDate", fiscalPeriod.getStartDate());
		}
		return "/fiscal/voucher/manage";
	}
	
	/**
	 * 凭证字弹出框
	 * @return
	 */
	@RequestMapping(value = "/voucherWordWindow")
	public String voucherWordWindow(){
		return "/fiscal/voucher/voucherWordWindow";
	}
	
	/**
	 * 新增页面
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(VoucherVo vo,HttpServletRequest request){
		Map<String, Object> map=voucherService.getDefaultMessage(vo);
		request.setAttribute("voucherDate", map.get("voucherDate"));
		request.setAttribute("voucherNumber", map.get("voucherNumber"));
		request.setAttribute("voucherWordId", map.get("voucherWordId"));
		request.setAttribute("voucherWordName", map.get("voucherWordName"));
		request.setAttribute("getLastResume", map.get("getLastResume"));
		request.setAttribute("editVoucherNumber", map.get("editVoucherNumber"));
		return "/fiscal/voucher/voucherEdit";
	}
	
	/**
	 * 修改页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(String fid,HttpServletRequest request){
		VoucherVo vo=voucherService.getById(fid);
		String accId = SecurityUtil.getFiscalAccountId();
		FiscalConfig config1 = configService.getConfig(accId, "F01");
		FiscalConfig config2 = configService.getConfig(accId, "F05");
		if(config1!=null){
			request.setAttribute("getLastResume", config1.getValue());
		}else{
			request.setAttribute("getLastResume", "");
		}
		if(config2!=null){
			request.setAttribute("editVoucherNumber", config2.getValue());
		}else{
			request.setAttribute("getLastResume", "");
		}
		request.setAttribute("obj", vo);
		return "/fiscal/voucher/voucherEdit";
	}
	
	/**
	 * 冲销页面
	 * @return
	 */
	@RequestMapping(value = "/writeOff")
	public String writeOff(String fid,HttpServletRequest request){
		VoucherVo vo=voucherService.getById(fid);
		vo.setRecordStatus(null);
		request.setAttribute("fid", vo.getFid());
		vo.setFid(null);
		vo.setPostPeopleName(null);
		vo.setPostPeopleId(null);
		vo.setSupervisorId(null);
		vo.setSupervisorName(null);
		vo.setCreateTime(null);
		vo.setCreatorId(null);
		vo.setCreatorName(null);
		vo.setAuditorId(null);
		vo.setAuditorName(null);
		vo.setAuditDate(null);
		vo.setCancelDate(null);
		vo.setCancelorId(null);
		vo.setCancelorName(null);
		vo.setNumber(null);
		Map<String, Object> map=voucherService.getDefaultMessage(vo);
		request.setAttribute("voucherNumber", map.get("voucherNumber"));
		request.setAttribute("voucherWordId", map.get("voucherWordId"));
		request.setAttribute("voucherWordName", map.get("voucherWordName"));
		request.setAttribute("getLastResume", map.get("getLastResume"));
		request.setAttribute("subjectIsLeaf", map.get("subjectIsLeaf"));
		//request.setAttribute("number", map.get("number"));
		request.setAttribute("obj", vo);
		return "/fiscal/voucher/voucherEdit";
	}
	
	/**
	 * 筛选页面
	 * @return
	 */
	@RequestMapping(value = "/search")
	public String search(){
		return "/fiscal/voucher/search";
	}
	
	/**
	 * 批量审核界面
	 * @return
	 */
	@RequestMapping("/batchAuditUI")
	public String batchAuditUI(){
		return "/fiscal/voucher/pass"; 
	}
	
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(VoucherVo vo, PageParamater paramater){
		Page<VoucherVo> page = voucherService.query(vo, paramater);
		return new PageJson(page);
	}
	
	/**
	 * 获取凭证详细信息
	 * @param id 凭证ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/detail")
	public VoucherVo detail(String id){
		return voucherService.getById(id);
	}
	
	/**
	 * 添加凭证时，获取默认信息
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDefaultMessage")
	public Map<String, Object> getDefaultMessage(VoucherVo vo){
		return voucherService.getDefaultMessage(vo);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(VoucherVo vo){
		return voucherService.save(vo);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String id){
		return voucherService.delete(id);
	}
	
	/**
	 * 审核
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/audit")
	public RequestResult audit(String id){
		return voucherService.audit(id);
	}
	
	/**
	 * 批量审核
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/batchAudit")
	public RequestResult batchAudit(VoucherVo vo){
		return voucherService.batchAudit(vo);
	}
	
	/**
	 * 作废
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/cancel")
	public RequestResult cancel(String id){
		return voucherService.cancel(id);
	}
	
	/**
	 * 反审核
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/reverseAudit")
	public RequestResult reverseAudit(String id){
		return voucherService.reverseAudit(id);
	}
	
	/**
	 * 反作废
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/reverseCancel")
	public RequestResult reverseCancel(String id){
		return voucherService.reverseCancel(id);
	}
	
	/**
	 * 签字
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/signature")
	public RequestResult signature(String id){
		return voucherService.signature(id);
	}
	
	/**
	 * 过账
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/postAccount")
	public RequestResult postAccount(VoucherVo vo){
		return voucherService.postAccount(vo);
	}
	
	/**
	 * 反过账
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/reversePostAccount")
	public RequestResult reversePostAccount(VoucherVo vo){
		return voucherService.reversePostAccount(vo);
	}
	
	/**
	 * 过账界面 
	 * @return
	 */
	@RequestMapping(value="/post")
	public String poss(){
		return "/fiscal/voucher/posting";
	}
	
	/**
	 * 反过账界面
	 * @return
	 */
	@RequestMapping(value="reversePost")
	public String reversePost(){
		return "/fiscal/voucher/reversePost";
	}
	
	/**
	 * 打印界面
	 * @return
	 */
	@RequestMapping(value="print")
	public String print(String id, Model model){

		Voucher entity = voucherService.get(id);
		VoucherVo voucherVo = voucherService.getVo(entity, true);
		String json = voucherService.getJsonData(voucherVo);
		Date date = DateUtils.getDateFromString(voucherVo.getVoucherDate());
		FiscalPeriodVo period = periodService.getPeriodByDate(date);
		
		model.addAttribute("period", period.getPeriod());
		model.addAttribute("org", orgService.getOrg());
		model.addAttribute("data", json);
		
		return "/fiscal/voucher/print";
	}
	
}
