package com.brian.taotao.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.brian.taotao.dao.ItemDescMapper;
import com.brian.taotao.pojo.ItemDesc;
import com.brian.taotao.pojo.ItemDescExample;

public class TestItem {

    @Test
    public void queryItemDescById() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext
                ("classpath:spring/applicationContext-dao.xml");
        ItemDescMapper itemDescMapper = applicationContext.getBean(ItemDescMapper.class);

        long itemId = 152524252513421L;
//        long itemId = 1060847L;

        ItemDescExample example = new ItemDescExample();
        ItemDescExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<ItemDesc> itemDescList = itemDescMapper.selectByExampleWithBLOBs(example);

        if (null != itemDescList) {
            ItemDesc itemDesc = itemDescList.get(0);
            System.out.println(itemDesc);
            System.out.println(itemDesc.getItemDesc());
            System.out.println(itemDesc.getItemId());
        }
    }
}
