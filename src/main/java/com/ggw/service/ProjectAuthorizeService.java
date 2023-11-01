package com.ggw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.entity.ProjectAuthorize;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ggw.entity.ProjectSubAuthorize;
import com.ggw.entity.ProjectSubAuthorizeField;
import java.util.List;

/**
 * <p>
 * 合作机构表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectAuthorizeService extends IService<ProjectAuthorize> {

  /**
   * 分页查询
   * @param name
   * @param project
   * @param contact
   * @param contactDetails
   * @param address
   * @param description
   * @param page
   * @param limit
   * @param orderName
   * @param orderNum
   * @return
   */
  IPage<ProjectAuthorize> getList(String name, String project, String contact,
      String contactDetails, String address, String description, Integer page, Integer limit,
      String orderName, Integer orderNum);

  /**
   * 查询名称是否存在
   * @param name
   * @return
   */
  ProjectAuthorize getByName(String name);

  /**
   * 查询名称是否存在
   * @param newName
   * @param oldName
   * @return
   */
  ProjectAuthorize getWithoutName(String newName,String oldName);

  /**
   * 删除机构信息
   * @param id
   */
  void deleteAuthorize(Long id);

  /**
   * 保存机构设置
   * @param authorize
   * @param fieldList
   */
  void saveAuthorize(ProjectAuthorize authorize, List<ProjectSubAuthorizeField> fieldList,String admin);
}
