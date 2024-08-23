
package com.atguigu.gulimall.auth.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atguigu.gulimall.auth.feign.MemberFeignService;
import com.atguigu.gulimall.auth.feign.ThirdPartyFeignService;
import com.atguigu.gulimall.auth.vo.UserLoginVo;
import com.atguigu.gulimall.auth.vo.UserRegisterVo;
import com.atguigu.gulimall.common.constant.AuthServerConstant;
import com.atguigu.gulimall.common.exception.BizCodeEmum;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.common.vo.MemberResponseVo;
import com.fasterxml.jackson.core.type.TypeReference;

@Controller
public class LoginController {
    @Autowired
    private ThirdPartyFeignService thirdPartyFeignService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MemberFeignService memberFeignService;
    /*
     * 发送一个请求直接跳转到一个页面
     */

    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone) {
        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        /* TODO 1.接口防刷 */
        if (StringUtils.hasText(redisCode)) {
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 60000) {
                // 60秒内不能再发
                return R.error(BizCodeEmum.SMS_CODE_EXCEPTION.getCode(), BizCodeEmum.SMS_CODE_EXCEPTION.getMsg());
            }
        }
        String code = UUID.randomUUID().toString().substring(0, 6);
        // String code = "" + 123456;
        String subString = code + "_" + System.currentTimeMillis();
        /* 2.验证码的再次校验 */
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, subString, 1,
                TimeUnit.MINUTES);
        thirdPartyFeignService.sendCode(phone, code);
        return R.ok();
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterVo vo, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            // model.addAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/register.html";
        }
        String code = vo.getCode();
        String s = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (StringUtils.hasText(s)) {
            if (code.equals(s.split("_")[0])) {
                /* 删除验证码,令牌机制 */
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                /* 注册用户 */
                R r = memberFeignService.regist(vo);
                if (r.getCode() == 0) {
                    HashMap<String, Object> errors = new HashMap<>();
                    errors.put("msg", r.getData(new TypeReference<String>() {
                    }));
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.gulimall.com/login.html";
                } else {
                    return "redirect:http://auth.gulimall.com/register.html";
                }
            } else {
                HashMap<String, String> errors = new HashMap<String, String>();
                errors.put("code", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.gulimall.com/register.html";
            }
        } else {
            HashMap<String, String> errors = new HashMap<String, String>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/register.html";
        }
    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session) {
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute == null) {
            return "login";
        } else {
            return "redirect:http://gulimall.com";
        }

    }

    @GetMapping("/logout.html")
    public String logOut(HttpSession session) {
        session.removeAttribute(AuthServerConstant.LOGIN_USER);
        return "redirect:http://gulimall.com";
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes attributes, HttpSession session) {
        /* 远程登录 */
        R r = memberFeignService.login(vo);
        if (r.getCode() == 0) {
            MemberResponseVo entity = (MemberResponseVo) r.getData("entity",
                    new TypeReference<MemberResponseVo>() {
                    });
            session.setAttribute(AuthServerConstant.LOGIN_USER, entity);
            return "redirect:http://gulimall.com";
        }
        HashMap<String, Object> errors = new HashMap<>();
        errors.put("msg", r.getData("msg", new TypeReference<String>() {
        }));
        attributes.addFlashAttribute("errors", errors);
        return "redirect:http://auth.gulimall.com/login.html";
    }
}
