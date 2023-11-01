package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.SysUserToRole;
import com.ggw.dao.SysUserToRoleMapper;
import com.ggw.service.SysUserToRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-31 10:11:47
 */
@Service
public class SysUserToRoleServiceImpl extends
    ServiceImpl<SysUserToRoleMapper, SysUserToRole> implements SysUserToRoleService {

  @Autowired
  private SysUserToRoleMapper mapper;

  @Override
  public void deleteByRoleId(Long roleId) {
    QueryWrapper<SysUserToRole> wrapper = new QueryWrapper<>();
    wrapper.eq("role_id", roleId);
    mapper.delete(wrapper);
  }

  @Override
  public void deleteByUserId(Long userId) {
    QueryWrapper<SysUserToRole> wrapper = new QueryWrapper<>();
    wrapper.eq("admin_id", userId);
    mapper.delete(wrapper);
  }
}
