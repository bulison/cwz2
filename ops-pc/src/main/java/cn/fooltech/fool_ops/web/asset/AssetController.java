package cn.fooltech.fool_ops.web.asset;

import org.apache.commons.lang3.StringUtils;
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
import cn.fooltech.fool_ops.domain.asset.entity.Asset;
import cn.fooltech.fool_ops.domain.asset.service.AssetService;
import cn.fooltech.fool_ops.domain.asset.vo.AssetVo;
import cn.fooltech.fool_ops.web.base.BaseController;


/**
 * <p>固定资产卡片网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-12-28 14:23:18
 */
@Controller
@RequestMapping(value = "/asset")
public class AssetController extends BaseController{
	
	/**
	 * 固定资产卡片网页服务类
	 */
	@Autowired
	private AssetService assetService;
	
	/**
	 * 去固定资产卡片列表信息页面<br>
	 */
	@RequestMapping("/listAsset")
	public String listAsset(ModelMap model){
		return "/asset/listAsset/manage";
	}
	
	/**
	 * 查找固定资产卡片列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/queryAsset")
	public PageJson queryAsset(AssetVo assetVo,PageParamater pageParamater){
		Page<Asset> page = assetService.query(assetVo,pageParamater);
		PageJson pageJson = new  PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(assetService.getVos(page.getContent()));
		return pageJson;
	}
	
	/**
	 * 新增/编辑固定资产卡片页面
	 * @return
	 */
	@RequestMapping(value = "/editAsset")
	public String editAsset(ModelMap model, String fid){
		if(StringUtils.isNotBlank(fid)){
			AssetVo assetVo = assetService.getByFid(fid);
			model.put("asset",assetVo);
			model.put("subjectEdit", assetService.isEnableEdit(fid));
		}
	    return "/asset/listAsset/edit";
	}
	
	/**
	 * 新增/编辑固定资产卡片
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(AssetVo assetVo){
		return assetService.save(assetVo);
	}
	
	/**
	 * 删除固定资产卡片
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteAsset")
	public RequestResult deleteAsset(@RequestParam String fid){
		return assetService.delete(fid);
	}
	
	
	/**
	 * 计提
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveAccrued")
	public RequestResult saveAccrued(@RequestParam String fid){
		return assetService.saveAccrued(fid);
	}
	
	/**
	 * 反审核
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveCancleAudit")
	public RequestResult saveCancleAudit(@RequestParam String fid){
		return assetService.saveCancleAudit(fid);
	}
	
	/**
	 * 清算
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveClear")
	public RequestResult saveClear(@RequestParam String fid){
		return assetService.saveClear(fid);
	}
	
	/**
	 * 审核
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/savePassAudit")
	public RequestResult savePassAudit(@RequestParam String fid){
		return assetService.savePassAudit(fid);
	}
	
	/**
	 * 全计
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveAccruedAll")
	public RequestResult saveAccruedAll(){
		return assetService.saveAccruedAll();
	}
}