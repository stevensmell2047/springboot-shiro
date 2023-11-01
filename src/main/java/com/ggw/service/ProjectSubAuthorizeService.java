package com.ggw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.entity.ProjectSubAuthorize;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ggw.entity.ProjectSubAuthorizeData;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 下级机构表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectSubAuthorizeService extends IService<ProjectSubAuthorize> {

  /**
   * 通过机构ID删除
   *
   * @param authorizedId
   */
  void deleteByAuthorizedId(Long authorizedId);

  /**
   * 增加子机构
   *
   * @param authorize
   * @param dataList
   */
  void addSubAuthorized(ProjectSubAuthorize authorize, List<ProjectSubAuthorizeData> dataList);

  /**
   * 通过子机构ID删除
   *
   * @param id
   */
  void deleteBySubAuthorizedId(Long id);

  /**
   * 查询子机构列表
   *
   * @return
   */
  IPage<Map<String,Object>> getSubAuthorized(Long id,String name, Integer page, Integer limit, String orderName,
      Integer orderNum);
}
