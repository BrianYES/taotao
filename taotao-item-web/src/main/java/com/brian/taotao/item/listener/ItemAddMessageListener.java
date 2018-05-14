package com.brian.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.pojo.Item;
import com.brian.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            long itemId = Long.parseLong(text);

            // 等待事务提交
            Thread.sleep(1000);

            Map dataModel = new HashMap();

            Item item = itemService.getItemById(itemId);
            dataModel.put("item", item);

            String image = item.getImage();
            if (StringUtils.isNotBlank(image)) {
                String[] strings = image.split(",");
                dataModel.put("images", strings);
            }

            TaotaoResult itemDesc = itemService.getItemDescById(itemId);
            dataModel.put("itemDesc", itemDesc.getData());

            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            Writer out = new FileWriter(new File("src/main/webapp/items/" + text + ".html"));
            template.process(dataModel, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
