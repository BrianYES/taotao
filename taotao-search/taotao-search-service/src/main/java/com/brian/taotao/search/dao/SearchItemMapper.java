package com.brian.taotao.search.dao;

import java.util.List;

import com.brian.taotao.common.pojo.SearchItem;

public interface SearchItemMapper {

    List<SearchItem> getItemList();

    SearchItem getItemById(long itemId);
}
