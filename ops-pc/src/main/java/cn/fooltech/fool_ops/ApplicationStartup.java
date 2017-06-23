package cn.fooltech.fool_ops;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

public class ApplicationStartup implements ApplicationListener<ApplicationStartingEvent> {
	
	//private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
	
	@Override
	public void onApplicationEvent(ApplicationStartingEvent event) {
		System.out.println("==== ops application now beginning ====");
	}


}
