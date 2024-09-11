/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-07-24 23:32:03
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-07-29 22:15:15
 * @FilePath: \Guli\search\src\main\java\com\atguigu\gulimall\search\service\impl\SearchServiceImpl.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.search.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.atguigu.gulimall.common.constant.ElasticConstant;
import com.atguigu.gulimall.common.to.es.SkuEsModel;
import com.atguigu.gulimall.common.utils.R;
import com.atguigu.gulimall.search.feign.ProductFeignService;
import com.atguigu.gulimall.search.service.SearchService;
import com.atguigu.gulimall.search.vo.AttrResponseVo;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResult;
import com.atguigu.gulimall.search.vo.SearchResult.AttrVo;
import com.atguigu.gulimall.search.vo.SearchResult.BrandVo;
import com.atguigu.gulimall.search.vo.SearchResult.CatalogVo;
import com.atguigu.gulimall.search.vo.SearchResult.NavVo;
import com.google.gson.Gson;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.NestedAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
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
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public SearchResult search(SearchParam param) {

        try {
            SearchRequest request = buildSearchRequest(param);
            SearchResponse<SkuEsModel> response = elasticsearchClient.search(request, SkuEsModel.class);
            return buildSearchResult(response, param);
        } catch (ElasticsearchException | IOException e) {

            log.warn("ElasticSearch检索搜索参数的SKU失败");
            log.warn(e.getMessage());
            log.warn(e.getCause());
            log.warn("报错类位于:" + getClass());
        }
        return null;
    }

    /* 准备返回结果 */
    private SearchResult buildSearchResult(SearchResponse<SkuEsModel> response, SearchParam param) {
        SearchResult result = new SearchResult();
        long value = response.hits().total().value();
        List<Hit<SkuEsModel>> hits = response.hits().hits();
        /* 返回所有查询到的商品 */
        List<SkuEsModel> skus = hits.stream().map(source -> {
            return source.source();
        }).collect(Collectors.toList());
        /* ===================Brand=========================== */
        /* 获取品牌聚合 */
        Aggregate brand_agg = response.aggregations().get("brand_agg");
        List<BrandVo> brandVos = new ArrayList<>();
        brand_agg.lterms().buckets().array().forEach((bucket) -> {
            BrandVo brandVo = new SearchResult.BrandVo();
            /* 得到品牌ID */
            brandVo.setBrandId(bucket.key());
            Aggregate brand_name_agg = bucket.aggregations().get("brand_name_agg");
            List<StringTermsBucket> brandNameBucketList = brand_name_agg.sterms().buckets().array();
            if (brandNameBucketList.size() > 0) {
                StringTermsBucket brandNameBucket = brandNameBucketList.get(0);
                /* 得到品牌名字 */
                brandVo.setBrandName(brandNameBucket.key().stringValue());
            }

            Aggregate brand_img_agg = bucket.aggregations().get("brand_img_agg");
            List<StringTermsBucket> brandImgBucketList = brand_img_agg.sterms().buckets().array();
            if (brandImgBucketList.size() > 0) {
                StringTermsBucket brandImgBucket = brandImgBucketList.get(0);
                /* 得到品牌图片 */
                brandVo.setBrandImg(brandImgBucket.key().stringValue());
            }
            brandVos.add(brandVo);
        });
        /* 品牌信息 */
        result.setBrands(brandVos);
        /* ===================Catalog=========================== */
        Aggregate catalog_agg = response.aggregations().get("catalog_agg");
        /* 封装CatalogList数据 */
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        catalog_agg.lterms().buckets().array().forEach((bucket) -> {
            CatalogVo catalogVo = new SearchResult.CatalogVo();
            /* 设置分类ID */
            catalogVo.setCatalogId(bucket.key());
            /* 设置分类名 */
            StringTermsAggregate sterms = bucket.aggregations().get("catalog_name_agg").sterms();
            List<StringTermsBucket> catalogNameBucketList = sterms.buckets().array();
            if (catalogNameBucketList.size() > 0) {
                StringTermsBucket catalogNameBucket = catalogNameBucketList.get(0);
                catalogVo.setCatalogName(catalogNameBucket.key().stringValue());
            }
            catalogVos.add(catalogVo);
        });
        /* 分类信息 */
        result.setCatalogs(catalogVos);
        /* ===================Attrs=========================== */
        /* 获取属性聚合 */
        Aggregate attr_agg = response.aggregations().get("attr_agg");
        List<AttrVo> attrVos = new ArrayList<>();
        Aggregate attr_id_agg = attr_agg.nested().aggregations().get("attr_id_agg");
        attr_id_agg.lterms().buckets().array().forEach(bucket -> {
            AttrVo attrVo = new SearchResult.AttrVo();
            /* 设置属性ID */
            attrVo.setAttrId(bucket.key());
            /* 一个属性ID对应一个属性名和属性值 */
            Aggregate attr_name_agg = bucket.aggregations().get("attr_name_agg");
            List<StringTermsBucket> attrNameBucketList = attr_name_agg.sterms().buckets().array();
            if (attrNameBucketList.size() > 0) {
                StringTermsBucket attrNameBucket = attrNameBucketList.get(0);
                attrVo.setAttrName(attrNameBucket.key().stringValue());
            }
            Aggregate attr_value_agg = bucket.aggregations().get("attr_value_agg");
            List<StringTermsBucket> attrValueBucketList = attr_value_agg.sterms().buckets().array();
            if (attrValueBucketList.size() > 0) {
                List<String> attrValueList = attrValueBucketList.stream().map((valueBucket) -> {
                    return valueBucket.key().stringValue();
                }).collect(Collectors.toList());
                attrVo.setAttrValue(attrValueList);
            }
            attrVos.add(attrVo);
        });
        /* 属性信息 */
        result.setAttrs(attrVos);
        /* 分页信息 */
        result.setPageNum(param.getPageNum());
        /* 产品信息 */
        result.setProducts(skus);
        /* 总记录信息 */
        result.setTotal(value);
        /* 总页码信息 */
        /*
         * 计算方式
         * 总记录对于分页大小进行求余
         * 根据计算结果来进行对应的操作
         */
        long pages = value / ElasticConstant.PRODUCT_PAGESIZE;
        long remainder = value % ElasticConstant.PRODUCT_PAGESIZE;
        if (remainder == 0) {
            result.setTotalPages((int) pages);
        } else {
            result.setTotalPages((int) (pages + 1));
        }
        List<Integer> pageNavs = new ArrayList<>();
        /* 总面包屑数 */
        for (int i = 1; i <= pages; i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);
        /* 面包屑导航 */
        /* 属性 */
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
            List<NavVo> navs = param.getAttrs().stream().map((attr) -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] split = attr.split("_");
                navVo.setNavValue(split[1]);
                R r = productFeignService.attrsInfo(Long.parseLong(split[0]));
                result.getAttrIds().add(Long.parseLong(split[0]));
                if (r.getCode() != 0) {
                    log.info("检索服务远程调用Product查询属性失败");
                    navVo.setNavName(split[0]);
                } else {
                    String replace = replaceQueryString(param, attr, "attrId");
                    navVo.setLink("http://search.gulimall.com/list.html?" + replace);
                }
                AttrResponseVo data = (AttrResponseVo) r.getData("attr", new TypeReference<AttrResponseVo>() {
                });
                navVo.setNavName(data.getAttrName());
                return navVo;
            }).collect(Collectors.toList());
            result.setNavs(navs);
        }
        /* 商品 */
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            List<NavVo> navs = new ArrayList<>();
            NavVo navVo = new NavVo();
            navVo.setNavName("品牌");
            /* ToDO 远程查询品牌数据 */
            R r = productFeignService.brandsInfo(param.getBrandId());
            if (r.getCode() == 0) {
                List<BrandVo> brand = (List<BrandVo>) r.getData("brands", new TypeReference<List<BrandVo>>() {
                });
                StringBuffer buffer = new StringBuffer();
                String replace = "";
                for (BrandVo brandVo : brand) {
                    buffer.append(brandVo.getBrandName() + ";");
                    replace = replaceQueryString(param, brandVo.getBrandId() + "", "brandId");
                }
                navVo.setNavValue(buffer.toString());
                navVo.setLink("http://search.gulimall.com/list.html?" + replace);
            }
            navs.add(navVo);
            result.setNavs(navs);
        }
        // TODO 分类面包屑导航,不需要导航取消
        if (param.getCatalog3Id() != null && param.getCatalog3Id() >= 0) {
            List<NavVo> navs = new ArrayList<>();
            NavVo navVo = new NavVo();
            navVo.setNavName("分类");
            R r = productFeignService.categorysInfo(param.getCatalog3Id());
            if (r.getCode() == 0) {
                CatalogVo catalog = (CatalogVo) r.getData("category", new TypeReference<CatalogVo>() {
                });
                navVo.setNavName(catalog.getCatalogName());
                String replace = replaceQueryString(param, param.getCatalog3Id() + "", "catId");
                navVo.setLink("http://search.gulimall.com/list.html?" + replace);
            } else {
                navVo.setNavName("");
            }
            navs.add(navVo);
            result.setNavs(navs);
        }
        return result;
    }

    private String replaceQueryString(SearchParam param, String item, String key) {
        String encode = "";
        try {
            encode = URLEncoder.encode(item, "UTF-8").replace("+", "%20")
                    .replace("%28", "(").replace("%29", ")");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        String replace = param.get_queryString().replace("&" + key + "=" + encode, "");
        return replace;
    }

    /* 准备检索请求 */
    private SearchRequest buildSearchRequest(SearchParam param) throws ElasticsearchException, IOException {
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
                        List<FieldValue> list = brandId.stream().map(id -> {
                            return FieldValue.of(id);
                        }).collect(Collectors.toList());
                        t.field("brandId").terms(terms -> terms.value(list));
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
        /* 构造请求体 */
        SearchRequest request = SearchRequest.of(s -> {
            /* 排序 */
            if (StringUtils.hasText(param.getSort())) {
                String sort = param.getSort();
                String[] split = sort.split("_");
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
            if (StringUtils.hasText(param.getKeyword())) {
                /* 高亮 */
                HighlightField field = HighlightField.of(h -> {
                    h.preTags("<b style='color:red'>").postTags("</b>");
                    return h;
                });
                Highlight highlight = Highlight.of(h -> h.fields("skuTitle", field));
                s.highlight(highlight);
            }
            s.from((param.getPageNum() - 1) * ElasticConstant.PRODUCT_PAGESIZE);
            s.size(ElasticConstant.PRODUCT_PAGESIZE);
            s.query(query);
            /* 聚合搜索 */
            /* 品牌聚合 */
            Aggregation brand_agg = Aggregation.of(a -> {
                TermsAggregation brandIdTerms = TermsAggregation.of(t -> {
                    t.field("brandId").size(50);
                    return t;
                });
                Aggregation brandNameAggregation = Aggregation.of(agg -> {
                    /* 品牌名 */
                    TermsAggregation brandNameTerms = TermsAggregation.of(t -> {
                        t.field("brandName.keyword").size(1);
                        return t;
                    });
                    return agg.terms(brandNameTerms);
                });
                Aggregation brandImgAggregation = Aggregation.of(agg -> {
                    /* 品牌图片 */
                    TermsAggregation brandImgTerms = TermsAggregation.of(t -> {
                        t.field("brandImg.keyword").size(1);
                        return t;
                    });
                    return agg.terms(brandImgTerms);
                });
                /* 品牌名和图片聚合 */
                a.aggregations("brand_name_agg", brandNameAggregation);
                a.aggregations("brand_img_agg", brandImgAggregation);
                a.terms(brandIdTerms);
                return a.terms(brandIdTerms);
            });
            /* 目录聚合 */
            Aggregation catelog_agg = Aggregation.of(a -> {
                TermsAggregation catalogIdTerms = TermsAggregation.of(t -> {
                    /* 目录ID */
                    t.field("catalogId").size(20);
                    return t;
                });
                Aggregation catalogNameAggregation = Aggregation.of(agg -> {
                    /* 目录名 */
                    TermsAggregation catalogNameTerms = TermsAggregation.of(t -> {
                        t.field("catalogName.keyword").size(1);
                        return t;
                    });
                    return agg.terms(catalogNameTerms);
                });
                /* 目录名聚合 */
                a.aggregations("catalog_name_agg", catalogNameAggregation);
                return a.terms(catalogIdTerms);
            });
            /* 属性聚合 */
            Aggregation attr_agg = Aggregation.of(a -> {
                /* 属性字段查询 */
                NestedAggregation nestedAggregation = NestedAggregation.of(n -> {
                    n.path("attrs");
                    return n;
                });
                Aggregation attrIdAggregation = Aggregation.of(agg -> {
                    /* 属性ID */
                    TermsAggregation attrIdTerms = TermsAggregation.of(t -> {
                        /* ID默认是关键字 */
                        t.field("attrs.attrId").size(10);
                        return t;
                    });
                    Aggregation attrNameAggregation = Aggregation.of(aggregation -> {
                        /* 属性名 */
                        TermsAggregation attrNameTerms = TermsAggregation.of(t -> {
                            /* 文本需要显式指明keyword关键字 */
                            t.field("attrs.attrName.keyword").size(1);
                            return t;
                        });
                        return aggregation.terms(attrNameTerms);
                    });
                    Aggregation attrValueAggregation = Aggregation.of(aggregation -> {
                        /* 属性值 */
                        TermsAggregation attrValueTerms = TermsAggregation.of(t -> {
                            t.field("attrs.attrValue.keyword").size(50);
                            return t;
                        });
                        return aggregation.terms(attrValueTerms);
                    });
                    /* 属性名值聚合 */
                    agg.aggregations("attr_name_agg", attrNameAggregation);
                    agg.aggregations("attr_value_agg", attrValueAggregation);
                    return agg.terms(attrIdTerms);
                });
                /* 属性ID聚合 */
                a.aggregations("attr_id_agg", attrIdAggregation);
                return a.nested(nestedAggregation);
            });
            /* 查询属性集合 */
            s.aggregations("brand_agg", brand_agg);
            s.aggregations("catalog_agg", catelog_agg);
            s.aggregations("attr_agg", attr_agg);
            s.index(ElasticConstant.PRODUCT_INDEX);
            return s;
        });
        if (request != null) {
            Gson gson = new Gson();
            String json = gson.toJson(request);
            saveQuery(json);
        }
        return request;
    }

    private void saveQuery(String json) {
        try {
            File queryFile = new File(new ClassPathResource("/").getFile(), "query.json");
            queryFile.setReadOnly();
            try (FileOutputStream outputStream = new FileOutputStream(queryFile.getAbsolutePath() + "/", false)) {
                outputStream.write(json.getBytes());
                outputStream.flush();
            }
            log.info("保存DSL查询文件成功");
            log.info("保存DSL查询文件至:" + queryFile.getCanonicalPath());
        } catch (IOException e) {

            log.error("保存DSL查询文件失败");
            log.error("错误原因:" + e.getMessage());
        }
    }
}