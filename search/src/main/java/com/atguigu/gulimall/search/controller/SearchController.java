package com.atguigu.gulimall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.atguigu.gulimall.search.service.SearchService;
import com.atguigu.gulimall.search.vo.SearchParam;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @GetMapping("/list.html")
    public String listPage(SearchParam param){
        Object result=searchService.search(param);
        return "list";
    }
}
