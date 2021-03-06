package com.example.oauth.controller;

import com.example.oauth.dto.UserDto;
import com.example.oauth.model.User;
import com.example.oauth.response.ResponseEntity;
import com.example.oauth.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/jwttest")
public class ResourceController {
    private UserService userService;

    @Autowired
    public ResourceController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    @ApiOperation("Test API")
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public String getUsers() {
        return "success";
    }

    @PostMapping("/register")
    @ApiOperation("Register")
    public ResponseEntity registrationProcessing(HttpServletRequest req, @Valid @RequestBody UserDto userDto) {
        UserDto newUserDto = userService.registerUser(userDto);
        return new ResponseEntity(HttpStatus.OK, "Success Register", newUserDto);
    }

    @GetMapping("/getTokensSample")
    @ApiOperation("Register")
    public ResponseEntity getTokens(HttpServletRequest req,  @RequestParam(name = "cliendId") String cliendId,
            @RequestParam(name = "userName") String userName) {
        return new ResponseEntity(HttpStatus.OK, "Success",
            userService.getAccessTokens(userName, cliendId));
    }
}
