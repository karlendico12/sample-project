package com.example.oauth.security;

import com.example.oauth.model.User;
import com.example.oauth.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Primary
public class CustomTokenEnhancer implements TokenEnhancer {
    private UserRepository userRepository;
    @Autowired
    public CustomTokenEnhancer(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        org.springframework.security.core.userdetails.User userDetail = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User user = userRepository.findByUserName(userDetail.getUsername());

        final Map<String, Object> additionalInfo = new HashMap<>();

        additionalInfo.put("user_id", user.getId());
        additionalInfo.put("user_first_name", user.getFirstName());
        additionalInfo.put("user_last_name", user.getLastName());
        additionalInfo.put("user_email", "test@gmail.com");
        additionalInfo.put("authorities", user.getRoles());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }
}
