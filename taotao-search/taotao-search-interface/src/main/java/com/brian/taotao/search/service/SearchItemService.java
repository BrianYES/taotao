package com.brian.taotao.search.service;

import com.brian.taotao.common.pojo.TaotaoResult;

public interface SearchItemService {

    TaotaoResult importItemsToIndex() throws Exception;
}
