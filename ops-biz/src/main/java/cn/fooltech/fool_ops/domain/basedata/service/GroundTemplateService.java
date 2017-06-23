package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundTemplate;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GroundTemplateRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundTemplateVo;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 场地报价服务类
 */
@Service
public class GroundTemplateService extends BaseService<GroundTemplate, GroundTemplateVo, String> implements AuxiliaryBiz {


    @Autowired
    private GroundTemplateRepository repository;

    @Autowired
    private AuxiliaryAttrRepository attrRepository;

    @Autowired
    private FreightAddressService addressService;

    /**
     * 场地报价查找分页
     *
     * @param groundId
     * @return
     */
    public List<GroundTemplateVo> query(String searchKey, String groundId, String addressId) {
        String accId = SecurityUtil.getFiscalAccountId();
        if(Strings.isNullOrEmpty(groundId) && !Strings.isNullOrEmpty(addressId)){
            FreightAddress address = addressService.get(addressId);
            groundId = address.getGround().getFid();
        }
        List<GroundTemplate> list = repository.findBy(searchKey, groundId, accId);
        return getVos(list);
    }

    @Override
    public GroundTemplateVo getVo(GroundTemplate entity) {
        GroundTemplateVo vo = VoFactory.createValue(GroundTemplateVo.class, entity);
        AuxiliaryAttr ground = entity.getGround();
        vo.setGroundName(ground.getName());
        vo.setGroundId(ground.getFid());
        return vo;
    }

    @Override
    public CrudRepository<GroundTemplate, String> getRepository() {
        return repository;
    }

    /**
     * 辅助属性-场地类型保存后新增一条记录
     *
     * @param attrId
     */
    @Override
    public void saveAfter(String attrId) {
        GroundTemplate groundt = new GroundTemplate();
        groundt.setAccId(SecurityUtil.getFiscalAccountId());
        groundt.setCreateTime(new Date());
        groundt.setCreatorId(SecurityUtil.getCurrentUserId());
        groundt.setDescribe("");
        groundt.setUpdateTime(new Date());
        groundt.setOrgId(SecurityUtil.getCurrentOrgId());

        AuxiliaryAttr ground = attrRepository.findOne(attrId);
        groundt.setGround(ground);
        repository.save(groundt);
    }

    /**
     * 当辅助属性类型=场地类型时才在保存后新增一条记录
     *
     * @param typeCode
     * @return
     */
    @Override
    public boolean isSupport(String typeCode) {
        if (AuxiliaryAttrType.CODE_SPACE_TYPE.equals(typeCode)) {
            return true;
        }
        return false;
    }

    /**
     * 修改场地报价模板描述
     *
     * @param id
     * @param describe
     * @return
     */
    public RequestResult save(String id, String describe, String updateTime) {
        GroundTemplate groundt = repository.findOne(id);
        if (!checkUpdateTime(updateTime, groundt.getUpdateTime())) {
            return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
        }
        groundt.setDescribe(describe);
        repository.save(groundt);
        return buildSuccessRequestResult();
    }

    /**
     * 删除场地报价模板
     *
     * @param id
     * @return
     */
    public RequestResult delete(String id) {
        GroundTemplate groundt = repository.findOne(id);
        String auxiliaryId = groundt.getGround().getFid();
        repository.delete(id);
        attrRepository.delete(auxiliaryId);
        return buildSuccessRequestResult();
    }
}
