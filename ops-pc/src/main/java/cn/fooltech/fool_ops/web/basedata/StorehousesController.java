package cn.fooltech.fool_ops.web.basedata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.StorehousesService;
import cn.fooltech.fool_ops.domain.basedata.vo.StorehousesVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;


/**
 * <p>仓库(辅助属性)控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年3月18日
 */
@Controller
@RequestMapping("/storehouses")
public class StorehousesController {

	@Autowired
	private StorehousesService storehousesWebService;
	
	/**
	 * 管理界面
	 * @return
	 */
	@RequestMapping("/manage")
	public String manage(){
		return "/basedata/warehourseManage/manage";
	}
	
	/**
	 * 获取所有仓库信息（树状结构）
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTree")
	public List<CommonTreeVo> getTree(){
		return storehousesWebService.getTree();
	}
	
	/**
	 * 获取仓库明细信息
	 * @param fid 仓库ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getDetail")
	public StorehousesVo getDetail(String fid){
		return storehousesWebService.getDetail(fid);
	}
	
	/**
	 * 新增、编辑
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(StorehousesVo vo){
		return storehousesWebService.save(vo);
	}
	
	/**
	 * 删除
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid){
		return storehousesWebService.delete(fid);
	}
	
}
