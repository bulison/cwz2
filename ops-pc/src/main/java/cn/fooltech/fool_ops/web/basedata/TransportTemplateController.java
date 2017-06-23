package cn.fooltech.fool_ops.web.basedata;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail1;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportTemplateDetail2;
import cn.fooltech.fool_ops.domain.basedata.service.TransportTemplateDetail1Service;
import cn.fooltech.fool_ops.domain.basedata.service.TransportTemplateDetail2Service;
import cn.fooltech.fool_ops.domain.basedata.service.TransportTemplateService;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateDetail1Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateDetail2Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportTemplateVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <p>运输费报价模板网页控制器类</p>
 * @author cwz
 * @date 2016-12-9
 */
@Controller
@RequestMapping(value = "/transportTemplate")
public class TransportTemplateController {
	
	/**
	 *运输费报价模板服务类
	 */
	@Autowired
	private TransportTemplateService templateService;
	/**
	 *运输费报价模板(从1表)服务类
	 */
	@Autowired
	private TransportTemplateDetail1Service detail1Service;
	/**
	 *运输费报价模板(从2表)服务类
	 */
	@Autowired
	private TransportTemplateDetail2Service detail2Service;
	
	
	/**
	 * 运输费报价模板弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/basedata/transportTemplate/templateWindow";
	}
	
	/**
	 * 运输费报价模板管理页面
	 * @param vo 
	 * @param page 当前页数
	 * @param rows 页面大小
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(TransportTemplateVo vo,PageParamater pageParamater, Model model){
//		PageJson result = templateService.queryJson(vo, page, rows);

		Page<TransportTemplateVo> page = templateService.query(vo,pageParamater);
		PageJson pageJson = new PageJson(page);
		model.addAttribute("result", pageJson);
		return "/basedata/transportTemplate/manage";
	}
	
	/**
	 * 运输费报价模板表信息(JSON)
	 * @param vo
	 * @param page 当前页数
	 * @param rows 页面大小
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public @ResponseBody PageJson list(TransportTemplateVo vo,PageParamater pageParamater, Model model){
		Page<TransportTemplateVo> query = templateService.query(vo, pageParamater);
		return templateService.getPageJson(query.getContent(), query.getTotalElements());
	}
	
	/**
	 * 运输费报价模板1列表
	 * @param templateId 	主表ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail")
	public @ResponseBody PageJson detail(String templateId, Model model){
		List<TransportTemplateDetail1> queryByTemplateId = detail1Service.queryByTemplateId(SecurityUtil.getFiscalAccountId(),templateId);
		List<TransportTemplateDetail1Vo> detail1 = detail1Service.getVos(queryByTemplateId);
		PageJson json = detail1Service.getPageJson(detail1, detail1.size());
		return json;
	}
	/**
	 * 运输费报价模板2列表
	 * @param templateId	主表ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail2")
	public @ResponseBody  JSONObject detail2(String templateId, Model model){
		List<TransportTemplateDetail1> queryByTemplateId = detail1Service.queryByTemplateId(SecurityUtil.getFiscalAccountId(),templateId);
		List<TransportTemplateDetail1Vo> detail1 = detail1Service.getVos(queryByTemplateId);
		List<TransportTemplateDetail2> list2 = detail2Service.queryByTemplateId(SecurityUtil.getFiscalAccountId(), templateId);
		List<TransportTemplateDetail2Vo> detail2 = detail2Service.getVos(list2);
		Map<String, List<JSONObject>> map = Maps.newHashMap();
		for (int i = 0; i < detail1.size(); i++) {
			String key=detail1.get(i).getCode();
			List<JSONObject> mapList = Lists.newArrayList();
			for (int j = 0; j < detail2.size(); j++) {
				String code = detail2.get(j).getCode();
				if (key.equals(code)) {
					mapList.add(JSONObject.fromObject(detail2.get(j)));
					
				}
			}
			map.put(key, mapList);
			
		}
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject;
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(){
		return "/basedata/transportTemplate/edit";
	}
	
	/**
	 * 编辑页面
	 * @param id 
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(String id, Model model){
		TransportTemplateVo templateVo = templateService.getById(id);
		model.addAttribute("obj", templateVo);
		return "/basedata/transportTemplate/edit";
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(TransportTemplateVo vo){
		try {
			return templateService.save(vo);
		} catch (Exception e) {
			e.printStackTrace();
			return new RequestResult(e.getMessage());
		}
	}
	
	/**
	 * 删除运输费报价模板信息
	 * @param id	模版id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(String id){
		return templateService.delete(id);
	}
	/**
	 * 启用/停用运输费报价模板
	 * @param id		模版id
	 * @param enable	状态
	 * @return
	 */
	@RequestMapping(value = "/changeEnable")
	@ResponseBody
	public RequestResult changeEnable(String id,Integer enable){
		return templateService.changeEnable(id, enable);
	}

	
}
