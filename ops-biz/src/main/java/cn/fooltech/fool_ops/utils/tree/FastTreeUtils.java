package cn.fooltech.fool_ops.utils.tree;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 树状结构数据工具类
 *
 * @param <V>
 * @author xjh
 * @version 1.0
 * @date 2015年12月4日
 */
public class FastTreeUtils<V extends FastTreeVo> {


    /**
     * 构建树的数据,默认2级以下的节点关闭
     *
     * @param dataVos :源数据
     */
    public List<V> buildTreeData(final List<V> dataVos) {
        return buildTreeData(dataVos, 2);
    }

    /**
     * 构建树的数据,默认2级以下的节点关闭
     *
     * @param dataVos :源数据
     */
    public List<V> buildTreeData(final List<V> dataVos, Comparator<V> comparator) {
        List<V> resultVos = buildTreeData(dataVos);
        recurseSort(resultVos, comparator);
        return resultVos;
    }

    /**
     * 构建树的数据,默认2级以下的节点关闭
     *
     * @param dataVos :源数据
     */
    public List<V> buildTreeData(final List<V> dataVos, TreeRootCallBack<V> callback) {
        List<V> resultVos = buildTreeData(dataVos, 2, callback);
        return resultVos;
    }

    /**
     * 构建树的数据
     *
     * @param dataVos    :源数据
     * @param closeLevel :要关闭的树节点的层次
     */
    public List<V> buildTreeData(final List<V> dataVos, final int closeLevel, Comparator<V> comparator) {

        List<V> treeData = buildTreeData(dataVos, closeLevel);
        recurseSort(treeData, comparator);
        return treeData;
    }

    /**
     * 构建树的数据,默认2级以下的节点关闭
     *
     * @param dataVos :源数据
     */
    public List<V> buildTreeData(final List<V> dataVos, final int closeLevel, Comparator<V> comparator, TreeRootCallBack<V> callback) {
        List<V> resultVos = buildTreeData(dataVos, closeLevel, callback);
        recurseSort(resultVos, comparator);
        return resultVos;
    }
    public List<V> buildTreeData2(final List<V> dataVos, final int closeLevel, Comparator<V> comparator, TreeRootCallBack<V> callback) {
    	List<V> resultVos = buildTreeData2(dataVos, closeLevel, callback);
    	recurseSort(resultVos, comparator);
    	return resultVos;
    }

    /**
     * 构建树的数据
     *
     * @param dataVos    :源数据
     * @param closeLevel :要关闭的树节点的层次
     */
    public List<V> buildTreeData(final List<V> dataVos, final int closeLevel) {

        List<V> treeData = new ArrayList<V>();

        for (int i = dataVos.size() - 1; i > -1; i--) {

            if (StringUtils.isBlank(dataVos.get(i).getParentId())) {
                V vo = dataVos.remove(i);
                vo.setLevel(1);
                if (closeLevel == 0) vo.setState(FastTreeVo.STATE_CLOSED);
                if (vo.getLevel() >= closeLevel) {
                    vo.setState(FastTreeVo.STATE_CLOSED);
                }
                treeData.add(vo);// 添加根节点
            }
        }
        recurseTreeList(dataVos, treeData, closeLevel);

        return treeData;
    }

    /**
     * 构建树的数据
     *
     * @param dataVos    :源数据
     * @param closeLevel :要关闭的树节点的层次
     */
    public List<V> buildTreeData(final List<V> dataVos, final int closeLevel, TreeRootCallBack<V> callback) {

        List<V> treeData = new ArrayList<V>();

        for (int i = dataVos.size() - 1; i > -1; i--) {

            if (callback.isRoot(dataVos.get(i))) {
                V vo = dataVos.remove(i);
                vo.setLevel(1);
                if (closeLevel == 0) vo.setState(FastTreeVo.STATE_CLOSED);
//                if (vo.getLevel() >= closeLevel) {
//                    vo.setState(FastTreeVo.STATE_CLOSED);
//                }
                treeData.add(vo);// 添加根节点
            }
        }
        recurseTreeList(dataVos, treeData, closeLevel);

        return treeData;
    }
    /**
     * 构建树的数据
     *
     * @param dataVos    :源数据
     * @param closeLevel :要关闭的树节点的层次
     */
    public List<V> buildTreeData2(final List<V> dataVos, final int closeLevel, TreeRootCallBack<V> callback) {
    	
    	List<V> treeData = new ArrayList<V>();
    	
    	for (int i = dataVos.size() - 1; i > -1; i--) {
    		
    		if (callback.isRoot(dataVos.get(i))) {
    			V vo = dataVos.remove(i);
    			vo.setLevel(1);
    			if (closeLevel == 0) vo.setState(FastTreeVo.STATE_CLOSED);
    			if (vo.getLevel() >= closeLevel) {
    				vo.setState(null);
    			}
    			treeData.add(vo);// 添加根节点
    		}
    	}
    	recurseTreeList(dataVos, treeData, closeLevel);
    	
    	return treeData;
    }


    /**
     * 递归设置子节点List
     *
     */
    private void recurseTreeList(List<V> dataVos,
                                 List<V> treeData, int closeLevel) {
        if (dataVos.size() > 0) {
            for (int i = dataVos.size() - 1; i > -1; i--) {

                if (recurseTree(dataVos.get(i), treeData, closeLevel)) {
                    dataVos.remove(i);
                    recurseTreeList(dataVos, treeData, closeLevel);
                    return;
                }
            }
        }
    }

    /**
     * 递归设置子节点
     *
     */
    private boolean recurseTree(V curr, List<V> children, int closeLevel) {
        if (Strings.isNullOrEmpty(curr.getParentId())) return false;
        for (V child : children) {
            if (curr.getParentId().equals(child.getId())) {
                curr.setLevel(child.getLevel() + 1);
                child.getChildren().add(curr);
                if (closeLevel == 0) child.setState(FastTreeVo.STATE_CLOSED);
                if (child.getLevel() >= closeLevel) {
                    child.setState(FastTreeVo.STATE_CLOSED);
                }

                return true;
            } else {
                if (recurseTree(curr, child.getChildren(), closeLevel)) {
                    return true;
                }
            }
        }
        if(curr.getChildren()!=null && curr.getChildren().size()==0 && curr.getLevel()<closeLevel){
            curr.setState(FastTreeVo.STATE_OPEN);
        }
        return false;
    }

    /**
     * 排序
     *
     * @param vos
     * @param comparator
     */
    public void recurseSort(List<V> vos, Comparator<V> comparator) {
        if (vos == null || vos.size() == 0) return;
        Collections.sort(vos, comparator);
        for (V vo : vos) {
            recurseSort((List<V>) vo.getChildren(), comparator);
        }
    }

}
