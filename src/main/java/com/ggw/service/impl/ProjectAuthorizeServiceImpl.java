package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.entity.Member;
import com.ggw.entity.ProjectAuthorize;
import com.ggw.dao.ProjectAuthorizeMapper;
import com.ggw.entity.ProjectSubAuthorize;
import com.ggw.entity.ProjectSubAuthorizeField;
import com.ggw.service.ProjectAuthorizeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.service.ProjectAuthorizeSummaryService;
import com.ggw.service.ProjectSubAuthorizeFieldService;
import com.ggw.service.ProjectSubAuthorizeService;
import com.ggw.util.ReflectUtils;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 合作机构表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectAuthorizeServiceImpl extends
    ServiceImpl<ProjectAuthorizeMapper, ProjectAuthorize> implements ProjectAuthorizeService {

  @Autowired
  private ProjectAuthorizeMapper mapper;
  @Autowired
  private ProjectAuthorizeSummaryService projectAuthorizeSummaryService;
  @Autowired
  private ProjectSubAuthorizeFieldService projectSubAuthorizeFieldService;
  @Autowired
  private ProjectSubAuthorizeService projectSubAuthorizeService;


  @Override
  public IPage<ProjectAuthorize> getList(String name, String project, String contact,
      String contactDetails, String address, String description, Integer page, Integer limit,
      String orderName, Integer orderNum) {
    IPage<ProjectAuthorize> iPage = new Page<>(page, limit);
    QueryWrapper<ProjectAuthorize> wrapper = new QueryWrapper<>();
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
        orderName = ReflectUtils.getValue(new ProjectAuthorize(), orderName);
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
  public ProjectAuthorize getByName(String name) {
    QueryWrapper<ProjectAuthorize> wrapper = new QueryWrapper<>();
    wrapper.eq("name", name);
    return mapper.selectOne(wrapper);
  }

  @Override
  public ProjectAuthorize getWithoutName(String newName, String oldName) {
    QueryWrapper<ProjectAuthorize> wrapper = new QueryWrapper<>();
    wrapper.eq("name", newName);
    wrapper.ne("name", oldName);
    return mapper.selectOne(wrapper);
  }

  @Transactional
  @Override
  public void deleteAuthorize(Long id) {
    mapper.deleteById(id);
    //删除机构简介
    projectAuthorizeSummaryService.deleteByAuthorizeId(id);
    //删除机构字段
    projectSubAuthorizeFieldService.deleteByAuthorizeId(id);
    //删除机构数据
    projectSubAuthorizeService.deleteByAuthorizedId(id);
  }

  @Override
  public void saveAuthorize(ProjectAuthorize authorize, List<ProjectSubAuthorizeField> fieldList,
      String admin) {
    mapper.insert(authorize);
    Long id = authorize.getId();
    Date now = new Date();

    if (fieldList.size() > 0) {
      for (ProjectSubAuthorizeField field : fieldList) {
        field.setAuthorizeId(id);
        field.setCreator(admin);
        field.setCreateTime(now);
        projectSubAuthorizeFieldService.save(field);
      }
    }
  }
}
