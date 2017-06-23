/**
 *
 */
package cn.fooltech.fool_ops.utils;

import org.springframework.stereotype.Component;


/**
 * PO与VO转换工厂
 *
 * @author Thomas Zheng
 */
@Component
public class VoFactory {


    /**
     * @param <T>
     * @param <V>
     * @param type
     * @param value
     * @return
     */
    public static <T, V> T createValue(Class<T> type, V value) {
        T typeInstance;
        try {
            typeInstance = type.newInstance();
            BeanUtil.copyNotNullProperties(typeInstance, value);
        } catch (Exception e) {
            typeInstance = null;
        }
        return typeInstance;
    }

    /**
     * @param <T>
     * @param <V>
     * @param type
     * @param value
     * @return
     */
    public <T, V> T create(Class<T> type, V value) {
        T typeInstance;
        try {
            typeInstance = type.newInstance();
            BeanUtil.copyNotNullProperties(typeInstance, value);
        } catch (Exception e) {
            typeInstance = null;
        }
        return typeInstance;
    }


}
