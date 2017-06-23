package cn.fooltech.fool_ops.web.fiscal;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.fooltech.fastjson.annotation.SerializeField;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.utils.*;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.vo.CustomerVo;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.excelmap.service.ExcelExceptionService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalTemplateTypeService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountingSubjectVo;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalTemplateTypeVo;
import cn.fooltech.fool_ops.utils.ExcelUtils.ImportType;
import cn.fooltech.fool_ops.web.base.BaseController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <p>财务科目管理控制器类</p>
 * @author lzf
 * @version 1.0
 * @date 2015年11月18日
 */
@Controller
@RequestMapping(value = "/fiscalSubject")
public class FiscalSubjectController extends BaseController{

	final int timeout =1*60*60*24*31*12;
	
	private Logger logger = LoggerFactory.getLogger(FiscalSubjectController.class);
	
	@Autowired
	private FiscalAccountingSubjectService fiscalAccountingSubjectService;
	
	@Autowired
	private AuxiliaryAttrService auxiliaryAttrWebService;
	
	@Autowired
	private ExcelExceptionService exExceptionService;
	
	@Autowired
	private FiscalTemplateTypeService fiscalTemplateTypeService;

	@Autowired
	private RedisService redisService;

	/**
	 * 财务科目管理界面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(){
		return "/fiscal/subject/manage";
	}
	
	/**
	 * 财务科目管理界面
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/fiscal/subject/window";
	}
	
	/**
	 * 新增财务科目
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(String fid,HttpServletRequest request){
		String code="";
		Integer type=null;
		Integer direction=null;
		if(StrUtil.notEmpty(fid)){
			FiscalAccountingSubjectVo entity=fiscalAccountingSubjectService.getById(fid);
			code=fiscalAccountingSubjectService.getCode(fid);
			type=entity.getType();
			direction=entity.getDirection();
			entity.setFid(null);
			entity.setParentId(null);
			entity.setUpdateTime(null);
			entity.setName(null);
			entity.setFlag((short) 1);
			request.setAttribute("entity", entity);
		}
		request.setAttribute("parentId", fid);
		request.setAttribute("code", code);
		request.setAttribute("type", type);
		request.setAttribute("direction", direction);
		return "/fiscal/subject/edit";
	}
	
	/**
	 * 修改财务科目
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(FiscalAccountingSubjectVo vo,HttpServletRequest request){
		FiscalAccountingSubjectVo obj=fiscalAccountingSubjectService.getById(vo.getFid());
		request.setAttribute("entity", obj);
		return "/fiscal/subject/edit";
	}
	
	/**
	 * 科目列表页面
	 * @return
	 */
	@RequestMapping(value = "/listPage")
	public String list(){
		return "/fiscal/subject/list";
	}
	
	/**
	 * 引入页面
	 * @return
	 */
	@RequestMapping(value = "/leadIn")
	public String leadIn(String fid,HttpServletRequest request){
		request.setAttribute("subjectId", fid);
		return "/fiscal/subject/leadIn";
	}
	
	/**
	 * 模板页面
	 * @return
	 */
	@RequestMapping(value = "/template")
	public String templet(){
		return "/fiscal/subject/template";
	}
	
	/**
	 * 科目树
	 * @return
	 */
	@RequestMapping(value = "/getTree")
	@ResponseBody
	//@SerializeField(excludes = {"state"}, clazz = FiscalAccountingSubjectVo.class)
	public List<FiscalAccountingSubjectVo> getTree(FiscalAccountingSubjectVo vo,
												   @RequestParam(required = false, defaultValue = "1") Integer closeLevel){
		
		long time1 = System.currentTimeMillis();
		
		List<FiscalAccountingSubjectVo> treeData = fiscalAccountingSubjectService.getTree(vo, closeLevel);

		long time2 = System.currentTimeMillis();
		long delta = time2-time1;
		logger.debug("查询科目树耗时："+delta+"ms");
		
		return treeData;
		/*JsonConfig config = new JsonConfig();
		JSONArray array = JSONArray.fromObject(treeData, config);
		processAttributes(array, "attributes");
		return array.toString();*/
	}
	
	/**
	 * 科目树
	 * @return
	 */
	@RequestMapping(value = "/getById")
	@ResponseBody
	public FiscalAccountingSubjectVo getById(String fid){
		return fiscalAccountingSubjectService.getById(fid);
	}
	
