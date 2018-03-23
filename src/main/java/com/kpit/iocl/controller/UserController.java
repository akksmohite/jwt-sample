package com.kpit.iocl.controller;

import java.util.HashSet;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kpit.iocl.domain.ApplicationUser;
import com.kpit.iocl.domain.Authority;
import com.kpit.iocl.repository.ApplicationUserRepository;
import com.kpit.iocl.repository.AuthorityRepository;
import com.kpit.iocl.utils.AuthoritiesConstants;




@RestController
@RequestMapping("/users")
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AuthorityRepository authorityRepository;
    
    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody ApplicationUser user) {
    	Set<Authority> authorities = new HashSet<>();
    	Authority authority = null;
    	if (user.getUsername().equalsIgnoreCase("admin")) {
    		authority = authorityRepository.findByName(AuthoritiesConstants.ADMIN);
    		authorities.add(authority);
    	}
    	authority = authorityRepository.findByName(AuthoritiesConstants.USER);
    	authorities.add(authority);
    	user.setAuthorities(authorities);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user = applicationUserRepository.save(user);
    }
}