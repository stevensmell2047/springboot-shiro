package com.ggw.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.entity.SysAdmin;
import com.ggw.entity.SysDepartment;
import com.ggw.entity.SysRole;
import com.ggw.entity.SysUserToDepartment;
import com.ggw.service.SysAdminService;
import com.ggw.service.SysDepartmentService;
import com.ggw.service.SysUserToDepartmentService;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-01 11:19:18
 */
@RestController
@RequestMapping("/sys/department")
public class SysDepartmentController {

  @Autowired
  private SysDepartmentService sysDepartmentService;
  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private SysUserToDepartmentService sysUserToDepartmentService;

  @Log(action="列表",modelName= "部门管理",description="查询部门列表")
  @ApiOperation(value = "查询部门列表", notes = "查询部门列表接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:department:list')")
  @PostMapping("/list")
  public R list() {
    return R.SUCCESS(sysDepartmentService.getDepTree());
  }
  @Log(action="新增",modelName= "部门管理",description="新增部门")
  @ApiOperation(value = "新增部门", notes = "新增部门接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:department:add')")
  @PostMapping("/add")
  public R add(@NotNull(message = "名称不能为空") @RequestParam String name,
      @RequestParam(required = false) Long parentId,
      @RequestParam(required = false) Long managerId, Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    String parentName = null;
    String managerName = null;
    if (null != parentId) {
      SysDepartment parent = sysDepartmentService.getById(parentId);
      if (null == parent) {
        return R.FAIL("上级部门不存在");
      } else {
        parentName = parent.getName();
      }
    }
    if (null != managerId) {
      SysAdmin manager = sysAdminService.getById(managerId);
      if (null == manager) {
        return R.FAIL("负责人不存在");
      } else {
        managerName = manager.getName();
      }
    }
    SysDepartment sysDepartment = sysDepartmentService.getDepByName(name);
    if (null != sysDepartment) {
      return R.FAIL("名称已存在");
    }
    SysDepartment department = new SysDepartment();
    department.setName(name);
    if (null == parentId) {
      department.setParentId(0L);
    } else {
      department.setParentId(parentId);
    }
    department.setParent(parentName);
    department.setManager(managerName);
    department.setManagerId(managerId);
    department.setCreator(admin.getUsername());
    department.setCreateTime(new Date());
    sysDepartmentService.save(department);
    return R.SUCCESS("创建成功");
  }

  @Log(action="编辑",modelName= "部门管理",description="编辑部门")
  @ApiOperation(value = "编辑部门", notes = "编辑部门接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:department:update')")
  @PostMapping("/update")
  public R update(@NotNull(message = "ID不能为空") @RequestParam Long id,
      @NotNull(message = "名称不能为空") @RequestParam String name,
      @RequestParam(required = false) Long parentId,
      @RequestParam(required = false) Long managerId, Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    String managerName = null;
    if (null != parentId && 0 != parentId) {
      SysDepartment parent = sysDepartmentService.getById(parentId);
      if (null == parent) {
        return R.FAIL("上级部门不存在");
      }
    }
    if (null != managerId) {
      SysAdmin manager = sysAdminService.getById(managerId);
      if (null == manager) {
        return R.FAIL("负责人不存在");
      } else {
        managerName = manager.getName();
      }
    }
    SysDepartment department = sysDepartmentService.getById(id);
    if (null == department) {
      return R.FAIL("ID不存在");
    }
    SysDepartment sysDepartment = sysDepartmentService
        .getDepWithoutName(name, department.getName());
    if (null != sysDepartment) {
      return R.FAIL("名称已存在");
    }
    department.setManager(managerName);
    department.setName(name);
    department.setParentId(parentId);
    department.setManagerId(managerId);
    department.setLastUpdate(admin.getUsername());
    department.setUpdateTime(new Date());
    sysDepartmentService.saveOrUpdate(department);
    return R.SUCCESS("更新成功");
  }

  @Log(action="详情",modelName= "部门管理",description="获取部门信息")
  @ApiOperation(value = "获取部门信息", notes = "获取部门信息接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:department:update')")
  @PostMapping("/detail")
  public R detail(@NotNull(message = "ID不能为空") @RequestParam Long id) {
    SysDepartment department = sysDepartmentService.getById(id);
    if (null == department) {
      return R.FAIL("ID不存在");
    }

    return R.SUCCESS(department);
  }

  @ApiOperation(value = "根据部门ID查询所有用户", notes = "根据部门ID查询所有用户接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:department:list')")
  @PostMapping("/users")
  public R users(@RequestParam(required = false) Long id,
      @NotNull(message = "页码不能为空") @RequestParam(value = "page", required = true) Integer page,
      @NotNull(message = "页数不能为空") @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(required = false) String orderName,
      @RequestParam(required = false) Integer orderNum) {
    if (null != id) {
      SysDepartment department = sysDepartmentService.getById(id);
      if (null == department) {
        return R.FAIL("ID不存在");
      }
    }

    return R.SUCCESS(sysAdminService.getUsersByDepId(id, page, limit, orderName, orderNum));
  }

  @Log(action="删除",modelName= "部门管理",description="删除部门")
  @ApiOperation(value = "删除部门", notes = "删除部门接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:department:delete')")
  @DeleteMapping("/delete")
  public R delete(@NotNull(message = "ID不能为空") @RequestParam Long id) {
    SysDepartment department = sysDepartmentService.getById(id);
    if (null == department) {
      return R.FAIL("ID不存在");
    }
    int num = sysUserToDepartmentService.countUserByDepId(id);
    if (num > 0) {
      return R.FAIL("当前部门存在用户,不能删除");
    }
    List<SysDepartment> subDep=sysDepartmentService.getSubDepartmentById(id);
    if(subDep.size()>0){
      return R.FAIL("当前部门存在子部门,不能删除");
    }
    sysDepartmentService.removeById(id);
    return R.SUCCESS("删除成功");
  }
}
