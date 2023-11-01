package com.ggw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
public interface SysRoleService extends IService<SysRole> {

  /**
   * 根据UserID查询RoleList
   *
   * @param UserId
   * @return
   */
  List<SysRole> getRoleByUserId(Long UserId);

  /**
   * 查询角色列表
   *
   * @param name
   * @param code
   * @param description
   * @param page
   * @param size
   * @param orderName
   * @param orderNum
   * @return
   */
  IPage<SysRole> getRoleList(String name, String code, String description, Integer page,
      Integer size,
      String orderName, Integer orderNum);

  /**
   * 新增角色
   *
   * @param name
   * @param code
   * @param description
   * @param menuIds
   */
  void addRole(String name, String code, String description, String admin, List<String> menuIds);

  /**
   * 修改角色
   *
   * @param name
   * @param code
   * @param description
   * @param admin
   * @param menuIds
   * @param sysRole
   */
  void updateRole(String name, String code, String description, String admin, List<String> menuIds,
      SysRole sysRole);

  /**
   * 删除角色
   * @param id
   */
  void deleteRole(Long id);
  /**
   * 通过名称查找
   *
   * @param name
   * @return
   */
  SysRole getRoleByName(String name);

  /**
   * 通过Code查找
   *
   * @param code
   * @return
   */
  SysRole getRoleByCode(String code);

  /**
   * 查询新名称是否存在
   *
   * @param newName
   * @param oldName
   * @return
   */
  SysRole getRoleWithOutName(String newName, String oldName);

  /**
   * 查询新角色码是否存在
   *
   * @param newCode
   * @param oldCode
   * @return
   */
  SysRole getRoleWithOutCode(String newCode, String oldCode);
}
