package com.appsdeveloperblog.ws.api.controller;

import com.appsdeveloperblog.ws.api.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    Environment env;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port: " + env.getProperty("local.server.port");
    }

    @PreAuthorize("hasRole('developer') or #id == #jwt.subject")
    //@Secured("ROLE_developer")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return "Disabled user with id " + id + " and JWT subject " + jwt.getSubject();
    }

    @PostAuthorize("returnObject.id == #jwt.subject")
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return new UserDto("23d091e8-3ba9-4877-bcc2-5cb0b0f7d94e", "Jonh", "Wall");
    }
}