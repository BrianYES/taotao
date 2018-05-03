package com.brian.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brian.taotao.common.pojo.EasyUITreeNode;
import com.brian.taotao.content.service.ContentCategoryService;


@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping(value = "/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name = "id", defaultValue = "0")long parentId) {
        return contentCategoryService.getContentCategoryList(parentId);
    }
    
}
