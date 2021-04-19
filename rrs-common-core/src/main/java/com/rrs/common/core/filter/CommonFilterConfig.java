package com.rrs.common.core.filter;

import com.rrs.common.core.xss.XssFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * 通用过滤器配置
 * rrs.common-filter.enabled=true 或者 未配置 都会注入
 *
 * @author zjm
 * @date 2020年3月24日
 */
@Configuration
@ConditionalOnProperty(name = "rrs.common-filter.enabled", havingValue = "true", matchIfMissing = true)
public class CommonFilterConfig {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }
}
