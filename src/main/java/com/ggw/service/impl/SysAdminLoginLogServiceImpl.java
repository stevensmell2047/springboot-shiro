package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.entity.SysAdminLoginLog;
import com.ggw.dao.SysAdminLoginLogMapper;
import com.ggw.entity.SysRole;
import com.ggw.service.SysAdminLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.util.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-04 11:25:25
 */
@Service
public class SysAdminLoginLogServiceImpl extends
    ServiceImpl<SysAdminLoginLogMapper, SysAdminLoginLog> implements SysAdminLoginLogService {

  @Autowired
  private SysAdminLoginLogMapper mapper;

  @Override
  public IPage<SysAdminLoginLog> getLogList(String username, String className, String methodName,
      String modelName, String action, String ip, String description, Integer page, Integer limit,
      String orderName, Integer orderNum) {
    IPage<SysAdminLoginLog> iPage = new Page<>(page, limit);
    QueryWrapper<SysAdminLoginLog> wrapper = new QueryWrapper<>();
    if (username != null) {
      wrapper.like("admin", username);
    }
    if (className != null) {
      wrapper.like("class_name", className);
    }
    if (modelName != null) {
      wrapper.like("model_name", modelName);
    }
    if (action != null) {
      wrapper.like("action", action);
    }
    if (ip != null) {
      wrapper.like("ip", ip);
    }
    if (description != null) {
      wrapper.like("description", description);
    }
    if (ObjectUtils.isNotNull(orderNum)) {
      if (orderName != null && !"id".equals(orderName)) {
        orderName = ReflectUtils.getValue(new SysAdminLoginLog(), orderName);
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
    return mapper.selectPage(iPage,wrapper);
  }
}
