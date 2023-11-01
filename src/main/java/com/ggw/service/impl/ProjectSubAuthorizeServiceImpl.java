package com.ggw.service.impl;

import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.entity.ProjectSubAuthorize;
import com.ggw.dao.ProjectSubAuthorizeMapper;
import com.ggw.entity.ProjectSubAuthorizeData;
import com.ggw.service.ProjectSubAuthorizeDataService;
import com.ggw.service.ProjectSubAuthorizeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 下级机构表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Service
public class ProjectSubAuthorizeServiceImpl extends
    ServiceImpl<ProjectSubAuthorizeMapper, ProjectSubAuthorize> implements
    ProjectSubAuthorizeService {

  @Autowired
  private ProjectSubAuthorizeMapper mapper;
  @Autowired
  private ProjectSubAuthorizeDataService projectSubAuthorizeDataService;
  @Autowired
  private ClassUtil classUtil;

  @Override
  public void deleteByAuthorizedId(Long authorizedId) {
    QueryWrapper<ProjectSubAuthorize> wrapper = new QueryWrapper<>();
    wrapper.eq("authorize_id", authorizedId);
    mapper.delete(wrapper);
    projectSubAuthorizeDataService.deleteByAuthorizedId(authorizedId);
  }

  @Transactional
  @Override
  public void addSubAuthorized(ProjectSubAuthorize authorize,
      List<ProjectSubAuthorizeData> dataList){
    mapper.insert(authorize);
    Long id = authorize.getId();
    if (dataList.size() > 0) {
      for (ProjectSubAuthorizeData data : dataList) {
        data.setSubAuthorizeId(id);
        projectSubAuthorizeDataService.save(data);
      }
    }
  }

  @Transactional
  @Override
  public void deleteBySubAuthorizedId(Long id) {
    mapper.deleteById(id);
    projectSubAuthorizeDataService.deleteBySubAuthorizedId(id);
  }

  @Override
  public IPage<Map<String, Object>> getSubAuthorized(Long id, String name, Integer page,
      Integer limit,
      String orderName,
      Integer orderNum) {
    IPage<ProjectSubAuthorize> iPage = new Page<>(page, limit);
    QueryWrapper<ProjectSubAuthorize> wrapper = new QueryWrapper<>();
    if (name != null) {
      wrapper.like("name", name);
    }
    if (orderNum == 0) {
      wrapper.orderByAsc(orderName);
    } else if (orderNum == 1) {
      wrapper.orderByDesc(orderName);
    } else {
      wrapper.orderByDesc("id");
    }
    IPage<Map<String, Object>> resultPage=new Page<>();
    IPage<ProjectSubAuthorize> subPages = mapper.selectPage(iPage, wrapper);
    List<ProjectSubAuthorize> subAuthorizes = subPages.getRecords();
    List<Map<String, Object>> resultList = new ArrayList<>();
    if (subAuthorizes.size() > 0) {
      BeanUtils.copyProperties(subPages, resultPage);
      HashMap<String, Object> valueMap = new HashMap<>();
      for (ProjectSubAuthorize item : subAuthorizes) {
        valueMap.put("id",item.getId());
        valueMap.put("name",item.getName());
        valueMap.put("authorizeId",item.getAuthorizeId());
        valueMap.put("quota",item.getQuota());
        List<ProjectSubAuthorizeData> subAuthorizeDataList = projectSubAuthorizeDataService
            .getBySubAuthorizedId(item.getId());
        if (subAuthorizeDataList.size() > 0) {
          for (ProjectSubAuthorizeData data : subAuthorizeDataList) {
            valueMap.put(data.getKey(), data.getValue());
          }
        }
        resultList.add(valueMap);
      }
      resultPage.setRecords(resultList);
    }

    return resultPage;
  }
}
