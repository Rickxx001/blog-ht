package com.stx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeselectVo {
    List<MenuTreeVo> menus;
    List<Long> checkedKeys;
}
