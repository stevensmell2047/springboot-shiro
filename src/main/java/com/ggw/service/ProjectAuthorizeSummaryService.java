package com.ggw.service;

import com.ggw.entity.ProjectAuthorizeSummary;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 机构简介表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectAuthorizeSummaryService extends IService<ProjectAuthorizeSummary> {

  /**
   * 通过机构ID查询
   * @param authorizedId
   */
  void deleteByAuthorizeId(Long authorizedId);

  /**
   * 保存机构简介
   * @param authorizedId
   */
  void saveSummary(Long authorizedId,String content,String admin);
}
