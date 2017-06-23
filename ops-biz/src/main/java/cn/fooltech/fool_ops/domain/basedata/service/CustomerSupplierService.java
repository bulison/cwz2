package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.redis.BaseDataCache;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.repository.CustomerSupplierViewRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.CsvVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 客户供应商网页服务类
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月8日
 */
@Service
public class CustomerSupplierService extends BaseService<CustomerSupplierView, CsvVo, String> implements BaseDataCache {

    /**
     * 客户供应商服务类
     */
    @Autowired
    private CustomerSupplierViewRepository csvRepo;


    /**
     * 查询客户供应商列表信息
     * 默认为第一页，每页大小默认为10<br>
     *
     * @param vo
     * @return
     */
    public Page<CsvVo> query(CsvVo vo, PageParamater pageParamater) {

        String orgId = SecurityUtil.getCurrentOrgId();
        String categoryName = vo.getCategoryName();
        String areaId = vo.getAreaId();
        Integer type = vo.getType();
        String name = vo.getName();
        String code = vo.getCode();
        String searchKey = vo.getSearchKey();

        Sort sort = new Sort(Direction.ASC, "type", "code");
        PageRequest pageRequest = getPageRequest(pageParamater, sort);
        Page<CustomerSupplierView> page = csvRepo.findPageBy(orgId, categoryName, areaId, type, name, code, searchKey, pageRequest);
        return getPageVos(page, pageRequest);
    }


    /**
     * 单个实体转换为vo
     *
     * @param entity
     * @return
     */
    public CsvVo getVo(CustomerSupplierView entity) {
        Assert.notNull(entity, "参数不能为空!");
        CsvVo vo = VoFactory.createValue(CsvVo.class, entity);
        //组织机构
        Organization org = entity.getOrg();
        if (org != null) {
            vo.setOrgId(org.getFid());
        }
        //地区
        AuxiliaryAttr area = entity.getArea();
        if (area != null) {
            vo.setAreaId(area.getFid());
            vo.setAreaName(area.getName());
        }
        //类别
        AuxiliaryAttr category = entity.getCategory();
        if (category != null) {
            vo.setCategoryId(category.getFid());
            vo.setCategoryName(category.getName());
        }
        return vo;
    }


    @Override
    public CrudRepository<CustomerSupplierView, String> getRepository() {
        return csvRepo;
    }

    @Override
    public String getCacheName() {
        return BaseConstant.CSV;
    }
}
