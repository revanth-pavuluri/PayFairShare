package com.payfairshare.app.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.payfairshare.app.security.JwtAuthenticationFilter;
import com.payfairshare.app.service.MyUserDetailsService;
import com.payfairshare.app.util.JwtUtil;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @Lazy}))
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
  private final MyUserDetailsService authUserService;
  
  private final JwtUtil jwtTokenUtil;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
     return new RestAuthenticationEntryPoint();
  }

  @Bean 
  public AuthenticationProvider authProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(authUserService);
    provider.setPasswordEncoder(bCryptPasswordEncoder());
    return provider;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


  public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter("/**/*");
    filter.setAuthenticationManager(super.authenticationManagerBean());
    filter.userDetailsService = authUserService;
    filter.jwtTokenUtil = jwtTokenUtil;
    return filter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

      configureFilters(
            configureExceptionHandling(
              configureUrlAuthorizations(
                configureCsrf(
                  configureCors(
                    basicConfig(http)
                  )
                )
              )
            )
      );
  }

  private HttpSecurity basicConfig(HttpSecurity http) throws Exception {
    return http.headers().frameOptions().disable().and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
  }

  private HttpSecurity configureCors(HttpSecurity http) throws Exception {
    return http.cors().and();
  }

  private HttpSecurity configureCsrf(HttpSecurity http) throws Exception {
    return http.csrf().disable();
  }

  private HttpSecurity configureUrlAuthorizations(HttpSecurity http) throws Exception {
    return http.authorizeRequests()
      
      .antMatchers(HttpMethod.POST, "/login","/user/register").permitAll()
      .antMatchers("/**/**").authenticated().and();
  }

  private HttpSecurity configureExceptionHandling(HttpSecurity http) throws Exception {
    return http.exceptionHandling().and();
  }

  private HttpSecurity configureFilters(HttpSecurity http) throws Exception {
    return http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}