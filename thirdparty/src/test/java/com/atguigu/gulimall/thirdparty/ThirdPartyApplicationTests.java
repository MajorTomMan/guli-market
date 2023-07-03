package com.atguigu.gulimall.thirdparty;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThirdPartyApplicationTests {
    @Autowired
    OSSClient client;
    @Test
    void testOssServer() {
        String bucketName = "gulimal-byme";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "test.txt";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath = "/home/master/project/gulimall/product/src/test/java/com/atguigu/gulimall/product/test.txt";

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObject请求。
            client.putObject(bucketName, objectName, inputStream);
            System.out.println("Upload Success!");
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
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }

}
