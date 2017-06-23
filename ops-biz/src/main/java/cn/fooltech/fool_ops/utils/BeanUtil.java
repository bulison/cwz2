package cn.fooltech.fool_ops.utils;

import org.hibernate.collection.spi.PersistentCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 对象克隆及值复制工具类
 *
 * @author pvsp
 */
public class BeanUtil {
    private static BeanUtil inst;
    private Logger log = LoggerFactory.getLogger(getClass());
    private Map<Class<?>, List<String>> class_field_names = new HashMap<Class<?>, List<String>>();
    /**
     * 目标类属性类型
     */
    private Map<Class<?>, Map<String, FieldType>> dest_clazz_field_types = new HashMap<Class<?>, Map<String, FieldType>>();
    /**
     * 源类属性类型
     */
    private Map<Class<?>, Map<String, FieldType>> src_clazz_field_types = new HashMap<Class<?>, Map<String, FieldType>>();
    /**
     * 类的SETTER集合
     */
    private Map<Class<?>, Map<String, Method>> class_setters = new HashMap<Class<?>, Map<String, Method>>();
    /**
     * 类的GETTER集合
     */
    private Map<Class<?>, Map<String, Method>> class_getters = new HashMap<Class<?>, Map<String, Method>>();
    /**
     * 类的字段集合
     */
    private Map<Class<?>, Map<String, Field>> class_fields = new HashMap<Class<?>, Map<String, Field>>();
    /**
     * 类的字段类型集合
     */
    private Map<Class<?>, Map<String, Class<?>>> class_field_types = new HashMap<Class<?>, Map<String, Class<?>>>();
    /**
     * 类的属性名集合
     */
    private Map<Class<?>, List<String>> class_property_names = new HashMap<Class<?>, List<String>>();

    private static BeanUtil getInstance() {
        if (inst == null) {
            inst = new BeanUtil();
        }
        return inst;
    }

    /**
     * 复制对象属性
     *
     * @param dest        目标对象
     * @param src         源对象
     * @param ignorNull   是否或略空值
     * @param ignorEntity 是否忽略懒加载的实体
     * @return 成功复制的字段名
     */
    public static String[] copyProperties(Object dest, Object src,
                                          boolean ignorNull, boolean ignorEntity) {

        Map<Object, Object> _obj_ref = new HashMap<Object, Object>();
        try {

            List<String> fields = getInstance().getFieldsAndProperties(
                    dest.getClass());
            List<String> list = getInstance()._copyProperties(dest, src,
                    fields, ignorNull, ignorEntity, _obj_ref);
            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            getInstance().log.error("", e);
        }
        return new String[0];
    }

    /**
     * 复制对象属性，忽略懒加载的字段
     *
     * @param dest        目标对象
     * @param src         源对象
     * @param ignorNull   是否或略空值
     * @param ignorFields <b>忽略</b>, 指定<b>不</b>复制的属性
     * @return 成功复制的字段名
     */
    public static String[] copyProperties(Object dest, Object src,
                                          boolean ignorNull, String... ignorFields) {
        Map<Object, Object> _obj_ref = new HashMap<Object, Object>();
        try {

            List<String> fields = getInstance().getFieldsAndProperties(
                    dest.getClass());
            fields.removeAll(Arrays.asList(ignorFields));
            List<String> list = getInstance()._copyProperties(dest, src,
                    fields, ignorNull, false, _obj_ref);
            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            getInstance().log.error("", e);
        }
        return new String[0];
    }

    /**
     * 复制对象属性，忽略懒加载的字段和空值
     *
     * @param dest 目标对象
     * @param src  源对象
     * @return 成功复制的字段名
     */
    public static String[] copyNotNullProperties(Object dest, Object src) {
        Map<Object, Object> _obj_ref = new HashMap<Object, Object>();
        try {

            List<String> fields = getInstance().getFieldsAndProperties(
                    dest.getClass());
            List<String> list = getInstance()._copyProperties(dest, src,
                    fields, true, true, _obj_ref);
            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            getInstance().log.error("", e);
        }
        return new String[0];
    }

