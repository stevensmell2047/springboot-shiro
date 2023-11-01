package com.ggw.controller;


import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.entity.Project;
import com.ggw.entity.ProjectOrganizer;
import com.ggw.entity.SysAdmin;
import com.ggw.service.ProjectOrganizerQuotaService;
import com.ggw.service.ProjectOrganizerService;
import com.ggw.service.ProjectService;
import com.ggw.service.SysAdminService;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import java.security.Principal;
import java.util.Date;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 主管单位表 前端控制器
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@RestController
@RequestMapping("/project/organizer")
public class ProjectOrganizerController {

  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private ProjectOrganizerService projectOrganizerService;
  @Autowired
  private ProjectService projectService;
  @Autowired
  private ProjectOrganizerQuotaService projectOrganizerQuotaService;

  @Log(action = "列表", modelName = "主管单位", description = "主管单位列表")
  @ApiOperation(value = "查询主管单位列表", notes = "查询主管单位列表接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:organizer:list')")
  @PostMapping("/list")
  public R list(@RequestParam(required = false) String name,
      @RequestParam(required = false) String project,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String description,
      @RequestParam(required = false) String contact,
      @RequestParam(required = false) String contactDetails,
      @RequestParam(required = false) String address,
      @NotNull(message = "页码不能为空") @RequestParam(value = "page", required = true) Integer page,
      @NotNull(message = "页数不能为空") @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(required = false) String orderName,
      @RequestParam(required = false) Integer orderNum) {

    return R.SUCCESS(projectOrganizerService
        .getList(name, project, code, description, contact, contactDetails, address, page,
            limit, orderName, orderNum));
  }

  @Log(action = "新增", modelName = "主管单位", description = "新增主管单位")
  @ApiOperation(value = "新增主管单位", notes = "新增主管单位接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:organizer:add')")
  @PostMapping("/add")
  public R add(@RequestParam String name,
      @RequestParam Long projectId,
      @RequestParam Integer quota,
      @RequestParam String code,
      @RequestParam(required = false) String description,
      @RequestParam String contact,
      @RequestParam String contactDetails,
      @RequestParam(required = false) String address, Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    Project project = projectService.getById(projectId);
    if (null == project) {
      return R.FAIL("项目不存在");
    }
    ProjectOrganizer projectOrganizer = projectOrganizerService.getByName(name);
    if (null != projectOrganizer) {
      return R.FAIL("名称已存在");
    }
    ProjectOrganizer organizer = new ProjectOrganizer();
    organizer.setName(name);
    organizer.setProjectId(projectId);
    organizer.setQuota(quota);
    organizer.setUsedQuota(0);
    organizer.setCode(code);
    organizer.setDescription(description);
    organizer.setContact(contact);
    organizer.setContactDetails(contactDetails);
    organizer.setAddress(address);
    organizer.setCreator(admin.getUsername());
    organizer.setCreateTime(new Date());
    projectOrganizerService.save(organizer);
    return R.SUCCESS("新增成功");
  }

  @Log(action = "编辑", modelName = "主管单位", description = "编辑主管单位")
  @ApiOperation(value = "编辑主管单位", notes = "编辑主管单位接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:organizer:update')")
  @PostMapping("/update")
  public R update(@RequestParam Long id, @RequestParam String name,
      @RequestParam Long projectId,
      @RequestParam Integer quota,
      @RequestParam String code,
      @RequestParam(required = false) String description,
      @RequestParam String contact,
      @RequestParam String contactDetails,
      @RequestParam(required = false) String address, Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    ProjectOrganizer organizer = projectOrganizerService.getById(id);
    if (null == organizer) {
      return R.FAIL("主管单位不存在");
    }
    Project project = projectService.getById(projectId);
    if (null == project) {
      return R.FAIL("项目不存在");
    }
    ProjectOrganizer projectOrganizer = projectOrganizerService
        .getWithOutName(name, organizer.getName());
    if (null != projectOrganizer) {
      return R.FAIL("名称已存在");
    }
    organizer.setName(name);
    organizer.setQuota(quota);
    organizer.setCode(code);
    organizer.setDescription(description);
    organizer.setContact(contact);
    organizer.setContactDetails(contactDetails);
    organizer.setAddress(address);
    organizer.setLastUpdate(admin.getUsername());
    organizer.setUpdateTime(new Date());
    projectOrganizerService.save(organizer);
    return R.SUCCESS("编辑成功");
  }
  @Log(action = "删除", modelName = "主管单位", description = "删除主管单位")
  @ApiOperation(value = "删除主管单位", notes = "删除主管单位接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:organizer:delete')")
  @PostMapping("/delete")
  public R update(@RequestParam Long id) {
    ProjectOrganizer organizer = projectOrganizerService.getById(id);
    if (null == organizer) {
      return R.FAIL("主管单位不存在");
    }
    projectOrganizerService.deleteOrganizer(id);
    return R.SUCCESS("删除成功");
  }
  @Log(action = "额度设置", modelName = "主管单位", description = "额度设置")
  @ApiOperation(value = "额度设置", notes = "额度设置接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:organizer:quota')")
  @PostMapping("/quota")
  public R update(@RequestParam Long id, @RequestParam Map<Long, Integer> quotaMap,
      Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    ProjectOrganizer organizer = projectOrganizerService.getById(id);
    if (null == organizer) {
      return R.FAIL("主管单位不存在");
    }
    projectOrganizerQuotaService.saveQuota(id,quotaMap);
    return R.SUCCESS("额度设置成功");
  }

}
