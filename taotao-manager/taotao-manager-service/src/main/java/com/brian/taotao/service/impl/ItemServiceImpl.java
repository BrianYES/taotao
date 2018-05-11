package com.brian.taotao.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.common.utils.IDUtil;
import com.brian.taotao.dao.ItemDescMapper;
import com.brian.taotao.dao.ItemMapper;
import com.brian.taotao.dao.ItemParamItemMapper;
import com.brian.taotao.pojo.Item;
import com.brian.taotao.pojo.ItemDesc;
import com.brian.taotao.pojo.ItemDescExample;
import com.brian.taotao.pojo.ItemExample;
import com.brian.taotao.pojo.ItemParamItem;
import com.brian.taotao.pojo.ItemParamItemExample;
import com.brian.taotao.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Autowired
    private ItemParamItemMapper itemParamItemMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name = "itemAddTopic")
    private Topic itemAddTopic;

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
     */
    public TaotaoResult addItem(Item item, String desc) {
        final long itemId = IDUtil.itemID();
        item.setId(itemId);
        // 商品状态 1：正常 2：下架 3：删除
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

        // 发送商品添加通知
        jmsTemplate.send(itemAddTopic, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(String.valueOf(itemId));
                return textMessage;
            }
        });

        return TaotaoResult.ok();
    }

    /**
     * 获取商品描述
     *
     * @param itemId 商品ID
     * @return TaotaoResult
     */
    public TaotaoResult getItemDescById(long itemId) {
        ItemDescExample example = new ItemDescExample();
        ItemDescExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<ItemDesc> itemDescList = itemDescMapper.selectByExampleWithBLOBs(example);

        TaotaoResult result = TaotaoResult.ok();
        if (null != itemDescList) {
            ItemDesc itemDesc = itemDescList.get(0);
            result.setData(itemDesc.getItemDesc());
        }
        return result;
    }

    /**
     * 获取商品规格
     *
     * @param itemId 商品ID
     * @return TaotaoResult
     */
    public TaotaoResult getItemParamById(long itemId) {
        ItemParamItemExample example = new ItemParamItemExample();
        ItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<ItemParamItem> itemParamItems = itemParamItemMapper.selectByExampleWithBLOBs(example);
        TaotaoResult result = TaotaoResult.ok();
        if (null != itemParamItems) {
            ItemParamItem itemParamItem = itemParamItems.get(0);
            result.setData(itemParamItem);
        }
        return result;
    }

    /**
     * 编辑商品
     *
     * @param item Item
     * @param desc 商品描述
     *
     * @return TaotaoResult
     */
    public TaotaoResult updateItem(Item item, String desc) {
        Date now = new Date();
        item.setUpdated(now);
        itemMapper.updateByPrimaryKeySelective(item);

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(now);

        ItemDescExample example = new ItemDescExample();
        ItemDescExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(item.getId());
        itemDescMapper.updateByExampleSelective(itemDesc, example);

        return TaotaoResult.ok();
    }

    /**
     * 删除商品
     *
     * @param ids 商品ID，多个用逗号分隔
     * @return TaotaoResult
     */
    public TaotaoResult deleteItems(String ids) {
        String[] values = ids.split(",");
        for (String value : values) {
            itemMapper.deleteByPrimaryKey(Long.parseLong(value));
        }
        return TaotaoResult.ok();
    }

    /**
     * 下架商品
     *
     * @param ids 商品ID，多个用逗号分隔
     * @return TaotaoResult
     */
    public TaotaoResult instockItems(String ids) {
        String[] values = ids.split(",");
        for (String value : values) {
            Item item = new Item();
            item.setId(Long.parseLong(value));
            // 1-正常，2-下架，3-删除
            item.setStatus((byte) 2);
            itemMapper.updateByPrimaryKeySelective(item);
        }
        return TaotaoResult.ok();
    }

    /**
     * 上架商品
     *
     * @param ids 商品ID，多个用逗号分隔
     * @return TaotaoResult
     */
    public TaotaoResult reshelfItems(String ids) {
        String[] values = ids.split(",");
        for (String value : values) {
            Item item = new Item();
            item.setId(Long.parseLong(value));
            // 1-正常，2-下架，3-删除
            item.setStatus((byte) 1);
            itemMapper.updateByPrimaryKeySelective(item);
        }
        return TaotaoResult.ok();
    }
}
