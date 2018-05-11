package com.brian.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.brian.taotao.common.pojo.SearchItem;
import com.brian.taotao.common.utils.JsonUtil;
import com.brian.taotao.search.dao.SearchItemMapper;
import com.brian.taotao.search.elasticsearch.EsClient;

/**
 * 监听商品添加通知，添加索引
 *
 * @author Brian
 * @date 2018/5/11
 */
public class ItemAddMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(ItemAddMessageListener.class);

    @Autowired
    private EsClient esClient;

    @Autowired
    private SearchItemMapper searchItemMapper;

    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String itemId = textMessage.getText();
            logger.info("itemId:{}", itemId);

            // 等待事务提交
            Thread.sleep(1000);

            if (itemId != null && !"".equals(itemId)) {
                SearchItem searchItem = searchItemMapper.getItemById(Long.parseLong(itemId));

                logger.info("searchItem:{}", JsonUtil.objectToJson(searchItem));

                IndexResponse response = esClient.getClient()
                        .prepareIndex("taotao", "item")
                        .setId(searchItem.getId())
                        .setSource(JsonUtil.objectToJson(searchItem), XContentType.JSON)
                        .get();

                logger.info("IndexResponse:{}", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
