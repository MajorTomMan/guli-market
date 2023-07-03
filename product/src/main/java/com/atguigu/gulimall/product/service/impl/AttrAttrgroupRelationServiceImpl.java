/*
 * @Author: flashnames 765719516@qq.com
 * @Date: 2022-07-21 16:08:04
 * @LastEditors: flashnames 765719516@qq.com
 * @LastEditTime: 2022-09-20 22:01:47
 * @FilePath: /common/home/master/project/gulimall/product/src/main/java/com/atguigu/gulimall/product/service/impl/AttrAttrgroupRelationServiceImpl.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.product.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.service.AttrAttrgroupRelationService;
import com.atguigu.gulimall.product.vo.AttrGroupRelationVo;

@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity>
        implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>());

        return new PageUtils(page);
    }

    @Override
    public void saveBatch(List<AttrGroupRelationVo> vos) {
        // TODO Auto-generated method stub
        List<AttrAttrgroupRelationEntity> collect = vos.stream().map(item -> {
            AttrAttrgroupRelationEntity groupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, groupRelationEntity);
            return groupRelationEntity;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}