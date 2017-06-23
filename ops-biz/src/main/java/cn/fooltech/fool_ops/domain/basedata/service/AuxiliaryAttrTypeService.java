package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrTypeRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrTypeVo;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountRepository;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.List;

/**
 * 辅助属性类型网页服务类
 *
 * @author lgk
 * @version V1.0
 * @date 2015年12月23日下午3:51:02
 */
@Service
public class AuxiliaryAttrTypeService extends BaseService<AuxiliaryAttrType, AuxiliaryAttrTypeVo, String> {
    @Autowired
    private AuxiliaryAttrTypeRepository attrTypeRepository;

    /**
     * 账套服务类
     */
    @Autowired
    private FiscalAccountRepository accountRepo;

    /**
     * 辅助属性服务类
     */
    @Autowired
    private AuxiliaryAttrService auxiliaryAttrService;
    @Autowired
    private AuxiliaryAttrRepository attrRepository;

    /**
     * 查询辅助属性类型列表信息，按照辅助属性类型主键降序排列<br>
     * 默认为第一页，每页大小默认为10<br>
     *
     * @param vo
     */
    public Page<AuxiliaryAttrTypeVo> query(AuxiliaryAttrTypeVo vo, PageParamater pageable) {
        PageRequest request = getPageRequest(pageable);
        String orgId = SecurityUtil.getCurrentOrgId();
        String accountId = SecurityUtil.getFiscalAccountId();
        Page<AuxiliaryAttrType> page = attrTypeRepository.findPageByOrgIdOrderByCreateTimeDesc(orgId, accountId, request);
        getPageVos(page, request);
        return getPageVos(page, request);
    }

    /**
     * 获取辅助属性类型信息
     *
     * @param fid 辅助属性ID
     * @return
     */
    public AuxiliaryAttrTypeVo getByFid(String fid) {
        Assert.notNull(fid);
        AuxiliaryAttrTypeVo entity = getById(fid);
        if (entity == null) throw new DataNotExistException();
        return entity;
    }

    /**
     * 新增/修改
     *
     * @return
     */
    public RequestResult save(AuxiliaryAttrTypeVo vo) {
        //演示hibernate-validator的使用；也可以加在方法的参数中methd(@Valid xxx)
        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
        }

        AuxiliaryAttrType entity = new AuxiliaryAttrType();
        if (StringUtils.isBlank(vo.getFid())) {
            entity.setCreateTime(Calendar.getInstance().getTime());
            entity.setCreator(SecurityUtil.getCurrentUser());
            entity.setOrg(SecurityUtil.getCurrentOrg());
        } else {

            if (vo.getEnable() == AuxiliaryAttrType.STATUS_DISABLE) {
                Long count = attrRepository.countByCategory(vo.getFid());
                if (count > 0) return new RequestResult(RequestResult.RETURN_FAILURE, "该节点存在有效的子节点属性，不允许设置为无效！");
            }

            entity = attrTypeRepository.findOne(vo.getFid());
            if (entity == null) throw new DataNotExistException();

            attrTypeRepository.save(entity);
        }

        entity.setCode(vo.getCode());
        entity.setName(vo.getName());
        entity.setDescribe(vo.getDescribe());
        entity.setEnable(vo.getEnable());
        entity.setUpdateTime(Calendar.getInstance().getTime());

        String oldAccId = entity.getFiscalAccount() == null ? "" : entity.getFiscalAccount().getFid();
        String newAccId = "";

        //财务账套
        if (StringUtils.isBlank(vo.getFiscalAccountId())) {
            if (vo.getIsAccount() != null && vo.getIsAccount() == 1) {
                entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
                newAccId = SecurityUtil.getFiscalAccountId();
            } else {
                entity.setFiscalAccount(accountRepo.findOne(newAccId));
            }
        } else {
            newAccId = vo.getFiscalAccountId();
            entity.setFiscalAccount(accountRepo.findOne(newAccId));
        }
        attrTypeRepository.save(entity);
        if (!oldAccId.equals(newAccId)) {
            auxiliaryAttrService.fixAccountByCategory(entity);
        }

        return buildSuccessRequestResult(getVo(entity));
    }


    @Override
    public AuxiliaryAttrTypeVo getVo(AuxiliaryAttrType entity) {
        AuxiliaryAttrTypeVo vo = VoFactory.createValue(AuxiliaryAttrTypeVo.class, entity);
        vo.setCreateName(entity.getCreator().getUserName());
        vo.setIsAccount((short) (entity.getFiscalAccount() == null ? 0 : 1));
        return vo;
    }

    public AuxiliaryAttrType findWarehouse(String orgId) {
        List<AuxiliaryAttrType> list = attrTypeRepository.findWarehouse(orgId);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    public AuxiliaryAttrType findTransportFee(String orgId) {
        List<AuxiliaryAttrType> list = attrTypeRepository.findTransportFee(orgId);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    @Override
    public CrudRepository<AuxiliaryAttrType, String> getRepository() {
        return this.attrTypeRepository;
    }

    /**
     * 根据辅助属性分类编码查找辅助属性分类
     *
     * @param code
     * @param orgId
     * @param accountId
     * @return
     */
    public AuxiliaryAttrType findByCode(String code, String orgId, String accountId) {
        List<AuxiliaryAttrType> list = attrTypeRepository.findByCode(code, orgId, accountId);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
