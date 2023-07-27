/*
 * @Author: MajorTomMan 765719516@qq.com
 * @Date: 2023-07-24 23:17:42
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2023-07-27 23:24:18
 * @FilePath: \Guli\search\src\main\java\com\atguigu\gulimall\search\controller\SearchController.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.atguigu.gulimall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.atguigu.gulimall.search.service.SearchService;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResult;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @GetMapping("/list.html")
    public String listPage(SearchParam param,Model model){
        SearchResult result=searchService.search(param);
        model.addAttribute("result", result);
        return "list";
    }
}
