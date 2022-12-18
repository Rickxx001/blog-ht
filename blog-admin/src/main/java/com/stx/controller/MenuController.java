package com.stx.controller;

import com.stx.annotation.SystemLog;
import com.stx.domain.ResponseResult;
import com.stx.domain.entity.Menu;
import com.stx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @SystemLog(businessName = "返回角色菜单树")
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getRoleMenuTreeselect(@PathVariable Long id){
        return menuService.getRoleMenuTreeselect(id);
    }

    @SystemLog(businessName = "返回菜单树")
    @GetMapping("/treeselect")
    public ResponseResult menuTree(){
       return menuService.menuTree();
    }


    @SystemLog(businessName = "删除菜单")
    @DeleteMapping("/{id}")
    public ResponseResult deleteMenu(@PathVariable Long id) {
        return menuService.deleteMenu(id);
    }

    @SystemLog(businessName = "获取菜单列表")
    @GetMapping("/list")
    public ResponseResult menuList(String status, String menuName) {
        return menuService.menuList(status, menuName);
    }

    @SystemLog(businessName = "新增菜单")
    @PostMapping
    public ResponseResult saveMenu(@RequestBody Menu menu) {
        return menuService.saveMenu(menu);
    }


    @SystemLog(businessName = "回显菜单信息")
    @GetMapping("/{id}")
    public ResponseResult selectMenu(@PathVariable Long id) {
        return menuService.selectMenu(id);
    }

    @SystemLog(businessName = "更新菜单")
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }
}
