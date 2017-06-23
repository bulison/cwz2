package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.FastStorehousesVo;
import cn.fooltech.fool_ops.domain.basedata.vo.StorehousesVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.TreeDataUtil;
import cn.fooltech.fool_ops.utils.tree.FastTreeUtils;
import cn.fooltech.fool_ops.utils.tree.TreeRootCallBack;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>仓库(辅助属性)网页服务类</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年3月18日
 */
@Service
public class StorehousesService extends BaseService<AuxiliaryAttr, StorehousesVo, String> {

    @Autowired
    private AuxiliaryAttrService attrService;

    @Autowired
    private AuxiliaryAttrRepository repository;


    @Autowired
    private AuxiliaryAttrTypeService attrTypeService;

    @Autowired
    private RedisService redisService;

    /**
     * 获取所有仓库信息（树状结构）
     *
     * @return
     */
    public List<CommonTreeVo> getTree() {

        String orgId = SecurityUtil.getCurrentOrgId(); //机构ID

        AuxiliaryAttrType attrType = attrTypeService.findWarehouse(orgId);

        if(attrType==null)return Collections.EMPTY_LIST;

        List<AuxiliaryAttr> attrRootData = attrService.findAllRootData(attrType.getFid(), orgId);

        List<CommonTreeVo> attrTreeVos = Lists.newArrayList();

        Map<String, String> alias = new HashMap<String, String>();
        alias.put("id", "fid");
        alias.put("text", "name");
        alias.put("children", "childs");
        TreeDataUtil<AuxiliaryAttr> util = new TreeDataUtil<AuxiliaryAttr>(alias, "code", false, true, StorehousesVo.class);

        if (CollectionUtils.isNotEmpty(attrRootData)) {
            for (AuxiliaryAttr attr : attrRootData) {
                List<CommonTreeVo> childTreeVos = util.getTreeData(attr);
                attrTreeVos.addAll(childTreeVos);
            }
        }

        CommonTreeVo vo = new CommonTreeVo();
        vo.setId(attrType.getFid());
        vo.setText(attrType.getName());
        vo.setChildren(new LinkedHashSet<CommonTreeVo>(attrTreeVos));

        List<CommonTreeVo> result = new ArrayList<CommonTreeVo>();
        result.add(vo);
        return result;
    }

    /**
     * 获取仓库明细信息
     *
     * @param fid 仓库ID
     * @return
     */
    public StorehousesVo getDetail(String fid) {
        AuxiliaryAttr entity = attrService.get(fid);
//        if (entity == null) throw new DataNotExistException();
        if (entity == null) return null;
        return getVo(entity);
    }

