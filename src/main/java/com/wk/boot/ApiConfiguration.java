package com.wk.boot;

import com.alibaba.druid.pool.DruidDataSource;
import com.wk.boot.client.ApiRestTemplate;
import com.wk.boot.client.Navigator;
import com.wk.boot.client.RestClient;
import com.wk.boot.service.IUserService;
import com.wk.boot.web.ApiErrorPageRegistrar;
import com.wk.boot.web.UserRealm;
import com.wk.boot.web.filter.ApiFormFilter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaige on 2017/4/7.
 */
@org.springframework.context.annotation.Configuration
public class ApiConfiguration {

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ConfigurationProperties("api.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean
    public ApiRestTemplate restTemplate(@Autowired RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder.build(ApiRestTemplate.class);
    }

    //    @Bean
//    public RestClient restClient(@Autowired ApiRestTemplate apiRestTemplate) {
//
//        return new RestClient(apiRestTemplate);
//    }

    @Bean
    public Navigator navigator(@Autowired ApiRestTemplate restTemplate) {
        return Navigator.instance(restTemplate);
    }

    @Bean
    public Navigator.WebPage defaultWebPage(@Autowired Navigator navigator) {

        return navigator.defaultWebPage();
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ApiErrorPageRegistrar();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {

        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public CredentialsMatcher credentialsMatcher() {

        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(1);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    @Bean
    public UserRealm userRealm(@Autowired IUserService userService, @Autowired CredentialsMatcher credentialsMatcher) {

        return new UserRealm(userService, credentialsMatcher);
    }

    @Bean
    public SecurityManager securityManager(@Autowired UserRealm userRealm) {

        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        webSecurityManager.setRealm(userRealm);

//        SecurityUtils.setSecurityManager(webSecurityManager);
        return webSecurityManager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroBean(@Autowired SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilterFactoryBean.setLoginUrl("/login");

        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc", new ApiFormFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public FilterRegistrationBean shiroFilter() {

        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new DelegatingFilterProxy());
        bean.setName("shiroFilter");

        Map<String, String> initParam = new HashMap<>();
        initParam.put("targetFilterLifecycle", "true");
        bean.setInitParameters(initParam);

        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        bean.setUrlPatterns(urlPatterns);

        return bean;
    }
}
