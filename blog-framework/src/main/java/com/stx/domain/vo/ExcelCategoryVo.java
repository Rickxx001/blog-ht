package com.stx.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelCategoryVo {

    //分类名
    @ExcelProperty("分类名")
    private String name;
    //描述
    @ExcelProperty("描述")
    private String description;
    @ExcelProperty("状态")
    private String status;
    @ExcelProperty("创建id")
    private Long createBy;
    @ExcelProperty("创建时间")
    private Date createTime;
    @ExcelProperty("修改id")
    private Long updateBy;
    @ExcelProperty("修改时间")
    private Date updateTime;
}
