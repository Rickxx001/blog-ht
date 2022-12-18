package com.stx.controller;

import com.stx.annotation.SystemLog;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.TagDto;
import com.stx.domain.vo.PageVo;
import com.stx.domain.vo.TagVo;
import com.stx.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
   private TagService tagService;

//    分页查询标签
@SystemLog(businessName ="分页查询标签")
    @GetMapping("/list")
    public ResponseResult<PageVo> PageTagList(Integer pageNum, Integer pageSize, TagDto tagDto){
        return tagService.listTag(pageNum,pageSize, tagDto);
    }
//  查询所有标签
@SystemLog(businessName ="查询所有标签")
    @GetMapping("/listAllTag")
    public ResponseResult tagList(){
        return tagService.tagList();
    }

//   添加标签
@SystemLog(businessName ="添加标签")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    @PostMapping
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.addTag(tagDto);
    }

    //    删除标签
    @SystemLog(businessName ="删除标签")
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    @DeleteMapping("/{Id}")
    public  ResponseResult deleteTag(@PathVariable Long Id){
        return tagService.deleteTag(Id);
    }
//    获取标签
@SystemLog(businessName ="获取标签")
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Long id){
        return tagService.getTag(id);
    }
//  修改标签byId
@SystemLog(businessName ="修改标签")
    @PutMapping
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    public ResponseResult updateTag(@RequestBody TagVo tagDto){
        return tagService.updateTag(tagDto);
    }


}
