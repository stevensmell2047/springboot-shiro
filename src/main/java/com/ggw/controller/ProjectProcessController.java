package com.ggw.controller;


import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.dto.ProjectProcessDto;
import com.ggw.entity.Project;
import com.ggw.entity.ProjectProcess;
import com.ggw.entity.ProjectProcessData;
import com.ggw.entity.ProjectProcessQuestions;
import com.ggw.entity.ProjectProcessReqSteps;
import com.ggw.entity.SysAdmin;
import com.ggw.service.ProjectProcessDataService;
import com.ggw.service.ProjectProcessQuestionsService;
import com.ggw.service.ProjectProcessReqStepsService;
import com.ggw.service.ProjectProcessService;
import com.ggw.service.ProjectService;
import com.ggw.service.SysAdminService;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/project/process")
public class ProjectProcessController {

  @Autowired
  private ProjectService projectService;
  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private ProjectProcessService projectProcessService;
  @Autowired
  private ProjectProcessDataService projectProcessDataService;
  @Autowired
  private ProjectProcessReqStepsService projectProcessReqStepsService;
  @Autowired
  private ProjectProcessQuestionsService projectProcessQuestionsService;

  @Log(action = "详情", modelName = "项目管理", description = "查询申请流程详情")
  @PreAuthorize("hasAuthority('project:process:detail')")
  @PostMapping("/detail")
  public R detail(@NotNull(message = "名称不能为空") @RequestParam Long id) {
    Project project = projectService.getById(id);
    if (null == project) {
      return R.FAIL("ID不存在");
    }
    ProjectProcessDto result = new ProjectProcessDto();
    ProjectProcess process = projectProcessService.getByProjectId(project.getId());
    if (null != process) {
      BeanUtils.copyProperties(process, result);
      result.setDataList(projectProcessDataService.getListByProcessId(process.getId()));
      result.setStepList(projectProcessReqStepsService.getByProcessId(process.getId()));
      result.setQuestionsList(projectProcessQuestionsService.getByProcessId(process.getId()));
    }
    return R.SUCCESS(result);
  }

  @Log(action = "编辑申请流程", modelName = "项目管理", description = "编辑申请流程")
  @PreAuthorize("hasAuthority('project:process:update')")
  @PostMapping("/update")
  public R update(@NotNull(message = "名称不能为空") @RequestParam Long id,
      @NotNull(message = "基本信息不能为空") @RequestParam String description,
      @NotNull(message = "基本信息不能为空") @RequestParam List<ProjectProcessData> dataList,
      @NotNull(message = "受理条件不能为空") @RequestParam String conditions,
      @NotNull(message = "流程不能为空") @RequestParam List<ProjectProcessReqSteps> stepList,
      @RequestParam(required = false) List<ProjectProcessQuestions> questionsList,
      Principal principal) {
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
