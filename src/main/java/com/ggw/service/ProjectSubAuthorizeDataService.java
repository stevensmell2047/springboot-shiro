package com.ggw.service;

import com.ggw.entity.ProjectSubAuthorizeData;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-06 12:13:10
 */
public interface ProjectSubAuthorizeDataService extends IService<ProjectSubAuthorizeData> {

  /**
   * 通过子机构ID查询
   * @param id
   * @return
   */
  List<ProjectSubAuthorizeData> getBySubAuthorizedId(Long id);

  /**
   * 通过机构ID删除
   * @param id
   */
  void deleteByAuthorizedId(Long id);

  /**
   * 通过子机构ID删除
   * @param id
   */
  void deleteBySubAuthorizedId(Long id);
}
