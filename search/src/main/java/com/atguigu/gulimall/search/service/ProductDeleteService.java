package com.atguigu.gulimall.search.service;

import java.io.IOException;
import java.util.List;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;

public interface ProductDeleteService {

    void deleteById(List<Long> skuIds) throws ElasticsearchException, IOException;
    
}
