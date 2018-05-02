package com.brian.taotao.service;

import java.util.List;

import com.brian.taotao.common.pojo.EasyUITreeNode;

public interface ItemCatService {

    List<EasyUITreeNode> getItemCatList(long parentId);
}
