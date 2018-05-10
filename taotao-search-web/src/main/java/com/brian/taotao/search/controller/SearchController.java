package com.brian.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.brian.taotao.common.pojo.SearchResult;
import com.brian.taotao.search.service.SearchService;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/search")
    public String search(@RequestParam(value = "q")String queryString, @RequestParam(defaultValue = "1")int
            page, Model model) {
        SearchResult searchResult = searchService.search(queryString, page);

        model.addAttribute("query", queryString);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("itemList", searchResult.getItemList());
        model.addAttribute("page", page);
        return "search";
    }
}
