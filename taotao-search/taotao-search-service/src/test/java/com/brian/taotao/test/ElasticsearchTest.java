package com.brian.taotao.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.brian.taotao.common.pojo.SearchItem;
import com.brian.taotao.common.utils.JsonUtil;

public class ElasticsearchTest {

    private TransportClient client = null;

    @Before
    public void getConnet() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "swmv-es") // 集群名 默认名:elasticsearch 如果不是默认的，需要设置
                .put("client.transport.sniff", true)  // 自动把集群下的机器添加到列表中
                .build();
        client = new PreBuiltTransportClient(settings);
        client.addTransportAddresses(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        System.out.println("连接信息:" + client.toString());
    }

    @After
    public void closeConnect() {
        if (null != client) {
            System.out.println("执行关闭连接操作...");
            client.close();
        }
    }

    @Test
    public void testBulk() {
        BulkRequestBuilder bulk = client.prepareBulk();

        List<SearchItem> itemList = new ArrayList<SearchItem>();
        for (int i = 1; i < 100; i++) {
            SearchItem item = new SearchItem();
            item.setId(i+"");
            item.setCategoryName("手机");
            item.setImage("http://www.baidu.com");
            item.setItemDesc("item_desc");
            item.setPrice(1200);
            item.setSellPoint("sell_point");
            item.setTitle("title");
            itemList.add(item);
        }

        for (SearchItem searchItem : itemList) {
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex("test", "test", searchItem.getId());
            indexRequestBuilder.setSource(JsonUtil.objectToJson(searchItem), XContentType.JSON);
            bulk.add(indexRequestBuilder);
        }

        if (bulk.get().hasFailures()) {
            System.out.println("有错误");
        }
    }

    @Test
    public void testAdd() throws IOException {
        XContentBuilder source = XContentFactory.jsonBuilder()
                .startObject()
                    .field("userName", "张三")
                    .field("sendDate", new Date())
                    .field("msg", "你好李四")
                .endObject();

        IndexResponse response = client.prepareIndex("msg", "tweet", "2").setSource(source).get();

        System.out.println("索引名称:" + response.getIndex() + "\n类型:" + response.getType()
                + "\n文档ID:" + response.getId() + "\n当前实例状态:" + response.status());
    }

    @Test
    public void testGet() {
        GetResponse response = client.prepareGet("msg", "tweet", "2").get();
        System.out.println("索引库的数据:" + response.getSourceAsString());
    }

    @Test
    public void testDelete() {
        DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete("msg", "tweet", "1");
        DeleteResponse deleteResponse = deleteRequestBuilder.get();

        System.out.println("deleteResponse索引名称:" + deleteResponse.getIndex() + "\n deleteResponse类型:" + deleteResponse.getType() + "\n deleteResponse文档ID:" + deleteResponse.getId() + "\n当前实例deleteResponse状态:" + deleteResponse.status());
    }

    @Test
    public void testSearch() {
        MatchQueryBuilder query = QueryBuilders.matchQuery("title", "Apple");
        SearchResponse response = client.prepareSearch("taotao")
                .setTypes("item")
                .setQuery(query)
                .setFrom(0)
                .setSize(10)
                .setScroll(new TimeValue(60000))
                .get();

        System.out.println("scrollId:" + response.getScrollId());

        System.out.println("搜索总数:" + response.getHits().getTotalHits());
        for (SearchHit searchHit : response.getHits().getHits()) {
            System.out.println(searchHit.getSourceAsString());
        }

        SearchResponse response1 = client.prepareSearchScroll(response.getScrollId())
                .setScroll(new TimeValue(60000))
                .get();

        System.out.println("scrollId2:" + response1.getScrollId());

        System.out.println("搜索总数2:" + response1.getHits().getTotalHits());
        for (SearchHit searchHit : response1.getHits().getHits()) {
            System.out.println(searchHit.getSourceAsString());
        }
    }
}
