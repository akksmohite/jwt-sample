package com.kpit.iocl.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kpit.iocl.domain.ApplicationUser;
import com.kpit.iocl.repository.ApplicationUserRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private ApplicationUserRepository applicationUserRepository;

    public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	System.out.println("--------------------------");
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        //return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
        return createSpringSecurityUser(applicationUser.getUsername(), applicationUser);
    }
    
    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, ApplicationUser user) {
       /* if (!user.getActivated()) {
            //throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }*/
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());
        System.out.println("==========================================");
        System.out.println("USER " +user.toString());
        System.out.println("@@@@@@@@@@@@@@  "+ new org.springframework.security.core.userdetails.User(user.getUsername(),
            user.getPassword(),
            grantedAuthorities));
        System.out.println("==========================================");
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
            user.getPassword(),
            grantedAuthorities);
    }
}