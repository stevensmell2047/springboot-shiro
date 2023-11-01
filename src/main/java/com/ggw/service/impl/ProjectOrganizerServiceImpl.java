package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.entity.ProjectAuthorize;
import com.ggw.entity.ProjectOrganizer;
import com.ggw.dao.ProjectOrganizerMapper;
import com.ggw.entity.ProjectOrganizerQuota;
import com.ggw.service.ProjectOrganizerQuotaService;
import com.ggw.service.ProjectOrganizerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.util.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 主管单位表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectOrganizerServiceImpl extends
    ServiceImpl<ProjectOrganizerMapper, ProjectOrganizer> implements ProjectOrganizerService {

  @Autowired
  private ProjectOrganizerMapper mapper;
  @Autowired
  private ProjectOrganizerQuotaService quotaService;

  @Override
  public IPage<ProjectOrganizer> getList(String name, String project, String code,
      String description,
      String contact, String contactDetails, String address, Integer page, Integer limit,
      String orderName, Integer orderNum) {
    IPage<ProjectOrganizer> iPage = new Page<>(page, limit);
    QueryWrapper<ProjectOrganizer> wrapper = new QueryWrapper<>();
    if (null != name) {
      wrapper.like("name", name);
    }
    if (null != project) {
      wrapper.like("project", project);
    }
    if (null != contact) {
      wrapper.like("contact", contact);
    }
    if (null != contactDetails) {
      wrapper.like("contact_details", contactDetails);
    }
    if (null != address) {
      wrapper.like("address", address);
    }
    if (null != description) {
      wrapper.like("description", description);
    }
    if (ObjectUtils.isNotNull(orderNum)) {
      if (orderName != null && !"id".equals(orderName)) {
        orderName = ReflectUtils.getValue(new ProjectOrganizer(), orderName);
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
  public ProjectOrganizer getByName(String name) {
    QueryWrapper<ProjectOrganizer> wrapper = new QueryWrapper<>();
    wrapper.eq("name", name);
    return mapper.selectOne(wrapper);
  }

  @Override
  public ProjectOrganizer getWithOutName(String newName, String oldName) {
    QueryWrapper<ProjectOrganizer> wrapper = new QueryWrapper<>();
    wrapper.eq("name", newName);
    wrapper.ne("name", oldName);
    return mapper.selectOne(wrapper);
  }
  @Transactional
  @Override
  public void deleteOrganizer(Long id) {
    mapper.deleteById(id);
    quotaService.deleteByOrganizerId(id);
  }
}
