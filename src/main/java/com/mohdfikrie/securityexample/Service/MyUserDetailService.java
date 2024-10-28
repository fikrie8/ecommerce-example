package com.mohdfikrie.securityexample.Service;

import com.mohdfikrie.securityexample.Model.UserPrincipal;
import com.mohdfikrie.securityexample.Model.Users;
import com.mohdfikrie.securityexample.Repository.UserRepo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyUserDetailService implements UserDetailsService {


    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userRepo.findByUsername(username);
        //log.info("inside MyUSerDetailService. user is :" + user.getUsername());
        if(user == null) {
            log.info("User not found");
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user);
    }
}
