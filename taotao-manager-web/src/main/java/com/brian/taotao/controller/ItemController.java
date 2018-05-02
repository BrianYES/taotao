package com.brian.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.pojo.Item;
import com.brian.taotao.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/{itemId}")
    @ResponseBody
    public Item getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @RequestMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(int page, int rows) {
        return itemService.getItemList(page, rows);
    }
}
