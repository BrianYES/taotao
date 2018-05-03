package com.brian.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.EasyUITreeNode;
import com.brian.taotao.common.pojo.TaotaoResult;
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

    /**
     * 新建一个内容分类
     *
     * @param parentId 父节点ID
     * @param name 节点名称
     * @return TaotaoResult
     */
    public TaotaoResult createContentCategory(long parentId, String name) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);
        contentCategory.setIsParent(false);
        Date now = new Date();
        contentCategory.setCreated(now);
        contentCategory.setUpdated(now);
        contentCategoryMapper.insertSelective(contentCategory);

        ContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parentCategory.getIsParent()) {
            parentCategory.setIsParent(true);
            parentCategory.setUpdated(now);
            contentCategoryMapper.updateByPrimaryKeySelective(parentCategory);
        }

        return TaotaoResult.ok(contentCategory);
    }

    /**
     * 编辑内容分类
     *
     * @param id 当前节点id
     * @param name 重命名后的节点名称
     * @return TaotaoResult
     */
    public TaotaoResult updateContentCategory(long id, String name) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setId(id);
        contentCategory.setName(name);
        Date now = new Date();
        contentCategory.setUpdated(now);
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        return TaotaoResult.ok();
    }

    /**
     * 删除内容分类
     *
     * @param id 当前节点id
     * @return TaotaoResult
     */
    public TaotaoResult deleteContentCategory(long id) {
        ContentCategory currentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        if (currentCategory.getIsParent()) {
            ContentCategoryExample example = new ContentCategoryExample();
            ContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            List<ContentCategory> childCategory = contentCategoryMapper.selectByExample(example);
            for (ContentCategory contentCategory : childCategory) {
                contentCategoryMapper.deleteByPrimaryKey(contentCategory.getId());
            }
        }
        contentCategoryMapper.deleteByPrimaryKey(id);

        ContentCategoryExample example = new ContentCategoryExample();
        ContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(currentCategory.getParentId());
        List<ContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
        if (contentCategoryList.size() == 0) {
            ContentCategory contentCategory = new ContentCategory();
            contentCategory.setId(currentCategory.getParentId());
            contentCategory.setIsParent(false);
            contentCategory.setUpdated(new Date());
            contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        }
        return TaotaoResult.ok();
    }
}