    /**
     * @param <T>
     * @param type
     * @param value
     * @return
     */
    public static <T> T copyNotNullProperties(Class<T> type, Object value) {
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
     * 复制对象属性
     *
     * @param fields      指定*不*复制的字段
     * @param dest        目标对象
     * @param src         源对象
     * @param ignorFields <b>忽略</b>, 指定<b>不</b>复制的属性
     * @return 成功复制的字段名
     */
    public static String[] copyNotNullProperties(Object dest, Object src,
                                                 String... ignorFields) {
        Map<Object, Object> _obj_ref = new HashMap<Object, Object>();
        try {

            List<String> fields = getInstance().getFieldsAndProperties(
                    dest.getClass());
            fields.removeAll(Arrays.asList(ignorFields));
            List<String> list = getInstance()._copyProperties(dest, src,
                    fields, true, false, _obj_ref);
            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            getInstance().log.error("", e);
        }
        return new String[0];
    }

    /**
     * 克隆对象，忽略懒加载的字段
     *
     * @param <T>
     * @param src 源对象
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(T src) {
        Map<Object, Object> _obj_ref = new HashMap<Object, Object>();
        try {
            return (T) getInstance()._clone(src, false, _obj_ref);
        } catch (Exception e) {
            getInstance().log.error("", e);
        }
        return null;

    }

    /**
     * 是否包装类型
     *
     * @param type1
     * @param type2
     * @return
     */
    public static boolean isWrappedType(Class<?> type1, Class<?> type2) {
        if (type1 == int.class && type2 == Integer.class)
            return true;
        else if (type2 == int.class && type1 == Integer.class)
            return true;
        else if (type1 == long.class && type2 == Long.class)
            return true;
        else if (type2 == long.class && type1 == Long.class)
            return true;
        else if (type1 == short.class && type2 == Short.class)
            return true;
        else if (type2 == short.class && type1 == Short.class)
            return true;
        else if (type2 == byte.class && type1 == Byte.class)
            return true;
        else if (type1 == byte.class && type2 == Byte.class)
            return true;
        else if (type2 == float.class && type1 == Float.class)
            return true;
        else if (type1 == float.class && type2 == Float.class)
            return true;
        else if (type2 == double.class && type1 == Double.class)
            return true;
        else if (type1 == double.class && type2 == Double.class)
            return true;
        else if (type2 == boolean.class && type1 == Boolean.class)
            return true;
        else if (type1 == boolean.class && type2 == Boolean.class)
            return true;

        return false;
    }

    /**
     * 获取SETTER
     *
     * @param clazz     类
     * @param fieldName 属性
     * @return 方法
     */
    public static Method getDeclaredSetter(final Class<?> clazz,
                                           final String fieldName) {
        Map<String, Method> cmap = getInstance().class_setters.get(clazz);
        if (cmap == null) {
            cmap = new HashMap<String, Method>();
            getInstance().class_setters.put(clazz, cmap);
        }

        Method m = cmap.get(fieldName);
        if (m != null) {
            return m;
        }

        Method getter = getDeclaredGetter(clazz, fieldName);
        if (getter == null) {
            return null;
        }

        Class<?> clz = getter.getDeclaringClass();
        String name = new StringBuilder("set").append(
                fieldName.substring(0, 1).toUpperCase()).append(
                fieldName.substring(1)).toString();

        try {
            Method setter = clz.getMethod(name, getter.getReturnType());
            return setter;
        } catch (Exception e) {
            if (getInstance().log.isDebugEnabled())
                getInstance().log.debug("Setter for [{}] NOT FOUND on [{}]",
                        fieldName, clz);
            return null;
        }
    }

    /**
     * 获取GETTER
     *
     * @param clazz     类
     * @param fieldName 属性
     * @return 方法
     */
    public static Method getDeclaredGetter(final Class<?> clazz,
                                           final String fieldName) {

        Map<String, Method> cmap = getInstance().class_getters.get(clazz);
        if (cmap == null) {
            cmap = new HashMap<String, Method>();
            getInstance().class_getters.put(clazz, cmap);
        }

        Method m = cmap.get(fieldName);
        if (m != null) {
            return m;
        }

        String nameGet = new StringBuilder("get").append(
                fieldName.substring(0, 1).toUpperCase()).append(
                fieldName.substring(1)).toString();

        String nameIs = new StringBuilder("is").append(
                fieldName.substring(0, 1).toUpperCase()).append(
                fieldName.substring(1)).toString();

        // Method m = null;
        Class<?> clz = clazz;
        while (m == null && clz != null && !clz.equals(Object.class)) {
            try {
                m = clz.getDeclaredMethod(nameGet, new Class[0]);
            } catch (Exception e) {
                try {
                    m = clz.getDeclaredMethod(nameIs, new Class[0]);
                } catch (Exception e1) {
                }
            }
            if (m != null)
                break;
            if (getInstance().log.isDebugEnabled()) {
                if (clz.getSuperclass().equals(Object.class)) {
                    getInstance().log.debug(
                            "Getter for [{}] NOT FOUND on [{}]", new Object[]{
                                    fieldName, clz});

                } else {
                    getInstance().log
                            .debug(
                                    "Getter for [{}] NOT FOUND on [{}], try super class [{}]",
                                    new Object[]{fieldName, clz,
                                            clz.getSuperclass()});
                }
            }
            clz = clz.getSuperclass();
        }

        cmap.put(fieldName, m);
        return m;
    }

    /**
     * 获取字段
     *
     * @param clazz     类
     * @param fieldName 属性
     * @return 方法
     */
    public static Field getDeclaredField(final Class<?> clazz,
                                         final String fieldName) {
        Map<String, Field> cmap = getInstance().class_fields.get(clazz);
        if (cmap == null) {
            cmap = new HashMap<String, Field>();
            getInstance().class_fields.put(clazz, cmap);
        }

        if (cmap.containsKey(fieldName)) {
            Field f = cmap.get(fieldName);
            return f;
        }

        Field f = null;
        Class<?> clz = clazz;
        while (f == null && clz != null && !clz.equals(Object.class)) {
            try {
                f = clz.getDeclaredField(fieldName);
                if (f != null)
                    break;
                clz = clz.getSuperclass();
            } catch (Exception e) {
                if (getInstance().log.isDebugEnabled()) {
                    if (clz.getSuperclass().equals(Object.class)) {
                        getInstance().log.debug("FIELD[{}] NOT FOUND on [{}]",
                                new Object[]{fieldName, clz});

                    } else {
                        getInstance().log
                                .debug(
                                        "FIELD[{}] NOT FOUND on [{}], try super class [{}]",
                                        new Object[]{fieldName, clz,
                                                clz.getSuperclass()});
                    }
                }
                clz = clz.getSuperclass();
            }
        }
        cmap.put(fieldName, f);
        return f;
    }

    /**
     * 获取属性类型
     *
     * @param clz       类
     * @param fieldName 属性
     * @return 类型
     */
    public static Class<?> getPropertyType(Class<?> clz, String fieldName) {
        if (clz == null) {
            getInstance().log.error("NULL CLASS???!!!", fieldName);
            return null;
        }

        Map<String, Class<?>> cmap = getInstance().class_field_types.get(clz);
        if (cmap == null) {
            cmap = new HashMap<String, Class<?>>();
            getInstance().class_field_types.put(clz, cmap);
        }

        if (cmap.containsKey(fieldName)) {
            return cmap.get(fieldName);
        }

        if (fieldName.indexOf('.') > 0) {
            String sub_field = fieldName.substring(0, fieldName.indexOf('.'));
            Class<?> sub_clz = getPropertyType(clz, sub_field);
            if (sub_clz == null) {
                return null;
            }
            String nf = fieldName.substring(fieldName.indexOf('.') + 1);
            return getPropertyType(sub_clz, nf);
        }

        Class<?> ft = null;
        Method getter = getDeclaredGetter(clz, fieldName);
        if (getter != null) {
            ft = getter.getReturnType();
        } else {
            Field f = getDeclaredField(clz, fieldName);
            if (f != null) {
                ft = f.getType();
            }
        }
        cmap.put(fieldName, ft);
        return ft;
    }

    /**
     * 解析字符串获取属性对应的值
     *
     * @param clz   类
     * @param field 属性
     * @param s     字符串值
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static Object parsePropertyValue(Class<?> clz, String field, String s) {
        Class type = getPropertyType(clz, field);
        if (type == null) {
            getInstance().log.error("CAN'T FIND PROPERTY [{}] @ [{}]", field,
                    clz);
            return s;
        }

        if (String.class.isAssignableFrom(type)) {
            return s;
        } else if (Integer.class.isAssignableFrom(type)
                || int.class.isAssignableFrom(type)) {
            return Integer.valueOf(s);
        } else if (Long.class.isAssignableFrom(type)
                || long.class.isAssignableFrom(type)) {
            return Long.valueOf(s);
        } else if (Byte.class.isAssignableFrom(type)
                || byte.class.isAssignableFrom(type)) {
            return Byte.valueOf(s);
        } else if (Short.class.isAssignableFrom(type)
                || short.class.isAssignableFrom(type)) {
            return Short.valueOf(s);
        } else if (Float.class.isAssignableFrom(type)
                || float.class.isAssignableFrom(type)) {
            return Float.valueOf(s);
        } else if (Double.class.isAssignableFrom(type)
                || double.class.isAssignableFrom(type)) {
            return Double.valueOf(s);
        } else if (Boolean.class.isAssignableFrom(type)
                || boolean.class.isAssignableFrom(type)) {
            if (s.trim().equalsIgnoreCase("true") || s.trim().equals("1")) {
                return true;
            } else {
                return false;
            }
        } else if (Enum.class.isAssignableFrom(type)) {
            return Enum.valueOf(type, s);
        } else if (Date.class.isAssignableFrom(type)) {
            // 时间类型比较麻烦
            SimpleDateFormat df = null;
            if (s
                    .matches("\\d{2,4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
                df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            } else if (s
                    .matches("\\d{2,4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
                df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            } else if (s.matches("\\d{2,4}-\\d{1,2}-\\d{1,2}")) {
                df = new SimpleDateFormat("yyyy-MM-dd");

            } else if (s.matches("\\d{2,4}/\\d{1,2}/\\d{1,2}")) {
                df = new SimpleDateFormat("yyyy/MM/dd");
            }

            if (df == null) {
                getInstance().log
                        .error(
                                "WRONG DATE STRING [{}] FORMAT FOR PROPERTY [{}] @ [{}]",
                                new Object[]{s, field, clz});
                return null;
            } else {
                try {
                    return df.parse(s);
                } catch (ParseException e) {
                    getInstance().log.error("WRONG DATE FORMAT [{}]", s);
                    getInstance().log.error("", e);
                }
            }
        }
        getInstance().log.warn("UNKONWN TYPE [{}] FOR PROPERTY [{}] @ [{}]",
                new Object[]{type, field, clz});
        return s;
    }

    /**
     * 获取属性
     *
     * @param obj       对象
     * @param fieldName 属性
     * @return 属性值
     */
    public static Object getProperty(Object obj, String fieldName) {
        if (obj == null) {
            getInstance().log.error(" evaluate FIELD[{}] on NULL object!",
                    fieldName);
            return null;
        }

        Class<?> propertyType = getPropertyType(obj.getClass(), fieldName);
        if (propertyType == null) {
            getInstance().log.debug("PROPERTY [{}] NOT FOUNT @[{}]", fieldName,
                    obj);
            return null;
        }

        Method getter = getDeclaredGetter(obj.getClass(), fieldName);
        if (getter != null) {
            try {
                getter.setAccessible(true);
                return getter.invoke(obj);
            } catch (Exception e) {
                getInstance().log.error("ERROR WHILE INVOKE GETTER [{}@{}]",
                        getter.getName(), obj);
            }
        } else {
            Field f = getDeclaredField(obj.getClass(), fieldName);
            if (f != null) {
                try {
                    f.setAccessible(true);
                    return f.get(obj);
                } catch (Exception e) {
                    getInstance().log.error("ERROR WHILE GET FIELD [{}@{}]",
                            fieldName, obj);
                }
            }
        }
        return null;
    }

    /**
     * 设置属性
     *
     * @param obj       对象
     * @param fieldName 属性
     * @param value     属性值
     */
    public static void setProperty(Object obj, String fieldName, Object value) {
        if (obj == null) {
            getInstance().log.error(" evaluate FIELD[{}] on NULL object!",
                    fieldName);
            return;
        }

        Method setter = getDeclaredSetter(obj.getClass(), fieldName);
        if (setter != null) {
            Class<?>[] parameterTypes = setter.getParameterTypes();
            if (parameterTypes.length == 1
                    && (value == null || parameterTypes[0]
                    .isAssignableFrom(value.getClass()))
                    || isWrappedType(parameterTypes[0], value.getClass())) {
                try {
                    setter.setAccessible(true);
                    setter.invoke(obj, value);
                } catch (Exception e) {
                    getInstance().log.error(
                            "ERROR WHILE INVOKE SETTER [{}@{}]", setter
                                    .getName(), obj);
                }
            }
        } else {
            Field f = getDeclaredField(obj.getClass(), fieldName);
            if (f != null) {
                f.setAccessible(true);
                if (value == null
                        || f.getType().isAssignableFrom(value.getClass())
                        || isWrappedType(f.getType(), value.getClass())) {
                    try {
                        f.set(obj, value);
                    } catch (Exception e) {
                        getInstance().log
                                .error("ERROR WHILE SET FIELD [{}@{}]",
                                        fieldName, obj);
                    }
                }
            } else {
                getInstance().log.error("FIELD [{}] NOT FOUNT @[{}]",
                        fieldName, obj);
            }
        }
    }

    /**
     * 获取属性名列表
     *
     * @param clazz 类
     * @return 属性名列表
     */
    public static List<String> getProperties(Class<?> clazz) {
        if (getInstance().class_property_names.containsKey(clazz)) {
            List<String> list = getInstance().class_property_names.get(clazz);
            if (list != null) {
                return list;
            }
        }

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            getInstance().log.error("", e);
            return Collections.emptyList();
        }

        PropertyDescriptor[] arr = beanInfo.getPropertyDescriptors();
        List<String> result = new ArrayList<String>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            String name = arr[i].getName();
            if (name.equals("class")) {
                continue;
            }
            result.add(name);
        }

