package com.rrs.common.security.component.authorization;

import com.rrs.common.security.component.authorization.exception.RrsWebResponseExceptionTranslator;
import com.rrs.common.security.service.RedisClientDetailsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.util.Arrays;

/**
 * @author zjm
 */
public class RrsAuthorizationServerConfigurerAdapter extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisClientDetailsService redisClientDetailsService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private TokenEnhancer tokenEnhancer;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Value("${rrs.old-access-token-validity-seconds:20}")
    private int oldAccessTokenValiditySeconds;

    @Override
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        clients.withClientDetails(redisClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.allowFormAuthenticationForClients()
                // .checkTokenAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // ????????????
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                // ???????????????
                .authorizationCodeServices(authorizationCodeServices)
                // ??????token????????????
                .tokenStore(tokenStore)
                // ?????????????????????
                .tokenEnhancer(tokenEnhancer)
                // ????????????????????????
                .userDetailsService(userDetailsService)
                // ?????????????????????
                .authenticationManager(authenticationManager)
                // ?????????????????? refresh_token
                .reuseRefreshTokens(false)
                // RrsTokenServices
                .tokenServices(tokenServices(tokenStore, tokenEnhancer, endpoints))
                // ??????????????????????????????
                .exceptionTranslator(new RrsWebResponseExceptionTranslator());
    }

    private RrsTokenServices tokenServices(TokenStore tokenStore, TokenEnhancer tokenEnhancer,
                                           AuthorizationServerEndpointsConfigurer endpoints) {
        RrsTokenServices tokenServices = new RrsTokenServices(oldAccessTokenValiditySeconds);
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        addUserDetailsService(tokenServices, userDetailsService);

        return tokenServices;
    }

    private void addUserDetailsService(RrsTokenServices tokenServices, UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
            tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
        }
    }
}
