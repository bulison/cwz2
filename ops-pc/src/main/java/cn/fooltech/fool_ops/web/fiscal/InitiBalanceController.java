package cn.fooltech.fool_ops.web.fiscal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalInitBalance;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalInitBalanceService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountingSubjectVo;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalInitBalanceVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.utils.WebUtils;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <p>科目初始化管理控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年11月18日
 */
@Controller
@RequestMapping(value = "/initiBalance")
public class InitiBalanceController {
	
	@Autowired
	private FiscalInitBalanceService fiscalInitBalanceWebService;
	
	@Autowired
	private FiscalAccountingSubjectService fiscalAccountingSubjectWebService;
	
	@Autowired
	private ExcelExceptionService exExceptionWebService;
	
	
	/**
	 * 科目初始化管理界面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/fiscal/initiBalance/manage";
	}
	
	/**
	 * 核算页面
	 * @return
	 */
	@RequestMapping(value = "/sign")
	public String sign(String fid,HttpServletRequest request){
		FiscalAccountingSubjectVo obj=fiscalAccountingSubjectWebService.getById(fid);
		request.setAttribute("obj", obj);
		FiscalInitBalanceVo vo = fiscalInitBalanceWebService.getBySubjectId(fid);
		request.setAttribute("jsonObj", JSONObject.fromObject(vo));
		return "/fiscal/initiBalance/sign";
	}
	
	
	/**
	 * 期初页面
	 * @return
	 */
	@RequestMapping(value = "/period")
	public String period(){
		return "/fiscal/initiBalance/periodSelection";
	}
	
	/**
	 * 科目树结构
	 * @return
	 */
	@RequestMapping(value = "/tree")
	@ResponseBody
	public List<FiscalInitBalanceVo> tree(FiscalInitBalanceVo vo,PageParamater pageParamater){
		
//		long time1 = System.currentTimeMillis();
		
		List<FiscalInitBalanceVo> tree=fiscalInitBalanceWebService.queryTree(vo);
		
//		long time2 = System.currentTimeMillis();
//		long delta = time2-time1;
//		System.out.println("查询科目初始树耗时："+delta+"ms");
		
		return tree;
	}
	
	
	/**
	 * 保存科目期初
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(String vos){
		List<FiscalInitBalanceVo> list = new ArrayList<FiscalInitBalanceVo>();
		if(StringUtils.isNotBlank(vos)){
			JSONArray array = JSONArray.fromObject(vos);
			List voList = (List)JSONArray.toCollection(array, FiscalInitBalanceVo.class);
			Iterator iterator = voList.iterator();
			while(iterator.hasNext()){
				FiscalInitBalanceVo vo = (FiscalInitBalanceVo) iterator.next();
				String inValid = ValidatorUtils.inValidMsg(vo);
				if(inValid!=null){
					return new RequestResult(RequestResult.RETURN_FAILURE,inValid);
				}
				list.add(vo);
			}
		}
		if(list.size()<1)return new RequestResult(RequestResult.RETURN_FAILURE, "未提交任何数据");
		
		FiscalInitBalanceVo main = list.get(0);
		for(int i=1;i<list.size();i++){
			FiscalInitBalanceVo vo = list.get(i);
			vo.setDirection(main.getDirection());
			vo.setSubjectId(main.getSubjectId());
			vo.setSubjectType(main.getSubjectType());
			vo.setSubjectCategory(main.getSubjectCategory());
		}
		return fiscalInitBalanceWebService.save(list);
	}
	
	/**
	 * 删除科目期初
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(String fid){
		return fiscalInitBalanceWebService.delete(fid);
	}
	
	/**
	 * 查找核算科目
	 * @return
	 */
	@RequestMapping(value="/queryAccounting")
	@ResponseBody
	public List<FiscalInitBalanceVo> queryAccounting(String subjectId){
		return fiscalInitBalanceWebService.queryAccountingSubject(subjectId);
	}
	
	/**
	 * 从期初应付和期初应收导入数据
	 * @return
	 */
	@RequestMapping(value = "/saveByInitalPayReceive")
	@ResponseBody
	public RequestResult saveByInitalPayReceive(){
		return fiscalInitBalanceWebService.saveByInitalPayReceive();
	}
	
	/**
	 * 期初数据试算
	 * @return
	 */
	@RequestMapping(value = "/trailInital")
	@ResponseBody
	public RequestResult trailInital(){
		return fiscalInitBalanceWebService.trailInital();
	}
	
	/**
	 * 导出
	 * @author xjh
	 * @throws Exception 
	 * @date 2015年12月1日
	 */
	@RequestMapping(value="/export")
	public void export(FiscalInitBalanceVo vo, HttpServletResponse response) throws Exception{
		
		List<FiscalInitBalance> data = fiscalInitBalanceWebService.queryAll();
		List<FiscalInitBalanceVo> vos = fiscalInitBalanceWebService.getVos(data);
		
		try {
			ExcelUtils.exportExcel(FiscalInitBalanceVo.class, vos, "科目初始数据.xls", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 上传文件并导入
	 * @author xjh
	 * @date 2015年12月1日
	 */
	@RequestMapping(value="/import", method=RequestMethod.POST)
	@ResponseBody
	public void importExcel(HttpServletRequest request, HttpServletResponse response){
		
		List<ImportVoBean> voBeans = Lists.newArrayList();
		
		//把excel转换成VO对象
		RequestResult result = ExcelUtils.importExcel(FiscalInitBalanceVo.class, request, ImportType.SEQUNENCE, voBeans);
		
		int success = 0,fail = 0;
		
		//判断是否转换成功
		if(result.isSuccess()){
			
			//逐个vo保存，统计成功、失败个数
			for(ImportVoBean voBean:voBeans){
				if(voBean.getVaild()){
					RequestResult cur = fiscalInitBalanceWebService.saveByExcelImport((FiscalInitBalanceVo)voBean.getVo());
					if(cur.isSuccess()){
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
}
