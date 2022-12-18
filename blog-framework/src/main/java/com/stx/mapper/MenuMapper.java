package com.stx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stx.domain.entity.Menu;
import com.stx.domain.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-09 11:16:33
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<MenuVo> selectAllRouterMenu();

    List<MenuVo> SelectRouterMenuTreeByUserId(Long userId);
}
