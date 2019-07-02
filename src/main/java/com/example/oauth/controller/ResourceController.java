package com.example.oauth.controller;

import com.example.oauth.model.User;
import com.example.oauth.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/jwttest")
public class ResourceController {
    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/cities")
    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
    public String getUser() {
        return "getUser()";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public String getUsers() {
        return "karlpogi";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User registrationProcessing(HttpServletRequest req, @Valid @RequestBody User user) {
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setUserName(user.getUserName());

        String encodedPassword = new BCryptPasswordEncoder().encode("XY7kmzoNzl100");
        newUser.setPassword(encodedPassword);
        System.out.println(encodedPassword);

        return newUser;


    }

}
