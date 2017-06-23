package cn.fooltech.fool_ops.domain.report.script;

import java.util.List;
import java.util.Map;

/**
 * 处理脚本接口
 * @author xjh
 *
 */
public interface ScriptProcessor {

	public Map<String, Object> process(List<Object[]> result);
}
