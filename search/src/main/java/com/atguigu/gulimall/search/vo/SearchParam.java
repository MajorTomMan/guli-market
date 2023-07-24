/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-07-24 23:30:08
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-24 23:38:34
 * @FilePath: \Guli\search\src\main\java\com\atguigu\gulimall\search\vo\SearchParam.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.search.vo;

import java.util.List;

import lombok.Data;

@Data
public class SearchParam {
/**
     * 页面传递过来的全文匹配关键字
     */
    private String keyword;

    /**
     * 品牌id,可以多选
     */
    private List<Long> brandId;

    /**
     * 三级分类id
     */
    private Long catalog3Id;

    /**
     * 排序条件：sort=price/salecount/hotscore_desc/asc
     */
    private String sort;

    /**
     * 是否显示有货
     */
    private Integer hasStock;

    /**
     * 价格区间查询
     */
    private String skuPrice;

    /**
     * 按照属性进行筛选
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 原生的所有查询条件
     */
    private String _queryString;

}
