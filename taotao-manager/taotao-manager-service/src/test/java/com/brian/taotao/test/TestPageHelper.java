package com.brian.taotao.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.brian.taotao.dao.ItemMapper;
import com.brian.taotao.pojo.Item;
import com.brian.taotao.pojo.ItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TestPageHelper {

    @Test
    public void testPageHelper() throws Exception {
        PageHelper.startPage(1, 10);
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext
                ("classpath:spring/applicationContext-dao.xml");
        ItemMapper itemMapper = applicationContext.getBean(ItemMapper.class);
        ItemExample example = new ItemExample();
//        ItemExample.Criteria criteria = example.createCriteria();
        List<Item> items = itemMapper.selectByExample(example);

        PageInfo<Item> pageInfo = new PageInfo<Item>(items);
        System.out.println("总数："+ pageInfo.getTotal());
        System.out.println("总页数："+ pageInfo.getPages());
        System.out.println("返回的记录数："+ items.size());

    }
}
