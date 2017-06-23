package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.basedata.vo.UnitVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;


/**
 * 单位网页控制类
 * @author lzf
 * @version 1.0
 * @date 2015年6月26日
 * @update rqh 2015-09-06
 */
@Controller
@RequestMapping(value = "/unitController")
public class UnitController {
	
	@Autowired
	private UnitService unitService;
	
	/**
	 * 获取同级节点个数
	 * @param parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sameLevelCount")
	public int sameLevelCount(String parentId){
		if(StringUtils.isBlank(parentId))return 0;
		return unitService.sameLevelCount(parentId);
		
	}
	
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping("/unitManage")
	public String manage(Model model){
		UnitVo rootUnit = unitService.getRootUnit();
		model.addAttribute("rootId", rootUnit.getFid());
		return "unit/unitManage";
	}
	
	/**
	 * 获取所有单位
	 * @return
	 */
	@RequestMapping("/getAll")
	@ResponseBody
	public List<CommonTreeVo> getAll(){
		return unitService.getAll();
	}
	
	/**
	 * 获取全部有效的单位
	 * @return
	 */
	@RequestMapping("/getTree")
	@ResponseBody
	public List<CommonTreeVo> getTree(){
		return unitService.getTree();
	}
	
	/**
	 * 获取单位组下所有有效的单位
	 * @return
	 */
	@RequestMapping("/getPartTree")
	@ResponseBody
	public List<CommonTreeVo> getPartTree(String unitGroupId){
		if(StringUtils.isNotBlank(unitGroupId)){
			return unitService.getPartTree(unitGroupId);
		}
		else{
			return unitService.getTree();
		}
	}
	
	/**
	 * 获取单位组下的有效单位
	 * @param unitGroupId 单位组ID
	 * @return
	 */
	@RequestMapping("/getChilds")
	@ResponseBody
	public List<UnitVo> getChilds(String unitGroupId){
		return unitService.getChilds(unitGroupId);
	}
	
	/**
	 * 获取单位组下的有效单位，通过单位名称模糊匹配
	 * @param unitGroupId 单位组ID
	 * @param unitName 单位名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getChildsOfMatch")
	public List<UnitVo> getChildsOfMatch(String unitGroupId, @RequestParam("q") String unitName){
		return unitService.getChildsOfMatch(unitGroupId, unitName);
	}
	
	/**
	 * 获取单位
	 * @param id 货品单位ID
	 * @return
	 */
	@RequestMapping("/get")
	@ResponseBody
	public UnitVo get(String id){
		return unitService.getById(id);
	}
	
	/**
	 * 添加页面
	 * @param parentId 父单位ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/add")
	public String add(String parentId, Model model){
		if(!StringUtils.isBlank(parentId)){
			model.addAttribute("parentId", parentId);
			model.addAttribute("scaleOne", unitService.findAccountUnit(parentId));
		}
		return "unit/addUnit";
	}
	
	/**
	 * 编辑页面
	 * @param id 单位ID
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(String id, Model model){
		UnitVo unit = unitService.getById(id);
		model.addAttribute("unit", unit);
		model.addAttribute("scaleOne", unitService.findAccountUnit(unit.getParentId()));
		return "unit/editUnit";
	}
	
	/**
	 * 添加、编辑
	 * @param vo
	 */
	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(UnitVo vo){
		return unitService.save(vo);
	}
	
	/**
	 * 删除
	 * @param id 单位ID
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id){
		return unitService.delete(id);
	}
	
	/**
	 * 单位详情
	 * @return
	 */
	@RequestMapping("/detail")
	public String detail(String id, Model model){
		if(!StringUtils.isBlank(id)){
			UnitVo unit = unitService.getById(id);
			model.addAttribute("unit", unit);
		}
		return "unit/unitDetail";
	}
	
	/**
	 * 获取机构下的叶子单位
	 * @return
	 */
	@RequestMapping("/getLeafUnit")
	@ResponseBody
	public List<UnitVo> getLeafUnit(){
		return unitService.getLeafUnit();
	}
	
	/**
	 * 通过货品ID获取单位组ID
	 * @return
	 */
	@RequestMapping("/getByGoodsId")
	@ResponseBody
	public String getByGoodsId(String goodsId){
		return unitService.getByGoodsId(goodsId).getFid();
	}
	
    /**
	* 根据父ID获取同样父ID的换算关系=1的记账单位
	* @return
	*/
	@RequestMapping("/findAccountUnit")
	@ResponseBody
	public UnitVo findAccountUnit(String parentId){
		UnitVo vo= unitService.getVo(unitService.findAccountUnit(parentId));
		if(vo!=null){
			return vo;
		}else{
			return new UnitVo();
		}
	}


}
