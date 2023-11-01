package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.ProjectSubAuthorizeData;
import com.ggw.dao.ProjectSubAuthorizeDataMapper;
import com.ggw.service.ProjectSubAuthorizeDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-06 12:13:10
 */
@Service
public class ProjectSubAuthorizeDataServiceImpl extends
    ServiceImpl<ProjectSubAuthorizeDataMapper, ProjectSubAuthorizeData> implements
    ProjectSubAuthorizeDataService {

  @Autowired
  private ProjectSubAuthorizeDataMapper mapper;

  @Override
  public List<ProjectSubAuthorizeData> getBySubAuthorizedId(Long id) {
    QueryWrapper<ProjectSubAuthorizeData> wrapper = new QueryWrapper<>();
    wrapper.eq("sub_authorize_id", id);
    return mapper.selectList(wrapper);
  }

  @Override
  public void deleteByAuthorizedId(Long id) {
    QueryWrapper<ProjectSubAuthorizeData> wrapper = new QueryWrapper<>();
    wrapper.eq("authorize_id", id);
    mapper.delete(wrapper);
  }

  @Override
  public void deleteBySubAuthorizedId(Long id) {
    QueryWrapper<ProjectSubAuthorizeData> wrapper = new QueryWrapper<>();
    wrapper.eq("sub_authorize_id", id);
    mapper.delete(wrapper);

  }
}
