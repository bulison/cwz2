package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.TransportFeeUnitService;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrVo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportFeeUnitVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.utils.StringUtils;
import cn.fooltech.fool_ops.utils.tree.TreeVo;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
/**
 * <p>运输单位(辅助属性)控制器</p>
 * @author hjr
 * @date 2017年3月9日
 */
@Controller
@RequestMapping(value = "/transportFeeUnit")
public class TransportFeeUnitController {
	/**
	 * 运输单位(辅助属性)网页服务类
	 */
	@Autowired
	private AuxiliaryAttrService auxiliaryAttrWebService;
	@Autowired
	private TransportFeeUnitService transportFeeUnitService;
	/**
	 * 去运输单位(辅助属性)列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String listAuxiliaryAttr(ModelMap model) {
		return "/basedata/transportFeeUnit/manage";
	}
/*	*//**
	 * 根据分类编码查找某一分类的所有运输单位(辅助属性)
	 *//*
	@RequestMapping("/findTransportFeeUnitTree")
	@ResponseBody
	public List<CommonTreeVo> findTransportUnitAttrTree(HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		if (StringUtils.isNotBlank(code))
			return auxiliaryAttrWebService.findSubAuxiliaryAttrTree(code);
		return null;
	}
*/
	/**
	 * 新增/编辑运输单位(辅助属性)
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveTransportFeeUnit", method = RequestMethod.POST)
	public RequestResult save(TransportFeeUnitVo transportFeeUnitVo) {
		String inValid = ValidatorUtils.inValidMsg(transportFeeUnitVo);
		if (inValid != null) {
			return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
		}
		return transportFeeUnitService.save(transportFeeUnitVo);
	}

	/**
	 * 删除运输单位(辅助属性)
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteTransportFeeUnit")
	public RequestResult deleteTransportUnit(@RequestParam String fid) throws Exception {
		// JSONObject result = new JSONObject();
		RequestResult result = null;
		try {
			result = transportFeeUnitService.delete(fid);
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
 /**
	 *  获取运输费计价单位
	 * 
	 * @return
	 *
	 */
	@RequestMapping(value = "/query")
	@ResponseBody
	public PageJson query(PageParamater pageParamater) {
		return new PageJson(transportFeeUnitService.query(pageParamater));
	}
	
}
