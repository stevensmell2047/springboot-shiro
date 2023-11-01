package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.ProjectProcessReqSteps;
import com.ggw.dao.ProjectProcessReqStepsMapper;
import com.ggw.service.ProjectProcessReqStepsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 申请步骤表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectProcessReqStepsServiceImpl extends ServiceImpl<ProjectProcessReqStepsMapper, ProjectProcessReqSteps> implements ProjectProcessReqStepsService {
  @Autowired
  private ProjectProcessReqStepsMapper mapper;
  @Override
  public List<ProjectProcessReqSteps> getByProcessId(Long processId) {
    QueryWrapper<ProjectProcessReqSteps> wrapper=new QueryWrapper<>();
    wrapper.eq("process_id",processId);
    return mapper.selectList(wrapper);
  }

  @Override
  public void deleteByProcessId(Long processId) {
    QueryWrapper<ProjectProcessReqSteps> wrapper=new QueryWrapper<>();
    wrapper.eq("process_id",processId);
    mapper.delete(wrapper);
  }
}
