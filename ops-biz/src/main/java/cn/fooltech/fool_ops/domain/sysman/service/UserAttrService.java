package cn.fooltech.fool_ops.domain.sysman.service;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.sysman.entity.UserAttr;
import cn.fooltech.fool_ops.domain.sysman.repository.UserAttrRepository;
import cn.fooltech.fool_ops.domain.sysman.vo.UserAttrVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>用户属性服务类</p>
 *
 * @author xjh
 * @version 2.0
 * @date 2016年10月18日
 */
@Service
public class UserAttrService extends BaseService<UserAttr, UserAttrVo, String> {

    @Autowired
    private UserAttrRepository userAtrrRepo;

    /**
     * 根据用户ID和KEY获取用户属性
     *
     * @param userId
     * @param key
     * @return
     */
    public List<UserAttr> queryByIdAndKey(String userId, String key) {
        return userAtrrRepo.findByIdAndKey(userId, key);
    }

    /**
     * 获得当前用户输入法
     *
     * @return
     */
    public String getInputType() {
        String userId = SecurityUtil.getCurrentUserId();
        return getInputType(userId);
    }

    /**
     * 获得当前用户缓存配置
     *
     * @return
     */
    public String getLocalCache() {
        String userId = SecurityUtil.getCurrentUserId();
        return getInputType(userId);
    }

    /**
     * 获得指定用户输入法
     *
     * @param userId
     * @return
     */
    public String getInputType(String userId) {
        UserAttr attr = userAtrrRepo.findTopBy(userId, UserAttr.INPUT_TYPE);
        if (attr == null) return Constants.DEFAULT_INPUT_TYPE;
        if (Strings.isNullOrEmpty(attr.getValue())) return Constants.DEFAULT_INPUT_TYPE;
        return attr.getValue();
    }

    /**
     * 获得指定用户缓存配置
     *
     * @param userId
     * @return
     */
    public String getLocalCache(String userId) {
        UserAttr attr = userAtrrRepo.findTopBy(userId, UserAttr.LOCAL_CACHE);
        if (attr == null) return Constants.DEFAULT_LOCAL_CACHE;
        if (Strings.isNullOrEmpty(attr.getValue())) return Constants.DEFAULT_LOCAL_CACHE;
        return attr.getValue();
    }

    @Override
    public UserAttrVo getVo(UserAttr entity) {
        return null;
    }

    @Override
    public CrudRepository<UserAttr, String> getRepository() {
        return userAtrrRepo;
    }

}
