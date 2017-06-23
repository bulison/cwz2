package cn.fooltech.fool_ops.utils;

import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 树状结构数据工具类,适用于EasyUI
 * 
 * @author rqh
 * @version 1.0
 * @date 2015年4月17日
 */
public class TreeDataUtil<T> {

    /**
     * 参数别名<br>
     * key EashUI参数  value 对象参数名称<br>
     */
    private Map<String, String> alias = new HashMap<String, String>();

    /**
     * 排序字段(对象参数名称)
     */
    private String sortField = null;

    /**
     * 是否展开子节点，默认展开
     */
    private boolean isExpand = true;

    /**
     * 判断对象是否被忽略的属性
     */
    private String ignoreField;

    /**
     * 忽略属性的值
     */
    private Object[] ignoreValues;

    /**
     * 是否设置叶子节点为异步节点
     */
    private boolean asynLeafNode = false;

    /**
     * 是否加载实体Bean的属性
     */
    private boolean loadBean = false;

    /**
     * VO类型
     */
    private Class<?> voClass;

    public TreeDataUtil() {

    }

    /**
     * @param alias 参数别名
     */
    public TreeDataUtil(Map<String, String> alias) {
        this.alias = alias;
    }

    /**
     * @param alias     参数别名
     * @param sortField 排序字段(对象参数名称)
     */
    public TreeDataUtil(Map<String, String> alias, String sortField) {
        this.alias = alias;
        this.sortField = sortField;
    }

    /**
     * @param alias 参数别名
     * @param state 是否展开
     */
    public TreeDataUtil(Map<String, String> alias, boolean isExpand) {
        this.alias = alias;
        this.isExpand = isExpand;
    }

    /**
     * @param alias     参数别名
     * @param sortField 排序字段(对象参数名称)
     * @param state     是否展开
     */
    public TreeDataUtil(Map<String, String> alias, String sortField, boolean isExpand) {
        this.alias = alias;
        this.sortField = sortField;
        this.isExpand = isExpand;
    }

    /**
     * @param alias        参数别名
     * @param sortField    排序字段(对象参数名称)
     * @param state        是否展开
     * @param asynLeafNode 把叶子节点设置为异步节点
     * @author tjr 2016-06-16
     */
    public TreeDataUtil(Map<String, String> alias, String sortField, boolean isExpand, boolean asynLeafNode) {
        this.alias = alias;
        this.sortField = sortField;
        this.isExpand = isExpand;
        this.asynLeafNode = asynLeafNode;
    }

    /**
     * @param alias        参数别名
     * @param ignoreField  判断对象是否被忽略的属性
     * @param ignoreValues 忽略属性的值
     */
    public TreeDataUtil(Map<String, String> alias, String ignoreField, Object[] ignoreValues) {
        this.alias = alias;
        this.ignoreField = ignoreField;
        this.ignoreValues = ignoreValues;
    }

    /**
     * @param alias        参数别名
     * @param sortField    排序字段(对象参数名称)
     * @param ignoreField  判断对象是否被忽略的属性
     * @param ignoreValues 忽略属性的值
     */
    public TreeDataUtil(Map<String, String> alias, String sortField, String ignoreField, Object[] ignoreValues) {
        this.alias = alias;
        this.sortField = sortField;
        this.ignoreField = ignoreField;
        this.ignoreValues = ignoreValues;
    }

    /**
     * @param alias        参数别名
     * @param sortField    排序字段(对象参数名称)
     * @param state        是否展开
     * @param ignoreField  判断对象是否被忽略的属性
     * @param ignoreValues 忽略属性的值
     */
    public TreeDataUtil(Map<String, String> alias, String sortField, boolean isExpand, String ignoreField, Object[] ignoreValues) {
        this.alias = alias;
        this.sortField = sortField;
        this.isExpand = isExpand;
        this.ignoreField = ignoreField;
        this.ignoreValues = ignoreValues;
    }

    /**
     * @param alias     参数别名
     * @param sortField 排序字段(对象参数名称)
     * @param isExpand  是否展开
     * @param loadBean  是否加载实体Bean的属性
     * @param voClass   vo类型
     */
    public TreeDataUtil(Map<String, String> alias, String sortField, boolean isExpand, boolean loadBean, Class<?> voClass) {
        this.alias = alias;
        this.sortField = sortField;
        this.isExpand = isExpand;
        this.loadBean = loadBean;
        this.voClass = voClass;
    }

