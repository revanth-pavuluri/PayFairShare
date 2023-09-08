package com.payfairshare.app.security;


import com.fasterxml.jackson.annotation.JsonTypeInfo.None;
import com.payfairshare.app.configuration.URLConstants;
import com.payfairshare.app.dto.UserLoginDto;
import com.payfairshare.app.exception.CustomErrorResponse;
import com.payfairshare.app.service.MyUserDetailsService;
import com.payfairshare.app.service.ObjectMapperService;
import com.payfairshare.app.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@CrossOrigin(origins = "http://*")
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final RequestMatcher apiRequestMatcher = new AntPathRequestMatcher(URLConstants.ALL_URLS);
  private final RequestMatcher errorRequestMatcher = new AntPathRequestMatcher(URLConstants.ERROR);
  
  public JwtAuthenticationFilter(String string){
    super(string);
  }

  public MyUserDetailsService userDetailsService;
	public JwtUtil jwtTokenUtil;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException, IOException {
    Authentication auth = null;
      try{
      String jwtCookie = getJWTCookieValue(request, "LoginToken");
      System.out.println("attempt for authentication");
      System.out.println("Cookie value : "+jwtCookie);
      if (jwtCookie != null) {
        System.out.println("JWT Cookie found. processing it for authentication");
        String username = jwtTokenUtil.extractUsername(jwtCookie);
        if(username != null) {
          System.out.println("Username found");
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          if (jwtTokenUtil.validateToken(jwtCookie, userDetails)) {
              auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
              System.out.println("Authenticated");
          }
        }else{
            throw new BadCredentialsException("Invalid token.");
        }
      } else if(SecurityContextHolder.getContext().getAuthentication() == null){

        UserLoginDto req = getPostData(request);
        if(req != null){
          if(req.getUsername() != null && req.getPassword()!=null){
          System.out.println("requested to login:"+req.getUsername());
        
         auth = this.getAuthenticationManager().authenticate(
              new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
             );
            UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());
            System.out.println("Login successful");
            final String jwt = jwtTokenUtil.generateToken(userDetails);
            Cookie cookie = new Cookie("LoginToken", jwt);
            cookie.setPath("/");
            response.addCookie(cookie);
            //ResponseCookie cookie = ResponseCookie.from("LoginToken", jwt).secure(true).path("/").sameSite("None").build();
            //response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            System.out.println("Token sent");
        }
        }else{
            throw new BadCredentialsException("Provide credentials for login");
      }
      }
      if (auth == null) {
        throw new BadCredentialsException("Authentication failed");
      }

    }catch (ExpiredJwtException e){
        System.out.println("Authentication token is expired !");
        Cookie loginCookie = new Cookie("LoginToken","");
        loginCookie.setMaxAge(0);
        response.addCookie(loginCookie);
        sendErrorResponse(response,"Authentication token is expired !");
    }
    catch(ResponseStatusException e){
      throw new ResponseStatusException(e.getStatus(), e.getReason());
    }
  catch(UsernameNotFoundException e){
    System.out.println(e.getMessage());
    sendErrorResponse(response, "Username not found");
  }
  catch (BadCredentialsException e) {
    System.out.println("Bad credentials : " +e.getMessage());
    sendErrorResponse(response,e.getMessage());

  }
  catch(Exception e){
    sendErrorResponse(response,e.getMessage());
  }
  return auth;
}

  @Override
  protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
    System.out.println(request.getRequestURL().toString());
    RequestMatcher register = new AntPathRequestMatcher("/user/register");
    if(register.matches(request)){
      return false;
    }
    
    System.out.println("Requested : "+request.getRequestURL().toString()+" : "+(super.requiresAuthentication(request, response)
      && isAPIRequest(request) && !isErrorRequest(request)));
    return super.requiresAuthentication(request, response)
      && isAPIRequest(request) && !isErrorRequest(request);
  }

  private boolean isAPIRequest(HttpServletRequest request) {
    return apiRequestMatcher.matches(request);
  }
  private boolean isErrorRequest(HttpServletRequest request) {
    return errorRequestMatcher.matches(request);
  }

  private String getJWTCookieValue(HttpServletRequest request, String name) {
    String cookieValue = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          cookieValue = cookie.getValue();
        }
      }
    }
    final String authorizationHeader = request.getHeader("Login");
    if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
        cookieValue = authorizationHeader.substring(7);
    }
    return cookieValue;
  }

  private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
    CustomErrorResponse res = new CustomErrorResponse(401, errorMessage, "/");
    String resp = new ObjectMapperService().convertToJson(res);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.getWriter().write(resp);
}

  private UserLoginDto getPostData(HttpServletRequest request) throws IOException, BadCredentialsException {
    BufferedReader reader = request.getReader();
    StringBuilder sb = new StringBuilder();
    String line = reader.readLine();
    while (line != null) {
      sb.append(line + "\n");
      line = reader.readLine();
    }
    reader.close();

    return new ObjectMapperService().parseJson(sb.toString(), UserLoginDto.class);
  }

  
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {

    if (authResult != null) {
      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(authResult);
      SecurityContextHolder.setContext(context);
      //response.setStatus(HttpStatus.ACCEPTED.value());
    }

        chain.doFilter(request, response);

  }
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
	}


}
