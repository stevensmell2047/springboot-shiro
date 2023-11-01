package com.ggw.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.entity.SysAdmin;
import com.ggw.entity.SysMenu;
import com.ggw.entity.SysRole;
import com.ggw.entity.SysRoleToMenu;
import com.ggw.service.SysAdminService;
import com.ggw.service.SysMenuService;
import com.ggw.service.SysRoleService;
import com.ggw.service.SysRoleToMenuService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@Slf4j
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

  @Autowired
  private SysRoleService sysRoleService;
  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private SysRoleToMenuService sysRoleToMenuService;
  @Autowired
  private SysMenuService sysMenuService;

  @Log(action="列表",modelName= "角色管理",description="查询角色列表")
  @ApiOperation(value = "查询角色列表", notes = "查询角色列表接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:role:list')")
  @PostMapping("/list")
  public R list(@RequestParam(required = false) String name,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String description,
      @NotNull(message = "页码不能为空") @RequestParam(value = "page", required = true) Integer page,
      @NotNull(message = "页数不能为空") @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(required = false) String orderName,
      @RequestParam(required = false) Integer orderNum, Principal principal) {

    return R.SUCCESS(sysRoleService.getRoleList(name, code, description, page,
        limit, orderName, orderNum));
  }
  @ApiOperation(value = "查询所有角色", notes = "查询所有角色接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:role:list')")
  @GetMapping("/all")
  public R all() {
    return R.SUCCESS(sysRoleService.list());
  }
  @Log(action="新增",modelName= "角色管理",description="查询角色列表")
  @ApiOperation(value = "新增角色", notes = "新增角色接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:role:add')")
  @PostMapping("/add")
  public R add(@NotNull(message = "名称不能为空") @RequestParam String name,
      @NotNull(message = "角色码不能为空") @RequestParam String code,
      @RequestParam String description,
      @NotNull(message = "菜单类型不能为空") @RequestParam String ids, Principal principal) {
    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());
    SysRole sysRole = sysRoleService.getRoleByName(name);
    if (sysRole != null) {
      return R.FAIL("名称已存在");
    }
    SysRole sysRole1 = sysRoleService.getRoleByCode(code);
    if (sysRole1 != null) {
      return R.FAIL("角色码已存在");
    }
    List<String> menuIds = Arrays.asList(ids.split(","));
    sysRoleService.addRole(name, code, description, sysUser.getUsername(), menuIds);
    return R.SUCCESS("创建成功");
  }

  @Log(action="编辑",modelName= "角色管理",description="编辑角色")
  @ApiOperation(value = "编辑角色", notes = "更新角色接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:role:edit')")
  @PostMapping("/edit")
  public R edit(@NotNull(message = "ID不能为空") @RequestParam Long id,
      @NotNull(message = "名称不能为空") @RequestParam String name,
      @NotNull(message = "角色码不能为空") @RequestParam String code,
      @RequestParam String description,
      @NotNull(message = "菜单类型不能为空") @RequestParam String ids, Principal principal) {
    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());
    SysRole sysRole = sysRoleService.getById(id);
    if (sysRole == null) {
      return R.FAIL("ID不存在");
    }
    if (sysRoleService.getRoleWithOutName(name, sysRole.getName()) != null) {
      return R.FAIL("名称已存在");
    }

    if (sysRoleService.getRoleWithOutCode(code, sysRole.getCode()) != null) {
      return R.FAIL("角色码已存在");
    }
    List<String> menuIds = Arrays.asList(ids.split(","));
    sysRoleService.updateRole(name, code, description, sysUser.getUsername(), menuIds, sysRole);
    return R.SUCCESS("更新成功");
  }

  @Log(action="详情",modelName= "角色管理",description="获取角色信息")
  @ApiOperation(value = "获取角色信息", notes = "获取角色信息接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:role:edit')")
  @PostMapping("/detail")
  public R detail(@NotNull(message = "ID不能为空") @RequestParam Long id) {
    SysRole sysRole = sysRoleService.getById(id);
    if (sysRole == null) {
      return R.FAIL("ID不存在");
    }

    return R.SUCCESS(MapUtil.builder()
        .put("roles", sysRole)
        .put("menus", sysRoleToMenuService.getByRoleId(id))
        .map());
  }
  @Log(action="删除",modelName= "角色管理",description="删除角色")
  @ApiOperation(value = "删除角色", notes = "删除角色接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:role:delete')")
  @DeleteMapping("/delete")
  public R delete(@NotNull(message = "ID不能为空") @RequestParam Long id) {
    SysRole sysRole = sysRoleService.getById(id);
    if (sysRole == null) {
      return R.FAIL("ID不存在");
    }
    sysRoleService.deleteRole(id);
    return R.SUCCESS("删除成功");
  }
}
