package com.example.oauth.service;

import com.example.oauth.dto.UserDto;
import com.example.oauth.model.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Collection;

public interface UserService {
    UserDto registerUser(UserDto userDto);
    Collection<OAuth2AccessToken> getAccessTokens(String userName, String clientId);
}
