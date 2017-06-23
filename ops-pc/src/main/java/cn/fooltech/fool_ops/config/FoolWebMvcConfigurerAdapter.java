package cn.fooltech.fool_ops.config;

import cn.fooltech.fool_ops.component.core.filesystem.FileSystem;
import cn.fooltech.fool_ops.component.core.filesystem.LocalFileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import java.time.format.FormatStyle;
import java.util.concurrent.TimeUnit;

@Configuration
public class FoolWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter 
	implements EmbeddedServletContainerCustomizer{
	
	@Value("${cn.fooltech.fool_ops.filepath}")
	private String filepath;

	/**
	 * 用于处理编码问题
	 * @return
	 */
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.setDateStyle(FormatStyle.FULL);
//            registry.addConverter(new StringToLocalDateConverter("yyyy-MM-dd"));

        registrar.registerFormatters(registry);
    }

    /**
     * 文件系统
     * @return
     */
    @Bean
    public FileSystem fileSystem(){
    	return new LocalFileSystem(filepath);
    }

    @Bean
    public CorsFilter corsFilter() {
       // log.debug("Registering CORS filter");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/v2/api-docs", config);
        return new CorsFilter(source);
    }

    /**
     * 默认view映射
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController( "/login" ).setViewName( "/main/login" );
    }

    /**
     * 默认错误页面
     */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {

        ErrorPage error403Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/403");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");

        container.addErrorPages(error403Page, error404Page, error500Page);
        container.setSessionTimeout(2, TimeUnit.HOURS);
	}

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
}
