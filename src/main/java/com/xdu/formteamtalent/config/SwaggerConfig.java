package com.xdu.formteamtalent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author akyna
 * @date 04/12 012 11:44 AM
 */
@Configuration
@EnableOpenApi
@EnableWebMvc
public class SwaggerConfig {
    @Value("${swagger.enable}")
    private boolean enableSwagger;
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xdu.formteamtalent.controller"))
                .paths(PathSelectors.any())
                .build()
                .enable(enableSwagger);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("组队达人项目 API")
                .description("通过 Swagger3 构建的 API")
                .version("1.0.0")
                .license("GPL-3.0 license")
                .licenseUrl("https://www.gnu.org/licenses/gpl-3.0.html")
                .contact(new Contact("akynazh", "https://github.com/akynazh/form-team-talent", "akynazh@qq.com"))
                .build();
    }
}
