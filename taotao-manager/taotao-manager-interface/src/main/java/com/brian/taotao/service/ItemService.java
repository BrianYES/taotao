package com.brian.taotao.service;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.pojo.Item;

public interface ItemService {

    Item getItemById(long itemId);

    EasyUIDataGridResult getItemList(int page, int rows);
}
