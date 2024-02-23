package com.fadesp.processor.config;

import com.fadesp.processor.filters.AuthFilter;
import com.fadesp.processor.filters.LogFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final LogFilter loggerFilter;
  private final AuthFilter authFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .cors().disable()
        .logout().disable()
        .headers().disable()
        .httpBasic().disable()
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(loggerFilter, AuthFilter.class)
        .authorizeRequests()
        .antMatchers("/h2/**").permitAll()
        .anyRequest().permitAll();
  }

}
