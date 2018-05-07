package com.brian.taotao.search.service.impl;

import java.net.InetAddress;
import java.util.List;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.SearchItem;
import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.search.dao.SearchItemMapper;
import com.brian.taotao.search.elasticsearch.EsClientBuilder;

@Service
public class SearchItemServiceImpl {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private EsClientBuilder esClientBuilder;

    public TaotaoResult importItemsToIndex() throws Exception {
        BulkRequestBuilder bulk = esClientBuilder.getClient().prepareBulk();

        List<SearchItem> itemList = searchItemMapper.getItemList();
        for (SearchItem searchItem : itemList) {
            IndexRequestBuilder indexRequestBuilder = esClientBuilder.getClient().prepareIndex("msg", "tweet", "1");
            indexRequestBuilder.setSource(searchItem.toString());

            bulk.add(indexRequestBuilder);
        }

        if (bulk.get().hasFailures()) {
            for (BulkItemResponse bulkItemResponse : bulk.get().getItems()) {
            }
        }

        return TaotaoResult.ok();
    }
}
