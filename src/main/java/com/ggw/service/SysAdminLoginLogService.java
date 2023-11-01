package com.ggw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.entity.SysAdminLoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 登录日志表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-04 11:25:25
 */
public interface SysAdminLoginLogService extends IService<SysAdminLoginLog> {

  /**
   * 查询日志
   * @param username
   * @param className
   * @param methodName
   * @param modelName
   * @param action
   * @param ip
   * @param description
   * @param page
   * @param limit
   * @param orderName
   * @param orderNum
   * @return
   */
  IPage<SysAdminLoginLog> getLogList(String username,String className,String methodName,String modelName,String action,String ip,String description,
      Integer page,Integer limit,String orderName,Integer orderNum);
}
