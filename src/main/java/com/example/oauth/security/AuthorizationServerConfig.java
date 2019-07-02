package com.example.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private TokenStore tokenStore;
    private JwtAccessTokenConverter accessTokenConverter;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorizationServerConfig(TokenStore tokenStore,
            JwtAccessTokenConverter accessTokenConverter,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JdbcTemplate jdbcTemplate){
        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
        this.userDetailsService = userDetailsService;
        this.jdbcTemplate = jdbcTemplate;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
            .reuseRefreshTokens(false)
            .accessTokenConverter(accessTokenConverter)
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService)
        ;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.jdbc(jdbcTemplate.getDataSource());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
            .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
    }

}
