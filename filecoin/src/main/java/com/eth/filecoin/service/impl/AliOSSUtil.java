package com.eth.filecoin.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * ali:
 *   oss:
 *     endpoint: http://oss-cn-hangzhou.aliyuncs.com
 *     accessKeyId:
 *     accessKeySecret:
 *     bucketName:
 *     firstKey:
 *     url: http://aqi-shop.oss-cn-hangzhou.aliyuncs.com/
 *     catalogue:
 *
 *
 * @author: aqi
 * @Date: 2022/5/7 10:59
 * @Description: 工具类
 */
@Component
public class AliOSSUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${ali.oss.endpoint}")
    public String endpoint;

    @Value("${ali.oss.accessKeyId}")
    public String accessKeyId;

    @Value("${ali.oss.accessKeySecret}")
    public String accessKeySecret;

    @Value("${ali.oss.bucketName}")
    public String bucketName;

    @Value("${ali.oss.firstKey}")
    public String firstKey;

    @Value("${ali.oss.url}")
    public String url;

    @Value("${ali.oss.catalogue}")
    public String catalogue;

    /**
     * 上传Byte数组
     */
    public void putObjectByte(byte[] content, String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            this.createBucket(ossClient);
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (IOException e) {
            logger.error("Error Message:" + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


    /**
     * 上传字符串
     */
    public void putObjectString(String content, String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            this.createBucket(ossClient);
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));

            // 上传字符串。
            ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            logger.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            logger.error("Error Message:" + oe.getErrorMessage());
            logger.error("Error Code:" + oe.getErrorCode());
            logger.error("Request ID:" + oe.getRequestId());
            logger.error("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            logger.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            logger.error("Error Message:" + ce.getMessage());
        } catch (IOException e) {
            logger.error("Error Message:" + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 创建存储空间
     */
    public void createBucket(OSS ossClient) throws OSSException, ClientException, IOException {
        // 判断存储空间是否存在。如果返回值为true，则存储空间存在，如果返回值为false，则存储空间不存在。
        boolean exists = ossClient.doesBucketExist(bucketName);
        if (exists) {
            return;
        }
        // 创建存储空间。
        ossClient.createBucket(bucketName);
        logger.info("创建存储空间：" + bucketName);
    }
}
