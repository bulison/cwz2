package cn.fooltech.fool_ops.web.sysman;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.sysman.entity.CommonUseModule;
import cn.fooltech.fool_ops.domain.sysman.service.CommonUseModuleService;

/**
 * <p>用户常用模块网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年2月29日
 */
@Controller
@RequestMapping("/commonUseModule")
public class CommonUseModuleController {

	@Autowired
	public CommonUseModuleService moduleService;
	
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping("/manage")
	public String manage(){
		return "indexPage/stock";
	}
	
	/**
	 * 查询常用模块(资源)的ID
	 */
	@ResponseBody
	@RequestMapping("/query")
	public Object query(){
		List<CommonUseModule> modules = moduleService.query();
		
		int size = modules.size();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("{");
		for(int i=0; i<size; i++){
			CommonUseModule module  = modules.get(i);
			stringBuffer.append("\"");
			stringBuffer.append(module.getResource().getFid());
			stringBuffer.append("\"");
			stringBuffer.append(":");
			stringBuffer.append("\"");
			stringBuffer.append(module.getResource().getFid());
			stringBuffer.append("\"");
			if(i < size - 1){
				stringBuffer.append(",");
			}
		}
		stringBuffer.append("}");
		return JSONArray.parse(stringBuffer.toString());
	}
	
	/**
	 * 新增(没有编辑)
	 * @param resourceIds
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(@RequestParam String resourceIds){
		return moduleService.save(resourceIds);
	}

}
