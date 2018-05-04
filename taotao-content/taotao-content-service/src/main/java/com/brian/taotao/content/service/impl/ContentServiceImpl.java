package com.brian.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.brian.taotao.common.pojo.EasyUIDataGridResult;
import com.brian.taotao.common.pojo.TaotaoResult;
import com.brian.taotao.common.utils.JsonUtil;
import com.brian.taotao.content.service.ContentService;
import com.brian.taotao.dao.ContentMapper;
import com.brian.taotao.jedis.JedisClient;
import com.brian.taotao.pojo.Content;
import com.brian.taotao.pojo.ContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_INDEX_KEY}")
    private String CONTENT_INDEX_KEY;

    public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
        PageHelper.startPage(page, rows);
        ContentExample example = new ContentExample();
        ContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<Content> contentList = contentMapper.selectByExampleWithBLOBs(example);
        PageInfo<Content> pageInfo = new PageInfo<Content>(contentList);

        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(contentList);

        return result;
    }

    public List<Content> getContentList(long cid) {
        // 从redis查询缓存
        try {
            String content = jedisClient.hget(CONTENT_INDEX_KEY, cid + "");
            if (StringUtils.isNotBlank(content)) {
                return JsonUtil.jsonToList(content, Content.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 缓存没有命中 查询数据库
        ContentExample example = new ContentExample();
        ContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<Content> contentList = contentMapper.selectByExampleWithBLOBs(example);

        // 添加redis缓存
        try {
            jedisClient.hset(CONTENT_INDEX_KEY, cid + "", JsonUtil.objectToJson(contentList));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contentList;
    }

    public TaotaoResult addContent(Content content) {
        Date now = new Date();
        content.setCreated(now);
        content.setUpdated(now);
        contentMapper.insertSelective(content);

        // 删除缓存
        deleteRedis(content.getCategoryId().toString());

        return TaotaoResult.ok();
    }

    public TaotaoResult updateContent(Content content) {
        Date now = new Date();
        content.setUpdated(now);
        contentMapper.updateByPrimaryKeySelective(content);

        // 删除缓存
        deleteRedis(content.getCategoryId().toString());

        return TaotaoResult.ok();
    }

    public TaotaoResult deleteContent(String ids) {
        String[] values = ids.split(",");
        for (String value : values) {
            Content content = contentMapper.selectByPrimaryKey(Long.parseLong(value));
            contentMapper.deleteByPrimaryKey(Long.parseLong(value));

            // 删除缓存
            deleteRedis(content.getCategoryId().toString());
        }
        return TaotaoResult.ok();
    }

    private void deleteRedis(String cid) {
        try {
            jedisClient.hdel(CONTENT_INDEX_KEY, cid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
