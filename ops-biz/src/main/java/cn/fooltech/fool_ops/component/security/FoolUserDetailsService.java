package cn.fooltech.fool_ops.component.security;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.sysman.entity.Resource;
import cn.fooltech.fool_ops.domain.sysman.entity.Role;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserAuthRepository;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;


@Component
public class FoolUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private FiscalAccountService accountService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(Strings.isNullOrEmpty(username))return null;
        User user = userRepository.findOneByUserCode(username);
        if (user == null) return null;

        List<Resource> resources = userAuthRepository.findByUser(user.getFid());
        Set<String> permCodes = Sets.newHashSet();
        for (Resource perm : resources) {
            permCodes.add(perm.getCode());
        }

        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            List<Resource> roleAuths = role.getResources();
            for (Resource perm : roleAuths) {
                permCodes.add(perm.getCode());
            }
        }

        //账套
        String orgId = user.getTopOrg().getFid();
        String userId = user.getFid();
        FiscalAccount defaultAccount = accountService.createDefaultAccount(orgId, userId);
        SecurityUser suser = new SecurityUser(user, permCodes);

        suser.setFiscalAccountId(defaultAccount.getFid());
        suser.setFiscalAccountName(defaultAccount.getName());
        return suser;
    }
}
