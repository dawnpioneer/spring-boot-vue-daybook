package com.nagino.daybook.controller;

import com.nagino.daybook.security.service.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class BaseController {

    public UserDetailsImpl getCurrentUser() {
        UserDetailsImpl customer = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customer;
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

}
