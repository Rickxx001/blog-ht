package com.stx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCategoryVo {
    private String description;
    private Long id;
    private String name;
    private String status;
}
