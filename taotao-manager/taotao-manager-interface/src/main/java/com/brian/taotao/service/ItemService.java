package com.brian.taotao.service;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.pojo.Item;

public interface ItemService {

    Item getItemById(long itemId);

    EasyUIDataGridResult getItemList(int page, int rows);

    /**
     * 添加商品
     *
     * @param item Item
     * @param desc 商品描述；
     * @return TaotaoResult
     */
    TaotaoResult addItem(Item item, String desc);

    /**
     * 获取商品描述
     *
     * @param itemId 商品ID
     * @return TaotaoResult
     */
    TaotaoResult getItemDescById(long itemId);

    /**
     * 获取商品规格
     *
     * @param itemId 商品ID
     * @return TaotaoResult
     */
    TaotaoResult getItemParamById(long itemId);

    /**
     * 编辑商品
     *
     * @param item Item
     * @param desc 商品描述
     * @return TaotaoResult
     */
    TaotaoResult updateItem(Item item, String desc);


    /**
     * 删除商品
     *
     * @param ids 商品ID，多个用逗号分隔
     * @return TaotaoResult
     */
    TaotaoResult deleteItems(String ids);
}
