package com.stx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListAllRoleVo {

    private Long createBy;
    private Date createTime;
    private String delFlag;
    private Long id;
    private String remark;
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    private String status;
    private Date updateTime;
}
