/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-07-27 22:35:14
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-08-25 23:48:39
 * @FilePath: \Guli\search\src\main\java\com\atguigu\gulimall\search\vo\SearchResult.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
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
    private List<NavVo> navs;
    @Data
    public static class NavVo{
        private String navName;
        private String navValue;
        private String link;
        
    }
    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;
    }
    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
