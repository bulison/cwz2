package cn.fooltech.fool_ops.web.freight;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;

/**
 * <p>
 * 收货地址网页控制器
 * </p>
 * 
 * @author cwz
 * @date 2016-12-5
 */
@Controller
@RequestMapping("/freightAddress")
public class FreightAddressController {
	private Logger logger = LoggerFactory.getLogger(FreightAddressController.class);
	@Autowired
	private FreightAddressService service;

	/**
	 * 管理界面
	 */
	@RequestMapping("/manage")
	public String manage() {
		return "/basedata/freightAddress/freightAddress";
	}

	/**
	 * 收货地址树
	 * @return
	 */
	@RequestMapping(value = "/getTree")
	@ResponseBody
	public List<FreightAddressVo> getTree(FreightAddressVo vo){
		
		long time1 = System.currentTimeMillis();
		
		List<FreightAddressVo> treeData = service.getTree(vo);
		
		long time2 = System.currentTimeMillis();
		long delta = time2-time1;
		logger.debug("查询科目树耗时："+delta+"ms");
		
		return treeData;
		/*JsonConfig config = new JsonConfig();
		JSONArray array = JSONArray.fromObject(treeData, config);
		processAttributes(array, "attributes");
		return array.toString();*/
	}
	/**
	 *  获取货运地址表下拉树
	 * @return
	 */
	@RequestMapping(value = "/findAddressTree")
	@ResponseBody
    public List<FreightAddressVo> findAddressTree(FreightAddressVo vo) {
		 List<FreightAddressVo> list = service.findAddressTree(vo);
		return list;
    }

	/**
	 * 新增、编辑
	 * 
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(FreightAddressVo vo) {
		return service.save(vo);
	}

	/**
	 * 删除
	 * 
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid) {
		return service.delete(fid);
	}
	/**
	 * 调整父地址
	 * @param beforeFid  调整前的fid
	 * @param afterFid	   需要调整的fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeEntity")
	public RequestResult changeEntity(String beforeFid,String afterFid){
		return service.changeEntity(beforeFid, afterFid);
	}
	
	/**
	 * 根据父ID查找第一个子科目 
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryByParentId")
	public FreightAddressVo queryByParentId(String fid){
		return service.queryByParentId(fid);
				
	}
	/**
	 * 根据id查找父节点
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryFullParentById")
	public FreightAddressVo queryFullParentById(String fid){
		return service.queryFullParentById(fid);
		
	}
	
	

}
