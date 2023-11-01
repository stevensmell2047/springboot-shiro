package com.ggw.service;

import com.ggw.entity.SysUserToPosition;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 用户职位映射表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-02 12:33:53
 */
public interface SysUserToPositionService extends IService<SysUserToPosition> {

  /**
   * 根据职位ID统计用户数量
   * @param positionId
   * @return
   */
  int countUsersByPositionId(Long positionId);

  /**
   * 根据职位ID获取用户ID
   * @param positionId
   * @return
   */
  List<Long> getUserIdsByPositionId(Long positionId);

  /**
   * 根据用户ID获取职位
   * @param userId
   * @return
   */
  SysUserToPosition getByUserId(Long userId);

  /**
   * 删除用户
   * @param userId
   */
  void deleteByUserId(Long userId);
}
