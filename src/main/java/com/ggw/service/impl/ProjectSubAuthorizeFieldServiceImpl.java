package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.ProjectSubAuthorizeField;
import com.ggw.dao.ProjectSubAuthorizeFieldMapper;
import com.ggw.service.ProjectSubAuthorizeFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 下级机构字段表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectSubAuthorizeFieldServiceImpl extends
    ServiceImpl<ProjectSubAuthorizeFieldMapper, ProjectSubAuthorizeField> implements
    ProjectSubAuthorizeFieldService {

  @Autowired
  private ProjectSubAuthorizeFieldMapper mapper;

  @Override
  public void deleteByAuthorizeId(Long authorizedId) {
    QueryWrapper<ProjectSubAuthorizeField> wrapper = new QueryWrapper<>();
    wrapper.eq("authorize_id", authorizedId);
    mapper.delete(wrapper);
  }

  @Override
  public List<ProjectSubAuthorizeField> getByAuthorizeId(Long authorizedId) {
    QueryWrapper<ProjectSubAuthorizeField> wrapper = new QueryWrapper<>();
    wrapper.eq("authorize_id", authorizedId);
    return mapper.selectList(wrapper);
  }

  @Override
  public ProjectSubAuthorizeField getByKey(String key) {
    QueryWrapper<ProjectSubAuthorizeField> wrapper = new QueryWrapper<>();
    wrapper.eq("key", key);
    return mapper.selectOne(wrapper);
  }

  @Override
  public ProjectSubAuthorizeField getWithoutKey(String newKey, String oldKey) {
    QueryWrapper<ProjectSubAuthorizeField> wrapper = new QueryWrapper<>();
    wrapper.eq("key", newKey);
    wrapper.ne("key", oldKey);
    return mapper.selectOne(wrapper);
  }
}
