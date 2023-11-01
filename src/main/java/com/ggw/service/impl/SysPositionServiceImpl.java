package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.SysPosition;
import com.ggw.dao.SysPositionMapper;
import com.ggw.service.SysPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 职位表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-02 12:33:53
 */
@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionMapper, SysPosition> implements
    SysPositionService {

  @Autowired
  private SysPositionMapper mapper;

  @Override
  public SysPosition getByName(String name) {
    QueryWrapper<SysPosition> wrapper = new QueryWrapper<>();
    wrapper.eq("name", name);
    return mapper.selectOne(wrapper);
  }

  @Override
  public SysPosition getWithoutName(String newName, String oldName) {
    QueryWrapper<SysPosition> wrapper = new QueryWrapper<>();
    wrapper.eq("name", newName);
    wrapper.ne("name", oldName);
    return mapper.selectOne(wrapper);
  }
}
