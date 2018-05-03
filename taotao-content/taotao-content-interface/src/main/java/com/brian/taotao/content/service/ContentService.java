package com.brian.taotao.content.service;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.pojo.Content;

public interface ContentService {

    EasyUIDataGridResult getContentList(long categoryId, int page, int rows);

    TaotaoResult addContent(Content content);

    TaotaoResult updateContent(Content content);

    TaotaoResult deleteContent(String ids);
}