    /**
     * 单个实体转vo
     *
     * @param entity
     * @return
     */
    public StorehousesVo getVo(AuxiliaryAttr entity) {
        if (entity == null) {
            return null;
        }
        StorehousesVo vo = new StorehousesVo();
        vo.setFid(entity.getFid());
        vo.setCode(entity.getCode());
        vo.setName(entity.getName());
        vo.setFlag(entity.getFlag());
        vo.setLevel(entity.getLevel());
        vo.setEnable(entity.getEnable());
        vo.setDescribe(entity.getDescribe());
        vo.setSystemSign(entity.getSystemSign());
        vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS));
        //上级仓库
        AuxiliaryAttr parent = entity.getParent();
        if (parent != null) {
            vo.setParentId(parent.getFid());
            vo.setParentName(parent.getName());
        }
        return vo;
    }

    /**
     * 新增、编辑仓库
     *
     * @return
     */
    public RequestResult save(StorehousesVo vo) {

        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return buildFailRequestResult(inValid);
        }

        String fid = vo.getFid();
        String code = vo.getCode();
        String name = vo.getName();
        Short enable = vo.getEnable();
        String describe = vo.getDescribe();
        String parentId = vo.getParentId();

        String orgId = SecurityUtil.getCurrentOrgId(); //机构ID
        String accountId = SecurityUtil.getFiscalAccountId(); //财务账套ID
        Date now = Calendar.getInstance().getTime(); //当前时间
        AuxiliaryAttrType attrType = attrTypeService.findByCode(AuxiliaryAttrType.CODE_WAREHOUSE, orgId, accountId); //辅助属性类别
        Assert.notNull(attrType, "仓库类别不存在!");

        if (!checkDataRealTime(vo)) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "页面数据失效，请重新刷新页面!");
        }

        if (attrService.isCodeExist(orgId, accountId, AuxiliaryAttrType.CODE_WAREHOUSE, code, parentId, fid)) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "组内编号重复!");
        }

        AuxiliaryAttr entity = null;
        if (StringUtils.isBlank(fid)) {
            entity = new AuxiliaryAttr();
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entity.setOrg(SecurityUtil.getCurrentOrg());
            entity.setCreator(SecurityUtil.getCurrentUser());
            entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
            //节点标识
            entity.setFlag(AuxiliaryAttr.LEAF);
        } else {
            entity = attrService.get(fid);
            if (entity == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "仓库不存在或已被删除!");
            }
            entity.setUpdateTime(now);
        }

        entity.setCode(code);
        entity.setName(name);
        entity.setEnable(enable);
        entity.setDescribe(describe);
        entity.setCategory(attrType);

        if (StringUtils.isNotBlank(parentId)) {
            AuxiliaryAttr parent = attrService.get(parentId);
            if (parent.getFlag() == AuxiliaryAttr.LEAF) {
                parent.setFlag(AuxiliaryAttr.PARENT);
                attrService.save(parent);
            }

            //上级节点
            entity.setParent(parent);

            //上级节点ID路径
            String parentIds = parent.getParentIds() == null ? "" : parent.getParentIds();
            parentIds = parentIds + "," + parent.getFid();
            entity.setParentIds(parentIds);

            //节点层级
            Integer parentLevel = parent.getLevel();
            entity.setLevel(parentLevel + 1);
        } else {
            //一级节点
            entity.setLevel(1);
        }

        attrService.save(entity);

        if (entity.getFlag() == AuxiliaryAttr.PARENT && entity.getEnable() == AuxiliaryAttr.STATUS_DISABLE) {
            updateChildStatus(entity, AuxiliaryAttr.STATUS_DISABLE);
        }

        redisService.remove(getCacheKey(attrType.getCode()));

        return new RequestResult();
    }


    /**
     * 递归更新下级仓库的有效状态
     *
     * @param entity
     * @param status 状态
     */
    private void updateChildStatus(AuxiliaryAttr entity, Short status) {
        Set<AuxiliaryAttr> childs = entity.getChilds();
        if (CollectionUtils.isNotEmpty(childs)) {
            for (AuxiliaryAttr child : childs) {
                if (child.getEnable() == AuxiliaryAttr.STATUS_ENABLE) {
                    child.setEnable(status);
                    attrService.save(child);
                }
                updateChildStatus(child, status);
            }
        }
    }

    /**
     * 删除仓库
     *
     * @return
     */
    public RequestResult delete(String fid) {
        AuxiliaryAttr attr = attrService.get(fid);
        String typeCode = attr.getCategory().getCode();

        if (attr == null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "仓库不存在或已被删除!");
        }

        try {
            if (CollectionUtils.isNotEmpty(attr.getChilds())) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "请先删除下级仓库!");
            } else if (attrService.auxiliaryUse(fid)) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "仓库已被使用，不允许删除!");
            } else {
                repository.delete(fid);
                redisService.remove(getCacheKey(typeCode));

                return new RequestResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RequestResult(RequestResult.RETURN_FAILURE, "系统繁忙，请稍后再试!");
        }


    }

    /**
     * 更新操作时，校验数据的实时性
     *
     * @param vo 主键、更新时间
     * @return true 有效  false 无效
     */
    private boolean checkDataRealTime(StorehousesVo vo) {
        if (StringUtils.isNotBlank(vo.getFid())) {
            AuxiliaryAttr entity = attrService.get(vo.getFid());
            Date formDate = DateUtils.getDateFromString(vo.getUpdateTime());
            int num = formDate.compareTo(entity.getUpdateTime());
            return num == 0;
        }
        return true;
    }

    @Override
    public CrudRepository<AuxiliaryAttr, String> getRepository() {
        return repository;
    }

    /**
     * 获得缓存的Key
     * @return
     */
    protected String getCacheKey(final String categoryCode){

        StringBuffer keybuff = new StringBuffer();
        keybuff.append(BaseConstant.getCacheKey(categoryCode));
        keybuff.append(":");
        keybuff.append(SecurityUtil.getCurrentOrgId());
        keybuff.append(":");
        keybuff.append(SecurityUtil.getFiscalAccountId());

        String key = keybuff.toString();
        return key;
    }
	/**
	 * 获得简易FastStorehousesVos
	 * @param entitys
	 * @return
	 */
    public List<FastStorehousesVo> getFastVos(List<StorehousesVo> vos){
    	List<FastStorehousesVo> list=Lists.newArrayList();
    	if(vos==null) return list;
    	list=vos.stream().map(s->getFastVo(s)).collect(Collectors.toList());
		return list;
    	
    }
    /**
     * 获取fastVO
     */
    public FastStorehousesVo getFastVo(StorehousesVo vo){
    	if(vo==null) return null;
    	FastStorehousesVo fastVo=new FastStorehousesVo();
    	fastVo.setFid(vo.getFid());
    	fastVo.setName(vo.getName());
    	fastVo.setCode(vo.getCode());
    	
    	//父节点
    	if(!Strings.isNullOrEmpty(vo.getParentId())){
        	StorehousesVo parent=getVo(attrService.findOne(vo.getParentId()));
        	if(parent!=null){
        		String fid = parent.getFid();
        		fastVo.setParentId(fid);
        	}
    	}
    	return fastVo;
    }
	/**
	 * 查询仓库地址树
	 * @param vo
	 */
    public List<FastStorehousesVo> getFastTree(){
        String orgId = SecurityUtil.getCurrentOrgId(); //机构ID

        AuxiliaryAttrType attrType = attrTypeService.findWarehouse(orgId);
        if(attrType==null)return Collections.EMPTY_LIST;

        List<AuxiliaryAttr> attrRootData = attrService.findAllData(attrType.getFid(), orgId);
        List<StorehousesVo> subjects=getVos(attrRootData);
        List<FastStorehousesVo> vos=getFastVos(subjects);
        FastTreeUtils<FastStorehousesVo> fastTreeUtils = new FastTreeUtils<FastStorehousesVo>();
        return fastTreeUtils.buildTreeData(vos,0,new Comparator<FastStorehousesVo>(){

			@Override
			public int compare(FastStorehousesVo arg0, FastStorehousesVo arg1) {
				return arg0.getCode().compareTo(arg0.getCode());
			}
        	
        },new TreeRootCallBack<FastStorehousesVo>(){

			@Override
			public boolean isRoot(FastStorehousesVo v) {
				return Strings.isNullOrEmpty(v.getParentId());
			}
        	
        });
    }
}
