package cn.fooltech.fool_ops.domain.basedata.service;


import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundTemplate;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundTemplateDetail;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GroundTemplateDetailRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GroundTemplateRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundTemplateDetailVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
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
 * 场地报价模板
 */
@Service
public class GroundTemplateDetailService extends BaseService<GroundTemplateDetail, GroundTemplateDetailVo, String> {

    @Autowired
    private AuxiliaryAttrRepository attrRepo;

    @Autowired
    private GroundTemplateRepository templateRrepository;

    @Autowired
    private GroundTemplateDetailRepository repository;

    @Override
    public GroundTemplateDetailVo getVo(GroundTemplateDetail entity) {
        GroundTemplateDetailVo vo = VoFactory.createValue(GroundTemplateDetailVo.class, entity);

        if (!Strings.isNullOrEmpty(entity.getTransportCostId())) {
            AuxiliaryAttr transportCost = attrRepo.findOne(entity.getTransportCostId());
            vo.setTransportCostName(transportCost.getName());
        }

        if (!Strings.isNullOrEmpty(entity.getTransportUnitId())) {
            AuxiliaryAttr transportUnit = attrRepo.findOne(entity.getTransportUnitId());
            vo.setTransportUnitName(transportUnit.getName());
        }

        return vo;
    }

    @Override
    public CrudRepository<GroundTemplateDetail, String> getRepository() {
        return repository;
    }

    /**
     * 保存明细
     *
     * @param vo
     * @return
     */
    public RequestResult save(GroundTemplateDetailVo vo) {

        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return buildFailRequestResult(inValid);
        }

        GroundTemplateDetail entity = null;
        if (Strings.isNullOrEmpty(vo.getId())) {
            entity = new GroundTemplateDetail();
            entity.setOrgId(SecurityUtil.getCurrentOrgId());
            entity.setAccId(SecurityUtil.getFiscalAccountId());
            entity.setCreatorId(SecurityUtil.getCurrentUserId());
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            GroundTemplate groundTemplate = templateRrepository.findOne(vo.getTemplateId());
            entity.setTemplate(groundTemplate);
        } else {
            entity = repository.findOne(vo.getId());
            if (!checkUpdateTime(vo.getUpdateTime(), entity.getUpdateTime())) {
                return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
            }
        }
        entity.setDescribe(vo.getDescribe());
        entity.setCostSign(vo.getCostSign());
        entity.setTransportCostId(vo.getTransportCostId());
        entity.setTransportUnitId(vo.getTransportUnitId());
        entity.setUpdateTime(new Date());
        repository.save(entity);

        return buildSuccessRequestResult(getVo(entity));
    }

    /**
     * 查找列表
     * @param templateId
     * @return
     */
    public List<GroundTemplateDetailVo> findByTemplateId(String templateId) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        String accId = SecurityUtil.getFiscalAccountId();
        List<GroundTemplateDetail> list = repository.findBy(accId, templateId, sort);
        return getVos(list);
    }

    /**
     * 查找列表
     * @param groundId
     * @return
     */
    public List<GroundTemplateDetailVo> findByGroundId(String groundId) {
        String accId = SecurityUtil.getFiscalAccountId();
        List<GroundTemplateDetail> list = repository.findBy(accId, groundId);
        return getVos(list);
    }

    /**
     * 查找分页
     *
     * @param paramater
     * @return
     */
    public Page<GroundTemplateDetailVo> query(String templateId, PageParamater paramater) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = getPageRequest(paramater, sort);
        String accId = SecurityUtil.getFiscalAccountId();
        Page<GroundTemplateDetail> page = repository.findPageBy(accId, templateId, pageRequest);
        return getPageVos(page, pageRequest);
    }
}
