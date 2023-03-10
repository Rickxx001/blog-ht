package com.stx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllLinkVo {
    private Long id;

    private String name;

    private String logo;

    private String description;
    //网站地址
    private String address;


}
