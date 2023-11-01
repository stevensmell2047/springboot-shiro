package com.ggw.security;

import cn.hutool.json.JSONUtil;
import com.ggw.common.R;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {

    response.setContentType("application/json;charset=UTF-8");
    ServletOutputStream outputStream = response.getOutputStream();
    R result = R.FAIL(exception.getLocalizedMessage());

    outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

    outputStream.flush();
    outputStream.close();
  }
}
