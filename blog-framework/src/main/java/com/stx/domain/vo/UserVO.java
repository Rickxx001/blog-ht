package com.stx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private String email;
    private Long id;
    private String nickName;
    private String sex;
    private String status;
    private String userName;
}
