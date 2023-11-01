package com.ggw.aspect;

/**
 * @author qizhuo
 * @date 2022/6/5 0:00
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ggw.annotation.Log;
import com.ggw.entity.SysAdmin;
import com.ggw.entity.SysAdminLoginLog;
import com.ggw.service.SysAdminLoginLogService;
import com.ggw.service.SysAdminService;
import com.ggw.util.IPUtils;
import com.ggw.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 系统日志切面
 */
@Slf4j
@Aspect  // 使用@Aspect注解声明一个切面
@Component
public class SysLogAspect {

  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private SysAdminLoginLogService loginLogService;
  @Autowired
  private SysAdminService sysAdminService;

  /**
   * 这里我们使用注解的形式 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method 切点表达式:   execution(...)
   * <p>
   * execution(public * *(..)) 任意的公共方法 execution（* set*（..）） 以set开头的所有的方法 execution（*
   * com.LoggerApply.*（..））com.LoggerApply这个类里的所有的方法 execution（* com.annotation.*.*（..））com.annotation包下的所有的类的所有的方法
   * execution（* com.annotation..*.*（..））com.annotation包及子包下所有的类的所有的方法 execution(*
   * com.annotation..*.*(String,?,Long)) com.annotation包及子包下所有的类的有三个参数，第一个参数为String类型，第二个参数为任意类型，第三个参数为Long类型的方法
   * execution(@annotation(com.lingyejun.annotation.Lingyejun))
   */
  @Pointcut("@annotation(com.ggw.annotation.Log)")
  public void logPointCut() {
  }

  /**
   * 环绕通知 @Around  ， 当然也可以使用 @Before (前置通知)  @After (后置通知)
   *
   * @param point
   * @return
   * @throws Throwable
   */
  @Around("logPointCut()")
  public Object around(ProceedingJoinPoint point) throws Throwable {
    long beginTime = System.currentTimeMillis();
    Object result = point.proceed();
    long time = System.currentTimeMillis() - beginTime;
    try {
      saveLog(point, time);
    } catch (Exception e) {
    }
    return result;
  }

  /**
   * 保存日志
   */
  private void saveLog(ProceedingJoinPoint joinPoint, long time) {
    ServletRequestAttributes attributes = (ServletRequestAttributes)
        RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String authorization = request.getHeader("Authorization");
    Claims claims = JwtUtils.getClaimByToken(authorization);
    if (claims == null) {
      return;
    }
    if (jwtUtils.isTokenExpired(claims)) {
      return;
    }
    Method method = signature.getMethod();
    SysAdminLoginLog sysLogBO = new SysAdminLoginLog();
    String username = claims.getSubject();
    // 获取用户的权限等信息
    SysAdmin sysUser = sysAdminService.getUserByName(username);
    sysLogBO.setAdmin(sysUser.getUsername());
    sysLogBO.setAdminId(sysUser.getId());
    sysLogBO.setCreateTime(new Date());
    Log sysLog = method.getAnnotation(Log.class);
    if (sysLog != null) {
      //注解上的描述
      sysLogBO.setDescription(sysLog.description());
      sysLogBO.setModelName(sysLog.modelName());
      sysLogBO.setAction(sysLog.action());
    }
    //请求的 类名、方法名
    String className = joinPoint.getTarget().getClass().getName();
    String methodName = signature.getName();
    sysLogBO.setClassName(className);
    sysLogBO.setMethodName(methodName);
    sysLogBO.setIp(request.getRemoteAddr());
    //请求的参数
    Object[] args = joinPoint.getArgs();
    try {
      List<String> list = new ArrayList<String>();
      for (Object o : args) {
        String value = JSON.toJSONString(o);
        if (!value.contains("authenticated")) {
          list.add(String.valueOf(o));
        }
      }
      sysLogBO.setActionArgs(list.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
    loginLogService.save(sysLogBO);
  }
}
