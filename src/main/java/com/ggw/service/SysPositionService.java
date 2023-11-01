package com.ggw.service;

import com.ggw.entity.SysPosition;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 职位表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-02 12:33:53
 */
public interface SysPositionService extends IService<SysPosition> {

  /**
   * 查询名称是否存在
   * @param name
   * @return
   */
  SysPosition getByName(String name);

  /**
   * 查询名称是否存在
   * @param newName
   * @param oldName
   * @return
   */
  SysPosition getWithoutName(String newName,String oldName);
}
