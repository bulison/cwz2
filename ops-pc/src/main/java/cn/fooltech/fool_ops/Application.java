package cn.fooltech.fool_ops;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableDiscoveryClient
@ImportResource(locations={"classpath:flowTag-context.xml"})
@MapperScan("cn.fooltech.fool_ops.domain.*.dao")
public class Application {
    public static void main( String[] args ){
    	SpringApplication springApplication =new SpringApplication(Application.class);
    	springApplication.addListeners(
    			new ApplicationStartup(),
				new ApplicationStarted());
		springApplication.run(args);
    }
}
