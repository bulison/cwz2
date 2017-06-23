package cn.fooltech.fool_ops.domain.common.excelProcessor;

/**
 * <p>处理器的抽象接口</p>
 * @author xjh
 * @version 1.0
 * @date 2015年9月23日
 */
public interface Processor<K,V> {

	/**
	 * 该方法用于导出
	 */
	public V process(K key);
	
	/**
	 * 该方法用于导入
	 */
	public K reprocess(V value);
}
