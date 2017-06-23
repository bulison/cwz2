package cn.fooltech.fool_ops.domain.report.script.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.domain.report.script.ScriptProcessor;

public class BalanceTrailScirptProcessor implements ScriptProcessor{

	@Override
	public Map<String, Object> process(List<Object[]> result) {
		Map<String, Object> map = Maps.newHashMap();
		if(result!=null&&result.size()>0){
			Object[] objs = result.get(result.size()-1);
			map.put("msg", objs[1].toString());
		}
		return map;
	}
}
