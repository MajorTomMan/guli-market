package com.atguigu.gulimall.auth.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.gulimall.auth.constant.GithubOauthConstant;
import com.atguigu.gulimall.auth.constant.OAuthConstant;
import com.atguigu.gulimall.auth.entity.github.GithubResponseEntity;
import com.atguigu.gulimall.auth.feign.MemberFeignService;
import com.atguigu.gulimall.common.constant.AuthServerConstant;
import com.atguigu.gulimall.common.userinfo.GithubUserInfo;
import com.atguigu.gulimall.common.utils.HttpUtils;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.common.vo.MemberResponseVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
@RequestMapping("/oauth")
public class OauthController {
    @Autowired
    Gson gson;
    @Autowired
    MemberFeignService feignService;

    @GetMapping("/github/success")
    public String oauthGithubSuccess(@RequestParam("code") String code, HttpSession session) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        Map<String, String> querys = new HashMap<>();
        querys.put("client_id", GithubOauthConstant.GITHUB_OAUTH_CLIENT_ID);
        querys.put("client_secret", GithubOauthConstant.GITHUB_OAUTH_CLIENT_SECRECT);
        querys.put("code", code);
        querys.put("grant_type", OAuthConstant.GRANT_TYPE);
        GithubResponseEntity githubResponseEntity = null;
        try (CloseableHttpResponse response = HttpUtils.doPost(GithubOauthConstant.GITHUB_OAUTH_HOST,
                GithubOauthConstant.GITHUB_OAUTH_PATH, headers, querys, null)) {
            if (response.getCode() == 200) {
                log.info("用户三方登录成功");
                // 获取AccessToken并转换成实体类
                githubResponseEntity = getEntity(response, GithubResponseEntity.class);

            } else {
                log.error("status_code:" + response.getCode());
                log.error("用户三方登录状态码不为200");
                log.error("重定向至登录页");
                return "redirect:http://auth.gulimall.com/login.html";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
            log.error("用户三方登录失败");
            log.error("重定向至登录页");
            return "redirect:http://auth.gulimall.com/login.html";
        }
        if (githubResponseEntity != null) {
            headers = new HashMap<>();
            headers.put("Accept", "application/vnd.github.v3+json");
            headers.put("Authorization",
                    githubResponseEntity.getTokenType() + " " + githubResponseEntity.getAccessToken());
            GithubUserInfo userInfo = null;
            try (CloseableHttpResponse response = HttpUtils.doGet(GithubOauthConstant.GITHUB_OAUTH_USER_HOST,
                    GithubOauthConstant.GITHUB_OAUTH_USER_PATH,
                    headers, null)) {
                if (response.getCode() == 200) {
                    log.info("查询GitHub用户信息成功");
                    userInfo = getEntity(response, GithubUserInfo.class);
                } else {
                    log.error("status_code:" + response.getCode());
                    log.error("用户信息查询状态码不为200");
                    log.error("重定向至登录页");
                    return "redirect:http://auth.gulimall.com/login.html";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error(e.getMessage());
                log.error("查询Github用户信息失败");
                log.error("重定向至登录页");
                return "redirect:http://auth.gulimall.com/login.html";
            }
            if (userInfo != null) {
                R r = feignService.oauthLogin(userInfo);
                if (r.getCode() == 0) {
                    MemberResponseVo entity = (MemberResponseVo) r.getData("entity",
                            new TypeReference<MemberResponseVo>() {
                            });
                    session.setAttribute(AuthServerConstant.LOGIN_USER, entity);
                    return "redirect:http://gulimall.com";
                } else {
                    log.error("查询会员信息失败");
                    log.error("重定向至登录页");
                    return "redirect:http://auth.gulimall.com/login.html";
                }
            } else {
                return "redirect:http://auth.gulimall.com/login.html";
            }
        } else {
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }

    private <T> T getEntity(CloseableHttpResponse response, Class<T> clazz) throws ParseException, IOException {
        String json = EntityUtils.toString(response.getEntity());
        log.info("response:" + json);
        T entity = gson.fromJson(json,
                clazz);
        log.info("entity:" + entity);
        return entity;
    }
}
