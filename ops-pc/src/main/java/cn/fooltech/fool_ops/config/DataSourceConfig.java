package cn.fooltech.fool_ops.config;

import javax.sql.DataSource;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaRepositoryImpl;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.fooltech.fool_ops.component.core.FoolRepositoryFactoryBean;

@Configuration
@EnableJpaRepositories(basePackages="cn.fooltech.fool_ops.domain.*",
        repositoryFactoryBeanClass=FoolRepositoryFactoryBean.class,
        repositoryBaseClass = FoolJpaRepositoryImpl.class
)
@EnableTransactionManagement(proxyTargetClass=true,mode=AdviceMode.PROXY)
public class DataSourceConfig {

    @Bean(name = "dataSource")
    @Qualifier("dataSource")
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

//    @Bean
//    @ConfigurationProperties(prefix="mybatis")
//    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws Exception {
//        Interceptor[] interceptors = {pageHelperPlugins()};
//
//        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//        sessionFactory.setPlugins(interceptors);
//        return sessionFactory;
//    }
//
//    @Bean
//    public Interceptor pageHelperPlugins(){
//        return new PageHelper();
//    }

}
