package com.stx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stx.constants.SystemConstants;
import com.stx.domain.ResponseResult;
import com.stx.domain.entity.Menu;
import com.stx.domain.entity.RoleMenu;
import com.stx.domain.vo.*;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.exception.SystemException;
import com.stx.mapper.MenuMapper;
import com.stx.mapper.RoleMenuMapper;
import com.stx.service.MenuService;
import com.stx.service.RoleMenuService;
import com.stx.utils.BeanCopyUtils;
import com.stx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-12-09 11:16:35
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public ResponseResult getRoleMenuTreeselect(Long id) {
//        菜单树 + menu id列表
        List<MenuTreeVo> menuTreeVos = builderMenuTreeV(0L);
        List<Long> checkedKeys = roleMenuMapper.getRoleMenuList(id);
        RoleMenuTreeselectVo roleMenuTreeselectVo = new RoleMenuTreeselectVo(menuTreeVos, checkedKeys);
        return ResponseResult.okResult(roleMenuTreeselectVo);
    }

    @Override
    public ResponseResult menuTree() {

        List<MenuTreeVo> menuTreeVos1 = builderMenuTreeV(0l);
        return ResponseResult.okResult(menuTreeVos1);
    }

    //    构建菜单树
    private List<MenuTreeVo> builderMenuTreeV(long parentId) {
        List<Menu> list = list();
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(list, MenuTreeVo.class);
        List<MenuTreeVo> collect = menuTreeVos.stream()
                .filter(menutree -> menutree.getParentId().equals(parentId))
                .map(m -> m.setLabel(m.getMenuName()))
                .map(menuTreeVo -> menuTreeVo.setChildren(getChildrenV(menuTreeVo, menuTreeVos)))
                .collect(Collectors.toList());
        return collect;
    }

    private List<MenuTreeVo> getChildrenV(MenuTreeVo menuTreeVo, List<MenuTreeVo> menuTreeVos) {
        List<MenuTreeVo> collect = menuTreeVos.stream()
                .filter(mt -> mt.getParentId().equals(menuTreeVo.getId()))
                .map(m -> m.setLabel(m.getMenuName()))
                .map(m -> m.setChildren(getChildrenV(m, menuTreeVos)))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getParentId, id);
        List<Menu> list = list(menuLambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            throw new SystemException(AppHttpCodeEnum.MENU_DEL_ERROR);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            throw new SystemException(AppHttpCodeEnum.MENU_ERROR);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectMenu(Long id) {
        Menu byId = getById(id);
        SelectMenuVo selectMenuVo = BeanCopyUtils.copyBean(byId, SelectMenuVo.class);
        return ResponseResult.okResult(selectMenuVo);
    }

    @Override
    public ResponseResult saveMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult menuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.like(StringUtils.hasText(status), Menu::getStatus, status);
        menuLambdaQueryWrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        menuLambdaQueryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> list = list(menuLambdaQueryWrapper);
        List<MenuListVo> menuListVos = BeanCopyUtils.copyBeanList(list, MenuListVo.class);
        return ResponseResult.okResult(menuListVos);
    }

    @Override
    public List<String> selectPermsByUserId(Long id) {
//        对于管理员 返回全部菜单
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuLambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            menuLambdaQueryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> list = list(menuLambdaQueryWrapper);
            List<String> Perms = list.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return Perms;
        }
        List<String> perms = getBaseMapper().selectPermsByUserId(id);
        return perms;
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper baseMapper = getBaseMapper();
        List<MenuVo> menu = null;
//        管理员返回所有菜单
        if (SecurityUtils.isAdmin()) {
            menu = baseMapper.selectAllRouterMenu();
        } else {
//            否者返回当前用户具有的menu
            menu = baseMapper.SelectRouterMenuTreeByUserId(userId);
        }

//        构建tree
        List<MenuVo> menuTree = builderMenuTree(menu, 0L);
        return menuTree;
    }

    private List<MenuVo> builderMenuTree(List<MenuVo> menu, long parentId) {
//    先找出第一层菜单， 然后去找其子菜单 ，设置为children
        List<MenuVo> menuTree = menu.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(getChildren(menuVo, menu)))
                .collect(Collectors.toList());
        return menuTree;
    }

    //    或取 子菜单
    private List<MenuVo> getChildren(MenuVo menuVo, List<MenuVo> menu) {
        List<MenuVo> collect = menu.stream()
                .filter(menu1 -> menu1.getParentId().equals(menuVo.getId()))
                .map(m -> m.setChildren(getChildren(m, menu)))
                .collect(Collectors.toList());
        return collect;
    }


}

