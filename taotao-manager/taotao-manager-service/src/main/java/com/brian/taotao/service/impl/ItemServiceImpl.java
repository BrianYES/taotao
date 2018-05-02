package com.brian.taotao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.dao.ItemMapper;
import com.brian.taotao.pojo.Item;
import com.brian.taotao.pojo.ItemExample;
import com.brian.taotao.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    public Item getItemById(long itemId) {
        return itemMapper.selectByPrimaryKey(itemId);
    }

    public EasyUIDataGridResult getItemList(int page, int rows) {
        PageHelper.startPage(page, rows);
        ItemExample example = new ItemExample();
        List<Item> itemList = itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(itemList);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(itemList);
        return result;
    }
}
