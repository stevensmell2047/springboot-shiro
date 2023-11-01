package com.ggw.controller;


import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.entity.Project;
import com.ggw.entity.ProjectProcessData;
import com.ggw.entity.ProjectProcessQuestions;
import com.ggw.entity.ProjectProcessReqSteps;
import com.ggw.entity.SysAdmin;
import com.ggw.entity.SysRole;
import com.ggw.service.ProjectService;
import com.ggw.service.SysAdminService;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 项目管理 前端控制器
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

  @Autowired
  private ProjectService projectService;
  @Autowired
  private SysAdminService sysAdminService;

  @Log(action = "列表", modelName = "项目管理", description = "查询项目管理")
  @ApiOperation(value = "查询项目管理", notes = "查询项目管理接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('project')")
  @PostMapping("/list")
  public R list(@RequestParam(required = false) String name,
      @RequestParam(required = false) String manager,
      @RequestParam(required = false) Integer status,
      @RequestParam(required = false) String description,
      @NotNull(message = "页码不能为空") @RequestParam(value = "page", required = true) Integer page,
      @NotNull(message = "页数不能为空") @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(required = false) String orderName,
      @RequestParam(required = false) Integer orderNum) {

    return R.SUCCESS(projectService.getProjectList(name, manager, status, description, page,
        limit, orderName, orderNum));
  }

  @Log(action = "新增", modelName = "项目管理", description = "新增项目管理")
  @PreAuthorize("hasAuthority('project:add')")
  @PostMapping("/add")
  public R add(@NotNull(message = "名称不能为空") @RequestParam String name,
      @NotNull(message = "负责人不能为空") @RequestParam Long managerId,
      @RequestParam String description, Principal principal) {
    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());
    Project project = projectService.getProjectByName(name);
    if (null != project) {
      return R.FAIL("名称已存在");
    }
    SysAdmin manager = sysAdminService.getById(managerId);
    if (null == manager) {
      return R.FAIL("负责人不存在");
    }
    Project pro = new Project();
    pro.setName(name);
    pro.setManagerId(managerId);
    pro.setManager(manager.getUsername());
    pro.setDescription(description);
    pro.setStatus(0);
    pro.setCreator(sysUser.getUsername());
    pro.setCreateTime(new Date());
    projectService.save(pro);
    return R.SUCCESS("创建成功");
  }

  @Log(action = "编辑", modelName = "项目管理", description = "编辑项目管理")
  @PreAuthorize("hasAuthority('project:update')")
  @PostMapping("/update")
  public R update(@NotNull(message = "名称不能为空") @RequestParam Long id,
      @NotNull(message = "名称不能为空") @RequestParam String name,
      @NotNull(message = "负责人不能为空") @RequestParam Long managerId,
      @RequestParam String description, Principal principal) {
    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());
    Project project = projectService.getById(id);
    if (null == project) {
      return R.FAIL("ID不存在");
    }
    Project project1 = projectService.getProjectWithoutName(name, project.getName());
    if (null != project1) {
      return R.FAIL("名称已存在");
    }
    SysAdmin manager = sysAdminService.getById(managerId);
    if (null == manager) {
      return R.FAIL("负责人不存在");
    }
    project.setName(name);
    project.setManagerId(managerId);
    project.setManager(manager.getUsername());
    project.setDescription(description);
    project.setLastUpdate(sysUser.getUsername());
    project.setUpdateTime(new Date());
    projectService.saveOrUpdate(project);
    return R.SUCCESS("编辑成功");
  }

  @Log(action = "关闭项目", modelName = "项目管理", description = "关闭项目")
  @PreAuthorize("hasAuthority('project:close')")
  @PostMapping("/close")
  public R close(@NotNull(message = "名称不能为空") @RequestParam Long id, Principal principal) {
    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());
    Project project = projectService.getById(id);
    if (null == project) {
      return R.FAIL("ID不存在");
    }
    project.setStatus(2);
    project.setLastUpdate(sysUser.getUsername());
    project.setUpdateTime(new Date());
    projectService.saveOrUpdate(project);
    return R.SUCCESS("关闭成功");
  }
}
