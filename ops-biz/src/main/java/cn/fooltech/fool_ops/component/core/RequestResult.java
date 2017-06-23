package cn.fooltech.fool_ops.component.core;

import cn.fooltech.fool_ops.utils.DateTimeUtils;
import com.alibaba.fastjson.JSON;
import lombok.ToString;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 响应消息。controller中处理后，返回此对象，响应请求结果给客户端。
 */

public class RequestResult implements Serializable {
    private static final long serialVersionUID = 8992436576262574064L;

    /**
     * 处理成功标识
     */
    public static final int RETURN_SUCCESS = 0;
    /**
     * 处理失败标识
     */
    public static final int RETURN_FAILURE = 1;

    public static final String SUCCESS = "操作成功";
    
    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 反馈数据
     */
    private Object data;

    /**
     * 反馈信息
     */
    private String message = SUCCESS;

    /**
     * 响应码
     */
    private int returnCode = RETURN_SUCCESS;
    /**
     * 错误码
     */
    private int errorCode = RETURN_SUCCESS;
    /**
     * 扩展数据
     */
    private Map<String, Object> dataExt = new HashMap<String, Object>(0);

    /**
     * 过滤字段：指定需要序列化的字段
     */
    private transient Map<Class<?>, Set<String>> includes;

    /**
     * 过滤字段：指定不需要序列化的字段
     */
    private transient Map<Class<?>, Set<String>> excludes;

    private transient boolean onlyData;

    private transient String callback;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", this.success);
        if (data != null)
            map.put("data", this.getData());
        if (message != null)
            map.put("message", this.getMessage());
        if (errorCode != RETURN_SUCCESS)
            map.put("errorCode", this.getErrorCode());
        if (dataExt != null)
            map.put("dataExt", this.getDataExt());

        map.put("returnCode", this.getReturnCode());
        return map;
    }

    public RequestResult() {
    }

    public RequestResult(String message) {
        this.returnCode = RETURN_SUCCESS;
        this.message = message;
        this.errorCode=RETURN_SUCCESS;
//        this.success = isSuccess();
    }
    public RequestResult(int returnCode,String message) {
        super();
        this.returnCode = returnCode;
        this.message = message;
        this.errorCode=returnCode;
//        this.success = isSuccess();
    }
    public RequestResult(int returnCode, String message, Object data) {
        super();
        this.returnCode = returnCode;
        this.message = message;
        this.data = data;
        this.errorCode=returnCode;
//        this.success = isSuccess();
    }

    public RequestResult(int returnCode, int errorCode, String message) {
        super();
        this.returnCode = returnCode;
        this.errorCode = errorCode;
        this.message = message;
    }
    public RequestResult(boolean success, Object data) {
        super();
        this.returnCode = success ? RETURN_SUCCESS : RETURN_FAILURE;
        this.data = data;
        this.errorCode = success ? RETURN_SUCCESS :RETURN_FAILURE;
        this.success = success;
    }

    public RequestResult(boolean success, Object data, int returnCode) {
        this(success, data);
        this.returnCode = returnCode;
        this.errorCode=RETURN_FAILURE;
    }
    public RequestResult(boolean success, Object data, int returnCode,int errorCode) {
        this(success, data);
        this.returnCode = returnCode;
        this.errorCode=errorCode;
    }

    public RequestResult include(Class<?> type, String... fields) {
        return include(type, Arrays.asList(fields));
    }

    public RequestResult include(Class<?> type, Collection<String> fields) {
        if (includes == null)
            includes = new HashMap<>();
        if (fields == null || fields.isEmpty()) return this;
        fields.forEach(field -> {
            if (field.contains(".")) {
                String tmp[] = field.split("[.]", 2);
                try {
                    Field field1 = type.getDeclaredField(tmp[0]);
                    if (field1 != null) {
                        include(field1.getType(), tmp[1]);
                    }
                } catch (Throwable e) {
                }
            } else {
                getStringListFormMap(includes, type).add(field);
            }
        });
        return this;
    }

    public RequestResult exclude(Class type, Collection<String> fields) {
        if (excludes == null)
            excludes = new HashMap<>();
        if (fields == null || fields.isEmpty()) return this;
        fields.forEach(field -> {
            if (field.contains(".")) {
                String tmp[] = field.split("[.]", 2);
                try {
                    Field field1 = type.getDeclaredField(tmp[0]);
                    if (field1 != null) {
                        exclude(field1.getType(), tmp[1]);
                    }
                } catch (Throwable e) {
                }
            } else {
                getStringListFormMap(excludes, type).add(field);
            }
        });
        return this;
    }

    public RequestResult exclude(Collection<String> fields) {
        if (excludes == null)
            excludes = new HashMap<>();
        if (fields == null || fields.isEmpty()) return this;
        Class type;
        if (data != null) type = data.getClass();
        else return this;
        exclude(type, fields);
        return this;
    }

    public RequestResult include(Collection<String> fields) {
        if (includes == null)
            includes = new HashMap<>();
        if (fields == null || fields.isEmpty()) return this;
        Class type;
        if (data != null) type = data.getClass();
        else return this;
        include(type, fields);
        return this;
    }

    public RequestResult exclude(Class type, String... fields) {
        return exclude(type, Arrays.asList(fields));
    }


    public RequestResult exclude(String... fields) {
        return exclude(Arrays.asList(fields));
    }

    public RequestResult include(String... fields) {
        return include(Arrays.asList(fields));
    }

    protected Set<String> getStringListFormMap(Map<Class<?>, Set<String>> map, Class type) {
        Set<String> list = map.get(type);
        if (list == null) {
            list = new HashSet<>();
            map.put(type, list);
        }
        return list;
    }

    public boolean isSuccess() {
        if (this.returnCode == RETURN_SUCCESS) {
            return true;
        }
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public RequestResult setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, DateTimeUtils.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
    }

    public int getReturnCode() {
        return returnCode;
    }

    public RequestResult setReturnCode(int returnCode) {
        this.returnCode = returnCode;
        return this;
    }

    public static RequestResult fromJson(String json) {
        return JSON.parseObject(json, RequestResult.class);
    }

    public Map<Class<?>, Set<String>> getExcludes() {
        return excludes;
    }

    public Map<Class<?>, Set<String>> getIncludes() {
        return includes;
    }

    public RequestResult onlyData() {
        setOnlyData(true);
        return this;
    }

    public void setOnlyData(boolean onlyData) {
        this.onlyData = onlyData;
    }

    public boolean isOnlyData() {
        return onlyData;
    }

    public RequestResult callback(String callback) {
        this.callback = callback;
        return this;
    }

    public String getCallback() {
        return callback;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setExcludes(Map<Class<?>, Set<String>> excludes) {
        this.excludes = excludes;
    }

    public static RequestResult ok() {
        return ok(null);
    }

    public static RequestResult ok(Object data) {
        return new RequestResult(true, data);
    }

    public static RequestResult created(Object data) {
        return new RequestResult(true, data, 0);
    }

    public static RequestResult error(String message) {
        return new RequestResult(message);
    }

    public static RequestResult error(String message, int returnCode) {

        RequestResult result = new RequestResult(message);
        result.setErrorCode(returnCode);
        result.setReturnCode(returnCode);
        return result;
    }

    public Map<String, Object> getDataExt() {
        return dataExt;
    }

    public void setDataExt(Map<String, Object> dataExt) {
        this.dataExt = dataExt;
    }
}