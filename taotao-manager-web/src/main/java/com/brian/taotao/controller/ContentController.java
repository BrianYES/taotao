package com.brian.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.content.service.ContentService;

@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/content/list")
    @ResponseBody
    public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
        return contentService.getContentList(categoryId, page, rows);
    }
}
