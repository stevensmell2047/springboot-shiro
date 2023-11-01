package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.Project;
import com.ggw.entity.ProjectProcess;
import com.ggw.dao.ProjectProcessMapper;
import com.ggw.entity.ProjectProcessData;
import com.ggw.entity.ProjectProcessQuestions;
import com.ggw.entity.ProjectProcessReqSteps;
import com.ggw.service.ProjectProcessDataService;
import com.ggw.service.ProjectProcessQuestionsService;
import com.ggw.service.ProjectProcessReqStepsService;
import com.ggw.service.ProjectProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 申请流程表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectProcessServiceImpl extends
    ServiceImpl<ProjectProcessMapper, ProjectProcess> implements ProjectProcessService {

  @Autowired
  private ProjectProcessMapper mapper;
  @Autowired
  private ProjectProcessDataService projectProcessDataService;

  @Autowired
  private ProjectProcessReqStepsService projectProcessReqStepsService;
  @Autowired
  private ProjectProcessQuestionsService projectProcessQuestionsService;

  @Override
  public ProjectProcess getByProjectId(Long projectId) {
    QueryWrapper<ProjectProcess> wrapper = new QueryWrapper<>();
    wrapper.eq("project_id", projectId);
    return mapper.selectOne(wrapper);
  }
  @Transactional
  @Override
  public void saveProjectProcess(Project project, String description,
      List<ProjectProcessData> dataList, String conditions, List<ProjectProcessReqSteps> stepList,
      List<ProjectProcessQuestions> questionsList, String adminName) {
    ProjectProcess process = getByProjectId(project.getId());
    Long processId = null;
    if (null == process) {
      ProjectProcess projectProcess = new ProjectProcess();
      projectProcess.setProjectId(project.getId());
      projectProcess.setCondition(conditions);
      projectProcess.setDescription(description);
      projectProcess.setCreator(adminName);
      projectProcess.setCreateTime(new Date());
      mapper.insert(projectProcess);
      processId = projectProcess.getId();
    } else {
      process.setCondition(conditions);
      process.setDescription(description);
      mapper.updateById(process);
      processId = process.getId();
      if (dataList.size() > 0) {
        List<ProjectProcessData> processData = projectProcessDataService
            .getListByProcessId(processId);
        if (processData.size() > 0) {
          projectProcessDataService.deleteByProcessId(processId);
        }
      }
      if (stepList.size() > 0) {
        List<ProjectProcessReqSteps> reqSteps = projectProcessReqStepsService
            .getByProcessId(processId);
        if (reqSteps.size() > 0) {
          projectProcessReqStepsService.deleteByProcessId(processId);
        }
      }
      if (questionsList.size() > 0) {
        List<ProjectProcessQuestions> projectProcessQuestions = projectProcessQuestionsService
            .getByProcessId(processId);
        if(projectProcessQuestions.size()>0){
          projectProcessQuestionsService.deleteByProcessId(processId);
        }
      }
    }

    if (dataList.size() > 0) {
      for (ProjectProcessData data : dataList) {
        data.setProjectId(project.getId());
        data.setProcessId(processId);
        projectProcessDataService.save(data);
      }
    }
    if (stepList.size() > 0) {
      for (ProjectProcessReqSteps step : stepList) {
        step.setProjectId(project.getId());
        step.setProcessId(processId);
        projectProcessReqStepsService.save(step);
      }
    }
    if (questionsList.size() > 0) {
      for (ProjectProcessQuestions qes : questionsList) {
        qes.setProjectId(project.getId());
        qes.setProcessId(processId);
        projectProcessQuestionsService.save(qes);
      }
    }
  }
}
