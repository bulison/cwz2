package cn.fooltech.fool_ops.web.asset;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.asset.service.AssetDetailService;
import cn.fooltech.fool_ops.domain.asset.vo.AssetDetailVo;
import cn.fooltech.fool_ops.web.base.BaseController;



/**
 * <p>固定资产计提网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-12-28 14:24:15
 */
@Controller
@RequestMapping(value = "/assetdetail")
public class AssetDetailController {//extends BaseController{
	
	/**
	 * 固定资产计提网页服务类
	 */
	@Autowired
	private AssetDetailService assetDetailService;
	
	
	/**
	 * 查找固定资产计提列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/queryDetail")
	public List<AssetDetailVo> queryDetail(@RequestParam String assetId){
		return assetDetailService.query(assetId);
	}
	
	/**
	 * 删除固定资产计提
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteAssetDetail")
	public RequestResult deleteAssetDetail(@RequestParam String fid){
		return assetDetailService.delete(fid);
	}
	
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryByPage")
	public PageJson queryByPage(AssetDetailVo vo, PageParamater paramater){
		return assetDetailService.queryByPage(vo, paramater);
	}


	@ResponseBody
	@RequestMapping("/testaaa")
	public Date testaaa(){
		return new Date();
	}

	@ResponseBody
	@RequestMapping("/testbbb")
	public TestVo testbbb(){
		TestVo a= new TestVo();
		a.setAdate(new Date());
		a.setBdate(new Date());
		a.setCdate(new Date());
		a.setName("aaaa");
		return a;
	}
}