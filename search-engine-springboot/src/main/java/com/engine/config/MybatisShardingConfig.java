package com.engine.config;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


/**
 * mybatis sharding配置
 */
@Configuration
@MapperScan(basePackages = "com.engine.shardingMapper", sqlSessionTemplateRef = "shardingSqlSessionTemplate",sqlSessionFactoryRef ="shardingSqlSessionFactory" )
public class MybatisShardingConfig {

    // sharding数据源
    @Bean("shardingSqlSessionFactory")
    public SqlSessionFactory ds1SqlSessionFactory(@Qualifier("shardingDateSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com.engine.shardingMapper/*.xml"));
        return sqlSessionFactory.getObject();
    }

    @Bean(name = "shardingTransactionManager")
    public DataSourceTransactionManager ds1TransactionManager(@Qualifier("shardingDateSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "shardingSqlSessionTemplate")
    public SqlSessionTemplate ds1SqlSessionTemplate(@Qualifier("shardingSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}