	/**
	 * 根据科目编号查找
	 * @return
	 */
	@RequestMapping(value = "/getByCode")
	@ResponseBody
	public FiscalAccountingSubjectVo getByCode(@RequestParam String code, Integer getChild){
		FiscalAccountingSubject subject = null;
		if(getChild!=null && getChild==1){
			subject = fiscalAccountingSubjectService.getFirstLeafByCode(code);
		}else{
			subject = fiscalAccountingSubjectService.getByCode(code);
		}
		return fiscalAccountingSubjectService.getVo(subject);
	}
	
	/**
	 * 处理多余的层次关系
	 * @param array
	 * @param key
	 * @return
	 */
	private void processAttributes(JSONArray array, String key){
		for(int i=0;i<array.size();i++){
			JSONObject json = (JSONObject) array.get(i);
			if(json.containsKey(key)){
				JSONObject attrVal = (JSONObject) json.remove(key);
				json.putAll(attrVal);
			}
			JSONArray children = json.getJSONArray("children");
			if(children.size()>0){
				processAttributes(children, key);
			}
		}
	}
	
	/**
	 * 科目列表
	 * @return
	 */
	@RequestMapping(value = "/getList")
	@ResponseBody
	public PageJson getList(FiscalAccountingSubjectVo vo,PageParamater pageParamater){
		Page<FiscalAccountingSubjectVo> page=fiscalAccountingSubjectService.getChildrenPage(vo.getFid(), pageParamater);
		PageJson pageJson = new PageJson(page);
		return pageJson;
	}
	
	/**
	 * 删除科目
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(FiscalAccountingSubjectVo vo){
		return fiscalAccountingSubjectService.delete(vo.getFid());
	}
	
	/**
	 * 保存账套
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(FiscalAccountingSubjectVo vo){
		String parentId=vo.getParentId();
		if(StrUtil.notEmpty(parentId)){
			FiscalAccountingSubjectVo obj=fiscalAccountingSubjectService.getById(parentId);
			if(vo.getType()==null){
				vo.setType(obj.getType());
			}
			if(vo.getDirection()==null){
				vo.setDirection(obj.getDirection());
			}
			if(vo.getCashSign()==null){
				vo.setCashSign((short) 0);
			}
			if(vo.getCurrencySign()==null){
				vo.setCurrencySign((short) 0);
			}
			if(vo.getCussentAccountSign()==null){
				vo.setCussentAccountSign((short) 0);
			}
			if(vo.getSupplierSign()==null){
				vo.setSupplierSign((short) 0);
			}
			if(vo.getCustomerSign()==null){
				vo.setCustomerSign((short) 0);
			}
			if(vo.getDepartmentSign()==null){
				vo.setDepartmentSign((short) 0);
			}
			if(vo.getMemberSign()==null){
				vo.setMemberSign((short) 0);
			}
			if(vo.getWarehouseSign()==null){
				vo.setWarehouseSign((short) 0);
			}
			if(vo.getProjectSign()==null){
				vo.setProjectSign((short) 0);
			}
			if(vo.getGoodsSign()==null){
				vo.setGoodsSign((short) 0);
			}
			if(vo.getQuantitySign()==null){
				vo.setQuantitySign((short) 0);
			}
		};
		return fiscalAccountingSubjectService.save(vo,2);
	}
	
	//科目类别
	@RequestMapping(value="/subjectType")
	@ResponseBody
	public List<CommonTreeVo> subjectType(CustomerVo vo){
		 return auxiliaryAttrWebService.findSubAuxiliaryAttrTree(AuxiliaryAttrType.CODE_SUBJECT_TYPE);
	}
	
	//币别
	@RequestMapping(value="/currencyType")
	@ResponseBody
	public List<CommonTreeVo> currencyType(CustomerVo vo){
		 return auxiliaryAttrWebService.findSubAuxiliaryAttrTree(AuxiliaryAttrType.CODE_CURRENCY);
	}
	
	//引入
	@RequestMapping(value="/saveImport")
	@ResponseBody
	public RequestResult saveImport(String subjectId, int relationType){
		 return fiscalAccountingSubjectService.saveImport(subjectId, relationType);
	}
	
	/**
	 * 初始化
	 * @param pwd 输入密码
	 * @return
	 */
	@RequestMapping(value="/saveInitialize")
	@ResponseBody
	public RequestResult saveInitialize(String pwd){
		
//		 Object accountId = SecurityUtils.getSubject().getSession().getAttribute(FiscalAccountVo.SESSION_KEY_ID);
		 return fiscalAccountingSubjectService.saveInitialize(pwd,1, SecurityUtil.getFiscalAccountId());
	}
	
