package com.ggw.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.dto.SysMenuDto;
import com.ggw.entity.SysAdmin;
import com.ggw.entity.SysMenu;
import com.ggw.dao.SysMenuMapper;
import com.ggw.entity.SysRoleToMenu;
import com.ggw.service.SysAdminService;
import com.ggw.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.service.SysRoleToMenuService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements
    SysMenuService {

  @Autowired
  private SysMenuMapper menuMapper;
  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private SysRoleToMenuService sysRoleToMenuService;


  @Override
  public List<SysMenuDto> getCurrentUserNav() {
    String username = (String) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    SysAdmin sysUser = sysAdminService.getUserByName(username);
    List<Long> menuIds = sysAdminService.getNavMenuIds(sysUser.getId());
    List<SysMenu> menus = this.listByIds(menuIds);

    // 转树状结构
    List<SysMenu> menuTree = buildTreeMenu(menus);

    // 实体转DTO
    return convert(menuTree);
  }



  private List<SysMenuDto> convert(List<SysMenu> menuTree) {
    List<SysMenuDto> menuDtos = new ArrayList<>();

    menuTree.forEach(m -> {
      SysMenuDto dto = new SysMenuDto();

      dto.setId(m.getId());
      dto.setName(m.getCode());
      dto.setLabel(m.getName());
      dto.setComponent(m.getComponent());
      dto.setPath(m.getPath());
      dto.setIcon(m.getIcon());
      dto.setSortNum(m.getOrderNum());

      if (m.getChildren().size() > 0) {

        // 子节点调用当前方法进行再次转换
        dto.setChildren(convert(m.getChildren()));
      }

      menuDtos.add(dto);
    });

    return menuDtos;
  }

  private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {

    List<SysMenu> finalMenus = new ArrayList<>();

    // 先各自寻找到各自的孩子
    for (SysMenu menu : menus) {
      for (SysMenu e : menus) {
        if (menu.getId() == e.getParentId()) {
          menu.getChildren().add(e);
        }
      }

      // 提取出父节点
      if (menu.getParentId() == 0L) {
        finalMenus.add(menu);
      }
    }

    return finalMenus;
  }
}
