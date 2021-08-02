package com.eth.filecoin.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author catface
 * @date 2018/3/6
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("eth.filecoin")
        .apiInfo(apiInfo())
        .select()
        // 扫描的包所在位置
        .apis(RequestHandlerSelectors.basePackage("com.eth.filecoin"))
        // 扫描的 URL 规则
        .paths(PathSelectors.any())
        .build();
  }
//
//  @Bean
//  public Docket createRestApi100() {
//    return new Docket(DocumentationType.SWAGGER_2)
////        .groupName(ApiVersionConst.VERSION_1_0_0)
//        .apiInfo(apiInfo())
//        .select()
//        // 扫描的包所在位置
//        .apis(input -> {
//          assert input != null;
//          Optional<ApiVersion> optional = input.findAnnotation(ApiVersion.class);
//          return optional.isPresent()
//              && Arrays.asList(optional.get().group()).contains(ApiVersionConst.VERSION_1_0_0);
//        })
//        // 扫描的 URL 规则
//        .paths(PathSelectors.any())
//        .build();
//  }

  private ApiInfo apiInfo() {
    // 联系信息
    return new ApiInfoBuilder()
        // 大标题
        .title("filecoin")
        // 描述
//        .description("jxmc科技ballclub")
        // 版本
        .version("0.0.1-SNAPSHOT")
        .build();
  }
}
