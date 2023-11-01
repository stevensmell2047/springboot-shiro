package com.ggw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.entity.ProjectOrganizer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 主管单位表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectOrganizerService extends IService<ProjectOrganizer> {

  /**
   * 分页查询
   *
   * @param name
   * @param code
   * @param description
   * @param contact
   * @param contactDetails
   * @param address
   * @param page
   * @param limit
   * @param orderName
   * @param orderNum
   * @return
   */
  IPage<ProjectOrganizer> getList(String name, String project, String code, String description,
      String contact,
      String contactDetails, String address, Integer page, Integer limit, String orderName,
      Integer orderNum);

  ProjectOrganizer getByName(String name);

  ProjectOrganizer getWithOutName(String newName, String oldName);

  void deleteOrganizer(Long id);
}
