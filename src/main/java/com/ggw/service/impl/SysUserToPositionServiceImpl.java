package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.SysUserToPosition;
import com.ggw.dao.SysUserToPositionMapper;
import com.ggw.service.SysUserToPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户职位映射表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-02 12:33:53
 */
@Service
public class SysUserToPositionServiceImpl extends
    ServiceImpl<SysUserToPositionMapper, SysUserToPosition> implements SysUserToPositionService {

  @Autowired
  private SysUserToPositionMapper mapper;

  @Override
  public int countUsersByPositionId(Long positionId) {
    QueryWrapper<SysUserToPosition> wrapper = new QueryWrapper<>();
    wrapper.eq("position_id", positionId);
    return mapper.selectCount(wrapper);
  }

  @Override
  public List<Long> getUserIdsByPositionId(Long positionId) {
    QueryWrapper<SysUserToPosition> wrapper = new QueryWrapper<>();
    wrapper.eq("position_id", positionId);
    List<SysUserToPosition> list = mapper.selectList(wrapper);
    if (list.size() > 0) {
      return list.stream().map(SysUserToPosition::getAdminId).collect(Collectors.toList());
    }
    return null;
  }

  @Override
  public SysUserToPosition getByUserId(Long userId) {
    QueryWrapper<SysUserToPosition> wrapper = new QueryWrapper<>();
    wrapper.eq("admin_id", userId);
    return mapper.selectOne(wrapper);
  }

  @Override
  public void deleteByUserId(Long userId) {
    QueryWrapper<SysUserToPosition> wrapper = new QueryWrapper<>();
    wrapper.eq("admin_id", userId);
    mapper.delete(wrapper);
  }
}
