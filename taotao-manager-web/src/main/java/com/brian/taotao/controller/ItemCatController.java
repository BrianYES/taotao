package com.brian.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brian.taotao.common.pojo.EasyUITreeNode;
import com.brian.taotao.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

    @Autowired
    ItemCatService itemCatService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(name = "id", defaultValue = "0")long parentId) {
        return itemCatService.getItemCatList(parentId);
    }

}
