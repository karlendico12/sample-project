package com.example.oauth.service.impl;

import com.example.oauth.dto.RoleDto;
import com.example.oauth.dto.UserDto;
import com.example.oauth.model.Role;
import com.example.oauth.model.User;
import com.example.oauth.repo.RoleRepository;
import com.example.oauth.repo.UserRepository;
import com.example.oauth.response.ResponseEntity;
import com.example.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RoleRepository roleRepository;
    private TokenStore tokenStore;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            RoleRepository roleRepository,
            TokenStore tokenStore) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenStore = tokenStore;
    }

    @Override
    public Collection<OAuth2AccessToken> getAccessTokens(String userName, String clientId){
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(clientId, userName);
        return tokens;
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        User user = userRepository.findByUserName(userDto.getUserName());
        if(user != null) {
            throw new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE,
                "User already exists " + userDto.getUserName());
        }
        User newUser = convertToUser(userDto);
        newUser = userRepository.save(newUser);
        UserDto newUserDto = convertToUserDto(newUser);
        return newUserDto;
    }

    private User convertToUser(UserDto userDto){
        User newUser = new User();
        newUser.setUserName(userDto.getUserName());
        newUser.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setRoles(convertToRoles(userDto.getRoles()));
        return newUser;
    }

    private List<Role> convertToRoles(List<RoleDto> roleDtos){
        List<Role> roles = new ArrayList<>();
        for(RoleDto roleDto : roleDtos){
            roles.add(roleRepository.findById(roleDto.getId()).get());
        }
        return roles;
    }

    private UserDto convertToUserDto(User user){
        UserDto newUserDto = new UserDto();
        newUserDto.setUserId(user.getId());
        newUserDto.setFirstName(user.getFirstName());
        newUserDto.setLastName(user.getLastName());
        newUserDto.setUserName(user.getUserName());
        newUserDto.setRoles(convertToRoleDto(user.getRoles()));
        return newUserDto;
    }

    private List<RoleDto> convertToRoleDto(List<Role> roles){
        List<RoleDto> roleDtos = new ArrayList<>();
        for(Role role : roles){
            RoleDto roleDto = new RoleDto();
            roleDto.setId(role.getId());
            roleDto.setDescription(role.getDescription());
            roleDto.setRoleName(role.getRoleName());
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }
}
