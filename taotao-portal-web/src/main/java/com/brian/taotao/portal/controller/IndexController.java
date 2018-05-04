package com.brian.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.brian.taotao.common.utils.JsonUtil;
import com.brian.taotao.content.service.ContentService;
import com.brian.taotao.pojo.Content;
import com.brian.taotao.portal.pojo.AD1Node;

@Controller
public class IndexController {

    @Autowired
    private ContentService contentService;

    @Value("${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;
    @Value("${AD1_WIDTH}")
    private Integer AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private Integer AD1_WIDTH_B;
    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;

    @RequestMapping(value = "/index")
    public ModelAndView showIndex() {
        ModelAndView mav = new ModelAndView("index");

        List<Content> contentList = contentService.getContentList(AD1_CATEGORY_ID);
        List<AD1Node> ad1NodeList = new ArrayList<AD1Node>();
        for (Content content : contentList) {
            AD1Node node = new AD1Node();
            node.setAlt(content.getTitle());
            node.setHeight(AD1_HEIGHT);
            node.setHeightB(AD1_HEIGHT_B);
            node.setWidth(AD1_WIDTH);
            node.setWidthB(AD1_WIDTH_B);
            node.setSrc(content.getPic());
            node.setSrcB(content.getPic2());
            node.setHref(content.getUrl());
            ad1NodeList.add(node);
        }
        String ad1Json = JsonUtil.objectToJson(ad1NodeList);
        mav.addObject("ad1", ad1Json);

        return mav;
    }
}
