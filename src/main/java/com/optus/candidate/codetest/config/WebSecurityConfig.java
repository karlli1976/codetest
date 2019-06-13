package com.optus.candidate.codetest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      //HTTP Basic authentication
      .httpBasic()
      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.POST, "/counter-api/search").authenticated()
      .antMatchers(HttpMethod.GET, "/counter-api/top/**").authenticated()
      .and()
      .csrf().disable()
      .formLogin().disable();
  }


}