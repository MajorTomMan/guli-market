package io.renren.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringDocConfig implements WebMvcConfigurer {

    @Bean
    public GroupedOpenApi createRestApi() {
        return GroupedOpenApi.builder()
                .group("renren")
                .addOpenApiCustomiser(openApi -> openApi
                        .info(new Info()
                                .title("人人开源")
                                .description("renren-fast文档")
                                .version("3.0.0")
                                .termsOfService("https://www.renren.io"))
                        .addSecurityItem(new SecurityRequirement().addList("token"))
                        .components(new Components()
                                .addSecuritySchemes("token", new SecurityScheme()
                                        .name("token")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER))))
                .pathsToMatch("/**")
                .packagesToScan("io.renren.controller")
                .build();
    }
}
