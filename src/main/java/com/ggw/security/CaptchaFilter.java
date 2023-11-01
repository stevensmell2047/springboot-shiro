package com.ggw.security;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import com.ggw.base.Constant;
import com.ggw.common.exception.CaptchaException;
import com.ggw.common.redis.RedisService;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class CaptchaFilter extends OncePerRequestFilter {

  @Autowired
  private RedisService redisUtil;

  @Autowired
  private LoginFailureHandler loginFailureHandler;

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      FilterChain filterChain) throws ServletException, IOException {
    String url = httpServletRequest.getRequestURI();
    if ("/api/login".equals(url) && httpServletRequest.getMethod().equals("POST")) {
      try {
        // 校验验证码
        validate(httpServletRequest);
      } catch (CaptchaException e) {
        // 交给认证失败处理器
        loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
      }
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  // 校验验证码逻辑
  private void validate(HttpServletRequest httpServletRequest) {
//    Map<String, String[]> map = httpServletRequest.getParameterMap();
//    log.info("httpServletRequest==="+JSON.toJSONString(map));
//    Set<Entry<String, String[]>> keys = map.entrySet();
//    Iterator<Entry<String, String[]>> it = keys.iterator();
//    while (it.hasNext()){
//      Map.Entry<String, String[]> itMap = it.next();
//      log.info("参数--"+itMap.getKey()+":"+ Arrays.toString(itMap.getValue()));
//    }
    String code = httpServletRequest.getParameter("code");
    String key = httpServletRequest.getParameter("key");
    if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
      throw new CaptchaException("验证码错误");
    }
    if (!code.equals(redisUtil.get(Constant.CAPTCHA_KEY + key))) {
      throw new CaptchaException("验证码错误");
    }

    // 一次性使用
    redisUtil.delete(Constant.CAPTCHA_KEY + key);
  }
//  public static void main(String args[]){
//    PasswordEncoder pe=new BCryptPasswordEncoder();
//    log.info(pe.encode("123456"));
//  }
}

