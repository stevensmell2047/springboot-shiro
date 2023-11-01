package com.ggw.controller;


import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.entity.Project;
import com.ggw.entity.ProjectAuthorize;
import com.ggw.entity.ProjectAuthorizeSummary;
import com.ggw.entity.ProjectSubAuthorizeField;
import com.ggw.entity.SysAdmin;
import com.ggw.service.ProjectAuthorizeService;
import com.ggw.service.ProjectAuthorizeSummaryService;
import com.ggw.service.ProjectService;
import com.ggw.service.SysAdminService;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 合作机构表 前端控制器
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@RestController
@RequestMapping("/project/authorize")
public class ProjectAuthorizeController {

  @Autowired
  private ProjectService projectService;
  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private ProjectAuthorizeService projectAuthorizeService;
  @Autowired
  private ProjectAuthorizeSummaryService projectAuthorizeSummaryService;

  @Log(action = "列表", modelName = "合作机构", description = "查询合作结构")
  @ApiOperation(value = "查询合作机构", notes = "查询合作机构接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('authorize')")
  @PostMapping("/list")
  public R list(@RequestParam(required = false) String name,
      @RequestParam(required = false) String project,
      @RequestParam(required = false) String contact,
      @RequestParam(required = false) String contactDetails,
      @RequestParam(required = false) String address,
      @RequestParam(required = false) String description,
      @NotNull(message = "页码不能为空") @RequestParam(value = "page", required = true) Integer page,
      @NotNull(message = "页数不能为空") @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(required = false) String orderName,
      @RequestParam(required = false) Integer orderNum) {

    return R.SUCCESS(projectAuthorizeService
        .getList(name, project, contact, contactDetails, address, description, page,
            limit, orderName, orderNum));
  }

  @Log(action = "新增", modelName = "合作机构", description = "新增合作机构")
  @PreAuthorize("hasAuthority('project:authorize:add')")
  @PostMapping("/add")
  public R add(@NotNull(message = "名称不能为空") @RequestParam String name,
      @NotNull(message = "项目ID不能为空") @RequestParam Long projectId,
      @NotNull(message = "配额不能为空") @RequestParam Integer quota,
      @NotNull(message = "联系人不能为空") @RequestParam String contact,
      @NotNull(message = "联系方式不能为空") @RequestParam String contactDetails,
      @NotNull(message = "详细地址不能为空") @RequestParam String address,
      @NotNull(message = "开始时间不能为空") @RequestParam Date startTime,
      @NotNull(message = "结束时间不能为空") @RequestParam Date endTime,
      @RequestParam(required = false) List<ProjectSubAuthorizeField> fieldList,
      @RequestParam String description, Principal principal) {
    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());
    Project project = projectService.getById(projectId);
    if (null == project) {
      return R.FAIL("项目不存在");
    }
    ProjectAuthorize projectAuthorize = projectAuthorizeService.getByName(name);
    if (null != projectAuthorize) {
      return R.FAIL("名称已存在");
    }
    ProjectAuthorize authorize = new ProjectAuthorize();
    authorize.setEndTime(endTime);
    authorize.setName(name);
    authorize.setProject(project.getName());
    authorize.setProjectId(projectId);
    authorize.setContact(contact);
    authorize.setContactDetails(contactDetails);
    authorize.setStartTime(startTime);
    authorize.setDescription(description);
    authorize.setQuota(quota);
    authorize.setAddress(address);
    authorize.setUsedQuota(0);
    authorize.setCreator(sysUser.getUsername());
    authorize.setCreateTime(new Date());
    projectAuthorizeService.saveAuthorize(authorize, fieldList, sysUser.getUsername());
    return R.SUCCESS("创建成功");
  }

  @Log(action = "编辑", modelName = "合作结构", description = "编辑合作结构")
  @PreAuthorize("hasAuthority('project:authorize:update')")
  @PostMapping("/update")
  public R update(@NotNull(message = "ID不能为空") @RequestParam Long id,
      @NotNull(message = "名称不能为空") @RequestParam String name,
      @NotNull(message = "项目ID不能为空") @RequestParam Long projectId,
      @NotNull(message = "配额不能为空") @RequestParam Integer quota,
      @NotNull(message = "联系人不能为空") @RequestParam String contact,
      @NotNull(message = "联系方式不能为空") @RequestParam String contactDetails,
      @NotNull(message = "详细地址不能为空") @RequestParam String address,
      @NotNull(message = "开始时间不能为空") @RequestParam Date startTime,
      @NotNull(message = "结束时间不能为空") @RequestParam Date endTime,
      @RequestParam String description, Principal principal) {
    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());
    ProjectAuthorize projectAuthorize = projectAuthorizeService.getById(id);
    if (null == projectAuthorize) {
      return R.FAIL("ID不存在");
    }
    Project project = projectService.getById(projectId);
    if (null == project) {
      return R.FAIL("项目不存在");
    }
    ProjectAuthorize authorize = projectAuthorizeService
        .getWithoutName(name, projectAuthorize.getName());
    if (null != authorize) {
      return R.FAIL("名称已存在");
    }
    projectAuthorize.setEndTime(endTime);
    projectAuthorize.setName(name);
    projectAuthorize.setProject(project.getName());
    projectAuthorize.setContact(contact);
    projectAuthorize.setContactDetails(contactDetails);
    projectAuthorize.setStartTime(startTime);
    projectAuthorize.setDescription(description);
    projectAuthorize.setQuota(quota);
    projectAuthorize.setAddress(address);
    projectAuthorize.setLastUpdate(sysUser.getUsername());
    projectAuthorize.setUpdateTime(new Date());
    projectAuthorizeService.saveOrUpdate(authorize);
    return R.SUCCESS("编辑成功");
  }

  @Log(action = "删除", modelName = "合作结构", description = "删除合作结构")
  @PreAuthorize("hasAuthority('project:authorize:delete')")
  @PostMapping("/delete")
  public R delete(@NotNull(message = "ID不能为空") @RequestParam Long id) {
    ProjectAuthorize projectAuthorize = projectAuthorizeService.getById(id);
    if (null == projectAuthorize) {
      return R.FAIL("ID不存在");
    }
    projectAuthorizeService.deleteAuthorize(id);
    return R.SUCCESS("编辑成功");
  }

  @Log(action = "编辑机构简介", modelName = "合作机构", description = "编辑机构简介")
  @PreAuthorize("hasAuthority('project:authorize:summary')")
  @PostMapping("/summary")
  public R summary(@NotNull(message = "ID不能为空") @RequestParam Long id,
      @NotNull(message = "内容不能为空") @RequestParam String content, Principal principal) {
    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());
    ProjectAuthorize projectAuthorize = projectAuthorizeService.getById(id);
    if (null == projectAuthorize) {
      return R.FAIL("ID不存在");
    }
    projectAuthorizeSummaryService.saveSummary(id,content,sysUser.getUsername());
    return R.SUCCESS("编辑成功");
  }
}
