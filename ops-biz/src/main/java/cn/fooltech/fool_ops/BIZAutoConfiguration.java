package cn.fooltech.fool_ops;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(
        basePackages = {
                "cn.fooltech.fool_ops.domain.*.service",
                "cn.fooltech.fool_ops.domain.*.repository",
                "cn.fooltech.fool_ops.domain.message.template.service",
                "cn.fooltech.fool_ops.domain.message.template.repository",
                "cn.fooltech.fool_ops.component"
        }
)
public class BIZAutoConfiguration {

}
