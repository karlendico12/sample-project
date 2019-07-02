package com.example.oauth.service;

import com.example.oauth.dto.UserDto;
import com.example.oauth.model.User;

public interface UserService {
    User registerUser(UserDto userDto);
}
