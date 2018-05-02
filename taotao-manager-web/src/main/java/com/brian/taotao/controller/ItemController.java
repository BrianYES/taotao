package com.brian.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.pojo.Item;
import com.brian.taotao.pojo.ItemDesc;
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


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addItem(Item item, String desc) {
        return itemService.addItem(item, desc);
    }

    @RequestMapping("/desc/{itemId}")
    @ResponseBody
    public TaotaoResult getItemDescById(@PathVariable Long itemId) {
        return itemService.getItemDescById(itemId);
    }

    @RequestMapping("/param/{itemId}")
    @ResponseBody
    public TaotaoResult getParamById(@PathVariable Long itemId) {
        return itemService.getItemParamById(itemId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateItem(Item item, String desc) {
        return itemService.updateItem(item, desc);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteItems(String ids) {
        return itemService.deleteItems(ids);
    }

    @RequestMapping(value = "/instock", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult instockItems(String ids) {
        return itemService.instockItems(ids);
    }

    @RequestMapping(value = "/reshelf", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult reshelfItems(String ids) {
        return itemService.reshelfItems(ids);
    }
}
