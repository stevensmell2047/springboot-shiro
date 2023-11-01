package com.ggw.controller;


import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.service.SysAdminLoginLogService;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 登录日志表 前端控制器
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-04 11:25:25
 */
@RestController
@RequestMapping("/sys/log")
public class SysAdminLoginLogController {

  @Autowired
  private SysAdminLoginLogService loginLogService;

  @Log(action = "列表", modelName = "操作日志", description = "查询操作日志")
  @ApiOperation(value = "查询操作日志", notes = "查询操作日志接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:log:list')")
  @PostMapping("/list")
  public R list(@RequestParam(required = false) String username,
      @RequestParam(required = false) String className,
      @RequestParam(required = false) String methodName,
      @RequestParam(required = false) String modelName,
      @RequestParam(required = false) String action,
      @RequestParam(required = false) String ip,
      @RequestParam(required = false) String description,
      @NotNull(message = "页码不能为空") @RequestParam(value = "page", required = true) Integer page,
      @NotNull(message = "页数不能为空") @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(required = false) String orderName,
      @RequestParam(required = false) Integer orderNum) {

    return R.SUCCESS(loginLogService
        .getLogList(username, className, methodName, modelName, action, ip, description, page,
            limit, orderName, orderNum));
  }
}
