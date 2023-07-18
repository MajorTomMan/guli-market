/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-08-30 10:24:12
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-19 00:05:30
 * @FilePath: /common/home/master/project/gulimall/thirdparty/src/main/java/com/atguigu/gulimall/thirdparty/controller/OssController.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.thirdparty.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSBuilder;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.atguigu.gulimall.common.utils.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OssController {
    @Value(value = "${spring.cloud.alicloud.access-key}")
    String accessId;
    @Value(value = "${spring.cloud.alicloud.oss.endpoint}")
    String endpoint;
    @Value(value = "${spring.cloud.alicloud.oss.bucket}")
    String bucket;
    @RequestMapping("/oss/policy")
    public R policy() {
        OSS client=new OSSClientBuilder().build(endpoint, accessId, accessId, accessId);
        // 填写Host地址，格式为https://bucketname.endpoint。
        String host = "https://" + bucket + "."+endpoint;
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String dir = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/";
        Map<String, String> respMap = new LinkedHashMap<String, String>();
        try {
            long expireTime = 100;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        }
        return R.ok().put("data", respMap);
    }
}
