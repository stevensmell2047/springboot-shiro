package com.ggw.service;

import com.ggw.entity.Project;
import com.ggw.entity.ProjectProcess;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ggw.entity.ProjectProcessData;
import com.ggw.entity.ProjectProcessQuestions;
import com.ggw.entity.ProjectProcessReqSteps;
import java.util.List;

/**
 * <p>
 * 申请流程表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectProcessService extends IService<ProjectProcess> {

  /**
   * 通过项目ID查询
   * @param projectId
   * @return
   */
  ProjectProcess getByProjectId(Long projectId);
  /**
   * 创建申请流程
   * @param project
   * @param description
   * @param dataList
   * @param conditions
   * @param stepList
   * @param questionsList
   */
  void saveProjectProcess(Project project, String description, List<ProjectProcessData> dataList,
      String conditions, List<ProjectProcessReqSteps> stepList,List<ProjectProcessQuestions> questionsList,String adminName);
}
