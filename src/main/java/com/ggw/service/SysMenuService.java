package com.ggw.service;

import com.ggw.dto.SysMenuDto;
import com.ggw.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
public interface SysMenuService extends IService<SysMenu> {

  /**
   * 获取用户所有菜单权限
   * @return
   */
  List<SysMenuDto> getCurrentUserNav();

}
