package cn.fooltech.fool_ops.analysis;
import cn.fooltech.mybatis.pagehelper.MybatisPageHelperAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@Configuration
@ComponentScan(basePackages = {"cn.fooltech.fool_ops"})
@EnableAutoConfiguration
//@Import(
//        {
//                AopAutoConfiguration.class,
//                DataSourceAutoConfiguration.class,
//                DataSourcePoolMetadataProvidersConfiguration.class,
//                DispatcherServletAutoConfiguration.class,
//                EmbeddedServletContainerAutoConfiguration.class,
//                ErrorMvcAutoConfiguration.class,
////                GenericCacheConfiguration.class,
//                HibernateJpaAutoConfiguration.class,
//                HttpEncodingAutoConfiguration.class,
//                HttpMessageConvertersAutoConfiguration.class,
//                JacksonAutoConfiguration.class,
////                JacksonHttpMessageConvertersConfiguration.class,
//                JdbcTemplateAutoConfiguration.class,
//                JmxAutoConfiguration.class,
//                JpaBaseConfiguration.class,
//                JpaRepositoriesAutoConfiguration.class,
//                JtaAutoConfiguration.class,
//                MultipartAutoConfiguration.class,
//                MybatisAutoConfiguration.class,
////                NoOpCacheConfiguration.class,
//                PersistenceExceptionTranslationAutoConfiguration.class,
//                PropertyPlaceholderAutoConfiguration.class,
////                RedisCacheConfiguration.class,
//                ServerPropertiesAutoConfiguration.class,
////                SimpleCacheConfiguration.class,
//                SpringDataWebAutoConfiguration.class,
//                TransactionAutoConfiguration.class,
//                WebClientAutoConfiguration.class,
//                WebMvcAutoConfiguration.class,
//                 MybatisPageHelperAutoConfiguration.class,
//                PropertyPlaceholderAutoConfiguration.class,
//
////                WebSocketAutoConfiguration.class
//        }
//)
public class SimpleApplication {

    public static void main(String[] args) {    	
        SpringApplication.run(SimpleApplication.class, args);
    }
}
