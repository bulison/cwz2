package cn.fooltech.fool_ops.eureka.rateService;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("cn.fooltech.fool_ops.eureka.rateService.dao")
public class RateServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(RateServiceApplication.class).web(true).run(args);
	}

}
