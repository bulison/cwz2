package cn.fooltech.fool_ops.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.domain.flow.vo.FlowOperation;

/**
 * 流程按钮操作服务类
 * @author xjh
 *
 */
@Service("ops.FlowMobileOperationService")
public class FlowMobileOperationService {

	/**
	 * 从xml注入
	 */
	private Map<String,List<FlowOperation>> operationMap = Maps.newLinkedHashMap();

	public Map<String,List<FlowOperation>> getOperationMap() {
		return operationMap;
	}

	public void setOperationMap(Map<String,List<FlowOperation>> operationMap) {
		this.operationMap = operationMap;
	}
}
