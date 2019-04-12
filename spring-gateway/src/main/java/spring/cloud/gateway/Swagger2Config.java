package spring.cloud.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Date: 2019/4/10 17:16
 * @Author: huwl
 * @Description:
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
              .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
              .title("说明文档")
              .description("接口说明文档")
              .termsOfServiceUrl("")
              .contact(new Contact("杨秀峰","franky.yang@foxmail.com","franky.yang@foxmail.com"))
              .version("1.0")
              .build();
    }
}