        getInstance().class_property_names.put(clazz, result);
        return result;
    }

    /**
     * 获取类的所有属性名列表
     *
     * @param clz 类
     * @return 属性名列表
     */
    private List<String> getFieldsAndProperties(Class<?> clz) {
        if (clz == null) {
            return Collections.emptyList();
        }
        if (class_field_names.containsKey(clz)) {
            return class_field_names.get(clz);
        }

        List<String> fields = new ArrayList<String>();

        // 所有字段， 包括私有字段
        Field[] fs = clz.getDeclaredFields();
        for (Field f : fs) {
            int mod = f.getModifiers();
            if ((mod & Modifier.STATIC) != 0 || (mod & Modifier.FINAL) != 0) {
                continue;
            } else {
                fields.add(f.getName());
            }
        }

        if (!Object.class.equals(clz.getSuperclass())) {
            fields.addAll(getFieldsAndProperties(clz.getSuperclass()));
        }

        // 所有公开的属性
        List<String> properties = getProperties(clz);
        for (String pro : properties) {
            if (!fields.contains(pro)) {
                fields.add(pro);
            }
        }

        class_field_names.put(clz, fields);
        return fields;
    }

    /**
     * 复制对象属性
     *
     * @param dest        目标对象
     * @param src         源对象
     * @param fields      复制的属性名
     * @param ignorNull   是否或略空值
     * @param ignorEntity 是否忽略懒加载的实体
     * @param _obj_ref    对象缓存
     * @return 成功复制的字段名
     */
    private List<String> _copyProperties(Object dest, Object src,
                                         List<String> fields, boolean ignorNull, boolean ignorEntity,
                                         Map<Object, Object> _obj_ref) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        List<String> copyed = new ArrayList<String>(fields.size());
        Class<?> dest_clz = dest.getClass();
        Class<?> src_clz = src.getClass();
        for (String s : fields) {
            // 获取目标属性类型
            Map<String, FieldType> dest_fts;
            if (dest_clazz_field_types.containsKey(dest_clz)) {
                if (dest_clazz_field_types.get(dest_clz) != null) {
                    dest_fts = dest_clazz_field_types.get(dest_clz);
                } else {
                    dest_fts = new HashMap<String, FieldType>();
                    dest_clazz_field_types.put(dest_clz, dest_fts);
                }
            } else {
                dest_fts = new HashMap<String, FieldType>();
                dest_clazz_field_types.put(dest_clz, dest_fts);
            }

            Field dest_field;
            // 属性类型判断
            FieldType fieldType = dest_fts.get(s);
            if (fieldType == null) { // 未定义
                dest_field = getDeclaredField(dest_clz, s);
                if (dest_field == null) { // FIELD不存在， 获取Setter
                    Method dest_set = getDeclaredSetter(dest_clz, s);
                    if (dest_set == null) { // Setter 不存在
                        dest_fts.put(s, FieldType.NULL);
                    } else {
                        dest_fts.put(s, FieldType.ACCESSOR);
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("cant found field[{}] on [{}]", s, dest_clz);
                    }
                    continue;
                } else {
                    dest_fts.put(s, FieldType.FIELD);
                    int mod = dest_field.getModifiers();
                    if ((mod & Modifier.STATIC) != 0
                            || (mod & Modifier.FINAL) != 0) {
                        continue;
                    }
                }
            } else if (FieldType.FIELD.equals(fieldType)) { // 是字段
                dest_field = getDeclaredField(dest_clz, s);
                // } else if (FieldType.NULL.equals(fieldType)) { // 目标不存在该字段
                // continue;
            } else { // 忽略SETTER和不存在属性
                continue;
            }

            Object value = null;
            // 目标字段类型
            Class<?> field_clz = dest_field.getType();
            // 获取源对象属性定义
            Map<String, FieldType> src_fts;
            if (src_clazz_field_types.containsKey(src_clz)) {
                if (src_clazz_field_types.get(src_clz) != null) {
                    src_fts = src_clazz_field_types.get(src_clz);
                } else {
                    src_fts = new HashMap<String, FieldType>();
                    src_clazz_field_types.put(src_clz, src_fts);
                }
            } else {
                src_fts = new HashMap<String, FieldType>();
                src_clazz_field_types.put(src_clz, src_fts);
            }

            Field src_field = null;

            fieldType = src_fts.get(s);
            if (FieldType.NULL.equals(fieldType)) { // 属性不存在， 忽略
                continue;
            } else {
                if (FieldType.FIELD.equals(fieldType)) { // 字段
                    src_field = getDeclaredField(src_clz, s);
                    Class<?> src_field_clz = src_field.getType();
                    if (!field_clz.isAssignableFrom(src_field_clz)
                            && !isWrappedType(field_clz, src_field_clz)
                            && (!field_clz.isEnum() && !src_field_clz.isEnum())) {
                        // 类型不匹配
                        continue;
                    }
                    src_field.setAccessible(true);
                    value = src_field.get(src);
                } else if (FieldType.ACCESSOR.equals(fieldType)) { // GETTER
                    Method getter = getDeclaredGetter(src_clz, s);
                    Class<?> src_field_clz = getter.getReturnType();
                    if (!field_clz.isAssignableFrom(src_field_clz)
                            && !isWrappedType(field_clz, src_field_clz)
                            && (!field_clz.isEnum() && !src_field_clz.isEnum())) {
                        // 类型不匹配
                        continue;
                    }
                    getter.setAccessible(true);
                    value = getter.invoke(src);
                } else if (fieldType == null) { // 未定义
                    Method getter = getDeclaredGetter(src_clz, s);
                    if (getter == null) { // 不存在GETTER
                        if (log.isDebugEnabled()) {
                            log
                                    .debug(
                                            "cant accessor field[{}] on [{}] try field",
                                            s, src_clz);
                        }
                        src_field = getDeclaredField(src_clz, s);
                        if (src_field == null) { // 不存在字段
                            if (log.isDebugEnabled()) {
                                log.debug("cant found field[{}] on [{}] ", s,
                                        src_clz);
                            }
                            src_fts.put(s, FieldType.NULL);
                            continue;
                        } else { // 字段
                            src_fts.put(s, FieldType.FIELD);
                            Class<?> src_field_clz = src_field.getType();
                            if (!field_clz.isAssignableFrom(src_field_clz)
                                    && !isWrappedType(field_clz, src_field_clz)
                                    && (!field_clz.isEnum() && !src_field_clz
                                    .isEnum())) {
                                // 类型不匹配
                                continue;
                            }
                            src_field.setAccessible(true);
                            value = src_field.get(src);
                        }
                    } else { // GETTER
                        src_fts.put(s, FieldType.ACCESSOR);
                        Class<?> src_field_clz = getter.getReturnType();
                        if (!field_clz.isAssignableFrom(src_field_clz)
                                && !isWrappedType(field_clz, src_field_clz)
                                && (!field_clz.isEnum() && !src_field_clz
                                .isEnum())) {
                            // 类型不匹配
                            continue;
                        }
                        getter.setAccessible(true);
                        value = getter.invoke(src);
                    }
                }
            }

            // 忽略空值
            if (value == null && ignorNull) {
                continue;
            }

            dest_field.setAccessible(true);
            if (value == null) { // 不忽略空值
                dest_field.set(dest, null);
            } else if (field_clz.isAnnotationPresent(Entity.class)
                    || value.getClass().isAnnotationPresent(Entity.class)) {
                // 属性是实体
                if (ignorEntity) {
                    dest_field.set(dest, null);
                } else {
                    Object _clone_value = _clone(value, ignorEntity, _obj_ref);
                    dest_field.set(dest, _clone_value);
                }
            } else if (ignorEntity
                    && value instanceof PersistentCollection) {
                // 延迟加载的Collection
                dest_field.set(dest, null);
            } else if (Collection.class.isAssignableFrom(field_clz)) {
                dest_field.set(dest, _clone(value, ignorEntity, _obj_ref));
            } else if (Map.class.isAssignableFrom(field_clz)) {
                dest_field.set(dest, _clone(value, ignorEntity, _obj_ref));
            } else if (field_clz.isEnum() || value.getClass().isEnum()) {
                if (field_clz.isEnum() && value.getClass().isEnum()) {
                    // 都是枚举
                    int ordinal = ((Enum<?>) value).ordinal();
                    @SuppressWarnings("unchecked")
                    Enum enum_value = EnumUtils.getEnum(
                            (Class<Enum>) field_clz, ordinal);
                    dest_field.set(dest, enum_value);
                } else {
                    // 枚举, 根据需求转换为 name或ordinal
                    Class<?> clz = value.getClass();
                    if (field_clz.isEnum()) {
                        if (clz == String.class) {
                            @SuppressWarnings("unchecked")
                            Object e = EnumUtils.getEnum(
                                    (Class<Enum>) field_clz, (String) value);
                            dest_field.set(dest, e);
                        } else if (clz == int.class || clz == Integer.class) {
                            @SuppressWarnings("unchecked")
                            Object e = EnumUtils.getEnum(
                                    (Class<Enum>) field_clz, (Integer) value);
                            dest_field.set(dest, e);
                        } else {
                            // 无法在枚举与值或间相互转换
                            continue;
                        }
                    } else if (clz.isEnum()) {
                        if (field_clz == String.class) {
                            dest_field.set(dest, ((Enum<?>) value).name());
                        } else if (field_clz == int.class
                                || field_clz == Integer.class) {
                            dest_field.set(dest, ((Enum<?>) value).ordinal());
                        } else {
                            // 无法在枚举与值或间相互转换
                            continue;
                        }
                    }
                }
            } else {
                dest_field.set(dest, value);
            }
            copyed.add(s);
        }
        return copyed;
    }

    /**
     * 克隆
     *
     * @param src
     * @param ignorEntity
     * @param _obj_ref
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    private Object _clone(Object src, boolean ignorEntity,
                          Map<Object, Object> _obj_ref) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        if (src == null) {
            return null;
        }

        if (_obj_ref.containsKey(src)) {
            return _obj_ref.get(src);
        }

        Class<?> clz = src.getClass();
        if (Collection.class.isAssignableFrom(clz)) {
            // Collection
            return _cloneCollection((Collection) src, _obj_ref);
        } else if (Map.class.isAssignableFrom(clz)) {
            // Map
            return _cloneMap((Map) src, _obj_ref);
        } else {
            Object dest;
            try {
                dest = clz.newInstance();

            } catch (Exception e) {
                log.error("COULD NOT INSTANCE [{}]", clz);
                return null;
            }

            List<String> fields = getFieldsAndProperties(clz);
            _copyProperties(dest, src, fields, true, ignorEntity, _obj_ref);
            _obj_ref.put(src, dest);
            return dest;
        }
    }

    @SuppressWarnings("unchecked")
    private Collection _cloneCollection(Collection src,
                                        Map<Object, Object> _obj_ref) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        if (src == null) {
            return null;
        }

        if (_obj_ref.containsKey(src)) {
            return (Collection) _obj_ref.get(src);
        }

        Class clz = src.getClass();

        if (SortedSet.class.isAssignableFrom(clz)) {
            clz = java.util.TreeSet.class;
        } else if (Set.class.isAssignableFrom(clz)) {
            clz = HashSet.class;
        } else if (List.class.isAssignableFrom(clz)) {
            clz = ArrayList.class;
        } else {
            clz = null;
            return null;
        }

        Collection dest;
        try {
            dest = (Collection) clz.newInstance();
            _obj_ref.put(src, dest);
        } catch (Exception e) {
            log.error("COULD NOT INSTANCE COLLECTION [{}]", clz);
            return null;
        }

        for (Object o : src) {
            if (o.getClass().isAnnotationPresent(Entity.class)) {
                dest.add(_clone(o, false, _obj_ref));
            } else {
                dest.add(o);
            }
        }
        return dest;
    }

    @SuppressWarnings("unchecked")
    private Map _cloneMap(Map src, Map<Object, Object> _obj_ref)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (src == null) {
            return null;
        }

        if (_obj_ref.containsKey(src)) {
            return (Map) _obj_ref.get(src);
        }

        Class clz = src.getClass();
        if (SortedMap.class.isAssignableFrom(clz)) {
            clz = TreeMap.class;
        } else if (Map.class.isAssignableFrom(clz)) {
            clz = HashMap.class;
        } else {
            clz = null;
            return null;
        }

        Map dest;
        try {
            dest = (Map) clz.newInstance();
            _obj_ref.put(src, dest);
        } catch (Exception e) {
            log.error("COULD NOT INSTANCE MAP [{}]", clz);
            return null;
        }
        Object v;
        for (Object k : src.keySet()) {
            v = src.get(k);
            if (v.getClass().isAnnotationPresent(Entity.class)) {
                dest.put(k, _clone(v, false, _obj_ref));
            } else {
                dest.put(k, v);
            }
        }
        return dest;
    }

    /**
     * 属性类型
     */
    public static enum FieldType {
        /**
         * 字段
         */
        FIELD,
        /**
         * getter & setter
         */
        ACCESSOR,
        /**
         * 没有这个属性
         */
        NULL
    }

}
