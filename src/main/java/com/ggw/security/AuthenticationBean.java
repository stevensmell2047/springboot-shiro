package com.ggw.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qizhuo
 * @date 2022/5/31 0:26
 */
@Getter
@Setter
public class AuthenticationBean {
  private String username;
  private String password;
}
