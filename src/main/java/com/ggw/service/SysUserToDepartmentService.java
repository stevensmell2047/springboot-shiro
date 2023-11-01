package com.ggw.service;

import com.ggw.entity.SysAdmin;
import com.ggw.entity.SysUserToDepartment;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-01 11:56:58
 */
public interface SysUserToDepartmentService extends IService<SysUserToDepartment> {

  /**
   * 通过部门ID统计用户数量
   * @param depId
   * @return
   */
  int countUserByDepId(Long depId);

  /**
   * 通过部门ID获取所有用户ID
   * @return
   */
  List<Long> getUserIdsByDepId(Long depId);

  /**
   * 查询用户关联信息
   * @param userId
   * @return
   */
  SysUserToDepartment getByUserId(Long userId);

  /**
   * 删除用户关联信息
   * @param userId
   */
  void deleteByUserId(Long userId);
}
