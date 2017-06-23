package cn.fooltech.fool_ops.web.basedata;

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
import cn.fooltech.fool_ops.domain.basedata.service.GoodsBarService;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsBarVo;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>货品条码网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-09-12 15:21:20
 */
@Controller
@RequestMapping(value = "/basedata/goodsBar")
public class GoodsBarController extends BaseController{
	
	/**
	 * 货品条码网页服务类
	 */
	@Autowired
	private GoodsBarService barService;
	
	/**
	 * 去货品条码列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String listGoodsBar(ModelMap model){
		return "/basedata/goodsBar/manage";
	}
	
	/**
	 * 查找货品条码列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(GoodsBarVo vo,PageParamater pageParamater){
		Page<GoodsBarVo> page = barService.query(vo, pageParamater);
		return new PageJson(page);
	}
	
	/**
	 * 根据条码查找货品信息<br>
	 */
	@ResponseBody
	@RequestMapping("/queryByBar")
	public GoodsBarVo queryByBar(String barCode){
		return barService.queryByBar(barCode);
	}
	
	/**
	 * 编辑货品条码页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(ModelMap model, @RequestParam(value = "mark", defaultValue = "0") Integer mark, String id){
		
		//==1则为修改
		if(mark==1){
			GoodsBarVo goodsBarVo = barService.getById(id);
			model.put("goodsBar",goodsBarVo);
		}
		
		return "/basedata/goodsBar/edit";
	}
	
	/**
	 * 新增/编辑货品条码
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(GoodsBarVo vo){
		return webService.save(vo);
	}*/
	
	/**
	 * 新增/编辑货品条码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveMulti",method=RequestMethod.POST)
	public RequestResult saveMulti(String vos, String goodsId){
		return barService.saveMulti(vos, goodsId);
	}
	
	/**
	 * 删除货品条码
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String id){
		return barService.delete(id);
	}
	
}