    /**
     * @param alias        参数别名
     * @param sortField    排序字段(对象参数名称)
     * @param isExpand     是否展开
     * @param ignoreField  判断对象是否被忽略的属性
     * @param ignoreValues 忽略属性的值
     * @param loadBean     是否加载实体Bean的属性
     * @param voClass      vo类型
     */
    public TreeDataUtil(Map<String, String> alias, String sortField, boolean isExpand, String ignoreField, Object[] ignoreValues, boolean loadBean, Class<?> voClass) {
        this.alias = alias;
        this.sortField = sortField;
        this.isExpand = isExpand;
        this.ignoreField = ignoreField;
        this.ignoreValues = ignoreValues;
        this.loadBean = loadBean;
        this.voClass = voClass;
    }

    /**
     * 获取泛型类
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private Class<T> getClaszz(T t) {
        return (Class<T>) t.getClass();
    }

    /**
     * 获取树状结构的数据
     *
     * @param root 根节点,Hibernate实体
     * @return
     */
    public List<CommonTreeVo> getTreeData(T root) {
        Assert.notNull(root, "根节点不能为空!");

        List<CommonTreeVo> datas = new ArrayList<CommonTreeVo>();
        CommonTreeVo result = recursionData(root);
        if (result != null) {
            datas.add(result);
        }
        return datas;
    }

    /**
     * 获取树状结构的数据
     *
     * @param root 根节点,Hibernate实体
     * @return add by xjh @date 2015/11/26 增加根节点多个的情况(或者说没有跟节点)
     */
    public List<CommonTreeVo> getTreeData(List<T> rootList) {
        Assert.notNull(rootList, "根节点列表不能为空!");

        List<CommonTreeVo> datas = new ArrayList<CommonTreeVo>();
        for (T root : rootList) {
            CommonTreeVo result = recursionData(root);
            if (result != null) {
                datas.add(result);
            }
        }
        return datas;
    }

    /**
     * 获取树状结构的JSON数据
     *
     * @param root 根节点,Hibernate实体
     * @return
     */
    public String getJsonTreeData(T root) {
        Assert.notNull(root, "根节点不能为空!");

        List<CommonTreeVo> datas = new ArrayList<CommonTreeVo>();
        CommonTreeVo result = recursionData(root);
        if (result != null) {
            datas.add(result);
        }
        return JSONArray.fromObject(datas).toString();
    }

    /**
     * 获取子集
     *
     * @param entity
     * @return
     */
    @SuppressWarnings("unchecked")
    private Collection<T> getChildren(T entity) {
        String childrenAlias = alias.get("children") == null ? "children" : alias.get("children");
        Collection<T> children = (Collection<T>) getFieldValue(entity, childrenAlias);
        return children;
    }

    /**
     * 递归构造数据
     *
     * @param entity
     * @return
     */
    private CommonTreeVo recursionData(T entity) {
        if (isIgnore(entity)) return null;
        CommonTreeVo parentVo = getTreeVo(entity);
        Collection<T> children = getChildren(entity);

        if (children != null && !children.isEmpty()) {
            children = sort(children);
            for (T child : children) {
                CommonTreeVo childVo = recursionData(child);
                if (childVo != null) {
                    parentVo.getChildren().add(childVo);
                }
            }
        }
        return parentVo;
    }

    /**
     * 单个实体转换为vo
     *
     * @param entity
     * @return
     */
    private CommonTreeVo getTreeVo(T entity) {
        String idAlias = alias.get("id") == null ? "id" : alias.get("id");
        String textAlias = alias.get("text") == null ? "text" : alias.get("text");

        //是否展开
        String state = null;
        //Collection<T> children = getChildren(entity);
        //|| children == null || children.isEmpty() || 0 == countValidChildren(children)
        //last update by tjr 20160616
        if (isExpand) {
            state = "open";
        } else if (asynLeafNode) {
            state = "closed";
        }

        CommonTreeVo vo = new CommonTreeVo();
        vo.setId((String) getFieldValue(entity, idAlias));
        vo.setText(getText(entity, textAlias));
        vo.setState(state);

        if (loadBean && voClass != null) {
            //属性集合
            Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put("detail", getVo(entity));
            vo.setAttributes(attributes);
        }
        return vo;
    }

