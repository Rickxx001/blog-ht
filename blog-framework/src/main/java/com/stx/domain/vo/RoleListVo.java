package com.stx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleListVo {

    private Long id;
    private String roleKey;
    private String roleName;
    private Integer roleSort;
    private String status;
}
