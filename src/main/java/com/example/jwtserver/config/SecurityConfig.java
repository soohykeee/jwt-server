package com.example.jwtserver.config;

import com.example.jwtserver.config.jwt.JwtAuthenticationFilter;
import com.example.jwtserver.config.jwt.JwtAuthorizationFilter;
import com.example.jwtserver.filter.MyFilter1;
import com.example.jwtserver.filter.MyFilter3;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class); //Basic~~ filter가 실행되기 전에 MyFilter를 추가
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Session을 사용하지 않는다.
                .and()
                .addFilter(corsFilter)
                .formLogin().disable()          // security 자체에서 제공해주는 formLogin 사용하지 않겠다
                .httpBasic().disable()          // 기본 인증 방식을 사용하지 않겠다
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))   //AuthenticationManager
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))   //AuthenticationManager
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();

    }
}
