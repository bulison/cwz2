package cn.fooltech.fool_ops.web.sysman;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.flow.vo.PlanVo;
import cn.fooltech.fool_ops.domain.sysman.entity.CommonUseModule;
import cn.fooltech.fool_ops.domain.sysman.service.CommonUseModuleService;
import cn.fooltech.fool_ops.domain.sysman.service.SmgOrgAttrService;
import cn.fooltech.fool_ops.domain.sysman.vo.SmgOrgAttrVo;

/**
 * 预警阈值设置	操作类
 * @author cwz
 *
 */
@Controller
@RequestMapping("/smgOrgAttr")
public class SmgOrgAttrController {

	@Autowired
	public SmgOrgAttrService smgOrgAttrService;
	
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping("/manage")
	public String manage(){
		return "sysmanWeb/smgOrgAttrManage";
	}
	
	/**
	 * 查询配置列表
	 */
	@ResponseBody
	@RequestMapping("/query")
	public Page<SmgOrgAttrVo> query(SmgOrgAttrVo vo,PageParamater paramater){
		Page<SmgOrgAttrVo> query = smgOrgAttrService.query(vo, paramater);
		return query;
	}
	/**
	 * 获取计划信息
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getById")
	public SmgOrgAttrVo getById(String fid) throws Exception{
		return smgOrgAttrService.getById(fid);
	}
	
	/**
	 * 新增修改配置
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(SmgOrgAttrVo vo){
		return smgOrgAttrService.save(vo);
	}
	
	/**
	 * 检查资金池预警额度,如果不高于，则需要预警,自动发送信息给“发送预警人”
	 */
	@RequestMapping("/checkWarningQuota")
	public void checkWarningQuota(){
		smgOrgAttrService.checkWarningQuota();
	}

}
