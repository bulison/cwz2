package cn.fooltech.fool_ops.utils.tree;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树状结构数据工具类,适用于EasyUI(原来的TreeDataUtil不满足需求，使用接口重写了查询子节点)
 * 建议使用FastTreeUtils
 *
 * @author xjh
 * @version 1.0
 * @date 2015年12月4日
 */
public class TreeUtils<E, V> {

    /**
     * 参数别名<br>
     * key EashUI参数  value 对象参数名称<br>
     */
    private Map<String, String> alias = new HashMap<String, String>();

    /**
     * 是否展开子节点，默认展开
     */
    private boolean isExpand = true;

    /**
     * 是否设置叶子节点为异步节点
     */
    private boolean asynLeafNode = false;

    /**
     * 是否加载实体Bean的属性
     */
    private boolean loadBean = false;


    /**
     * @param alias        参数别名
     * @param isExpand     是否展开
     * @param asynLeafNode 把叶子节点设置为异步节点
     * @param loadBean     是否加载实体Bean的属性
     */
    public TreeUtils(Map<String, String> alias, boolean isExpand,
                     boolean asynLeafNode, boolean loadBean) {
        super();
        this.alias = alias;
        this.isExpand = isExpand;
        this.asynLeafNode = asynLeafNode;
        this.loadBean = loadBean;
    }

    /**
     * 获取树状结构的数据
     *
     * @param root 根节点,Hibernate实体
     * @return
     */
    public List<TreeVo> getTreeData(TreeCallback<E, V> callback, E root) {
        Assert.notNull(root, "根节点不能为空!");

        List<TreeVo> datas = new ArrayList<TreeVo>();
        TreeVo result = recursionData(callback, root);
        if (result != null) {
            datas.add(result);
        }
        return datas;
    }

    /**
     * 递归构造数据
     *
     * @param entity
     * @return
     */
    private TreeVo recursionData(TreeCallback<E, V> callback, E entity) {

        TreeVo parentVo = getTreeVo(callback, entity);
        List<E> children = callback.getChildren(entity);

        if (children != null && !children.isEmpty()) {
            for (E child : children) {
                TreeVo childVo = recursionData(callback, child);
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
    private TreeVo getTreeVo(TreeCallback<E, V> callback, E entity) {
        String idAlias = alias.get("id") == null ? "id" : alias.get("id");
        String textAlias = alias.get("text") == null ? "text" : alias.get("text");

        //是否展开
        String state = null;
        if (isExpand) {
            state = "open";
        } else if (asynLeafNode) {
            state = "closed";
        }

        TreeVo vo = new TreeVo();
        vo.setId((String) getFieldValue(entity, idAlias));
        vo.setText(getText(entity, textAlias));
        vo.setState(state);

        if (loadBean) {
            //属性集合
            vo.setAttributes(callback.getDataVo(entity));
        }
        return vo;
    }

    /**
     * 获取泛型类
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private Class<E> getClaszz(E t) {
        return (Class<E>) t.getClass();
    }

    /**
     * 获取对象内部参数的值
     *
     * @param object 对象
     * @param field  参数名
     * @return
     * @throws Exception
     */
    private Object getFieldValue(E object, String field) {
        Assert.notNull(object);
        Assert.notNull(field);
        try {
            String methodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
            Class<E> clazz = getClaszz(object);
            Method method = clazz.getMethod(methodName);
            return method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取节点显示名称
     *
     * @param entity
     * @param textAlias 名称属性的别名
     * @return
     */
    private String getText(E entity, String textAlias) {
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

}