	/**
	 * 模板
	 * @param templateTypeId 模板id
	 * @param pwd 输入密码
	 * @return
	 */
	@RequestMapping(value="/saveByTemplateType")
	@ResponseBody
	public RequestResult saveByTemplateType(String templateTypeId,String pwd){
		 return fiscalAccountingSubjectService.saveByTemplate(templateTypeId,pwd);
	}
	
	
	
	//查找模板
	@RequestMapping(value="/querytemplate")
	@ResponseBody
	public List<FiscalTemplateTypeVo> querytemplate(){
		 return fiscalTemplateTypeService.queryAll();
	}
	
	/**
	 * 导出
	 * @author xjh
	 * @throws Exception 
	 * @date 2015年11月30日
	 */
	@RequestMapping(value="/export")
	public void export(FiscalAccountingSubjectVo vo, HttpServletResponse response) throws Exception{
		
		List<FiscalAccountingSubject> data = fiscalAccountingSubjectService.queryAll();
		List<FiscalAccountingSubjectVo> vos = fiscalAccountingSubjectService.getVos(data);
		
		try {
			ExcelUtils.exportExcel(FiscalAccountingSubjectVo.class, vos, "科目.xls", response);
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
		RequestResult result = ExcelUtils.importExcel(FiscalAccountingSubjectVo.class, request, ImportType.SEQUNENCE, voBeans);
		
		int success = 0,fail = 0;
		
		//判断是否转换成功
		if(result.getReturnCode() == RequestResult.RETURN_SUCCESS){
			
			//逐个vo保存，统计成功、失败个数
			for(ImportVoBean voBean:voBeans){
				if(voBean.getVaild()){
					RequestResult cur = fiscalAccountingSubjectService.save((FiscalAccountingSubjectVo)voBean.getVo(),1);
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
	 * 获取科目
	 * @param code 科目编号或科目助记码
	 * @param parentId 父科目ID
	 * @param direction 余额方向
	 * @param leafFlag 1表示叶子节点
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSubject")
	public List<FiscalAccountingSubjectVo> getSubject(String parentId, @RequestParam("q") String code, Integer direction, 
			@RequestParam(defaultValue = "0", required = false) Short leafFlag, Short bankSubject, Short cashSubject){


		List<FiscalAccountingSubjectVo> vos = null;

		String key = fiscalAccountingSubjectService.getCacheKey();

		if(Strings.isNullOrEmpty(code) && Strings.isNullOrEmpty(parentId) && leafFlag==0 && bankSubject==null && cashSubject==null){
			vos = redisService.get(key, new TypeReference<List<FiscalAccountingSubjectVo>>(){});
		}

		if(vos == null){
			vos = fiscalAccountingSubjectService.getSubject(parentId, code, direction,
					leafFlag, bankSubject, cashSubject);
			if(Strings.isNullOrEmpty(code) && leafFlag==0 && bankSubject==null && cashSubject==null){
				redisService.set(key, vos, timeout);
			}
		}

		return vos;
	}



	/**
	 * 获取科目
	 * @param code 科目编号或科目助记码
	 * @param direction 余额方向
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getBankSubject")
	public List<FiscalAccountingSubjectVo> getBankSubject(@RequestParam("q") String code, FiscalAccountingSubjectVo vo){
		vo.setCode(code);
		return fiscalAccountingSubjectService.getVos(fiscalAccountingSubjectService.query(vo));
	}
	/**
	 * 根据父节点查所有子
	 * @param parentId:父节点ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTreeByParentId")
	public List<FiscalAccountingSubjectVo> getTreeByParentId(@RequestParam String parentId){
		return fiscalAccountingSubjectService.getTreeByParentId(parentId);
	}
	
	/**
	 * 根据子节点查询所有父科目名称，用|隔开
	 * @param id:子节点ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getParentNamesById")
	public String getParentNamesById(@RequestParam String id){
		return fiscalAccountingSubjectService.getParentNamesById(id);
	}
}
