package com.rrs.common.security.component.resource;

import com.rrs.common.core.exception.BaseException;
import com.rrs.common.core.util.CommonUtil;
import com.rrs.common.core.util.SpringContextUtils;
import com.rrs.common.security.util.AuthorizeRequestUtil;
import com.rrs.common.security.annotation.AnonymousAccess;
import com.rrs.common.security.annotation.Inner;
import com.rrs.common.security.component.RrsAuthenticationEntryPointImpl;
import com.rrs.common.security.component.RrsUserAuthenticationConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.Filter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zjm
 * @date
 */
@Slf4j
public class RrsResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {

    @Autowired
    protected RrsAuthenticationEntryPointImpl authenticationEntryPointImpl;

    @Autowired
    private RrsResourceProperties rrsResourceProperties;

    @Autowired(required = false)
    private RestTemplate lbRestTemplate;

    @Autowired(required = false)
    private RemoteTokenServices remoteTokenServices;

    @Override
    @SneakyThrows
    public void configure(HttpSecurity httpSecurity) {
        // 搜寻匿名标记 url： @AnonymousAccess
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =
                SpringContextUtils.getApplicationContext().getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        Set<String> innerUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (null != anonymousAccess) {
                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
            if (rrsResourceProperties.getInner()) {
                Inner inner = handlerMethod.getMethodAnnotation(Inner.class);
                if (null != inner) {
                    innerUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                }
            }
        }

        httpSecurity.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                httpSecurity.authorizeRequests();
        rrsResourceProperties.getIgnoreUrls().forEach(url -> registry.antMatchers(url).permitAll());
        anonymousUrls.forEach(url -> registry.antMatchers(url).permitAll());
        innerUrls.forEach(url -> registry.antMatchers(url).permitAll());
        List<AuthorizeRequest> authorizeRequests = rrsResourceProperties.getAuthorizeRequests();
        AuthorizeRequestUtil.resolveAuthorizeRequest(registry, authorizeRequests);
        registry.and().csrf().disable();

        if (CommonUtil.isNotEmptyObject(rrsResourceProperties.getAddFilterBeforeUsernamePasswordAuthenticationFilter())) {
            List<String> addFilterBeforeUsernamePasswordAuthenticationFilter =
                    rrsResourceProperties.getAddFilterBeforeUsernamePasswordAuthenticationFilter();
            addFilterBeforeUsernamePasswordAuthenticationFilter.forEach(item -> {
                try {
                    Filter filter = (Filter) SpringContextUtils.getBean(Class.forName(item));
                    registry.and().addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
                } catch (Exception e) {
                    throw new BaseException("设置过滤器失败");
                }
            });
        }
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(authenticationEntryPointImpl);
        if (remoteTokenServices != null) {
            DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
            UserAuthenticationConverter userTokenConverter = new RrsUserAuthenticationConverter();
            accessTokenConverter.setUserTokenConverter(userTokenConverter);
            if (lbRestTemplate != null) {
                remoteTokenServices.setRestTemplate(lbRestTemplate);
            }
            remoteTokenServices.setAccessTokenConverter(accessTokenConverter);
            resources.tokenServices(remoteTokenServices);
        }
    }

}