    /**
     * 获取节点显示名称
     *
     * @param entity
     * @param textAlias 名称属性的别名
     * @return
     */
    private String getText(T entity, String textAlias) {
        StringBuffer buffer = new StringBuffer();
        if (StringUtils.isNotBlank(textAlias)) {
            String[] fields = textAlias.split(",");
            for (String field : fields) {
                Object value = getFieldValue(entity, field);
                if (value != null) {
                    buffer.append(value).append(" ");
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 统计子集中有效的个数
     *
     * @param children
     * @return
     */
    private long countValidChildren(Collection<T> children) {
        long total = 0L;
        if (children != null && !children.isEmpty()) {
            total = children.size();

            for (T t : children) {
                if (isIgnore(t)) {
                    total--;
                }
            }
        }
        return total;
    }

    /**
     * 数据集合排序
     *
     * @param children 子集
     * @return
     */
    private List<T> sort(Collection<T> children) {

        List<T> datas = new ArrayList<T>(children);
        if (StringUtils.isBlank(sortField) || children == null || children.isEmpty()) {
            return datas;
        }

        Collections.sort(datas, new Comparator<T>() {
            @Override
            public int compare(T t1, T t2) {
                Object object1 = getFieldValue(t1, sortField);
                Object object2 = getFieldValue(t2, sortField);

                //暂时只支持字符串、数字类型参数的排序
                if (object1 instanceof String) {
                    String value1 = (String) object1;
                    String value2 = (String) object2;
                    if (value1 != null && value2 != null) {
                        return value1.compareTo(value2);
                    }
                } else if (object1 instanceof Integer) {
                    Integer value1 = (Integer) object1;
                    Integer value2 = (Integer) object2;
                    if (value1 != null && value2 != null) {
                        return value1.compareTo(value2);
                    }
                } else if (object1 instanceof Date) {
                    Date value1 = (Date) object1;
                    Date value2 = (Date) object2;
                    if (value1 != null && value2 != null) {
                        return value1.compareTo(value2);
                    }
                } else if (object1 instanceof java.sql.Date) {
                    java.sql.Date value1 = (java.sql.Date) object1;
                    java.sql.Date value2 = (java.sql.Date) object2;
                    if (value1 != null && value2 != null) {
                        return value1.compareTo(value2);
                    }
                }
                return 0;
            }
        });
        return datas;
    }

    /**
     * 获取对象内部参数的值
     *
     * @param object 对象
     * @param field  参数名
     * @return
     * @throws Exception
     */
    private Object getFieldValue(T object, String field) {
        Assert.notNull(object);
        Assert.notNull(field);
        try {
            String methodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
            Class<T> clazz = getClaszz(object);
            Method method = clazz.getMethod(methodName);
            return method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否忽略该对象
     *
     * @param entity
     * @return true 忽略 false 不忽略
     */
    private boolean isIgnore(T entity) {
        if (StringUtils.isBlank(ignoreField)) {
            return false;
        }

        Object value = getFieldValue(entity, ignoreField); //对象属性值
        if (value == null && ignoreValues == null) {
            return true;
        }

        boolean result = false;
        if (value != null && ignoreValues != null) {
            for (int i = 0; i < ignoreValues.length && result == false; i++) {
                //对方法返回值的类型转换暂时写死
                if (value instanceof Short) {
                    Short v1 = (Short) value;
                    Short v2 = (Short) ignoreValues[i];
                    result = v1 == v2;
                    continue;
                } else if (value instanceof Integer) {
                    Integer v1 = (Integer) value;
                    Integer v2 = (Integer) ignoreValues[i];
                    result = v1 == v2;
                    continue;
                } else if (value instanceof Float) {
                    Float v1 = (Float) value;
                    Float v2 = (Float) ignoreValues[i];
                    result = v1 == v2;
                    continue;
                } else if (value instanceof Double) {
                    Double v1 = (Double) value;
                    Double v2 = (Double) ignoreValues[i];
                    result = v1 == v2;
                    continue;
                } else if (value instanceof String) {
                    String v1 = (String) value;
                    String v2 = (String) ignoreValues[i];
                    result = v1.equals(v2);
                    continue;
                }
            }
        }
        return result;
    }

    /**
     * 获取Vo对象
     *
     * @param entity
     * @return
     */
    private Object getVo(T entity) {
        Assert.notNull("VO类型不能为空!");
        return VoFactory.createValue(voClass, entity);
    }

}
