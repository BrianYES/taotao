package com.brian.taotao.content.service;

import java.util.List;

import com.brian.taotao.common.pojo.EasyUITreeNode;

public interface ContentCategoryService {

    /**
     * 获取内容分类列表
     *
     * @param parentId 父节点id
     * @return List<EasyUITreeNode>
     */
    List<EasyUITreeNode> getContentCategoryList(long parentId);
}
