package com.atguigu.gulimall.entity.github;

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
