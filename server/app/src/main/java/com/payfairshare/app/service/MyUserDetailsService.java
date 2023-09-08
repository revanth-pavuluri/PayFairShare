package com.payfairshare.app.service;



import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.payfairshare.app.models.User;
import com.payfairshare.app.repository.UserRepository;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
  

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password."); 
        }else{
            org.springframework.security.core.userdetails.User springSecurityUser = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),mapRolesToAuthorities("STUDENT"));
            return springSecurityUser;
        }
    }
      private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String roles){
		List<GrantedAuthority> rol = Arrays.stream(roles.split(","))
		.map(SimpleGrantedAuthority::new)
		.collect(Collectors.toList());
		return rol;
	}
}