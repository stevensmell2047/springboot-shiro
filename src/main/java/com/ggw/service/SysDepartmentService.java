package com.ggw.service;

import com.ggw.dto.SysDepartmentDto;
import com.ggw.entity.SysDepartment;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-01 11:19:18
 */
public interface SysDepartmentService extends IService<SysDepartment> {

  /**
   * 获取部门树
   *
   * @return
   */
  List<SysDepartmentDto> getDepTree();

  /**
   * 查询名称是否存在
   *
   * @param name
   * @return
   */
  SysDepartment getDepByName(String name);

  /**
   * 查询新名称是否存在
   *
   * @param newName
   * @param oldName
   * @return
   */
  SysDepartment getDepWithoutName(String newName, String oldName);


  /**
   * 查询所有子部门
   *
   * @param id
   * @return
   */
  List<SysDepartment> getSubDepartmentById(Long id);
}
