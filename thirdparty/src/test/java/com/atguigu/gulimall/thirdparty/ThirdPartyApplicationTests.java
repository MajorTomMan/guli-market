/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-06-23 17:37:38
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-10-27 23:20:47
 * @FilePath: \Guli\thirdparty\src\test\java\com\atguigu\gulimall\thirdparty\ThirdPartyApplicationTests.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.thirdparty;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.atguigu.gulimall.thirdparty.component.SMSComponent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThirdPartyApplicationTests {
    @Value(value = "${spring.cloud.alicloud.access-id}")
    String accessId;
    @Value(value = "${spring.cloud.alicloud.access-key}")
    String accesskey;
    @Value(value = "${spring.cloud.alicloud.oss.endpoint}")
    String endpoint;
    @Value(value = "${spring.cloud.alicloud.oss.bucket}")
    String bucket;
    @Autowired
    SMSComponent smsComponent;

    @Test
    void testOssServer() {
        String bucketName = "gulimal-byme";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "./testdata/test.txt";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath = "";
        OSS client = new OSSClientBuilder().build(endpoint, accessId, accesskey);
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

            e.printStackTrace();
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }

    @Test
    void testSendSMS() throws InterruptedException, ExecutionException {
        smsComponent.sendSms("xxx", "1234");
    }
}
