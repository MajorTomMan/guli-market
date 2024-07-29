package io.renren.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClients;

/**
 * MongoDB Configuration Class
 */
//@Configuration
@ConfigurationProperties(prefix = "mongodb")
public class MongoConfig {

    private String uri; // 连接字符串

    // MongoTemplate 是 Spring Data MongoDB 提供的用于操作 MongoDB 的工具类
    @Bean
    public MongoTemplate mongoTemplate() {
        if (uri != null) {
            return new MongoTemplate(MongoClients.create(uri), getDatabaseName());
        }
        return new MongoTemplate(MongoClients.create(),"fast");
    }

    // 从连接 URI 中提取数据库名称
    private String getDatabaseName() {
        if (uri != null && uri.contains("/")) {
            return uri.substring(uri.lastIndexOf("/") + 1);
        }
        return null;
    }

    // Getter 和 Setter
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
