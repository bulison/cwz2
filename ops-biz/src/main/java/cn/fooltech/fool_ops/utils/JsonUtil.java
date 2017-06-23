package cn.fooltech.fool_ops.utils;

import cn.fooltech.fool_ops.component.exception.ParseException;
import cn.fooltech.fool_ops.utils.tree.TreeFilter;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public abstract class JsonUtil {

    public static ObjectMapper DefaultMapper = new ObjectMapper();
    public static ObjectMapper DateTimeMapper = new ObjectMapper();
    public static ObjectMapper EasyUiTreeMapper = new ObjectMapper();

    static {

        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        DefaultMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // JSON与对象属性不匹配时不抛异常
        DefaultMapper.setSerializationInclusion(Include.NON_NULL);  // 不转换结果对象中值为null的属性
        DefaultMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); // 日期类型格式化
        DefaultMapper.setTimeZone(tz);

        DateTimeMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // JSON与对象属性不匹配时不抛异常
        DateTimeMapper.setSerializationInclusion(Include.NON_NULL);  // 不转换结果对象中值为null的属性
        DateTimeMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); // 日期类型格式化
        DateTimeMapper.setTimeZone(tz);

        EasyUiTreeMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // JSON与对象属性不匹配时不抛异常
        EasyUiTreeMapper.setSerializationInclusion(Include.NON_NULL);  // 不转换结果对象中值为null的属性
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("treeFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("id", "text", "children", "parentId", "state"));
        EasyUiTreeMapper.setFilterProvider(filterProvider);
        EasyUiTreeMapper.setTimeZone(tz);
    }

    /**
     * 对象转JSON字符串
     *
     * @param obj
     * @return (obj == null) ==> null
     */
    public static String toJsonString(Object obj) {
        if (obj != null) {
            try {
                return DefaultMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new ParseException("对象转换成JSON字符串失败", e);
            }
        } else {
            return null;
        }
    }

    /**
     * 对象转JSON字符串
     *
     * @param obj
     * @return (obj == null) ==> null
     */
    public static String toJsonString(Object obj, ObjectMapper mapper, Class<?> clazz) {
        if (obj != null) {
            try {
                Map<Class<?>, Class<?>> map = Maps.newHashMap();
                map.put(clazz, TreeFilter.class);
                mapper.setMixIns(map);
                return mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new ParseException("对象转换成JSON字符串失败", e);
            }
        } else {
            return null;
        }
    }

    /**
     * JSON字符串转对象：{}
     *
     * @param jsonString
     * @param clazz
     * @return (isEmpty(jsonString)) ==> null
     */
    public static <T> T toObject(String jsonString, Class<T> clazz) {
        if (StringUtils.isNotEmpty(jsonString)) {
            try {
                return DefaultMapper.readValue(jsonString, clazz);
            } catch (Exception e) {
                throw new ParseException("JSON字符串转换成对象失败", e);
            }
        } else {
            return null;
        }
    }

    /**
     * JSON字符串转对象：{}
     *
     * @param jsonString
     * @param clazz
     * @return (isEmpty(jsonString)) ==> null
     */
    public static <T> T toObject(String jsonString, Class<T> clazz, ObjectMapper mapper) {
        if (StringUtils.isNotEmpty(jsonString)) {
            try {
                return mapper.readValue(jsonString, clazz);
            } catch (Exception e) {
                throw new ParseException("JSON字符串转换成对象失败", e);
            }
        } else {
            return null;
        }
    }

    /**
     * JSON字符串转Map：{key:{}}
     *
     * @param jsonString
     * @param clazz
     * @return (isEmpty(jsonString)) ==> new HashMap();
     */
    @SuppressWarnings("unchecked")
    public static Map<String, ?> toObjectMap(String jsonString) {
        if (StringUtils.isNotEmpty(jsonString)) {
            try {
                return DefaultMapper.readValue(jsonString, Map.class);
            } catch (Exception e) {
                throw new ParseException("JSON字符串转换成对象失败", e);
            }
        } else {
            return new HashMap<String, Object>();
        }
    }

    /**
     * JSON字符串转Map：{key:{}}
     *
     * @param jsonString
     * @param clazz
     * @return (isEmpty(jsonString)) ==> new HashMap();
     */
    @SuppressWarnings("unchecked")
    public static Map<String, ?> toObjectMap(String jsonString, ObjectMapper mapper) {
        if (StringUtils.isNotEmpty(jsonString)) {
            try {
                return mapper.readValue(jsonString, Map.class);
            } catch (Exception e) {
                throw new ParseException("JSON字符串转换成对象失败", e);
            }
        } else {
            return new HashMap<String, Object>();
        }
    }

//	public static void main(String[] args) throws JsonProcessingException {
//		//String json = "{\"action\":\"queryVersion\",\"module\":\"mobileManagerService\",\"params\":{\"appName\":\"android\",\"versionCode\":1,\"versionName\":\"1.0\"}}";
//		//Map<String, ?> r = toObjectMap(json);
//		//System.out.println(r);
//
//
//		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
//		//filterProvider.setDefaultFilter(SimpleBeanPropertyFilter.filterOutAllExcept("code"));
//		filterProvider.addFilter("treeFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "code"));
//		//filterProvider.addFilter("treeFilter", SimpleBeanPropertyFilter.serializeAllExcept("code"));
//
//		MenuVo vo = new MenuVo();
//		vo.setCode("123");
//		vo.setId("456");
//
//		List<MenuVo> list = Lists.newArrayList(vo);
//
//
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // JSON与对象属性不匹配时不抛异常
//		mapper.setSerializationInclusion(Include.NON_NULL);  // 不转换结果对象中值为null的属性
//		mapper.setFilterProvider(filterProvider);
//
//		Map<Class<?>, Class<?>> map = Maps.newHashMap();
//		map.put(MenuVo.class, TreeFilter.class);
//		mapper.setMixIns(map);
//		System.out.println(mapper.writeValueAsString(vo));
//	}

    /**
     * JSON字符串转List：[{}]
     *
     * @param jsonString
     * @param clazz
     * @return (isEmpty(jsonString)) ==> new ArrayList();
     */
    public static <T> List<T> toObjectList(String jsonString, Class<T> clazz) {
        if (StringUtils.isNotEmpty(jsonString)) {
            try {
                return DefaultMapper.readValue(jsonString, getParametricJavaType(ArrayList.class, clazz));
            } catch (Exception e) {
                throw new ParseException("JSON字符串转换成对象失败", e);
            }
        } else {
            return new ArrayList<T>();
        }
    }

    /**
     * JSON字符串转List：[{}]
     *
     * @param jsonString
     * @param clazz
     * @return (isEmpty(jsonString)) ==> new ArrayList();
     */
    public static <T> List<T> toObjectList(String jsonString, Class<T> clazz, ObjectMapper mapper) {
        if (StringUtils.isNotEmpty(jsonString)) {
            try {
                return mapper.readValue(jsonString, getParametricJavaType(ArrayList.class, mapper, clazz));
            } catch (Exception e) {
                throw new ParseException("JSON字符串转换成对象失败", e);
            }
        } else {
            return new ArrayList<T>();
        }
    }

    private static JavaType getParametricJavaType(Class<?> collectionClass, Class<?>... elementClasses) {
        return DefaultMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    private static JavaType getParametricJavaType(Class<?> collectionClass, ObjectMapper mapper, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
