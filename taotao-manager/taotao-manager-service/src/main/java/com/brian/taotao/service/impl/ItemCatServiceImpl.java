package com.brian.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.EasyUITreeNode;
import com.brian.taotao.dao.ItemCatMapper;
import com.brian.taotao.pojo.ItemCat;
import com.brian.taotao.pojo.ItemCatExample;
import com.brian.taotao.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    public List<EasyUITreeNode> getItemCatList(long parentId) {
        ItemCatExample example = new ItemCatExample();
        ItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<ItemCat> itemCatList = itemCatMapper.selectByExample(example);
        List<EasyUITreeNode> nodeList = new ArrayList<EasyUITreeNode>();
        for (ItemCat itemCat : itemCatList) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(itemCat.getId());
            node.setText(itemCat.getName());
            node.setState(itemCat.getIsParent() ? "closed" : "open");
            nodeList.add(node);
        }
        return nodeList;
    }
}
