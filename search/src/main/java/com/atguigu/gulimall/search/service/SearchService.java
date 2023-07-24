package com.atguigu.gulimall.search.service;

import com.atguigu.gulimall.search.vo.SearchParam;

public interface SearchService {
    /* 
     * 根据参数返回检索结果
     */
    Object search(SearchParam param);

}
