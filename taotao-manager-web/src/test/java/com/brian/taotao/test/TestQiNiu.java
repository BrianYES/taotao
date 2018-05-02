package com.brian.taotao.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class TestQiNiu {

    private String QiNiu_AccessKey = "D6Vfszx6bjL4t87ZsGp27pwZm0AMCBodlPeDEq-4";
    private String QiNiu_SecretKey = "miBC8U_d3nluAPl3UgOuwJua8CGTc9J2X5r_BfCd";
    private String bucket = "taotao";
    private static final String spaceName = "p7vuu5nv4.bkt.clouddn.com";

    @Test
    public void testUpload() throws Exception {
        Configuration cfg = new Configuration(Zone.autoZone());
        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(QiNiu_AccessKey, QiNiu_SecretKey);
        String uploadToken = auth.uploadToken(bucket);

        String key = System.currentTimeMillis()+"";

        //开始上传
        File file = new File("/Users/xuyong/Desktop/路径配置.txt");
        InputStream inputStream = new FileInputStream(file);

        Response qiniuresponse=uploadManager.put(inputStream, key, uploadToken,null,null);

        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(qiniuresponse.bodyString(), DefaultPutRet.class);

        String qiniuUrl=spaceName+key;

        System.out.println("dizhi:"+qiniuUrl);

        System.out.println(putRet.key);
        System.out.println(putRet.hash);
    }

}
