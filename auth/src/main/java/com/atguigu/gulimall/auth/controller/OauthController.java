package com.atguigu.gulimall.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.gulimall.auth.constant.GithubOauthConstant;
import com.atguigu.gulimall.auth.constant.OAuthConstant;
import com.atguigu.gulimall.common.utils.HttpUtils;
import com.atguigu.gulimall.entity.GithubResponseEntity;
import com.google.gson.Gson;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
@RequestMapping("/oauth")
public class OauthController {
    @Autowired
    Gson gson;

    @GetMapping("/github/success")
    public String oauthGithubSuccess(@RequestParam("code") String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("client_id", GithubOauthConstant.GITHUB_OAUTH_CLIENT_ID);
        map.put("client_secrect", GithubOauthConstant.GITHUB_OAUTH_CLIENT_SECRECT);
        map.put("code", code);
        map.put("grant_type", OAuthConstant.GRANT_TYPE);
        String body = gson.toJson(map);
        GithubResponseEntity githubResponseEntity = null;
        try (CloseableHttpResponse response = HttpUtils.doPost(GithubOauthConstant.GITHUB_OAUTH_HOST,
                GithubOauthConstant.GITHUB_OAUTH_PATH, null, null,
                body)) {
            if (response.getCode() == 200) {
                log.info("用户三方登录成功");
                HttpEntity entity = response.getEntity();
                githubResponseEntity = gson.fromJson(EntityUtils.toString(entity),
                        GithubResponseEntity.class);
                log.info("response:" + githubResponseEntity);

            } else {
                log.error("status_code:" + response.getCode());
                throw new Exception("状态码不为200,登录失败");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
            log.error("用户三方登录失败");
            log.error("重定向至登录页");
            e.printStackTrace(); 
            return "redirect:http://auth.gulimall.com/login.html";
        }
        if (githubResponseEntity != null) {

        }
        return "redirect:http://gulimall.com";
    }

}
