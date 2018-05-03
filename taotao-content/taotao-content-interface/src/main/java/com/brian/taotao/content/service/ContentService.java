package com.brian.taotao.content.service;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;

public interface ContentService {

    EasyUIDataGridResult getContentList(long categoryId, int page, int rows);
}
