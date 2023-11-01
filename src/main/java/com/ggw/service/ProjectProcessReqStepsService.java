package com.ggw.service;

import com.ggw.entity.ProjectProcessReqSteps;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 申请步骤表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectProcessReqStepsService extends IService<ProjectProcessReqSteps> {

  /**
   * 通过ProcessID查询
   * @param processId
   * @return
   */
  List<ProjectProcessReqSteps> getByProcessId(Long processId);

  /**
   * 通过申请流程ID删除
   * @param processId
   */
  void deleteByProcessId(Long processId);
}
