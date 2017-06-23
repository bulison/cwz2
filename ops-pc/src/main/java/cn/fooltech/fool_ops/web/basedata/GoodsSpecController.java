package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsSpecService;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsSpecVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.utils.StrUtil;


/**
 * <p>货品属性网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月8日
 */
@RequestMapping("/goodsspec")
@Controller
public class GoodsSpecController {
	
	/**
	 * 货品属性服务类
	 */
	@Autowired
	private GoodsSpecService specService;
	
	/**
	 * 货品服务类
	 */
	@Autowired
	private GoodsService goodsService;
	
	
	/**
	 * 货品属性弹出窗口
	 * @return
	 */
	@RequestMapping(value = "/window")
	public String window(){
		return "/basedata/goodsSpecWindow";
	}
	
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping("/manage")
	public String manage(Model model){
		GoodsSpecVo vo=specService.getRootGoodsSpec();
		model.addAttribute("rootId", vo.getFid());
		return "basedata/goodsSpec/goodsSpecManage";
	}
	
	/**
	 * 获取货品属性
	 * @param id 货品属性ID
	 * @return
	 */
	@RequestMapping("/get/{fid}")
	public String get(ModelMap model, @PathVariable("fid") String fid){
		if(StringUtils.isNotBlank(fid)&&!StrUtil.isSame(fid, "init")){
			GoodsSpecVo goodsSpecVo = specService.getById(fid);
			model.put("goodsSpecVo",goodsSpecVo);
		}
		return "basedata/addGoodsSpec";
	}
	
	/**
	 * 获取货品属性
	 * @param id 货品属性ID
	 * @return
	 */
	@RequestMapping("/getById")
	@ResponseBody
	public GoodsSpecVo get(String id){
		GoodsSpecVo goodsSpecVo = specService.getById(id);
		return goodsSpecVo;
	}
	
	/**
	 * 获取所有货品属性
	 * @return
	 */
	@RequestMapping("/getAll")
	@ResponseBody
	public List<CommonTreeVo> getAll(){
		return specService.getAll();
	}
	
	/**
	 * 获取全部有效的货品属性
	 * @return
	 */
	@RequestMapping("/getTree")
	@ResponseBody
	public List<CommonTreeVo> getTree(){
		return specService.getTree();
	}
	
	/**
	 * 获取货品属性组下有效的属性
	 * @param groupId 属性组ID
	 * @return
	 */
	@RequestMapping("/getPartTree")
	@ResponseBody
	public List<CommonTreeVo> getPartTree(String groupId){
		return specService.getPartTree(groupId);
	}
	
	/**
	 * 获取全部有效的货品组
	 * @return
	 */
	@RequestMapping("/getSpecGroups")
	@ResponseBody
	public List<CommonTreeVo> getSpecGroups(){
		return specService.getSpecGroups();
	}
	
	/**
	 * 获取货品属性组下的子属性
	 * @param groupId 属性组ID
	 * @param name 货品属性名称
	 * @param paramater
	 * @return
	 */
	@RequestMapping("/getChilds")
	@ResponseBody
	public PageJson getChilds(String groupId, String name, PageParamater paramater){
		return new PageJson(specService.getChilds(groupId, name, paramater));
	}
	
	/**
	 * 获取货品属性组下的子属性
	 * @param groupId 货品属性组ID
	 * @param name 货品属性名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getChidlList")
	public List<GoodsSpecVo> getChidlList(String groupId, @RequestParam(value="q", required = false) String name){
		return specService.getChidlList(groupId, name);
	}
	
	/**
	 * 添加页面
	 * @param parentId 父货品属性ID
	 * @return
	 */
	@RequestMapping("/add")
	public String add(String parentId, Model model){
		model.addAttribute("parentId", parentId);
		return "basedata/addGoodsSpec";
	}
	
	/**
	 * 编辑页面
	 * @param id 货品属性ID
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(String id, Model model){
		GoodsSpecVo goodsSpec = specService.getById(id);
		model.addAttribute("goodsSpec", goodsSpec);
		return "basedata/editGoodsSpec";
	}
	
	/**
	 * 添加、编辑
	 * @param vo
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public RequestResult save(GoodsSpecVo vo){
		return specService.save(vo);
	}
	
	/**
	 * 删除
	 * @param id 货品属性ID
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RequestResult delete(String id){
		return specService.delete(id);
	}
	
	/**
	 * 获取某个机构全部有效的叶子货品属性
	 * @return
	 */
	@RequestMapping("/getLeafSpec")
	@ResponseBody
	public List<GoodsSpecVo> getLeafSpec(){
		return specService.getLeafSpec();
	}
	
	/**
	 * 通过货品ID获取属性组ID
	 * @return
	 */
	@RequestMapping("/getByGoodsId")
	@ResponseBody
	public String getByGoodsId(String goodsId){
		return goodsService.getById(goodsId).getGoodsSpecGroupId();
	}
	
	/**
	 * 模糊搜索货品属性(根据编号、名称)
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/vagueSearch")
	public List<GoodsSpecVo> vagueSearch(GoodsSpecVo vo){
		return specService.vagueSearch(vo);
	}
	
}
