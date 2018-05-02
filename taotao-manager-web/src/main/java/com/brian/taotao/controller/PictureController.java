package com.brian.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

@Controller
public class PictureController {

    @Value("${QiNiu_AccessKey}")
    private String QiNiu_AccessKey;
    @Value("${QiNiu_SecretKey}")
    private String QiNiu_SecretKey;
    @Value("${QiNiu_Bucket}")
    private String QiNiu_Bucket;
    @Value("${QiNiu_Url}")
    private String QiNiu_Url;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public Map picUpload(MultipartFile uploadFile) {
        Map result = new HashMap();
        try {
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            Configuration cfg = new Configuration(Zone.autoZone());
            UploadManager uploadManager = new UploadManager(cfg);

            Auth auth = Auth.create(QiNiu_AccessKey, QiNiu_SecretKey);
            String uploadToken = auth.uploadToken(QiNiu_Bucket);

            String filename = System.currentTimeMillis()+"."+extName;

            //开始上传
            uploadManager.put(uploadFile.getInputStream(), filename, uploadToken,null,null);

            String url = QiNiu_Url + filename;

            result.put("error", 0);
            result.put("url", url);
        } catch (Exception e) {
            result.put("error", 0);
            result.put("message", "图片上传失败");
        }
        return result;
    }
}
