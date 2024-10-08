/*
 * @Date: 2023-06-23 17:37:38
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-11-24 21:11:30
 * @FilePath: \Guli\member\src\main\java\com\atguigu\gulimall\member\service\impl\MemberServiceImpl.java
 * @Description: MajorTomMan @版权声明 保留文件所有权利
 */
package com.atguigu.gulimall.member.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alibaba.nacos.api.cmdb.pojo.Entity;
import com.atguigu.gulimall.common.userinfo.GithubUserInfo;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.common.vo.SocialUserVo;
import com.atguigu.gulimall.member.dao.MemberDao;
import com.atguigu.gulimall.member.dao.MemberLevelDao;
import com.atguigu.gulimall.member.entity.MemberEntity;
import com.atguigu.gulimall.member.entity.MemberLevelEntity;
import com.atguigu.gulimall.member.exception.PhoneExistException;
import com.atguigu.gulimall.member.exception.UserNameExistException;
import com.atguigu.gulimall.member.service.MemberService;
import com.atguigu.gulimall.member.vo.MemberLoginVo;
import com.atguigu.gulimall.member.vo.RegisterVo;

@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    MemberLevelDao memberLevelDao;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>());

        return new PageUtils(page);
    }

    @Override
    public void register(RegisterVo vo) {

        MemberEntity entity = new MemberEntity();
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());
        /* 检查用户名和手机号是否唯一 */
        checkUserNameIsUnique(vo.getUserName());
        entity.setUsername(vo.getUserName());
        checkPhoneIsUnique(vo.getPhone());
        entity.setMobile(vo.getPhone());
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);
        this.baseMapper.insert(entity);
    }

    @Override
    public void checkPhoneIsUnique(String phone) throws PhoneExistException {

        MemberDao memberDao = this.baseMapper;
        Long count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (count > 0) {
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUserNameIsUnique(String username) throws UserNameExistException {

        MemberDao memberDao = this.baseMapper;
        Long count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (count > 0) {
            throw new PhoneExistException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo vo) {

        MemberDao dao = this.baseMapper;
        MemberEntity entity = dao.selectOne(new QueryWrapper<MemberEntity>().eq("username", vo.getLoginAccount()).or()
                .eq("mobile", vo.getLoginAccount()));
        if (entity == null) {
            return null;
        } else {
            /* 获取到数据库的密码 */
            String password = entity.getPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            /* 密码匹配 */
            boolean matches = encoder.matches(vo.getPassword(), password);
            if (matches) {
                return entity;
            }
            return null;
        }
    }

    public MemberEntity login(GithubUserInfo info) {

        MemberEntity entity = this.baseMapper
                .selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", info.getId()));
        if (entity != null) {
            MemberEntity newEntity = new MemberEntity();
            newEntity.setId(entity.getId()); // 保留原有ID
            newEntity.setUsername(info.getLogin()); // 更新用户名
            newEntity.setNickname(info.getLogin());
            newEntity.setEmail(info.getEmail()); // 更新邮箱
            newEntity.setHeader(info.getAvatarUrl()); // 更新头像
            newEntity.setFromGithub(true);
            this.baseMapper.updateById(newEntity);
            return newEntity;
        } else {
            MemberEntity regist = new MemberEntity();
            regist.setUsername(info.getLogin()); // 设置用户名
            regist.setNickname(info.getLogin());
            regist.setEmail(info.getEmail()); // 设置邮箱
            regist.setMobile(null); // 手机号码为空
            regist.setSocial_uid(info.getId()); // 设置社交UID
            regist.setFromGithub(true); // 设置来源为GitHub
            regist.setHeader(info.getAvatarUrl()); // 设置头像
            regist.setCreateTime(new Date()); // 设置注册时间为当前时间
            this.baseMapper.insert(regist);
            return regist;
        }
    }
}