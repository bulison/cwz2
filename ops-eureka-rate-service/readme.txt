本模块开发调试方法：

本模块接口全部使用restful风格


1、先启动ops-eureka-server，启动服务成功后，
    打开浏览器，输入http://localhost:7777，可以看到eureka服务界面

2、启动ops-eureka-rate-service，启动服务成功后，
    刷新eureka服务界面，可以看到Instances currently registered with Eureka下面有我们启动注册的RATE-SERVICE服务，
    打开浏览器，输入http://localhost:7888/swagger-ui.html，可以看到swagger界面
    GET /api/rate/saleOrderAnalyze 接口 是一个简单的例子，对应代码RateResource.saleOrderAnalyze，



@HystrixCommand(fallbackMethod = "fallBack")
此注解表示该方法并发量过高或者等待超时的时候会调用fallBack函数



