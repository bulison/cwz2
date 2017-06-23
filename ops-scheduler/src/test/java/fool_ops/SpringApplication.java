package fool_ops;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Administrator on 2016/12/21.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"cn.fooltech.fool_ops"})
@EnableTransactionManagement(proxyTargetClass = true)
public class SpringApplication {
}
