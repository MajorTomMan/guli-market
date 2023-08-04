/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-07-24 23:32:03
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-08-04 21:14:11
 * @FilePath: /guli-market-master/search/src/main/java/com/atguigu/gulimall/search/service/impl/SearchServiceImpl.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.search.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.alibaba.nacos.common.packagescan.resource.ClassPathResource;
import com.atguigu.gulimall.common.constant.ElasticConstant;
import com.atguigu.gulimall.search.service.SearchService;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResult;
import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.json.JsonData;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    RestClient restClient;
    @Autowired
    ElasticsearchClient elasticsearchClient;

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
        /* 准备构建嵌套请求体,使用DSL语法 */
        Query query = Query.of(q -> {
            /* 构建一个Bool嵌套请求体 */
            BoolQuery boolQuery = BoolQuery.of(b -> {
                /*
                 * 模糊匹配
                 * 根据关键词匹配
                 */
                if (StringUtils.hasText(param.getKeyword())) {
                    /* 匹配查询skuTitle字段的值 */
                    MatchQuery matchQuery = MatchQuery.of(m -> {
                        m.field("skuTitle").query(param.getKeyword());
                        return m;
                    });
                    b.filter(matchQuery._toQuery());
                }
                /* 按照三级分类查询 */
                if (!ObjectUtils.isEmpty(param.getCatalog3Id())) {
                    TermQuery termQuery = TermQuery.of(t -> {
                        t.field("catalogId").value(param.getCatalog3Id());
                        return t;
                    });
                    b.filter(termQuery._toQuery());
                }
                /* 按照品牌ID查询 */
                if (!ObjectUtils.isEmpty(param.getBrandId())) {
                    TermsQuery termsQuery = TermsQuery.of(t -> {
                        List<Long> brandId = param.getBrandId();
                        /* 根据品牌ID列表构造ElasticSearch所需的查询参数 */
                        List<FieldValue> list=brandId.stream().map(id->{
                            return FieldValue.of(id);
                        }).collect(Collectors.toList());
                        t.field("brandId").terms(terms->terms.value(list));
                        return t;
                    });
                    b.filter(termsQuery._toQuery());
                }
                /* 按照指定的属性进行查询 */
                if (param.getAttrs() != null && param.getAttrs().size() > 0) {
                    // attrs=1_5寸:8寸&attrs=2_16G:8G
                    for (String attr : param.getAttrs()) {
                        String[] temp = attr.split("_");
                        /* 检索的属性ID */
                        String attrId = temp[0];
                        /* 这个属性检索用的值 */
                        String[] attrValues = temp[1].split(":");
                        List<FieldValue> collect = Arrays.asList(attrValues).stream().map(data -> {
                            return FieldValue.of(data);
                        }).collect(Collectors.toList());
                        /* Term请求条件构造 */
                        TermQuery termQuery = TermQuery.of(t -> {
                            t.field("attrs.attrId").value(attrId);
                            return t;
                        });
                        /* Terms请求条件构造 */
                        TermsQuery termsQuery = TermsQuery.of(t -> {
                            t.field("attrs.attrValue").terms(terms -> terms.value(collect));
                            return t;
                        });
                        /* 再嵌套一个Bool */
                        BoolQuery nestedBoolQuery = BoolQuery.of(bool -> {
                            bool.must(termQuery._toQuery(), termsQuery._toQuery());
                            return bool;
                        });
                        /* 每一个都要生成一个Nested查询 */
                        NestedQuery nestedQuery = NestedQuery.of(n -> {
                            n.path("attrs").query(nestedBoolQuery._toQuery());
                            return n;
                        });
                        b.filter(nestedQuery._toQuery());
                    }
                    /* 按照是否有库存进行查询 */
                    if (param.getHasStock() != null) {
                        TermQuery termQuery = TermQuery.of(t -> {
                            t.field("hasStock");
                            if (param.getHasStock() == 1) {
                                t.value(true);
                            } else {
                                t.value(false);
                            }
                            return t;
                        });
                        b.filter(termQuery._toQuery());
                    }
                    /* 按照价格区间来查询 */
                    if (StringUtils.hasText(param.getSkuPrice())) {
                        /*
                         * 三种情况
                         * 1_500/_500/500_
                         */
                        String[] temp = param.getSkuPrice().split("_");
                        RangeQuery rangeQuery = RangeQuery.of(r -> {
                            /* 根据价格所传参数和区间来构造请求体 */
                            if (temp.length == 2) {
                                r.field("skuPrice").gte(JsonData.of(temp[0])).lte(JsonData.of(temp[1]));
                            } else if (temp.length == 1) {
                                if (param.getSkuPrice().startsWith("_")) {
                                    r.field("skuPrice").lte(JsonData.of(temp[1]));
                                } else if (param.getSkuPrice().endsWith("_")) {
                                    r.field("skuPrice").gte(JsonData.of(temp[1]));
                                }
                            }
                            return r;
                        });
                        b.filter(rangeQuery._toQuery());
                    }
                }
                return b;
            });
            /* 构造Bool请求体完成 */
            q.bool(boolQuery);
            return q;
        });
        /*  构造请求体 */
        SearchRequest request = SearchRequest.of(s -> {
            /* 排序 */
            if (StringUtils.hasText(param.getSort())) {
                String sort = param.getSort();
                String[] split = sort.split(sort);
                String field = split[0];
                /* 构造排序请求体 */
                SortOptions sortOptions = SortOptions.of(options -> {
                    String order = split[1];
                    if (order.equalsIgnoreCase("asc")) {
                        FieldSort fieldSort = FieldSort.of(f -> {
                            f.field(field).order(SortOrder.Asc);
                            return f;
                        });
                        options.field(fieldSort);
                    } else {
                        FieldSort fieldSort = FieldSort.of(f -> {
                            f.field(field).order(SortOrder.Desc);
                            return f;
                        });
                        options.field(fieldSort);
                    }
                    return options;
                });
                s.sort(sortOptions);
            }
            /*
             * 分页
             * pageNum:1 from:0 size:5 [0,1,2,3,4]
             * pageNum:2 from:5 size:5
             * from=(pageNum-1)*size
             */
            s.from((param.getPageNum() - 1) * ElasticConstant.PRODUCT_PAGESIZE);
            s.size(ElasticConstant.PRODUCT_PAGESIZE);
            if (StringUtils.hasText(param.getKeyword())) {
                /* 高亮 */
                HighlightField field = HighlightField.of(h -> {
                    h.preTags("<b style='color:red'>").postTags("</b>");
                    return h;
                });
                Highlight highlight = Highlight.of(h -> h.fields("skuTitle", field));
                s.highlight(highlight);
            }
            s.query(query);
            return s;
        });
        if(request.query()!=null){
            Gson gson=new Gson();
            String json=gson.toJson(query);
            File file=new File(new ClassPathResource("/").getFile(),"query.json");
            try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
                outputStream.write(json.getBytes());
            }
        }
        elasticsearchClient.search(request, SearchResult.class);
        /* 聚合分析 */
        return null;
    }

}
