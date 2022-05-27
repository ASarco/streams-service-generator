package com.imgarena.sherlock.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.imgarena.sherlock.config.filter.TokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

  private final TokenAuthenticationFilter tokenAuthenticationFilter;

  public WebSecurityConfig(TokenAuthenticationFilter tokenAuthenticationFilter) {
    this.tokenAuthenticationFilter = tokenAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.sessionManagement(configurer -> configurer.sessionCreationPolicy(STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .authorizeRequests(
            authz ->
                authz
                    .antMatchers(
                        "/actuator/health",
                        "/actuator/health/liveness",
                        "/actuator/health/readiness")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class);
    return http.build();
  }
}
