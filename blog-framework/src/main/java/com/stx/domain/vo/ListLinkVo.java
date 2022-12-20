package com.stx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLinkVo {
    private String address;

    private String description;
    private Long id;
    private String logo;
    private String name;
    private String status;

}
