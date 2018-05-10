package com.brian.taotao.search.service;

import com.brian.taotao.common.pojo.SearchResult;

public interface SearchService {

    SearchResult search(String queryString, int page);
}
