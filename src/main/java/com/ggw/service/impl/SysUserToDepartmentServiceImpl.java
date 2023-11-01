package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.SysUserToDepartment;
import com.ggw.dao.SysUserToDepartmentMapper;
import com.ggw.service.SysUserToDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-01 11:56:58
 */
@Service
public class SysUserToDepartmentServiceImpl extends
    ServiceImpl<SysUserToDepartmentMapper, SysUserToDepartment> implements
    SysUserToDepartmentService {

  @Autowired
  private SysUserToDepartmentMapper mapper;

  @Override
  public int countUserByDepId(Long depId) {
    QueryWrapper<SysUserToDepartment> wrapper = new QueryWrapper<>();
    wrapper.eq("dep_id", depId);
    return mapper.selectCount(wrapper);
  }

  @Override
  public List<Long> getUserIdsByDepId(Long depId) {
    QueryWrapper<SysUserToDepartment> wrapper = new QueryWrapper<>();
    wrapper.eq("dep_id", depId);
    List<SysUserToDepartment> departments = mapper.selectList(wrapper);
    if (departments.size() > 0) {
      return departments.stream().map(SysUserToDepartment::getAdminId).collect(Collectors.toList());
    }
    return null;
  }

  @Override
  public SysUserToDepartment getByUserId(Long userId) {
    QueryWrapper<SysUserToDepartment> wrapper = new QueryWrapper<>();
    wrapper.eq("admin_id", userId);
    return mapper.selectOne(wrapper);
  }

  @Override
  public void deleteByUserId(Long userId) {
    QueryWrapper<SysUserToDepartment> wrapper = new QueryWrapper<>();
    wrapper.eq("admin_id", userId);
    mapper.delete(wrapper);
  }
}
