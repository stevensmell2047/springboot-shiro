package com.ggw.service;

import com.ggw.entity.ProjectProcessData;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 流程材料表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectProcessDataService extends IService<ProjectProcessData> {

  /**
   * 通过申请流程ID查询材料
   * @param processId
   * @return
   */
  List<ProjectProcessData> getListByProcessId(Long processId);

  /**
   * 通过申请流程ID删除材料
   * @param processId
   */
  void deleteByProcessId(Long processId);
}
