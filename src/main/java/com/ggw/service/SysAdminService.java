package com.ggw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.dto.SysAdminDto;
import com.ggw.entity.SysAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ggw.entity.SysDepartment;
import com.ggw.entity.SysPosition;
import com.ggw.entity.SysRole;
import com.ggw.entity.SysUserToDepartment;
import com.ggw.entity.SysUserToPosition;
import com.ggw.entity.SysUserToRole;
import io.swagger.models.auth.In;
import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
public interface SysAdminService extends IService<SysAdmin> {

  List<SysAdmin> getUserById(Long id);
  /**
   * 通过用户名获取用户信息
   *
   * @param username
   * @return
   */
  SysAdmin getUserByName(String username);

  /**
   * 查询账号是否存在
   * @param newName
   * @param oldName
   * @return
   */
  SysAdmin getUserWithOutUserName(String newName,String oldName);

  /**
   * 获取用户权限
   *
   * @param userId
   * @return
   */
  String getUserAuthorityInfo(Long userId);

  /**
   * 清除用户权限缓存
   *
   * @param userId
   */
  void clearUserAuthorityCache(Long userId);

  /**
   * 修改角色清理缓存
   *
   * @param roleId
   */
  void clearUserAuthorityCacheByRoleId(Long roleId);

  /**
   * 修改Menu清理缓存
   *
   * @param menuId
   */
  void clearUserAuthorityCacheByMenuId(Long menuId);

  /**
   * 查询用户菜单
   *
   * @param userId
   * @return
   */
  List<Long> getNavMenuIds(Long userId);

  /**
   * 根据部门ID分页查询用户
   *
   * @param depId
   * @return
   */
  IPage<SysAdminDto> getUsersByDepId(Long depId, Integer page, Integer size, String orderName,
      Integer orderNum);

  /**
   * 根据职位ID分页查询用户
   *
   * @param positionId
   * @return
   */
  IPage<SysAdminDto> getUsersByPositionId(Long positionId, Integer page, Integer size,
      String orderName,
      Integer orderNum);

  /**
   * 分页查询系统用户列表
   *
   * @return
   */
  IPage<SysAdminDto> getUserList(String name, String username, String position, String department,
      String phone, String email, String jobNum,
      String address, String roles,
      Integer page, Integer limit, String orderName, Integer orderNum);

  /**
   * 新增管理员
   * @param sysAdmin
   */
  void addAdmin(SysAdmin sysAdmin, Long positionId, Long depId, List<Long> roleId);

  /**
   * 更新管理员
   * @param sysAdmin
   * @param position
   * @param department
   * @param roleList
   */
  void updateAdmin(SysAdmin sysAdmin, SysPosition position,
      SysDepartment department,
      List<SysRole> roleList);

  /**
   * 获取用户信息
   * @param userId
   * @return
   */
  SysAdminDto getUserInfo(Long userId);

  /**
   * 删除用户
   * @param userId
   */
  void deleteUser(Long userId);
}
