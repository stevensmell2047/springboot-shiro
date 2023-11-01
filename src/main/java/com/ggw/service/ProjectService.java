package com.ggw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ggw.entity.ProjectProcessData;
import com.ggw.entity.ProjectProcessQuestions;
import com.ggw.entity.ProjectProcessReqSteps;
import java.util.List;

/**
 * <p>
 * 项目管理 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectService extends IService<Project> {

  /**
   * 获取项目列表
   *
   * @param name
   * @param manager
   * @param status
   * @param page
   * @param limit
   * @param orderName
   * @param orderNum
   * @return
   */
  IPage<Project> getProjectList(String name, String manager, Integer status, String description,
      Integer page,
      Integer limit, String orderName, Integer orderNum);

  /**
   * 查询名称是否存在
   *
   * @param name
   * @return
   */
  Project getProjectByName(String name);

  /**
   * 查询名称是否存在
   *
   * @param newName
   * @param oldName
   * @return
   */
  Project getProjectWithoutName(String newName, String oldName);


}
