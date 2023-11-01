package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.ProjectProcessQuestions;
import com.ggw.dao.ProjectProcessQuestionsMapper;
import com.ggw.service.ProjectProcessQuestionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 申请流程常见问题表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectProcessQuestionsServiceImpl extends
    ServiceImpl<ProjectProcessQuestionsMapper, ProjectProcessQuestions> implements
    ProjectProcessQuestionsService {

  @Autowired
  private ProjectProcessQuestionsMapper mapper;

  @Override
  public List<ProjectProcessQuestions> getByProcessId(Long processId) {
    QueryWrapper<ProjectProcessQuestions> wrapper = new QueryWrapper<>();
    wrapper.eq("process_id", processId);
    return mapper.selectList(wrapper);
  }

  @Override
  public void deleteByProcessId(Long processId) {
    QueryWrapper<ProjectProcessQuestions> wrapper = new QueryWrapper<>();
    wrapper.eq("process_id", processId);
    mapper.delete(wrapper);
  }
}
