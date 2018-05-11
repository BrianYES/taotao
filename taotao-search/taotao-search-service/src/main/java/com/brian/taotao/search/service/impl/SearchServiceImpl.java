package com.brian.taotao.search.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.SearchItem;
import com.brian.taotao.common.pojo.SearchResult;
import com.brian.taotao.common.utils.JsonUtil;
import com.brian.taotao.search.elasticsearch.EsClient;
import com.brian.taotao.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private EsClient esClient;

    private int rows = 60;

    public SearchResult search(String queryString, int page) {
        if (page < 1) {
            page = 1;
        }

        MatchQueryBuilder query = QueryBuilders.matchQuery("title", queryString);
        SearchResponse response = esClient.getClient().prepareSearch("taotao")
                .setTypes("item")
                .setQuery(query)
                .setFrom((page-1) * rows)
                .setSize(rows)
                .get();

        SearchResult result = new SearchResult();

        long totalHits = response.getHits().getTotalHits();
        result.setRecordCount(totalHits);

        long totalPages = totalHits / rows;
        if (totalHits % rows != 0) {
            totalPages++;
        }
        result.setTotalPages(totalPages);

        List<SearchItem> searchItemList = new ArrayList<SearchItem>();
        for (SearchHit searchHit : response.getHits().getHits()) {
            SearchItem searchItem = JsonUtil.jsonToPojo(searchHit.getSourceAsString(), SearchItem.class);
            searchItemList.add(searchItem);
        }
        result.setItemList(searchItemList);

        return result;
    }
}
