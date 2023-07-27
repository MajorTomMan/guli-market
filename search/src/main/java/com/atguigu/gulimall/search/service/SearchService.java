/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-07-24 23:31:30
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-27 23:23:47
 * @FilePath: \Guli\search\src\main\java\com\atguigu\gulimall\search\service\SearchService.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.search.service;

import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResult;

public interface SearchService {
    /* 
     * 根据参数返回检索结果
     */
    SearchResult search(SearchParam param);

}
