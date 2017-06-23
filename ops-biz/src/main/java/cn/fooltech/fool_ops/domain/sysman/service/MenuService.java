package cn.fooltech.fool_ops.domain.sysman.service;

import cn.fooltech.fool_ops.component.core.DtoTransfer;
import cn.fooltech.fool_ops.component.security.SecurityUser;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.sysman.entity.Resource;
import cn.fooltech.fool_ops.domain.sysman.entity.UserAttr;
import cn.fooltech.fool_ops.domain.sysman.repository.ResourceRepository;
import cn.fooltech.fool_ops.domain.sysman.repository.UserAttrRepository;
import cn.fooltech.fool_ops.domain.sysman.vo.MenuVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.input.FivepenUtils;
import cn.fooltech.fool_ops.utils.input.PinyinUtils;
import cn.fooltech.fool_ops.utils.tree.FastTreeUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 资源缓存对象
 *
 * @author xjh
 */
@Service
public class MenuService implements DtoTransfer<Resource, MenuVo> {

    @Autowired
    private ResourceRepository resourceRepo;

    @Autowired
    private UserAttrRepository userAttrRepo;

    /**
     * 构建资源菜单树
     *
     * @param trimRoot
     * @return
     */
    public List<MenuVo> buildMenuData(boolean trimRoot) {
        SecurityUser user = SecurityUtil.getSecurityUser();
        if (user == null) return Collections.emptyList();

        boolean isAdmin = user.isAdmin();
        List<Resource> resList = resourceRepo.findMenuWithOrgLevel(isAdmin);
        Collection<? extends GrantedAuthority> auths = user.getAuthorities();
        List<Resource> authRes = Lists.newArrayList();
        for (Resource res : resList) {
            if(res.getShow()!=null && res.getShow()== Constants.NOTSHOW)continue;
            for (GrantedAuthority auth : auths) {
                if (auth.getAuthority().equals(res.getCode())) {
                    authRes.add(res);
                    break;
                }
            }
        }

        List<MenuVo> menus = getVos(authRes);

        FastTreeUtils<MenuVo> util = new FastTreeUtils<MenuVo>();
        MenuVo comparator = new MenuVo();
        menus = util.buildTreeData(menus, comparator);
        if (trimRoot && menus.size()>0) {
            menus = menus.get(0).getChildren();//去掉根节点
        }

        return menus;
    }

    /**
     * 过滤资源
     *
     * @return
     */
    public List<MenuVo> filterRes(List<MenuVo> buildData, String name) {

        name = name.toLowerCase();
        String userId = SecurityUtil.getCurrentUserId();
        UserAttr userAttr = userAttrRepo.findTopBy(userId, UserAttr.INPUT_TYPE);
        String inputType = userAttr == null ? UserAttr.INPUT_TYPE_PINYIN : userAttr.getValue();

        for (int i = buildData.size() - 1; i >= 0; i--) {
            MenuVo vo = buildData.get(i);

            List<MenuVo> iterData = vo.getChildren();

            for (int j = iterData.size() - 1; j >= 0; j--) {

                MenuVo itervo = iterData.get(j);
                String resName = itervo.getLabel();
                String code = null;

                if (UserAttr.INPUT_TYPE_FIVEPEN.equals(inputType)) {
                    code = FivepenUtils.getFivePenCode(resName);
                } else {
                    code = PinyinUtils.getPinyinCode(resName);
                }
                if (code == null) code = "";
                code = code.toLowerCase();
                //排除过滤的节点
                if (!resName.contains(name) && !code.contains(name)) {
                    iterData.remove(j);
                }
            }

            //排除空目录
            if (vo.getChildren().size() == 0) {
                buildData.remove(i);
                continue;
            }
        }
        return buildData;
    }


    @Override
    public MenuVo getVo(Resource resource) {
        MenuVo menuItem = new MenuVo();
        menuItem.setId(resource.getFid());
        menuItem.setLabel(resource.getResName());
        menuItem.setSmallIcoPath(resource.getSmallIcoPath());
        menuItem.setUrl(makeURL(resource));
        if (resource.getRankOrder() != null) {
            menuItem.setIndex(resource.getRankOrder());
        }
        if (resource.getParent() != null) {
            menuItem.setParentId(resource.getParent().getFid());
        }
        return menuItem;
    }


    //	@Override
//	public  List<MenuVo> getVos(List<Resource> entitys){
//		List<MenuVo> list = Lists.newArrayList();
//		if(entitys==null)return list;
//		for(Resource e:entitys){
//			list.add(getVo(e));
//		}
//		return list;
//	}
    private String makeURL(Resource res) {
        String url = res.getResString();
        if (StringUtils.isBlank(url)) return "";
        if (url.indexOf("?") > 0) {
            return url + "&sys_menu_id=" + res.getFid();
        } else {
            return url + "?sys_menu_id=" + res.getFid();
        }
    }

}
