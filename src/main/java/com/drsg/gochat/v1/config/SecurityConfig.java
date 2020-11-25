package com.drsg.gochat.v1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers("/js/**", "/css/**", "/img/**");
    }

    @Bean
    public UsernamePasswordAuthenticationFilter iUsernamePasswordAuthenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter authenticationFilter = new IUsernamePasswordAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(super.authenticationManager());
        authenticationFilter.setFilterProcessesUrl("/v1/login");
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return authenticationFilter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionHandler())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .mvcMatchers("/v1/register", "/ws/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterAt(iUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/v1/logout")
                .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    Map<String, Object> map = new HashMap<>(1);
                    map.put("message", "success");
                    httpServletResponse.getWriter().println(new ObjectMapper().writeValueAsString(map));
                }).permitAll();
    }


    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            Map<String, Object> map = new HashMap<>(1);
            map.put("message", "success");
            logger.info("{} sign in.", ((User) authentication.getPrincipal()).getUsername());
            httpServletResponse.getWriter().println(new ObjectMapper().writeValueAsString(map));
        };
    }

    private AuthenticationEntryPoint authenticationExceptionHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, Object> map = new HashMap<>(1);
            map.put("error", "认证失败，请重新登录");
            logger.error("Authorization failed.");
            httpServletResponse.getWriter().println(new ObjectMapper().writeValueAsString(map));
        };
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> map = new HashMap<>(1);
            map.put("error", "用户名或密码错误");
            logger.error("Bad username or password.");
            httpServletResponse.getWriter().println(new ObjectMapper().writeValueAsString(map));
        };
    }
}
