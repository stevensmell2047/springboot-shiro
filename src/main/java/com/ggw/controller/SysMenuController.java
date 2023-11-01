package com.ggw.controller;


import cn.hutool.core.map.MapUtil;
import com.ggw.common.R;
import com.ggw.dto.SysMenuDto;
import com.ggw.entity.SysAdmin;
import com.ggw.entity.SysMenu;
import com.ggw.service.SysAdminService;
import com.ggw.service.SysMenuService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private SysMenuService sysMenuService;

  /**
   * 用户当前用户的菜单和权限信息
   *
   * @param principal
   * @return
   */
  @GetMapping("/nav")
  public R nav(Principal principal) {
    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());

    // 获取权限信息
    String authorityInfo = sysAdminService
        .getUserAuthorityInfo(sysUser.getId());// ROLE_admin,ROLE_normal,sys:user:list,....
    String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");

    // 获取导航栏信息
    List<SysMenuDto> navs = sysMenuService.getCurrentUserNav();

    return R.SUCCESS(MapUtil.builder()
        .put("authoritys", authorityInfoArray)
        .put("nav", navs)
        .map()
    );
  }
}
