package cn.fooltech.fool_ops.domain.sysman.service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.sysman.entity.CommonUseModule;
import cn.fooltech.fool_ops.domain.sysman.entity.Resource;
import cn.fooltech.fool_ops.domain.sysman.repository.CommonUseModuleRepository;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;


/**
 * <p>用户常用模块网页服务类</p>
 */
@Service
public class CommonUseModuleService extends AbstractBaseService<CommonUseModule, String> {

    @Autowired
    private CommonUseModuleRepository moduleRepo;

    @Autowired
    private ResourceService resourceService;

    /**
     * 查询
     */
    public List<CommonUseModule> query() {
        String userId = SecurityUtil.getCurrentUserId();
        List<CommonUseModule> modules = moduleRepo.findByCreatorId(userId);
        return modules;
    }

    /**
     * 新增(没有编辑)
     *
     * @param resourceIds
     */
    @Transactional
    public RequestResult save(String resourceIds) {
        deleteAll();
        if (StringUtils.isNotBlank(resourceIds)) {
            Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
            List<String> idArray = splitter.splitToList(resourceIds);
            for (String id : idArray) {
                Resource resource = resourceService.get(id);
                add(resource);
            }
        }
        return new RequestResult();
    }

    /**
     * 添加
     *
     * @param resource 资源
     */
    private void add(Resource resource) {
        CommonUseModule entity = new CommonUseModule();
        entity.setResource(resource);
        entity.setCreator(SecurityUtil.getCurrentUser());
        entity.setCreateTime(Calendar.getInstance().getTime());
        moduleRepo.save(entity);
    }

    /**
     * 删除用户的常用模块
     */
    private void deleteAll() {
        String userId = SecurityUtil.getCurrentUserId();
        List<CommonUseModule> modules = moduleRepo.findByCreatorId(userId);
        moduleRepo.delete(modules);
    }

    @Override
    public CrudRepository<CommonUseModule, String> getRepository() {
        return moduleRepo;
    }

}
