package com.ggw.service;

import com.ggw.entity.SysUserToRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-31 10:11:47
 */
public interface SysUserToRoleService extends IService<SysUserToRole> {

  /**
   * 删除RoleID
   * @param roleId
   */
  void deleteByRoleId(Long roleId);

  /**
   * 通过UserID删除关联
   * @param userId
   */
  void deleteByUserId(Long userId);
}
