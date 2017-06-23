package cn.fooltech.fool_ops.web.flow;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.flow.entity.MsgWarnSetting;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.service.MsgWarnSettingService;
import cn.fooltech.fool_ops.domain.flow.service.SuperviseService;
import cn.fooltech.fool_ops.domain.flow.vo.MsgWarnSettingVo;
import cn.fooltech.fool_ops.domain.flow.vo.SuperviseVo;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>消息预警配置网页控制器类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-05-17 14:08:34
 */
@Controller
@RequestMapping(value = "/flow/msgSetting")
public class MsgWarnSettingController extends BaseController{
	
	/**
	 * 消息预警配置网页服务类
	 */
	@Autowired
	private MsgWarnSettingService webService;
	
	/**
	 * 监督网页服务类
	 */
	@Autowired
	private SuperviseService superviseWebService;
	
	/**
	 * 去消息预警配置列表信息页面<br>
	 */
	@RequestMapping("/manage")
	public String listMsgWarnSetting(ModelMap model){
		return "/flow/msgSetting/manage";
	}
	
	/**
	 * 查找消息预警配置列表信息<br>
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(MsgWarnSettingVo vo,PageParamater pageParamater){
		Page<MsgWarnSetting> page = webService.query(vo, pageParamater);
		PageJson pageJson = new PageJson();
		pageJson.setTotal(page.getTotalElements());
		pageJson.setRows(webService.getVos(page.getContent()));
		return pageJson;
	}
	
	/**
	 * 编辑消息预警配置页面
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String edit(ModelMap model, @RequestParam(value = "mark", defaultValue = "0") Integer mark, String id){
		
		//==1则为修改
		if(mark==1){
			MsgWarnSettingVo vo = webService.getByFid(id);
			model.put("msgWarnSetting",vo);
			
			if(vo.getSendType()==MsgWarnSetting.SEND_TYPE_GSJDR||vo.getSendType()==MsgWarnSetting.SEND_TYPE_BMJDR){
				List<SuperviseVo> supervises = superviseWebService.queryBySettingId(vo.getFid(), vo.getSendType());
				model.put("supervises",supervises);
			}
		}
		
		return "/flow/msgSetting/edit";
	}
	
	/**
	 * 新增/编辑消息预警配置
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public RequestResult save(MsgWarnSettingVo vo){
		return webService.save(vo);
	}
	
	/**
	 * 删除消息预警配置
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public RequestResult delete(@RequestParam String id){
		return webService.delete(id);
	}
	

	
	/**
	 * 获取场景Map映射
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSceneMap")
	public String getSceneMap(){
		Map<Integer, String> map = Maps.newLinkedHashMap();
		map.put(Plan.STATUS_DRAFT, "计划草稿");
		map.put(Plan.STATUS_TO_CHECK, "计划已提交待审核");
		map.put(Plan.STATUS_EXECUTING, "计划执行中");
		map.put(Plan.STATUS_DELAYED, "计划已延迟");
		map.put(Plan.STATUS_STOPED, "计划终止");
		map.put(Plan.STATUS_FINISHED, "计划完成");
		map.put(Task.STATUS_DRAFT, "事件草稿");
		map.put(Task.STATUS_EXECUTING, "事件办理中");
		map.put(Task.STATUS_EXCUTED_CHECK, "事件已办理待审核");
		map.put(Task.STATUS_FINISHED, "事件已完成");
		map.put(Task.STATUS_DELAYED_UNSTART, "事件已延迟且未开始办理");
		map.put(Task.STATUS_DELAYED_UNFINISH, "事件已延迟且未结束办理");
		map.put(Task.STATUS_STOPED, "事件已终止");
		
		JSONArray array = new JSONArray();
		for(Integer key:map.keySet()){
			JSONObject json = new JSONObject();
			json.put("id", key);
			json.put("text", map.get(key));
			array.add(json);
		}
		
		return array.toString();
	}
	
	/**
	 * 获取场景Map映射
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTriggerMap")
	public String getTriggerMap(){
		Map<Integer, String> map = Maps.newLinkedHashMap();
		map.put(Message.TRIGGER_TYPE_SUBMIT, "提交");
		map.put(Message.TRIGGER_TYPE_UPDATE, "修改");
		map.put(Message.TRIGGER_TYPE_DELETE, "删除");
		map.put(Message.TRIGGER_TYPE_STOP, "终止");
		map.put(Message.TRIGGER_TYPE_CHANGE_UNDERTAKER, "变更承办人");
		map.put(Message.TRIGGER_TYPE_CHANGE_PRINCIPALER, "变更责任人");
		map.put(Message.TRIGGER_TYPE_DELAY, "申请延迟 ");
		map.put(Message.TRIGGER_TYPE_CHECK, "审核");
		map.put(Message.TRIGGER_TYPE_CHECK_EXECUTE_YES, "审核通过办理");
		map.put(Message.TRIGGER_TYPE_CHECK_EXECUTE_NO, "审核不通过办理");
		map.put(Message.TRIGGER_TYPE_CHECK_DELAY_YES, "审核通过申请延迟");
		map.put(Message.TRIGGER_TYPE_CHECK_DELAY_NO, "审核不通过申请延迟");
		map.put(Message.TRIGGER_TYPE_NEW, "新建、分派");
		map.put(Message.TRIGGER_TYPE_COMPLETE, "完成");
		map.put(Message.TRIGGER_TYPE_EXECUTE_END, "办理结束");
		map.put(Message.TRIGGER_TYPE_EXECUTE_START, "办理开始");
		map.put(Message.TRIGGER_TYPE_EVALUATE, "评价");
		map.put(Message.TRIGGER_TYPE_SCORE, "评分");
		map.put(Message.TRIGGER_TYPE_FOLLOW, "关注");
		map.put(Message.TRIGGER_TYPE_CHAT, "留言");
		map.put(Message.TRIGGER_TYPE_RELEVANCE, "关联");
		map.put(Message.TRIGGER_TYPE_EARLY_REMIND, "提醒");
		map.put(Message.TRIGGER_TYPE_DELAY_ALARM, "延迟报警");
		map.put(Message.TRIGGER_TYPE_YIELD_ALARM, "收益率报警");
		map.put(Message.TRIGGER_TYPE_STOCK_ALARM, "库存报警");
		map.put(Message.TRIGGER_TYPE_PURCHASE_PRODUCE, "库存不足触发采购/生产");
		
		JSONArray array = new JSONArray();
		for(Integer key:map.keySet()){
			JSONObject json = new JSONObject();
			json.put("id", key);
			json.put("text", map.get(key));
			array.add(json);
		}
		
		return array.toString();
	}
}