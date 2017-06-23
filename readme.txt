模块说明
====================================================================
ops-parent                              父POM文件，负责引入依赖
ops-common                              通用模块
ops-domain                              实体类、vo类模块
ops-biz                                 业务模块

=============A分割线=================================================

ops-pc                                  网页端

=============B分割线=================================================

ops-mobile                              手机端接口服务
ops-eureka-rate-service                 收益率服务注册
ops-eureka-server                       服务发现
ops-scheduler                           定时服务（暂时无用）
ops-eureka-ribbon-rate-consumer         收益率消费者（暂时无用）
ops-eureka-router-zuul                  路由器（暂时无用）


每次更新代码后，运行ops下面的install.bat即可以打包依赖模块代码
