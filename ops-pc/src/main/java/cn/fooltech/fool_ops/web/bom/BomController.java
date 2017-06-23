package cn.fooltech.fool_ops.web.bom;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.bom.entity.Bom;
import cn.fooltech.fool_ops.domain.bom.service.BomDetailService;
import cn.fooltech.fool_ops.domain.bom.service.BomService;
import cn.fooltech.fool_ops.domain.bom.vo.BomDetailVo;
import cn.fooltech.fool_ops.domain.bom.vo.BomVo;
import cn.fooltech.fool_ops.domain.sysman.service.UserAttrService;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>单据物料网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-04-05 10:09:13
 */
@Controller
@RequestMapping(value = "/bom")
public class BomController extends BaseController{
	
	/**
	 * 单据物料网页服务类
	 */
	@Autowired
	private BomService bomService;
	
	/**
	 * 单据物料明细网页服务类
	 */
	@Autowired
	private BomDetailService bomDetailService;
	
	/**
	 * 用户属性服务类
	 */
	@Autowired
	private UserAttrService userAttrService;
	
	/**
	 * 单据物料list
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageJson list(BomVo vo,PageParamater pageParamater){
		Page<BomVo> page = bomService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 * bom表管理页面
	 */
	@RequestMapping("/manage")
	public String manage(ModelMap model) {
		return "/warehourse/bom/bomManage";
	}
	
	/**
	 * bom表编辑页面
	 */
	@RequestMapping("/edit")
	public String edit(String id, @RequestParam(value = "mark", defaultValue = "0") Integer mark, 
			Model model) {
		if(StringUtils.isNotBlank(id)){
			BomVo vo=bomService.getById(id);
			if(mark == 1) {
				vo.setFid(null);
				vo.setEnable(Short.parseShort("0"));
			}
			model.addAttribute("obj", vo);
		}
		String localCache = userAttrService.getLocalCache();
		model.addAttribute("localCache", localCache);
		return "/warehourse/bom/bomEdit";
	}
	
	/**
	 * bom表编辑页面
	 */
	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(BomVo vo) {
		return bomService.save(vo);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id) {
		return bomService.delete(id);
	}
	
	/**
	 * 启用
	 */
	@RequestMapping("/launch")
	@ResponseBody
	public RequestResult launch(String id) {
		return bomService.updateEnable(id);
	}
	
	/**
	 * 停用
	 */
	@RequestMapping("/stop")
	@ResponseBody
	public RequestResult stop(String id) {
		return bomService.updateDisable(id);
	}
	
	/**
	 * 默认
	 */
	@RequestMapping("/default")
	@ResponseBody
	public RequestResult defaulter(String id) {
		return bomService.updateDefault(id);
	}
	
	/**
	 * 根据物料查询明细
	 */
	@RequestMapping("/bomDetails")
	@ResponseBody
	public List<BomDetailVo> bomDetails(String goodsId, String specId) {
		return bomDetailService.queryDefaultBomDetails(goodsId, specId);
	}
}