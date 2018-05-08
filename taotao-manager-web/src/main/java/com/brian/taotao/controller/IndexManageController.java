package com.brian.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.search.service.SearchItemService;

@Controller
public class IndexManageController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping(value = "/index/importall")
    @ResponseBody
    public TaotaoResult importAll() {
        try {
            return searchItemService.importItemsToIndex();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, "导入失败");
        }
    }
}
