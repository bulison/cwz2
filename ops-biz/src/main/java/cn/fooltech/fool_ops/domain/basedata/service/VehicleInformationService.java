package cn.fooltech.fool_ops.domain.basedata.service;


import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.core.filesystem.FileUtil;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.VehicleInformation;
import cn.fooltech.fool_ops.domain.basedata.repository.VehicleInformationRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.VehicleInformationVo;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.common.vo.AttachVo;
import cn.fooltech.fool_ops.domain.common.vo.Base64Img;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 服务类
 */
@Service
public class VehicleInformationService extends BaseService<VehicleInformation, VehicleInformationVo, String> {


    @Autowired
    private VehicleInformationRepository repository;

//
//    @Autowired
//    private UserAttrService userAttrService;
    @Autowired
    private AttachService attachService;



    /**
     * 实体转换VO
     * @param entity
     * @return
     */
    @Override
    public VehicleInformationVo getVo(VehicleInformation entity) {
        VehicleInformationVo vo = VoFactory.createValue(VehicleInformationVo.class, entity);
        return vo;
    }

    @Override
    public CrudRepository<VehicleInformation, String> getRepository() {
        return repository;
    }

    /**
     * 查找分页
     * @param vo
     * @param paramater
     * @return
     */
    public Page<VehicleInformationVo> query(VehicleInformationVo vo, PageParamater paramater){

        String accId = SecurityUtil.getFiscalAccountId();
        String creatorId = SecurityUtil.getCurrentUserId();
        Sort sort = new Sort(Sort.Direction.DESC, "drivingLicese");
        PageRequest pageRequest = getPageRequest(paramater, sort);
        Page<VehicleInformation> page = repository.findPageBy(accId, creatorId, vo, pageRequest);
        return getPageVos(page, pageRequest);
    }

    /**
     * 修改或新增
     * @param vo
     * @return
     */
    @Transactional
    public RequestResult save(VehicleInformationVo vo){

        VehicleInformation entity = null;
        Long byNum = repository.findByNum(SecurityUtil.getFiscalAccountId(), vo.getLicenseNumber());
        if(Strings.isNullOrEmpty(vo.getId())){
        	if(byNum>0) return buildFailRequestResult("车牌号重复!");
            entity = new VehicleInformation();
            entity.setAccId(SecurityUtil.getFiscalAccountId());
            entity.setCreatorId(SecurityUtil.getCurrentUserId());
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entity.setOrgId(SecurityUtil.getCurrentOrgId());
        }else{
        	
            entity = repository.findOne(vo.getId());
            if(!vo.getLicenseNumber().equals(entity.getLicenseNumber())){
            	if(byNum>0) return buildFailRequestResult("车牌号重复!");
            }
            if(entity.getUpdateTime().compareTo(vo.getUpdateTime())!=0){
//             throw new BusinessException("数据已被其他用户修改，请刷新再试!", 400);

                return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
            }
            entity.setUpdateTime(new Date());
        }

        entity.setId(vo.getId());
        entity.setLicenseNumber(vo.getLicenseNumber());
        entity.setDrivingLicese(vo.getDrivingLicese());
        entity.setOwnerName(vo.getOwnerName());
        entity.setOwnerIdCard(vo.getOwnerIdCard());
        entity.setContactPhone(vo.getContactPhone());
        entity.setFdescribe(vo.getFdescribe());

        repository.save(entity);

        attachService.saveBase64Attach(vo.getBase64Str(), entity.getId());

        return buildSuccessRequestResult(getVo(entity));
    }

    public List<VehicleInformationVo> vagueSearch(VehicleInformationVo vo) {
        String userId = SecurityUtil.getCurrentUserId();

//        String inputType = userAttrService.getInputType(userId);
        Date startDays=null;
        if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(vo.getStartDay()))){
            startDays= DateUtils.getDateFromString(vo.getStartDay());

        }
        Date endDay=null;
        if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(vo.getEndDay()))){
            endDay= DateUtils.getDateFromString(vo.getEndDay());

        }

        List<VehicleInformation> entities = repository.vagueSearch(SecurityUtil.getFiscalAccountId(),
                userId, vo.getSearchKey(), vo.getSearchSize(),startDays ,endDay);
        return getVos(entities);
    }
}
