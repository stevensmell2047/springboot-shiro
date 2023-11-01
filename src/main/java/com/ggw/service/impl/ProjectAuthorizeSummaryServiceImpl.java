package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.ProjectAuthorizeSummary;
import com.ggw.dao.ProjectAuthorizeSummaryMapper;
import com.ggw.service.ProjectAuthorizeSummaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 机构简介表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectAuthorizeSummaryServiceImpl extends
    ServiceImpl<ProjectAuthorizeSummaryMapper, ProjectAuthorizeSummary> implements
    ProjectAuthorizeSummaryService {

  @Autowired
  private ProjectAuthorizeSummaryMapper mapper;

  @Override
  public void deleteByAuthorizeId(Long authorizedId) {
    QueryWrapper<ProjectAuthorizeSummary> wrapper = new QueryWrapper<>();
    wrapper.eq("authorize_id", authorizedId);
    mapper.delete(wrapper);
  }

  @Override
  public void saveSummary(Long authorizedId, String content, String admin) {
    QueryWrapper<ProjectAuthorizeSummary> wrapper = new QueryWrapper<>();
    wrapper.eq("authorize_id", authorizedId);
    ProjectAuthorizeSummary summary = mapper.selectOne(wrapper);
    if (null == summary) {
      ProjectAuthorizeSummary projectAuthorizeSummary = new ProjectAuthorizeSummary();
      projectAuthorizeSummary.setAuthorizeId(authorizedId);
      projectAuthorizeSummary.setContent(content);
      projectAuthorizeSummary.setCreator(admin);
      projectAuthorizeSummary.setCreateTime(new Date());
      mapper.insert(projectAuthorizeSummary);
    } else {
      summary.setContent(content);
      summary.setLastUpdate(admin);
      summary.setUpdateTime(new Date());
      mapper.updateById(summary);
    }
  }
}
