package com.stx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoryVo {
    private Long id;
    private String name;
    private String description;
    //状态0:正常,1禁用
    private String status;
}
