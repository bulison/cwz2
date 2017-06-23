package cn.fooltech.fool_ops.domain.register.service;

import cn.fooltech.fool_ops.domain.datainit.DataInitService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountVo;
import cn.fooltech.fool_ops.domain.register.vo.UserAndOrgInfoVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.Role;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.sysman.service.RoleService;
import cn.fooltech.fool_ops.domain.sysman.service.UserService;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("ops.RegisterService")
public class RegisterService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private DataInitService initService;

    @Autowired
    private FiscalAccountService accountService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OrgService orgService;

    /**
     * 注册用户信息
     *
     * @param vo
     */
    @Transactional
    public void saveUserInfo(UserAndOrgInfoVo vo) {
        Date now = new Date();

        //角色
        List<Role> roles = Lists.newArrayList();
        Role role = roleService.get("402881114d8da47b014d8daa33120008");
        roles.add(role);

        //机构
        Organization org = VoFactory.createValue(Organization.class, vo);
        org.setFlevel(Short.valueOf("0"));
        org.setParentIds("");
        org.setValidation((short)1);
        orgService.save(org);

        //用户
        User user = VoFactory.createValue(User.class, vo);
        user.setSex((short) 1);
        user.setCreateDate(now);
        user.setIsAdmin((short) 1); //用户最高级别管理员
        user.setValidation((short) 1);
        user.setUserCode(vo.getUserCode());
        user.setUserName(vo.getUserCode());
        user.setAccountName(vo.getUserCode());
        user.setOrgId(org);
        user.setTopOrg(org);
        user.setRoles(roles);
        userRepo.save(user);

        //更新机构
        org.setCreateUser(user);
        org.setOrgId(org.getFid());
        orgService.save(org);

        //--------初始化数据 start------
        LinkedHashMap<String, String> map = Maps.newLinkedHashMap();
        map.put("CORP_NAME", org.getOrgName());
        map.put("FCREATE_TIME", DateUtils.format(now, "yyyy-MM-dd HH:mm:ss"));
        map.put("FCREATOR_ID", user.getFid());
        map.put("FORG_ID", org.getFid());
        initService.initAll(map);

        initFiscalAccount(org, user);
        //--------初始化数据 end------
    }


    /**
     * 初始化账套
     */
    private void initFiscalAccount(Organization org, User creator) {
        FiscalAccountVo account = new FiscalAccountVo();
        account.setDefaultFlag(FiscalAccount.FLAG_YES);
        account.setEnable(FiscalAccount.ENABLE_YES);

        account.setName(org.getOrgName());
        account.setDescription("企业默认账套");
        accountService.saveInitAccount(account, org, creator);
    }

    /**
     * 根据userCode判断用户是否重复
     *
     * @param vo
     * @return
     */
    public boolean isUserExists(UserAndOrgInfoVo vo) {
        User user = userRepo.findOneByUserCode(vo.getUserCode());
        if (user == null) {
            return false;
        }
        return true;
    }

}
