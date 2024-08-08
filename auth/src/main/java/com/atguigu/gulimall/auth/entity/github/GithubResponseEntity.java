/*
 * @Date: 2024-08-07 21:51:26
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-08-08 23:04:55
 * @FilePath: \Guli\auth\src\main\java\com\atguigu\gulimall\auth\entity\github\GithubResponseEntity.java
 * @Description: MajorTomMan @版权声明 保留文件所有权利
 */
package com.atguigu.gulimall.auth.entity.github;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GithubResponseEntity {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("scope")
    private String scope;

}
