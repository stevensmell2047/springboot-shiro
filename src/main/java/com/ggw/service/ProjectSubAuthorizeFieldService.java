package com.ggw.service;

import com.ggw.entity.ProjectSubAuthorizeField;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 下级机构字段表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectSubAuthorizeFieldService extends IService<ProjectSubAuthorizeField> {

  /**
   * 通过机构ID删除
   * @param authorizedId
   */
  void deleteByAuthorizeId(Long authorizedId);

  /**
   * 获取所有字段
   * @param authorizedId
   * @return
   */
  List<ProjectSubAuthorizeField> getByAuthorizeId(Long authorizedId);

  /**
   * 查询Key是否存在
   * @param key
   * @return
   */
  ProjectSubAuthorizeField getByKey(String key);
  /**
   * 查询Key是否存在
   * @param newKey
   * @return
   */
  ProjectSubAuthorizeField getWithoutKey(String newKey,String oldKey);
}
