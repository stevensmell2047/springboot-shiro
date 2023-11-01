package com.ggw.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.common.redis.RedisService;
import com.ggw.entity.SysRole;
import com.ggw.dao.SysRoleMapper;
import com.ggw.entity.SysRoleToMenu;
import com.ggw.service.SysAdminService;
import com.ggw.service.SysMenuService;
import com.ggw.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.service.SysRoleToMenuService;
import com.ggw.service.SysUserToRoleService;
import com.ggw.util.ReflectUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements
    SysRoleService {

  @Autowired
  private SysRoleMapper mapper;
  @Autowired
  private SysRoleToMenuService sysRoleToMenuService;
  @Autowired
  private SysUserToRoleService sysUserToRoleService;
  @Autowired
  private RedisService redisService;

  @Override
  public List<SysRole> getRoleByUserId(Long userId) {
    return mapper.selectList(new QueryWrapper<SysRole>()
        .inSql("id", "select role_id from gw_sys_user_to_role where admin_id=" + userId));
  }

  @Override
  public IPage<SysRole> getRoleList(String name, String code, String description, Integer page,
      Integer size, String orderName, Integer orderNum) {
    IPage<SysRole> iPage = new Page<>(page, size);
    QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
    if (!StringUtils.isBlank(name)) {
      wrapper.like("name", name);
    }
    if (!StringUtils.isBlank(code)) {
      wrapper.like("code", code);
    }
    if (!StringUtils.isBlank(description)) {
      wrapper.like("description", description);
    }
    if (ObjectUtils.isNotNull(orderNum)) {
      if (orderName != null && !"id".equals(orderName)) {
        orderName = ReflectUtils.getValue(new SysRole(), orderName);
      }
      if (orderNum == 0) {
        wrapper.orderByAsc(orderName);
      } else if (orderNum == 1) {
        wrapper.orderByDesc(orderName);
      } else {
        wrapper.orderByDesc("id");
      }
    } else {
      wrapper.orderByDesc("id");
    }
    return mapper.selectPage(iPage, wrapper);
  }

  @Transactional
  @Override
  public void addRole(String name, String code, String description, String admin,
      List<String> menuIds) {
    SysRole role = new SysRole();
    role.setName(name);
    role.setCode(code);
    role.setDescription(description);
    role.setStatus(0);
    role.setCreater(admin);
    role.setCreateTime(new Date());
    mapper.insert(role);
    Long roleId = role.getId();
    if (menuIds.size() > 0) {
      for (String id : menuIds) {
        SysRoleToMenu roleToMenu = new SysRoleToMenu();
        roleToMenu.setRoleId(roleId);
        roleToMenu.setMenuId(Long.valueOf(id));
        sysRoleToMenuService.save(roleToMenu);
      }
    }
  }

  @Transactional
  @Override
  public void updateRole(String name, String code, String description, String admin,
      List<String> menuIds, SysRole role) {
    role.setName(name);
    role.setCode(code);
    role.setDescription(description);
    role.setStatus(0);
    role.setLastUpdate(admin);
    role.setUpdateTime(new Date());
    mapper.updateById(role);
    sysRoleToMenuService.deleteByRoleId(role.getId());
//    sysUserToRoleService.deleteByRoleId(role.getId());
    List<Long> adminIds = mapper.getUserByRoleId(role.getId());
    for (Long id : adminIds) {
      redisService.delete("GrantedAuthority" + id);
    }
    if (menuIds.size() > 0) {
      List<SysRoleToMenu> list = new ArrayList<>();
      for (String id : menuIds) {
        SysRoleToMenu roleToMenu = new SysRoleToMenu();
        roleToMenu.setRoleId(role.getId());
        roleToMenu.setMenuId(Long.valueOf(id));
        list.add(roleToMenu);
      }

      sysRoleToMenuService.saveBatch(list);
    }
  }

  @Transactional
  @Override
  public void deleteRole(Long id) {
    mapper.deleteById(id);
    sysRoleToMenuService.deleteByRoleId(id);
    sysUserToRoleService.deleteByRoleId(id);
    List<Long> adminIds = mapper.getUserByRoleId(id);
    for (Long adminId : adminIds) {
      redisService.delete("GrantedAuthority" + adminId);
    }
  }

  @Override
  public SysRole getRoleByName(String name) {
    QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
    wrapper.eq("name", name);
    return mapper.selectOne(wrapper);
  }

  @Override
  public SysRole getRoleByCode(String code) {
    QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
    wrapper.eq("code", code);
    return mapper.selectOne(wrapper);
  }

  @Override
  public SysRole getRoleWithOutName(String newName, String oldName) {
    QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
    wrapper.eq("name", newName);
    wrapper.ne("name", oldName);
    return mapper.selectOne(wrapper);
  }

  @Override
  public SysRole getRoleWithOutCode(String newCode, String oldCode) {
    QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
    wrapper.eq("code", newCode);
    wrapper.ne("code", oldCode);
    return mapper.selectOne(wrapper);
  }
}
