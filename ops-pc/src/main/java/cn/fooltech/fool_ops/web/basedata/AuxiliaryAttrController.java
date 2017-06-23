package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.utils.StrUtil;
import cn.fooltech.fool_ops.utils.StringUtils;
import cn.fooltech.fool_ops.utils.tree.TreeVo;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import net.sf.json.JSONObject;

/**
 * <p>
 * 辅助属性网页控制器类
 * </p>
 * 
 * @author xjh
 * @version 1.0
 * @date 2015-09-06 10:14:28
 */
@Controller
@RequestMapping(value = "/basedata")
public class AuxiliaryAttrController {

	/**
	 * 辅助属性网页服务类
	 */
	@Autowired
	private AuxiliaryAttrService auxiliaryAttrWebService;
	/**
	 * 辅助属性类型网页服务类
	 */
//	@Autowired
//	private AuxiliaryAttrTypeWebService auxiliaryAttrTypeWebService;

	/**
	 * 根据分类编码查找某一分类的所有辅助属性
	 */
	@RequestMapping("/findSubAuxiliaryAttrTree")
	@ResponseBody
	public List<CommonTreeVo> findSubAuxiliaryAttrTree(HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		if (StringUtils.isNotBlank(code))
			return auxiliaryAttrWebService.findSubAuxiliaryAttrTree(code);
		return null;
	}
	/**
	 * 根据分类编码查找某一分类的所有辅助属性(加入模糊匹配)
	 */
	@RequestMapping("/fuzzyFindSubAuxiliaryAttrTree")
	@ResponseBody
	public List<CommonTreeVo> fuzzyFindSubAuxiliaryAttrTree(HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String searchKey = request.getParameter("searchKey");
		if (StringUtils.isNotBlank(code))
			return auxiliaryAttrWebService.fuzzyFindSubAuxiliaryAttrTree(code,searchKey);
		return null;
	}

	/**
	 * 去辅助属性列表信息页面<br>
	 */
	@RequestMapping("/listAuxiliaryAttr")
	public String listAuxiliaryAttr(ModelMap model) {
		return "/basedata/auxiliaryAttr/listAuxiliaryAttr";
	}

	@ResponseBody
	@RequestMapping("/getAuxiliaryTree")
	public List<TreeVo> getAuxiliaryTree() {

		List<TreeVo> treeData = auxiliaryAttrWebService.findTree();

		return treeData;
	}

	/**
	 * 查找辅助属性列表信息<br>
	 */
	
//	@ResponseBody
//	@RequestMapping("/queryAuxiliaryAttr")
//	public PageJson queryAuxiliaryAttr(AuxiliaryAttrVo auxiliaryAttrVo, PageParamater pageParamater) {
//
//		Page<AuxiliaryAttr> page = auxiliaryAttrWebService.query(auxiliaryAttrVo, pageParamater);
//
//		return getPageJson(page);
//	}
	 
	@ResponseBody
	@RequestMapping(value = "/repeatByCategory")
	public boolean findRepeatByCategory(HttpServletRequest request, HttpServletResponse response) {
		String fid = request.getParameter("fid");
		String cid = request.getParameter("cid");
		String name = request.getParameter("name");
		String code = request.getParameter("code");

		// name = StrUtil.toCoding(name,"utf-8");

		// return auxiliaryAttrWebService.findRepeatByCategory(fid, cid, name,
		// code);
		return false;
	}

