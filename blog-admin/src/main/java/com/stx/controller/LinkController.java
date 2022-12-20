package com.stx.controller;


import com.stx.annotation.SystemLog;
import com.stx.domain.ResponseResult;
import com.stx.domain.dto.AuditLinkDto;
import com.stx.domain.dto.ListLinkDto;
import com.stx.domain.dto.SaveLinkDto;
import com.stx.domain.dto.UpdateLinkDto;
import com.stx.service.LinkService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *   友情链接管理
  *
  **/

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    LinkService linkService;



    @PutMapping("/changeLinkStatus")
    public ResponseResult auditLink(@RequestBody AuditLinkDto auditLinkDto ){
        return linkService.auditLink(auditLinkDto);

    }


    @SystemLog(businessName = "更新友链")
    @PutMapping
    public ResponseResult updateLink(@RequestBody UpdateLinkDto dto){
        return linkService.updateLink(dto);
    }


    @SystemLog(businessName = "删除")
    @DeleteMapping("{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }

    @SystemLog(businessName = "page link")
    @GetMapping("/list")
    public ResponseResult ListLinks(Integer pageNum, Integer pageSize, ListLinkDto dto){
        return linkService.listLinks(pageNum, pageSize, dto);
    }

    @SystemLog(businessName = "新增标签")
    @PostMapping
    public ResponseResult saveLink(@RequestBody SaveLinkDto dto){
        return linkService.saveLink(dto);
    }



    @SystemLog(businessName = "回显友链信息")
    @GetMapping("/{id}")
    public ResponseResult getLink(@PathVariable Long id){
        return linkService.getLink(id);
    }




}
