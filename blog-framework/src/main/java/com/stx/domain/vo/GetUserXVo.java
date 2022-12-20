package com.stx.domain.vo;

import com.stx.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserXVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private  UserVO user;
}
