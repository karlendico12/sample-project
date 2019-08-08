package com.example.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private TokenStore tokenStore;
    private JwtAccessTokenConverter accessTokenConverter;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JdbcTemplate jdbcTemplate;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private BasicAuthenticationFilter basicAuthenticationFilter;
    private TokenEnhancer tokenEnhancer;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public AuthorizationServerConfig(TokenStore tokenStore,
            JwtAccessTokenConverter accessTokenConverter,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JdbcTemplate jdbcTemplate,
            AuthenticationEntryPoint authenticationEntryPoint,
            BasicAuthenticationFilter basicAuthenticationFilter,
            TokenEnhancer tokenEnhancer){

        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
        this.userDetailsService = userDetailsService;
        this.jdbcTemplate = jdbcTemplate;
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.basicAuthenticationFilter = basicAuthenticationFilter;
        this.tokenEnhancer = tokenEnhancer;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer, accessTokenConverter)
        );
        endpoints.tokenStore(tokenStore)
            .reuseRefreshTokens(false)
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService)
            .tokenEnhancer(tokenEnhancerChain)
        ;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.jdbc(jdbcTemplate.getDataSource());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
            .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')").allowFormAuthenticationForClients();
    }

    @EventListener
    public void authFailedEventListener(AbstractAuthenticationFailureEvent oAuth2AuthenticationFailureEvent){
        StringBuffer url = request.getRequestURL();
        if(url.indexOf("/oauth/token") !=-1? true: false){
            System.out.println("User Oauth2 login Failed");
            System.out.println(oAuth2AuthenticationFailureEvent.getAuthentication().getPrincipal());
            System.out.println(request.getRequestURL());
            System.out.println(request.getHeader("X-Forwarded-For"));
            System.out.println(request.getRemoteAddr());
        }
    }

}
