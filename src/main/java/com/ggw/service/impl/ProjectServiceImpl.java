package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.entity.Project;
import com.ggw.dao.ProjectMapper;
import com.ggw.entity.ProjectProcess;
import com.ggw.entity.ProjectProcessData;
import com.ggw.entity.ProjectProcessQuestions;
import com.ggw.entity.ProjectProcessReqSteps;
import com.ggw.service.ProjectProcessDataService;
import com.ggw.service.ProjectProcessQuestionsService;
import com.ggw.service.ProjectProcessReqStepsService;
import com.ggw.service.ProjectProcessService;
import com.ggw.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.util.ReflectUtils;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 项目管理 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements
    ProjectService {

  @Autowired
  private ProjectMapper mapper;


  @Override
  public IPage<Project> getProjectList(String name, String manager, Integer status,
      String description, Integer page,
      Integer limit, String orderName, Integer orderNum) {
    IPage<Project> iPage = new Page<>(page,limit);
    QueryWrapper<Project> wrapper = new QueryWrapper<>();
    if (null != name) {
      wrapper.like("name", name);
    }
    if (null != manager) {
      wrapper.like("manager", manager);
    }
    if (null != status) {
      wrapper.eq("status", status);
    }
    if (null != description) {
      wrapper.like("description", description);
    }
    if (ObjectUtils.isNotNull(orderNum)) {
      if (orderName != null && !"id".equals(orderName)) {
        orderName = ReflectUtils.getValue(new Project(), orderName);
      }
      if (orderNum == 0) {
        wrapper.orderByAsc(orderName);
      } else if (orderNum == 1) {
        wrapper.orderByDesc(orderName);
      } else {
        wrapper.orderByDesc("id");
      }
    } else {
      wrapper.orderByDesc("id");
    }
    return mapper.selectPage(iPage, wrapper);
  }

  @Override
  public Project getProjectByName(String name) {
    QueryWrapper<Project> wrapper = new QueryWrapper<>();
    wrapper.eq("name", name);
    return mapper.selectOne(wrapper);
  }

  @Override
  public Project getProjectWithoutName(String newName, String oldName) {
    QueryWrapper<Project> wrapper = new QueryWrapper<>();
    wrapper.eq("name", newName);
    wrapper.ne("name", oldName);
    return mapper.selectOne(wrapper);
  }


}
