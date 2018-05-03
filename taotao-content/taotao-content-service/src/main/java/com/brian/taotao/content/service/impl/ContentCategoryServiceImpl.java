package com.brian.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.EasyUITreeNode;
import com.brian.taotao.content.service.ContentCategoryService;
import com.brian.taotao.dao.ContentCategoryMapper;
import com.brian.taotao.pojo.ContentCategory;
import com.brian.taotao.pojo.ContentCategoryExample;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private ContentCategoryMapper contentCategoryMapper;


    /**
     * 获取内容分类列表
     *
     * @param parentId 父节点id
     * @return List<EasyUITreeNode>
     */
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        ContentCategoryExample example = new ContentCategoryExample();
        ContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<ContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
        List<EasyUITreeNode> nodeList = new ArrayList<EasyUITreeNode>();
        for (ContentCategory contentCategory : contentCategoryList) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(contentCategory.getId());
            node.setText(contentCategory.getName());
            node.setState(contentCategory.getIsParent() ? "closed" : "open");
            nodeList.add(node);
        }
        return nodeList;
    }
}
