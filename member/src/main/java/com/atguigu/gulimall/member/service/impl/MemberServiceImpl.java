/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-06-23 17:37:38
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-11-05 18:43:56
 * @FilePath: \Guli\member\src\main\java\com\atguigu\gulimall\member\service\impl\MemberServiceImpl.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.member.dao.MemberDao;
import com.atguigu.gulimall.member.dao.MemberLevelDao;
import com.atguigu.gulimall.member.entity.MemberEntity;
import com.atguigu.gulimall.member.entity.MemberLevelEntity;
import com.atguigu.gulimall.member.exception.PhoneExistException;
import com.atguigu.gulimall.member.exception.UserNameExistException;
import com.atguigu.gulimall.member.service.MemberService;
import com.atguigu.gulimall.member.vo.RegisterVo;

@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    MemberLevelDao memberLevelDao;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>());

        return new PageUtils(page);
    }

    @Override
    public void register(RegisterVo vo) {
        // TODO Auto-generated method stub
        MemberEntity entity = new MemberEntity();
        MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());
        entity.setMobile(vo.getPhone());
        /* 检查用户名和手机号是否唯一 */
        entity.setUsername(vo.getUserName());
        checkPhoneIsUnique(vo.getPhone());
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);
    }

    @Override
    public void checkPhoneIsUnique(String phone) throws PhoneExistException {
        // TODO Auto-generated method stub
        MemberDao memberDao = this.baseMapper;
        Long count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (count > 0) {
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUserNameIsUnique(String username) throws UserNameExistException {
        // TODO Auto-generated method stub
        MemberDao memberDao = this.baseMapper;
        Long count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (count > 0) {
            throw new PhoneExistException();
        }
    }

}