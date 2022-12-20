package com.stx.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserVo {
    private String avatar;
    private Date createTime;
    private String email;
    private Long id;
    private String nickName;
    private String phonenumber;
    private String sex;
    private String status;
    private String userName;
}
