package org.example.service;

import org.example.model.UserPrincipal;
import org.example.model.Users;
import org.example.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService  implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = repo.findByUsername(username);
        if(user == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("user not found");
        }
        System.out.println("load user"+user+"here is the obj"+new UserPrincipal(user));
        return new UserPrincipal(user);
    }
}
