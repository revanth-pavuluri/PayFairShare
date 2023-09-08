package com.payfairshare.app.controller;


import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.payfairshare.app.dto.UserResponseDto;
import com.payfairshare.app.mapper.UserMapper;
import com.payfairshare.app.repository.UserRepository;
import com.payfairshare.app.service.MyUserDetailsService;
import com.payfairshare.app.util.JwtUtil;


@RestController
public class LoginController {
	
	@Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserMapper userMapper;


	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserResponseDto Userdata(HttpServletResponse response) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		User user = (User) securityContext.getAuthentication().getPrincipal();
        UserResponseDto dto = userMapper.userToUserResponseDto(userRepository.findByUsername(user.getUsername()));
        return dto;
}
}
