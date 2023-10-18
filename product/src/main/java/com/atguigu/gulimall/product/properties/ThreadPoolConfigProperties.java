/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-10-18 23:46:57
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-10-18 23:47:00
 * @FilePath: \Guli\product\src\main\java\com\atguigu\gulimall\product\properties\ThreadPoolConfigProperties.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.product.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "gulimall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
