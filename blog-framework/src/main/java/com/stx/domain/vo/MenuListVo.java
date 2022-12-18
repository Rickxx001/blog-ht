package com.stx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuListVo {
    private String component;
    private String icon;
    private Long parentId;
    private String menuName;
    private Long id;
    private Integer isFrame;
    private String menuType;
    private Integer orderNum;
    private String path;
    private String perms;
    private String status;
    private String remark;
    private String visible;

}
