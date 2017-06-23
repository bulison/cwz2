package cn.fooltech.fool_ops.config;

import cn.fooltech.fool_ops.component.core.filesystem.FileSystem;
import cn.fooltech.fool_ops.component.core.filesystem.LocalFileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;

@Configuration
public class FoolWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter
        implements EmbeddedServletContainerCustomizer {

    @Value("${cn.fooltech.fool_ops.filepath}")
    private String filepath;

    @Value("${spring.application.name}")
    private String applicationName = "ops-m";


    /**
     * 用于处理编码问题
     *
     * @return
     */
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }


    @Bean
    public CorsFilter corsFilter() {
//        log.debug("Registering CORS filter");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/v2/api-docs", config);
        source.registerCorsConfiguration("/oauth/**", config);
        return new CorsFilter(source);
    }

    /**
     * 文件系统
     *
     * @return
     */
    @Bean
    public FileSystem fileSystem() {
        return new LocalFileSystem(filepath);
    }


    /**
     * 默认view映射
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/main/login");
    }

    /**
     * 默认错误页面
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
//        ErrorPage error403Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/403");
//        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
//        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");
//
//        container.addErrorPages(error403Page, error404Page, error500Page);
        container.setContextPath("/" + applicationName);
        container.setSessionTimeout(60 * 60 * 30 * 12);//默认30分钟
    }

//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
}
