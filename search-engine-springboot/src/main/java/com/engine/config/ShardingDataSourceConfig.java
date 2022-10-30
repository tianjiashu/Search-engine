package com.engine.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariDataSource;

/**
 * sharding 数据源
 */
@Configuration
public class ShardingDataSourceConfig {

    @Value("${spring.shardingsphere.datasource.names}")
    String dbNames;

    @Autowired
    private Environment env;

    private final Map<String, DataSource> dataSourceMap = new HashMap<>();

    @Bean(name = "shardingDateSource")
    public DataSource getDataSource() throws SQLException {
        return buildDataSource();
    }

    private DataSource buildDataSource() throws SQLException {
        initDataSourceMap();
        int dbNums = dbNames.split(",").length - 1;

        // 具体分库分表策略，按什么规则来分
        ShardingRuleConfiguration conf = new ShardingRuleConfiguration();

        // table rule
        TableRuleConfiguration tableRule = new TableRuleConfiguration("data_seg_relation", "master.data_seg_relation_$->{0..99}");

        // key生成规则
        KeyGeneratorConfiguration keyGen = new KeyGeneratorConfiguration("SNOWFLAKE", "id");
        tableRule.setKeyGeneratorConfig(keyGen);

        // 分表策略
        ShardingStrategyConfiguration tableShardingStrategyConfig = new InlineShardingStrategyConfiguration("seg_id", "data_seg_relation_$->{seg_id % 100}");
        tableRule.setTableShardingStrategyConfig(tableShardingStrategyConfig);



        conf.setTableRuleConfigs(Lists.newArrayList(tableRule));
        Properties props = new Properties();
        props.put("sql.show", true);

        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, conf, props);
        return dataSource;
    }

    public void initDataSourceMap() {

        System.out.println(dbNames);

        for (String name : dbNames.split(",")) {
            HikariDataSource ds = new HikariDataSource();
            ds.setDriverClassName(env.getProperty("spring.shardingsphere.datasource." + name + ".driver-class-name"));
            ds.setJdbcUrl(env.getProperty("spring.shardingsphere.datasource." + name + ".url"));
            ds.setUsername(env.getProperty("spring.shardingsphere.datasource." + name + ".username"));
            ds.setPassword(env.getProperty("spring.shardingsphere.datasource." + name + ".password"));

            dataSourceMap.put(name, ds);
        }

        System.out.println(dataSourceMap);
    }

}
