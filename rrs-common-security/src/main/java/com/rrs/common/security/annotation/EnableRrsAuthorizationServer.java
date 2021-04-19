package com.rrs.common.security.annotation;

import com.rrs.common.security.component.authorization.RrsAuthorizationServerAutoConfiguration;
import com.rrs.common.security.component.authorization.RrsAuthorizationServerBeanDefinitionRegistrar;
import com.rrs.common.security.component.authorization.RrsSecurityConfig;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.lang.annotation.*;

/**
 * @author zjm
 * @date 2020年7月18日
 */
@Documented
@Inherited
@EnableAuthorizationServer
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RrsAuthorizationServerAutoConfiguration.class, RrsSecurityConfig.class,
        RrsAuthorizationServerBeanDefinitionRegistrar.class})
public @interface EnableRrsAuthorizationServer {
}
