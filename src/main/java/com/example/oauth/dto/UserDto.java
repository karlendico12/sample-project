package com.example.oauth.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<RoleDto> roles;

}
