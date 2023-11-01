package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.entity.ProjectOrganizerQuota;
import com.ggw.dao.ProjectOrganizerQuotaMapper;
import com.ggw.entity.ProjectSubAuthorize;
import com.ggw.service.ProjectOrganizerQuotaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.service.ProjectSubAuthorizeService;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 主管单位额度表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectOrganizerQuotaServiceImpl extends
    ServiceImpl<ProjectOrganizerQuotaMapper, ProjectOrganizerQuota> implements
    ProjectOrganizerQuotaService {

  @Autowired
  private ProjectOrganizerQuotaMapper mapper;
  @Autowired
  private ProjectSubAuthorizeService projectSubAuthorizeService;

  @Override
  public void saveQuota(Long id, Map<Long, Integer> quotaMap) {
    if (quotaMap.size() > 0) {

      Iterator<Long> iter = quotaMap.keySet().iterator();
      while (iter.hasNext()) {
        Long key = iter.next();
        QueryWrapper<ProjectOrganizerQuota> wrapper = new QueryWrapper<>();
        wrapper.eq("sub_authorize_id", key);
        ProjectOrganizerQuota projectOrganizerQuota = mapper.selectOne(wrapper);
        ProjectSubAuthorize projectSubAuthorize = projectSubAuthorizeService.getById(key);
        if (projectOrganizerQuota == null) {
          ProjectOrganizerQuota quota = new ProjectOrganizerQuota();
          quota.setAuthorizeId(projectSubAuthorize.getAuthorizeId());
          quota.setSubAuthorizeId(key);
          quota.setOrganizerId(id);
          quota.setQuota(quotaMap.get(key));
          quota.setUsedQuota(0);
          mapper.insert(quota);
        } else {
          projectOrganizerQuota.setQuota(quotaMap.get(key));
          mapper.updateById(projectOrganizerQuota);
        }
      }
    }
  }

  @Override
  public void deleteByOrganizerId(Long id) {
    QueryWrapper<ProjectOrganizerQuota> wrapper = new QueryWrapper<>();
    wrapper.eq("organizer_id", id);
    mapper.delete(wrapper);
  }
}
