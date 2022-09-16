package com.aze.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.aze")
@Import(PropertiesConfig.class)
public class JdbcTemplateConfig {
    @Value("${jdbc.driverClass}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        //创建数据库连接池对象
        DruidDataSource dds = new DruidDataSource();
        //设置数据源
        dds.setDriverClassName(driverClassName);
        dds.setUrl(url);
        dds.setUsername(username);
        dds.setPassword(password);
        return dds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        //创建jdbc模板对象
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        //设置该模板对象的数据库连接池，从而使jdbc知道要操作哪个数据库
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }
}
