package com.brian.taotao.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.content.service.ContentService;
import com.brian.taotao.dao.ContentMapper;
import com.brian.taotao.pojo.Content;
import com.brian.taotao.pojo.ContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentMapper contentMapper;

    public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
        PageHelper.startPage(page, rows);
        ContentExample example = new ContentExample();
        ContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<Content> contentList = contentMapper.selectByExampleWithBLOBs(example);
        PageInfo<Content> pageInfo = new PageInfo<Content>(contentList);

        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(contentList);

        return result;
    }
}
