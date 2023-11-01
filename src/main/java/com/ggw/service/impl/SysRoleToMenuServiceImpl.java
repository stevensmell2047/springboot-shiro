package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.SysRoleToMenu;
import com.ggw.dao.SysRoleToMenuMapper;
import com.ggw.service.SysRoleToMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-31 10:11:47
 */
@Service
public class SysRoleToMenuServiceImpl extends
    ServiceImpl<SysRoleToMenuMapper, SysRoleToMenu> implements SysRoleToMenuService {

  @Autowired
  private SysRoleToMenuMapper mapper;

  @Override
  public void deleteByRoleId(Long roleId) {
    QueryWrapper<SysRoleToMenu> wrapper = new QueryWrapper<>();
    wrapper.eq("role_id", roleId);
    mapper.delete(wrapper);
  }

  @Override
  public List<Long> getByRoleId(Long roleId) {
    QueryWrapper<SysRoleToMenu> wrapper = new QueryWrapper<>();
    wrapper.eq("role_id", roleId);
    List<SysRoleToMenu> list = mapper.selectList(wrapper);
    List<Long> result = null;
    if (list.size() > 0) {
      result = list.stream().map(SysRoleToMenu::getMenuId).collect(Collectors.toList());
    }
    return result;
  }
}