	/**
	 * 编辑/新增辅助属性页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/viewAuxiliaryAttr/{fid}")
	public String viewAuxiliaryAttr(ModelMap model, @PathVariable("fid") String fid) {
		if (StringUtils.isNotBlank(fid) && !StrUtil.isSame(fid, "init")) {
			AuxiliaryAttrVo auxiliaryAttrVo = auxiliaryAttrWebService.getByFid(fid);
			model.put("auxiliaryAttr", auxiliaryAttrVo);
		}
		return "/basedata/auxiliaryAttr/addAuxiliaryAttr";
	}

	/**
	 * 新增/编辑辅助属性
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveAuxiliaryAttr", method = RequestMethod.POST)
	public RequestResult save(AuxiliaryAttrVo auxiliaryAttrVo) {
		String inValid = ValidatorUtils.inValidMsg(auxiliaryAttrVo);
		if (inValid != null) {
			return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
		}
		return auxiliaryAttrWebService.save(auxiliaryAttrVo);
	}

	/**
	 * 快速新增辅助属性
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveFast", method = RequestMethod.POST)
	public RequestResult saveFast(AuxiliaryAttrVo vo) {
		return auxiliaryAttrWebService.saveFast(vo);
	}

	/**
	 * 删除辅助属性
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteAuxiliaryAttr")
	public RequestResult deleteAuxiliaryAttr(@RequestParam String fid) throws Exception {
		// JSONObject result = new JSONObject();
		RequestResult result = null;
		try {
			result = auxiliaryAttrWebService.delete(fid);
			result = result == null ? new RequestResult(RequestResult.RETURN_SUCCESS, "") : result;
			// result.setReturnCode(RequestResult.RETURN_SUCCESS);
			// result.element("result", RESULT_OK);
			// return result;
		} catch (Exception e) {
			result = new RequestResult();
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			result.setMessage(e.getMessage());
			e.printStackTrace();
		}
		// return result.toString();
		return result;
	}

	@RequestMapping(value = "/warehourseList")
	@ResponseBody
	public List<CommonTreeVo> warehourseList() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("007");
	}

	@RequestMapping(value = "/costomerType")
	@ResponseBody
	public List<CommonTreeVo> costomerType() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("002");
	}

	@RequestMapping(value = "/resume")
	@ResponseBody
	public List<CommonTreeVo> resume() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("014");
	}

	@RequestMapping(value = "/voucherWord")
	@ResponseBody
	public List<CommonTreeVo> voucherWord() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("010");
	}

	@RequestMapping(value = "/getById")
	@ResponseBody
	public AuxiliaryAttrVo getById(String fid) {
		return auxiliaryAttrWebService.getByFid(fid);
	}

	@RequestMapping(value = "/settlementType")
	@ResponseBody
	public List<CommonTreeVo> settlementType() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("015");
	}

	@RequestMapping(value = "/assetType")
	@ResponseBody
	public List<CommonTreeVo> assetType() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("016");
	}
	/**
	 * 获取运输费用
	 * @return
	 */
	@RequestMapping(value = "/transitFee")
	@ResponseBody
	public List<CommonTreeVo> transitFee() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("018");
	}
	/**
	 * 获取运输方式
	 * @return
	 */
	@RequestMapping(value = "/transitType")
	@ResponseBody
	public List<CommonTreeVo> transitType() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("019");
	}
	/**
	 * 获取装运方式
	 * @return
	 */
	@RequestMapping(value = "/shipmentType")
	@ResponseBody
	public List<CommonTreeVo> shipmentType() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("020");
	}
	/**
	 * 获取运输费计价单位
	 * @return
	 */
	@RequestMapping(value = "/transitFeeUnit")
	@ResponseBody
	public List<CommonTreeVo> transitFeeUnit() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("021");
	}
	/**
	 * 获取场地类型
	 * @return
	 */
	@RequestMapping(value = "/spaceType")
	@ResponseBody
	public List<CommonTreeVo> spaceType() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("022");
	}
	/**
	 * 获取损耗地址
	 * @return
	 */
	@RequestMapping(value = "/transportLoss")
	@ResponseBody
	public List<CommonTreeVo> transportLoss() {
		return auxiliaryAttrWebService.findSubAuxiliaryAttrTree("023");
	}
	/**
	 * 维护ParentIds
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateOrgParentIds")
	public RequestResult updateOrgParentIds() {
		return auxiliaryAttrWebService.updateOrgParentIds();
	}


}