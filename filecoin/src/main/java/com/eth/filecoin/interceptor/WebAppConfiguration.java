//package com.eth.filecoin.interceptor;
//
//
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.support.config.FastJsonConfig;
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.servlet.ViewResolver;
//import org.springframework.web.servlet.config.annotation.*;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class WebAppConfiguration implements WebMvcConfigurer {
//
//    @Autowired
//    private AuthorizationInterceptor authorizationInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 自定义拦截器，添加拦截路径和排除拦截路径
//        registry.addInterceptor(authorizationInterceptor)
//                .addPathPatterns("/api/oms/filecoin/**")
//                .excludePathPatterns("/doc.html");
//    }
//
//    @Bean
//    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
//        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = new ObjectMapper();
////        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
////        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//
//        // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
//        // Include.Include.ALWAYS 默认
//        // Include.NON_DEFAULT 属性为默认值不序列化
//        // Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量
//        // Include.NON_NULL 属性为NULL 不序列化
//        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//        // 字段保留，将null值转为""
//        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//            @Override
//            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
//                jsonGenerator.writeString("");
//            }
//        });
//        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
//
//        return mappingJackson2HttpMessageConverter;
//    }
//
//
//    //配置解析器
//    @Bean
//    public ViewResolver viewResolver() {
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setPrefix("/**");
//        resolver.setExposeContextBeansAsAttributes(true);
//        return resolver;
//    }
//
//
////    @Override
////    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
////        //配置静态资源处理
////        configurer.enable();
////    }
//
//    @Override
//    //fastjson 配置
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        //调用父类的配置
////        super.configureMessageConverters(converters);
//        //创建fastJson消息转换器
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        //创建配置类
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        //修改配置返回内容的过滤
//        fastJsonConfig.setSerializerFeatures(
//                SerializerFeature.DisableCircularReferenceDetect,
//                SerializerFeature.WriteMapNullValue,
//                SerializerFeature.WriteNullStringAsEmpty
//        );
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//        //处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        fastConverter.setSupportedMediaTypes(fastMediaTypes);
//        converters.add(fastConverter);
//        //将fastjson添加到视图消息转换器列表内
//        converters.add(fastConverter);
//    }
//
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
//
//
//    private CorsConfiguration buildConfig() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//        return corsConfiguration;
//    }
//
//    /**
//     * 跨域过滤器
//     *
//     * @return
//     */
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", buildConfig()); // 4
//        return new CorsFilter(source);
//    }
//
//
//}
