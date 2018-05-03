package com.brian.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.content.service.ContentService;
import com.brian.taotao.pojo.Content;

@Controller
@RequestMapping(value = "/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
        return contentService.getContentList(categoryId, page, rows);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addContent(Content content) {
        return contentService.addContent(content);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContent(Content content) {
        return contentService.updateContent(content);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteContent(String ids) {
        return contentService.deleteContent(ids);
    }
}
