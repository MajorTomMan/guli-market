/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-07-24 23:32:03
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-30 23:03:28
 * @FilePath: \Guli\search\src\main\java\com\atguigu\gulimall\search\service\impl\SearchServiceImpl.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.search.service.impl;

import java.io.IOException;

import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.atguigu.gulimall.search.service.SearchService;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResult;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.SimpleJsonpMapper;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    RestClient restClient;
    @Autowired
    ElasticsearchClient esClient;

    @Override
    public SearchResult search(SearchParam param) {
        // TODO Auto-generated method stub
        try {
            return buildSearchRequest(param);
        } catch (ElasticsearchException | IOException e) {
            // TODO Auto-generated catch block
            log.warn("ElasticSearch检索搜索参数的SKU失败");
            log.warn(e.getMessage());
            log.warn(e.getCause());
            log.warn("报错类位于:" + getClass());
        }
        return null;
    }

    /* 准备检索请求 */
    private SearchResult buildSearchResult(SearchResponse response) {
        return null;
    }

    /* 准备检索请求 */
    private SearchResult buildSearchRequest(SearchParam param) throws ElasticsearchException, IOException {
        Builder bool = QueryBuilders.bool();
        /* 模糊匹配 */
        if (!StringUtils.hasText(param.getKeyword())) {
            bool.must(m -> m.match(match -> match.field("skuTitle").query(param.getKeyword())));
        }
        /* 按照三级分类查询 */
        if (!ObjectUtils.isEmpty(param.getCatalog3Id())) {
            bool.filter(f -> f.term(t -> t.field("catalogId").value(param.getCatalog3Id())));
        }
        /* 按照品牌ID查询 */
        if (!ObjectUtils.isEmpty(param.getBrandId())) {
            bool.filter(f -> f.terms(t -> t.field("brandId")));
        }
        /* 按照指定的属性进行查询 */
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
            BoolQuery.Builder nestedQuery=QueryBuilders.bool();
            for (String attr : param.getAttrs()) {
                String[] temp=attr.split("_");
                String attrId=temp[0];
                String[] attrValues=temp[1].split(":");
                nestedQuery.must(m->m.term(t->t.field("attrs.attrId").value(attrId)));
            }

            NestedQuery.Builder query = QueryBuilders.nested().path("attrs");
        }

        /* 按照是否有库存进行查询 */
        if (param.getHasStock() == 1) {
            bool.filter(f -> f.term(t -> t.field("hasStock").value(true)));
        } else {
            bool.filter(f -> f.term(t -> t.field("hasStock").value(false)));
        }
        /* 按照价格区间来查询 */
        if (!StringUtils.hasText(param.getSkuPrice())) {
            /*
             * 三种情况
             * 1_500/_500/500_
             */
            RangeQuery.Builder query = QueryBuilders.range().field("skuPrice");
            String[] temp = param.getSkuPrice().split("_");
            if (temp.length == 2) {
                query.gte(JsonData.of(temp[0], new SimpleJsonpMapper()))
                        .lte(JsonData.of(temp[1], new SimpleJsonpMapper()));
            } else if (temp.length == 1) {
                if (param.getSkuPrice().startsWith("_")) {
                    query.lte(JsonData.of(temp[0], new SimpleJsonpMapper()));
                } else if (param.getSkuPrice().endsWith("_")) {
                    query.gte(JsonData.of(temp[0], new SimpleJsonpMapper()));
                }
            }
            bool.filter(f -> f.range(query.build()));
        }
        return null;
    }

}
