package cn.fooltech.fool_ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationStarted implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationStarted.class);
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		long start = event.getApplicationContext().getStartupDate();
		long current = System.currentTimeMillis();
		long delta = (current - start)/1000;

		System.out.println("==== ops application start completed ====");
		System.out.println("==== ops start over use time:"+ delta +"s     ====");

		//启动推送服务
//		PushService pushService = event.getApplicationContext().getBean(PushService.class);
//		pushService.startServer();
	}
}
