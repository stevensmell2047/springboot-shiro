package com.ggw.service;

import com.ggw.entity.SysRoleToMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-31 10:11:47
 */
public interface SysRoleToMenuService extends IService<SysRoleToMenu> {

  /**
   * 通过RoleID删除关联
   *
   * @param roleId
   */
  void deleteByRoleId(Long roleId);

  /**
   * 获取所有菜单关联
   * @param roleId
   * @return
   */
  List<Long> getByRoleId(Long roleId);
}
