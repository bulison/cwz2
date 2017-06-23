package cn.fooltech.fool_ops.domain.sysman.service;

import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.flow.entity.SecurityLevel;
import cn.fooltech.fool_ops.domain.sysman.entity.UserSecurityLevel;
import cn.fooltech.fool_ops.domain.sysman.repository.UserSecurityLevelRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>用户保密级别服务类</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年6月30日
 */
@Service
public class UserSecurityLevelService extends BaseService<UserSecurityLevel, UserSecurityLevel, String> {

    @Autowired
    private UserSecurityLevelRepository repository;

    /**
     * 获取用户的保密级别
     *
     * @param userId
     * @return
     */
    public UserSecurityLevel getByUserId(String userId) {
        List<UserSecurityLevel> data = repository.getByUserId(userId);
        if (CollectionUtils.isNotEmpty(data)) {
            if (data.size() > 1) {
                throw new RuntimeException("用户拥有了多个保密级别!");
            }
            return data.get(0);
        }
        return null;
    }

    /**
     * 获取用户的保密级别
     *
     * @param userId 用户ID
     * @return
     */
    public SecurityLevel getSecurityLevel(String userId) {
        List<UserSecurityLevel> data = repository.getByUserId(userId);
        if (CollectionUtils.isNotEmpty(data)) {
            if (data.size() > 1) {
                throw new RuntimeException("用户拥有了多个保密级别!");
            }
            return data.get(0).getSecurityLevel();
        }
        return null;
    }

    /**
     * 获取用户的保密级别
     *
     * @param userId 用户ID
     * @return
     */
    public Integer getLevel(String userId) {
        List<UserSecurityLevel> data = repository.getByUserId(userId);
        if (CollectionUtils.isNotEmpty(data)) {
            if (data.size() > 1) {
                throw new RuntimeException("用户拥有了多个保密级别!");
            }
            return data.get(0).getSecurityLevel().getLevel();
        }
        return null;
    }

    /**
     * 统计保密级别被引用的次数
     *
     * @param securityLevelId 保密级别ID
     * @return
     */
    public long countBySecurityLevel(String securityLevelId) {
        return repository.countBySecurityLevel(securityLevelId);
    }


    @Override
    public CrudRepository<UserSecurityLevel, String> getRepository() {
        return repository;
    }

    @Override
    public UserSecurityLevel getVo(UserSecurityLevel entity) {
        return null;
    }

}
