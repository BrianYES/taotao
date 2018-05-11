package com.brian.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.pojo.Item;
import com.brian.taotao.service.ItemService;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItem(@PathVariable long itemId, Model model) throws Exception {
        Item item = itemService.getItemById(itemId);

        String image = item.getImage();
        if (image != null && !"".equals(image)) {
            String[] strings = image.split(",");
//            model.addAttribute("item.images", strings);
        }

        TaotaoResult itemDesc = itemService.getItemDescById(itemId);

        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);

        return "item";
    }
}
