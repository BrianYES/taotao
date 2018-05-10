package com.brian.taotao.search.service.impl;

import java.util.List;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.SearchItem;
import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.common.utils.JsonUtil;
import com.brian.taotao.search.dao.SearchItemMapper;
import com.brian.taotao.search.elasticsearch.EsClient;
import com.brian.taotao.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private EsClient esClient;

    public TaotaoResult importItemsToIndex() throws Exception {
        IndicesExistsResponse indicesExistsResponse = esClient.getClient().admin().indices().prepareExists("taotao").get();
        if (indicesExistsResponse.isExists()) {
            DeleteIndexResponse deleteIndexResponse = esClient.getClient().admin().indices().prepareDelete("taotao").get();
            System.out.println("deleteIndexResponse.isAcknowledged:"+ deleteIndexResponse.isAcknowledged());
        }
        CreateIndexResponse createIndexResponse = esClient.getClient().admin().indices().prepareCreate("taotao")
                .setSettings(Settings.builder().put("index.number_of_shards", 2).put("index.number_of_replicas", 0))
                .get();
        System.out.println("createIndexResponse.isAcknowledged:"+ createIndexResponse.isAcknowledged());

        PutMappingResponse putMappingResponse = esClient.getClient().admin().indices().preparePutMapping("taotao").setType("item")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject("properties")
                        .startObject("id")
                        .field("type", "text")
                        .endObject()
                        .startObject("title")
                        .field("type", "text")
                        .field("analyzer", "ik_smart")
                        .endObject()
                        .startObject("sellPoint")
                        .field("type", "text")
                        .field("analyzer", "ik_smart")
                        .endObject()
                        .startObject("price")
                        .field("type", "long")
//                        .field("index", "not_analyzed")
                        .endObject()
                        .startObject("image")
                        .field("type", "text")
//                        .field("index", "not_analyzed")
                        .endObject()
                        .startObject("categoryName")
                        .field("type", "text")
                        .field("analyzer", "ik_smart")
                        .endObject()
                        .startObject("itemDesc")
                        .field("type", "text")
//                        .field("index", "not_analyzed")
                        .endObject()
                        .endObject()
                        .endObject()
                ).get();
        System.out.println("putMappingResponse.isAcknowledged:"+ putMappingResponse.isAcknowledged());

        BulkRequestBuilder bulk = esClient.getClient().prepareBulk();

        List<SearchItem> itemList = searchItemMapper.getItemList();
        for (SearchItem searchItem : itemList) {
            IndexRequestBuilder indexRequestBuilder = esClient.getClient().prepareIndex("taotao", "item")
                    .setId(searchItem.getId())
                    .setSource(JsonUtil.objectToJson(searchItem), XContentType.JSON);

            bulk.add(indexRequestBuilder);
        }

        if (bulk.get().hasFailures()) {
            System.out.println("导入失败");
            TaotaoResult.build(500, "导入失败");
        }

        return TaotaoResult.ok();
    }


}
