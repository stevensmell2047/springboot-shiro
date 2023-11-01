package com.ggw.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggw.common.R;
import com.ggw.security.CaptchaFilter;
import com.ggw.security.CustomAuthenticationFilter;
import com.ggw.security.JwtAccessDeniedHandler;
import com.ggw.security.JwtAuthenticationEntryPoint;
import com.ggw.security.JwtAuthenticationFilter;
import com.ggw.security.JwtLogoutSuccessHandler;
import com.ggw.security.LoginFailureHandler;
import com.ggw.security.LoginSuccessHandler;
import com.ggw.security.UserDetailServiceImpl;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 *
 * @author qizhuo
 * @date 2022/5/29 22:30
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  LoginFailureHandler loginFailureHandler;

  @Autowired
  LoginSuccessHandler loginSuccessHandler;

  @Autowired
  CaptchaFilter captchaFilter;

  @Autowired
  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Autowired
  JwtAccessDeniedHandler jwtAccessDeniedHandler;

  @Autowired
  private UserDetailServiceImpl userDetailService;

  @Autowired
  JwtLogoutSuccessHandler jwtLogoutSuccessHandler;


  @Bean
  JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
        authenticationManager());
    return jwtAuthenticationFilter;
  }

  @Bean
  BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private static final String[] URL_WHITELIST = {
      "/login",
      "/logout",
      "/captcha",
      "/favicon.ico",
  };


  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        // 登录配置
        .formLogin()
        .successHandler(loginSuccessHandler)
        .failureHandler(loginFailureHandler)

        .and()
        .logout()
        .logoutSuccessHandler(jwtLogoutSuccessHandler)

        // 禁用session
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // 配置拦截规则
        .and()
        .authorizeRequests()
        .antMatchers(URL_WHITELIST).permitAll()
        .anyRequest().authenticated()

        // 异常处理器
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler)

        // 配置自定义的过滤器
        .and()
        .addFilter(jwtAuthenticationFilter())
        .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);

//    http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailService);
  }

  //注册自定义的UsernamePasswordAuthenticationFilter
//  @Bean
//  CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
//    CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
//    filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
//      @Override
//      public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
//        resp.setContentType("application/json;charset=utf-8");
//        PrintWriter out = resp.getWriter();
//        R respBean = R.SUCCESS("登录成功!");
//        out.write(new ObjectMapper().writeValueAsString(respBean));
//        out.flush();
//        out.close();
//      }
//    });
//    filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
//      @Override
//      public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
//        resp.setContentType("application/json;charset=utf-8");
//        PrintWriter out = resp.getWriter();
//        R respBean = R.FAIL("登录失败!");
//        out.write(new ObjectMapper().writeValueAsString(respBean));
//        out.flush();
//        out.close();
//      }
//    });
//    filter.setAuthenticationManager(authenticationManagerBean());
//    return filter;
//  }




}
