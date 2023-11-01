package com.ggw.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.annotation.AccessLimit;
import com.ggw.common.redis.RedisService;
import com.ggw.dto.SysAdminDto;
import com.ggw.entity.SysAdmin;
import com.ggw.dao.SysAdminMapper;
import com.ggw.entity.SysDepartment;
import com.ggw.entity.SysMenu;
import com.ggw.entity.SysPosition;
import com.ggw.entity.SysRole;
import com.ggw.entity.SysUserToDepartment;
import com.ggw.entity.SysUserToPosition;
import com.ggw.entity.SysUserToRole;
import com.ggw.service.SysAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.service.SysMenuService;
import com.ggw.service.SysRoleService;
import com.ggw.service.SysUserToDepartmentService;
import com.ggw.service.SysUserToPositionService;
import com.ggw.service.SysUserToRoleService;
import com.ggw.util.ReflectUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@Slf4j
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements
    SysAdminService {

  @Autowired
  private SysAdminMapper sysAdminMapper;
  @Autowired
  private SysRoleService sysRoleService;
  @Autowired
  private RedisService redisService;
  @Autowired
  private SysUserToDepartmentService sysUserToDepartmentService;
  @Autowired
  private SysUserToPositionService sysUserToPositionService;
  @Autowired
  private SysUserToRoleService sysUserToRoleService;


  @Override
  public List<SysAdmin> getUserById(Long id) {
    return null;
  }

  @Override
  public SysAdmin getUserByName(String username) {
    QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
    wrapper.eq("username", username);
    return sysAdminMapper.selectOne(wrapper);
  }

  @Override
  public SysAdmin getUserWithOutUserName(String newName, String oldName) {
    QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
    wrapper.eq("username", newName);
    wrapper.ne("username", oldName);
    return sysAdminMapper.selectOne(wrapper);
  }

  @Override
  public String getUserAuthorityInfo(Long userId) {
    String authority = "";
    if (redisService.exists("GrantedAuthority" + userId)) {
      authority = redisService.get("GrantedAuthority" + userId);
    } else {
      List<SysRole> roleList = sysRoleService.getRoleByUserId(userId);
      if (roleList.size() > 0) {
        String roleCodes = roleList.stream().map(r -> "Role_" + r.getCode())
            .collect(Collectors.joining(","));
        authority = roleCodes.concat(",");
      }
      //获取菜单编码
      List<SysMenu> menus = sysAdminMapper.getUserMenus(userId);
      if (menus.size() > 0) {
        String menuPerms = menus.stream().map(m -> m.getCode())
            .collect(Collectors.joining(","));
        authority = authority.concat(menuPerms);
      }
    }
    redisService.set("GrantedAuthority" + userId, authority, -1);
    return authority;
  }

  @Override
  public void clearUserAuthorityCache(Long userId) {
    redisService.delete("GrantedAuthority" + userId);
  }

  @Override
  public void clearUserAuthorityCacheByRoleId(Long roleId) {
    List<Long> adminIds = sysAdminMapper.getUserByRoleId(roleId);
    adminIds.forEach(this::clearUserAuthorityCache);
  }

  @Override
  public void clearUserAuthorityCacheByMenuId(Long menuId) {
    List<Long> adminIds = sysAdminMapper.getUserByMenuId(menuId);
    adminIds.forEach(this::clearUserAuthorityCache);
  }

  @Override
  public List<Long> getNavMenuIds(Long userId) {
    return sysAdminMapper.getUserMenuIds(userId);
  }

  @Override
  public IPage<SysAdminDto> getUsersByDepId(Long depId, Integer page, Integer size,
      String orderName,
      Integer orderNum) {
    IPage<SysAdmin> iPage = new Page<>(page, size);
    return sysAdminMapper.getUsersByDepId(iPage, depId, orderName, orderNum);
  }

  @Override
  public IPage<SysAdminDto> getUsersByPositionId(Long positionId, Integer page, Integer size,
      String orderName,
      Integer orderNum) {
    IPage<SysAdmin> iPage = new Page<>(page, size);

    return sysAdminMapper.getUsersByPositionId(iPage, positionId, orderName, orderNum);
  }

  @Override
  public IPage<SysAdminDto> getUserList(String name, String username, String position,
      String department, String phone, String email, String jobNum, String address, String roles,
      Integer page, Integer limit, String orderName, Integer orderNum) {
    IPage<SysAdmin> iPage = new Page<>(page, limit);
    return sysAdminMapper
        .getUserList(iPage, null, null, username, name, position, department, address, phone, email,
            orderName, orderNum);
  }

  @Transactional
  @Override
  public void addAdmin(SysAdmin sysAdmin, Long positionId, Long depId, List<Long> roleIds) {
    sysAdminMapper.insert(sysAdmin);
    Long adminId = sysAdmin.getId();
    SysUserToPosition position = new SysUserToPosition();
    position.setAdminId(adminId);
    position.setPositionId(positionId);
    sysUserToPositionService.save(position);

    for (Long roleId : roleIds) {
      SysUserToRole role = new SysUserToRole();
      role.setAdminId(adminId);
      role.setRoleId(roleId);
      sysUserToRoleService.save(role);
    }

    SysUserToDepartment department = new SysUserToDepartment();
    department.setAdminId(adminId);
    department.setDepId(depId);
    sysUserToDepartmentService.save(department);
  }

  @Override
  public void updateAdmin(SysAdmin sysAdmin, SysPosition position,
      SysDepartment department,
      List<SysRole> roleList) {
    sysAdminMapper.updateById(sysAdmin);
    SysUserToPosition sysUserToPosition = sysUserToPositionService.getByUserId(sysAdmin.getId());
    if (null != sysUserToPosition && !position.getId().equals(sysUserToPosition.getPositionId())) {
      sysUserToPosition.setPositionId(position.getId());
      sysUserToPositionService.updateById(sysUserToPosition);
    }
    if (roleList.size() > 0) {
      sysUserToRoleService.deleteByUserId(sysAdmin.getId());
      for (SysRole role : roleList) {
        SysUserToRole sysUserToRole = new SysUserToRole();
        sysUserToRole.setRoleId(role.getId());
        sysUserToRole.setAdminId(sysAdmin.getId());
        sysUserToRoleService.save(sysUserToRole);
      }
    }
    SysUserToDepartment sysUserToDepartment = sysUserToDepartmentService
        .getByUserId(sysAdmin.getId());
    if (null != sysUserToDepartment && !department.getId().equals(sysUserToDepartment.getDepId())) {
      sysUserToDepartment.setDepId(department.getId());
      sysUserToDepartmentService.updateById(sysUserToDepartment);
    }
  }

  @Override
  public SysAdminDto getUserInfo(Long userId) {
    return sysAdminMapper.getUserInfo(userId);
  }

  @Transactional
  @Override
  public void deleteUser(Long userId) {
    sysAdminMapper.deleteById(userId);
    sysUserToRoleService.deleteByUserId(userId);
    sysUserToDepartmentService.deleteByUserId(userId);
    sysUserToPositionService.deleteByUserId(userId);
  }
}
