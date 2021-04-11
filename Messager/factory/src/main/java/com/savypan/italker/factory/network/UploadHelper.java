package com.savypan.italker.factory.network;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.savypan.italker.factory.Factory;

import java.io.File;
import java.util.Date;

public class UploadHelper {

    private static final String ENDPOINT_SH = "https://oss-cn-shanghai.aliyuncs.com";
    private static final String BUCKET_NAME = "italker-savy";

    private static final String accessID = "LTAI5tCPri3qP79i6nMj4Vpk";
    private static final String accessKey = "Z0miWonPWGPjfEb5FHNWkrD1zJAWSJ";

    public static OSS getClient() {
        OSSCredentialProvider credentialProvider;
        credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessID, accessKey);

        OSS oss = new OSSClient(Factory.getApplication(), ENDPOINT_SH, credentialProvider);
        return oss;
    }


    private static String upload(String objKey, String path) {
        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, objKey, path);

        try {
            OSS ossClient = getClient();
            PutObjectResult result = ossClient.putObject(put);
            String url = ossClient.presignPublicObjectURL(BUCKET_NAME, objKey);
            Log.d("SAVY", "uploading URL - " + url);
            return url;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 上传普通图片
     * @param path
     * @return
     */
    public static String uploadImage(String path) {
        String key = getImageObjKey(path);
        return upload(key, path);
    }

    /***
     * 上传头像图片
     * @param path
     * @return
     */
    public static String uploadPortrait(String path) {
        String key = getPortraitObjKey(path);
        return upload(key, path);
    }


    /***
     * 上传音频文件
     * @param path
     * @return
     */
    public static String uploadAudio(String path) {
        String key = getAudioObjKey(path);
        return upload(key, path);
    }


    private static String getDataString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    //example: image/20210411/wusjfsonslxi20n.jpg
    private static String getImageObjKey(String path) {
        String fileMD5 = com.savypan.italker.common.utils.HashUtil.getMD5String(new File(path));
        String dateString = getDataString();

        return String.format("image/%s/%s.jpg", dateString, fileMD5);
    }


    private static String getPortraitObjKey(String path) {
        String fileMD5 = com.savypan.italker.common.utils.HashUtil.getMD5String(new File(path));
        String dateString = getDataString();

        return String.format("portrait/%s/%s.jpg", dateString, fileMD5);
    }


    private static String getAudioObjKey(String path) {
        String fileMD5 = com.savypan.italker.common.utils.HashUtil.getMD5String(new File(path));
        String dateString = getDataString();

        return String.format("audio/%s/%s.mp3", dateString, fileMD5);
    }
}
