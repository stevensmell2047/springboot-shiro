package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.ProjectProcessData;
import com.ggw.dao.ProjectProcessDataMapper;
import com.ggw.service.ProjectProcessDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流程材料表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectProcessDataServiceImpl extends
    ServiceImpl<ProjectProcessDataMapper, ProjectProcessData> implements ProjectProcessDataService {

  @Autowired
  private ProjectProcessDataMapper mapper;

  @Override
  public List<ProjectProcessData> getListByProcessId(Long processId) {
    QueryWrapper<ProjectProcessData> wrapper = new QueryWrapper<>();
    wrapper.eq("process_id", processId);
    return mapper.selectList(wrapper);
  }

  @Override
  public void deleteByProcessId(Long processId) {
    QueryWrapper<ProjectProcessData> wrapper = new QueryWrapper<>();
    wrapper.eq("process_id", processId);
    mapper.delete(wrapper);
  }
}
