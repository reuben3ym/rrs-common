package com.rrs.common.security.annotation;

import com.rrs.common.security.component.resource.RrsResourceServerAutoConfiguration;
import com.rrs.common.security.component.resource.RrsResourceServerBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * @author zjm
 * @date 2020年7月18日
 */
@Documented
@Inherited
@EnableResourceServer
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({RrsResourceServerAutoConfiguration.class, RrsResourceServerBeanDefinitionRegistrar.class})
public @interface EnableRrsResourceServer {
}
