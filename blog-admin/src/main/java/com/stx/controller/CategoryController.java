package com.stx.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.stx.annotation.SystemLog;
import com.stx.domain.ResponseResult;
import com.stx.domain.entity.Category;
import com.stx.domain.vo.AllCategoryVo;
import com.stx.domain.vo.ExcelCategoryVo;
import com.stx.enums.AppHttpCodeEnum;
import com.stx.service.CategoryService;
import com.stx.utils.BeanCopyUtils;
import com.stx.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RequestMapping("/content/category")
@RestController
public class CategoryController {

    @Autowired
   private CategoryService categoryservice;

    @SystemLog(businessName ="获取所有类别")
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<Category> categories = categoryservice.listAllCategory();
        List<AllCategoryVo> allCategoryVos = BeanCopyUtils.copyBeanList(categories, AllCategoryVo.class);
        return ResponseResult.okResult(allCategoryVos);
    }



    @SystemLog(businessName ="导出exel表格")
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void exportCategory(HttpServletResponse response){
        try {
            WebUtils.setDownLoadHeader("文章分类.xlsx",response);
            List<Category> categories = categoryservice.listAllCategory();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categories, ExcelCategoryVo.class);
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类表")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            e.printStackTrace();
            response.reset();
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.EXCE_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }

    }
}
