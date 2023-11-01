package com.ggw.controller;


import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.ggw.annotation.Log;
import com.ggw.base.Constant;
import com.ggw.common.R;
import com.ggw.common.redis.RedisService;
import com.ggw.dto.SysAdminDto;
import com.ggw.entity.SysAdmin;
import com.ggw.entity.SysDepartment;
import com.ggw.entity.SysPosition;
import com.ggw.entity.SysRole;
import com.ggw.service.SysAdminService;
import com.ggw.service.SysDepartmentService;
import com.ggw.service.SysPositionService;
import com.ggw.service.SysRoleService;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysAdminController {

  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private SysDepartmentService sysDepartmentService;
  @Autowired
  private SysPositionService sysPositionService;
  @Autowired
  private SysRoleService roleService;

  @Log(action="列表",modelName= "系统用户",description="查询列表")
  @ApiOperation(value = "查询系统用户列表", notes = "查询系统用户列表接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:user:list')")
  @PostMapping("/list")
  public R list(@RequestParam(required = false) String name,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String position,
      @RequestParam(required = false) String department,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String jobNum,
      @RequestParam(required = false) String address,
      @RequestParam(required = false) String roles,
      @NotNull(message = "页码不能为空") @RequestParam(value = "page", required = true) Integer page,
      @NotNull(message = "页数不能为空") @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(required = false) String orderName,
      @RequestParam(required = false) Integer orderNum, Principal principal) {

    return R.SUCCESS(sysAdminService
        .getUserList(name, username, position, department, phone, email, jobNum, address, roles,
            page, limit, orderName, orderNum));
  }

  @Log(action="新增",modelName= "系统用户",description="新增管理员")
  @ApiOperation(value = "新增用户", notes = "新增用户接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:user:add')")
  @PostMapping("/add")
  public R add(@NotNull(message = "账号不能为空") @RequestParam String username,
      @NotNull(message = "真实姓名不能为空") @RequestParam String name,
      @NotNull(message = "密码不能为空") @RequestParam String password,
      @NotNull(message = "性别不能为空") @RequestParam Integer sex,
      @NotNull(message = "部门不能为空") @RequestParam Long departmentId,
      @NotNull(message = "职位不能为空") @RequestParam Long positionId,
      @RequestParam(required = false) String address,
      @RequestParam(required = false) String jobNum,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) String email,
      @NotNull(message = "角色不能为空") @RequestParam String[] roles, Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    SysAdmin user = sysAdminService.getUserByName(username);
    if (user != null) {
      return R.FAIL("账号已存在");
    }
    SysDepartment department = sysDepartmentService.getById(departmentId);
    if (department == null) {
      return R.FAIL("部门不存在");
    }
    SysPosition position = sysPositionService.getById(positionId);
    if (position == null) {
      return R.FAIL("职位不存在");
    }
//    List<Long> roleIds =new ArrayList<Long>(Arrays.asList(roles));
    List<Long> roleIds = Arrays.asList(roles).stream().map(s -> Long.parseLong(s.trim())).collect(
        Collectors.toList());
    if (roles.length > 0) {
      for (Long roleId : roleIds) {
        SysRole role = roleService.getById(roleId);
        if (role == null) {
          return R.FAIL("角色不存在");
        }
      }
    }
    SysAdmin sysAdmin = new SysAdmin();
    sysAdmin.setUsername(username);
    sysAdmin.setName(name);
    sysAdmin.setPassword(new BCryptPasswordEncoder().encode(password));
    sysAdmin.setSex(sex);
    sysAdmin.setAddress(address);
    sysAdmin.setEmail(email);
    sysAdmin.setJobNum(jobNum);
    sysAdmin.setPhone(phone);
    sysAdmin.setStatus(0);
    sysAdmin.setCreator(admin.getUsername());
    sysAdmin.setCreateTime(new Date());
    sysAdminService.addAdmin(sysAdmin, positionId, departmentId, roleIds);
    return R.SUCCESS("创建成功");
  }
  @Log(action="编辑",modelName= "系统用户",description="编辑管理员")
  @ApiOperation(value = "编辑用户", notes = "编辑用户接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:user:update')")
  @PostMapping("/update")
  public R add(@NotNull(message = "ID不能为空") @RequestParam Long id,
      @NotNull(message = "真实姓名不能为空") @RequestParam String name,
      @NotNull(message = "性别不能为空") @RequestParam Integer sex,
      @NotNull(message = "部门不能为空") @RequestParam Long departmentId,
      @NotNull(message = "职位不能为空") @RequestParam Long positionId,
      @RequestParam(required = false) String address,
      @RequestParam(required = false) String jobNum,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) String email,
      @NotNull(message = "角色不能为空") @RequestParam String[] roles, Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    SysAdmin sysAdmin = sysAdminService.getById(id);
    if (sysAdmin == null) {
      return R.FAIL("ID不存在");
    }
    SysDepartment department = sysDepartmentService.getById(departmentId);
    if (department == null) {
      return R.FAIL("部门不存在");
    }
    SysPosition position = sysPositionService.getById(positionId);
    if (position == null) {
      return R.FAIL("职位不存在");
    }
    List<Long> roleIds = Arrays.asList(roles).stream().map(s -> Long.parseLong(s.trim())).collect(
        Collectors.toList());
    List<SysRole> roleList = new ArrayList<>();
    if (roles.length > 0) {
      for (Long roleId : roleIds) {
        SysRole role = roleService.getById(roleId);
        roleList.add(role);
        if (role == null) {
          return R.FAIL("角色不存在");
        }
      }
    }
    sysAdmin.setName(name);
    sysAdmin.setSex(sex);
    sysAdmin.setAddress(address);
    sysAdmin.setEmail(email);
    sysAdmin.setJobNum(jobNum);
    sysAdmin.setPhone(phone);
    sysAdmin.setLastUpdate(admin.getUsername());
    sysAdmin.setUpdateTime(new Date());
    log.info("roles:{}", JSON.toJSONString(roles));
    sysAdminService.updateAdmin(sysAdmin, position, department, roleList);
    return R.SUCCESS("更新成功");
  }
  @Log(action="详情",modelName= "系统用户",description="获取管理员信息")
  @ApiOperation(value = "获取用户信息", notes = "获取用户信息接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:user:detail')")
  @PostMapping("/detail")
  public R detail(@NotNull(message = "ID不能为空") @RequestParam Long id) {
    SysAdminDto sysAdmin = sysAdminService.getUserInfo(id);
    if (null == sysAdmin) {
      return R.FAIL("ID不存在");
    }
    List<SysRole> roleList = roleService.getRoleByUserId(id);
    if (roleList.size() > 0) {
      List<Long> idList = roleList.stream().map(SysRole::getId).collect(Collectors.toList());
      sysAdmin.setRoles(StringUtils.join(idList, ","));
    }
    return R.SUCCESS(sysAdmin);
  }

  @Log(action="删除",modelName= "系统用户",description="删除管理员")
  @ApiOperation(value = "删除用户信息", notes = "删除用户信息接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:user:delete')")
  @DeleteMapping("/delete")
  public R delete(@NotNull(message = "ID不能为空") @RequestParam Long id) {
    SysAdmin sysAdmin = sysAdminService.getById(id);
    if (null == sysAdmin) {
      return R.FAIL("ID不存在");
    }
    sysAdminService.deleteUser(id);
    return R.SUCCESS("删除成功");
  }

  @Log(action="重置密码",modelName= "系统用户",description="重置用户密码")
  @ApiOperation(value = "重置用户密码", notes = "重置用户密码接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:user:update')")
  @PostMapping("/password")
  public R password(@NotNull(message = "ID不能为空") @RequestParam Long id,
      @NotNull(message = "密码不能为空") @RequestParam String password, Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    SysAdmin sysAdmin = sysAdminService.getById(id);
    if (null == sysAdmin) {
      return R.FAIL("ID不存在");
    }
    sysAdmin.setPassword(new BCryptPasswordEncoder().encode(password));
    sysAdmin.setLastUpdate(admin.getUsername());
    sysAdmin.setUpdateTime(new Date());
    sysAdminService.updateById(sysAdmin);
    return R.SUCCESS("重置成功");
  }

}
