package com.ggw.common.exception;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.ggw.common.R;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @ClassName: GlobalControllerExceptionHandler
 * @Description:
 * @author: AI
 * @date: 2019/5/7  11:41
 */
@ControllerAdvice()
@Slf4j
public class GlobalControllerExceptionHandler {

  @ExceptionHandler()
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  private R runtimeExceptionHandler(Exception e) {
    return R.data("200", e.getMessage(), "接口参数错误");
  }

  @Async
  public void sendMessage(Exception e) {
    // 发送钉钉通知开发者
//        DingDingUtils.sendTextMessage(ExceptionUtil.stacktraceToString(e,1000,null));
  }

//    @ExceptionHandler({SaTokenException.class})
//    public R saTokenException(SaTokenException e) {
//        log.error("SaTokenException: ", e);
//        return R.FAIL(e.getMessage());
//    }
//
//    @ExceptionHandler({NotLoginException.class})
//    public R notLoginException(NotLoginException e) {
//        log.error("NotLoginException: ", e);
//        return R.FAIL(e.getMessage());
//    }

  @ExceptionHandler(ValidateException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private R ValidateExceptionHandler(ValidateException e) {
    return R.data("400", "ValidateExceptionHandler", "参数错误");
  }

  @ExceptionHandler(AuthException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  private R AuthException(AuthException e) {
    return R.data("401", "AuthException", "认证错误");
  }

  /**
   * 处理单个参数校验失败抛出的异常
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public R ParamsException(ConstraintViolationException e) {

    List errorList = CollectionUtil.newArrayList();
    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
    for (ConstraintViolation<?> violation : violations) {
      StringBuilder message = new StringBuilder();
      Path path = violation.getPropertyPath();
      String[] pathArr = StrUtil.splitToArray(path.toString(), '.');
      String msg = message.append(pathArr[1]).append(violation.getMessage()).toString();
      errorList.add(msg);
    }
    return R.data("400", errorList, "参数错误");
  }

  @ExceptionHandler(BindException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private R BindException(BindException e) {
    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    List<String> collect = fieldErrors.stream()
        .map(o -> o.getField() + o.getDefaultMessage())
        .collect(Collectors.toList());
    return R.data("400", collect, "参数错误");
//    return Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private R MethodArgumentNotValidException(MethodArgumentNotValidException e) {
    return R.data("400", "MethodArgumentNotValidException", "参数校验错误");
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private R MissingServletRequestParameterException(
      MissingServletRequestParameterException e) {
    return R.data("400", e.getMessage(), "请求参数错误");
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  private R HttpMessageNotReadableException(HttpMessageNotReadableException e) {
    sendMessage(e);
    return R.data("500", HttpStatus.INTERNAL_SERVER_ERROR, "入参错误");
  }

}
