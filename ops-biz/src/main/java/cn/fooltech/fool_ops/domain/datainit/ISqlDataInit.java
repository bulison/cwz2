package cn.fooltech.fool_ops.domain.datainit;

import java.util.LinkedHashMap;

/**
 * <p>数据初始化接口</p>
 * @author xjh
 * @version 1.0
 * @date 2015年9月15日
 */
public interface ISqlDataInit {

	public boolean init(LinkedHashMap<String, String> params);
}
