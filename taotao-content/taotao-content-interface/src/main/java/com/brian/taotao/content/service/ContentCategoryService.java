package com.brian.taotao.content.service;

import java.util.List;

import com.brian.taotao.common.pojo.EasyUITreeNode;
import com.brian.taotao.common.pojo.TaotaoResult;

public interface ContentCategoryService {

    /**
     * 获取内容分类列表
     *
     * @param parentId 父节点id
     * @return List<EasyUITreeNode>
     */
    List<EasyUITreeNode> getContentCategoryList(long parentId);

    /**
     * 新建一个内容分类
     *
     * @param parentId 父节点ID
     * @param name 节点名称
     * @return TaotaoResult.data = ContentCategory
     */
    TaotaoResult createContentCategory(long parentId, String name);

    /**
     * 编辑内容分类
     *
     * @param id 当前节点id
     * @param name 重命名后的节点名称
     * @return TaotaoResult
     */
    TaotaoResult updateContentCategory(long id, String name);

    /**
     * 删除内容分类
     *
     * @param id 当前节点id
     * @return TaotaoResult
     */
    TaotaoResult deleteContentCategory(long id);
}
