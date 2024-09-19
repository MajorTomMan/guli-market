package com.atguigu.gulimall.product.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.atguigu.gulimall.common.utils.PageUtils;
import com.atguigu.gulimall.common.utils.Query;
import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import com.atguigu.gulimall.product.vo.Catelog2Vo.Category3Vo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryBrandRelationService relationService;
    @Autowired
    StringRedisTemplate redisClient;
    @Autowired
    RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>());

        return new PageUtils(page);
    }

    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        List<CategoryEntity> topLevelMenus = entities.stream().filter(
                categoryEntity -> categoryEntity.getParentCid() == 0).map(
                        (menu) -> {
                            menu.setChildren(getChildrens(menu, entities));
                            return menu;
                        })
                .sorted(
                        (menu1, menu2) -> {
                            return menu1.getSort() - menu2.getSort();
                        })
                .collect(Collectors.toList());
        return topLevelMenus;
    }

    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(
                categoryEntity -> {
                    return categoryEntity.getParentCid() == root.getCatId();
                }).map(
                        categoryEntity -> {
                            categoryEntity.setChildren(getChildrens(categoryEntity, all));
                            return categoryEntity;
                        })
                .sorted(
                        (menu1, menu2) -> {
                            return (menu1.getSort() == null ? 0 : menu1.getSort())
                                    - (menu2.getSort() == null ? 0 : menu2.getSort());
                        })
                .collect(Collectors.toList());
        return children;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {

        baseMapper.deleteByIds(asList);
    }

    @Override
    public Long[] findCateLogPath(Long cateLogId) {

        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(cateLogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    @CacheEvict(value = "category", key = "'getLevel1Categorys'")
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {

        this.updateById(category);
        if (StringUtils.hasText(category.getName())) {
            relationService.updateCategory(category.getCatId(), category.getName());
        }
    }

    @Cacheable(value = { "category" }, key = "#root.method.name")
    @Override
    public List<CategoryEntity> getLevel1Categorys() {

        List<CategoryEntity> categoryEntities = baseMapper
                .selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    @Cacheable(value = "category", key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson()
            throws JsonProcessingException, InterruptedException {

        List<CategoryEntity> selectList = baseMapper.selectList(null);
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> {
            return k.getCatId().toString();
        }, v -> {
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(),
                            l2.getName());
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Category3Vo> catalog3vo = level3Catelog.stream().map(l3 -> {
                            Category3Vo catelog3Vo = new Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(),
                                    l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catalog3vo);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parent_cid;
    }

    public Map<String, List<Catelog2Vo>> getCatalogJson2() throws JsonProcessingException, InterruptedException {

        ObjectMapper mapper = new ObjectMapper();
        String catalogJSON = redisClient.opsForValue().get("catalogJSON");
        if (!StringUtils.hasText(catalogJSON)) {
            System.out.println("缓存未命中...开始查询数据库.....");
            Map<String, List<Catelog2Vo>> catalogJsonFromDB = getCatalogJsonFromDBWithRedisLock();
            return catalogJsonFromDB;
        }
        System.out.println("缓存命中...");
        Map<String, List<Catelog2Vo>> result = mapper.readValue(catalogJSON,
                new TypeReference<Map<String, List<Catelog2Vo>>>() {
                });
        return result;
    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDBWithRedisLock()
            throws JsonMappingException, JsonProcessingException, InterruptedException {

        System.out.println("获取分布式锁成功");
        Map<String, List<Catelog2Vo>> dataFromDB;
        RLock lock = redissonClient.getLock("catalogJson-lock");
        lock.lock();

        try {
            System.out.println("查询数据库");
            dataFromDB = getDataFromDB();
        } finally {
            // TODO: handle exception
            lock.unlock();
        }
        return dataFromDB;
    }

    private Map<String, List<Catelog2Vo>> getDataFromDB() throws JsonProcessingException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        String catalogJSON = redisClient.opsForValue().get("catalogJSON");
        if (StringUtils.hasText(catalogJSON)) {
            Map<String, List<Catelog2Vo>> result = mapper.readValue(catalogJSON,
                    new TypeReference<Map<String, List<Catelog2Vo>>>() {
                    });
            return result;
        }
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> {
            return k.getCatId().toString();
        }, v -> {
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(),
                            l2.getName());
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Category3Vo> catalog3vo = level3Catelog.stream().map(l3 -> {
                            Category3Vo catelog3Vo = new Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(),
                                    l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catalog3vo);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));

        String s = mapper.writeValueAsString(
                parent_cid);
        redisClient.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);
        return parent_cid;
    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDB()
            throws JsonMappingException, JsonProcessingException {
        return getDataFromDB();
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(
                item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
        return collect;
    }
}