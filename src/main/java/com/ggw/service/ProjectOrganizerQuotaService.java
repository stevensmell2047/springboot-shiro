package com.ggw.service;

import com.ggw.entity.ProjectOrganizerQuota;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Map;

/**
 * <p>
 * 主管单位额度表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectOrganizerQuotaService extends IService<ProjectOrganizerQuota> {

  /**
   * 设置配额值
   *
   * @param id
   * @param quotaMap
   */
  void saveQuota(Long id, Map<Long, Integer> quotaMap);

  void  deleteByOrganizerId(Long id);
}
