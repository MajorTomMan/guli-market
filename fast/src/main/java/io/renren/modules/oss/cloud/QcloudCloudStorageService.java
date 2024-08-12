/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.oss.cloud;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.exception.CosClientException;
import org.apache.commons.io.IOUtils;
import io.renren.common.exception.RRException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 腾讯云存储
 *
 * @author Mark sunlightcs@gmail.com
 */
public class QcloudCloudStorageService extends CloudStorageService {
    private COSClient client;

    public QcloudCloudStorageService(CloudStorageConfig config) {
        this.config = config;
        // 初始化
        init();
    }

    private void init() {
        // 初始化客户端配置
        ClientConfig clientConfig = new ClientConfig();
        // 设置bucket所在的区域，华南：gz 华北：tj 华东：sh
        clientConfig.setRegion(clientConfig.getRegion());

        // 创建 Credentials
        COSCredentials credentials = new BasicCOSCredentials(
                config.getQcloudSecretId(),
                config.getQcloudSecretKey());

        client = new COSClient(credentials, clientConfig);
    }

    @Override
    public String upload(byte[] data, String path) {
        // 腾讯云必需要以"/"开头
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        try {
            // 创建上传请求
            PutObjectRequest request = new PutObjectRequest(config.getQcloudBucketName(), config.getQcloudSecretKey(),
                    new File(path));
            client.putObject(request);

            return config.getQcloudDomain() + path;
        } catch (CosServiceException e) {
            throw new RRException("文件上传失败，" + e.getErrorMessage(), e);
        } catch (CosClientException e) {
            throw new RRException("文件上传失败，" + e.getMessage(), e);
        }
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new RRException("上传文件失败", e);
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQcloudPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQcloudPrefix(), suffix));
    }
}
