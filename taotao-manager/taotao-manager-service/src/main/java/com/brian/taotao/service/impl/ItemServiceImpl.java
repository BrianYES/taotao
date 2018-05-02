package com.brian.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.common.utils.IDUtil;
import com.brian.taotao.dao.ItemDescMapper;
import com.brian.taotao.dao.ItemMapper;
import com.brian.taotao.pojo.Item;
import com.brian.taotao.pojo.ItemDesc;
import com.brian.taotao.pojo.ItemExample;
import com.brian.taotao.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;

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

    /**
     * 添加商品
     *
     */
    public TaotaoResult addItem(Item item, String desc) {
        long itemId = IDUtil.itemID();
        item.setId(itemId);
        item.setStatus((byte) 1);
        Date now = new Date();
        item.setCreated(now);
        item.setUpdated(now);
        itemMapper.insert(item);

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(now);
        itemDesc.setUpdated(now);
        itemDescMapper.insert(itemDesc);

        return TaotaoResult.ok();
    }
}
