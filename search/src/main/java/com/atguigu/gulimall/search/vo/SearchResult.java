package com.atguigu.gulimall.search.vo;

import java.util.List;

import com.atguigu.gulimall.common.to.es.SkuEsModel;

import lombok.Data;

@Data
public class SearchResult {
    private List<SkuEsModel> products;
    private Integer pageNum;
    private Long total;
    private Integer totalPages;
    /* 返回给页面的所有信息 */
    private List<BrandVo> brands; // 品牌
    private List<CatalogVo> catalogs; //  
    private List<AttrVo> attrs; // 属性
    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    @Data
    private static class CatalogVo{
        private Long catalogId;
        private String catalogName;
    }
    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> brandImg;
    }
}
