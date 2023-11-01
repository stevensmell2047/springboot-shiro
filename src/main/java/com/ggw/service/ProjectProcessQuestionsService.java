package com.ggw.service;

import com.ggw.entity.ProjectProcessQuestions;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * <p>
 * 申请流程常见问题表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
public interface ProjectProcessQuestionsService extends IService<ProjectProcessQuestions> {

  /**
   * 通过申请流程ID查询
   * @param processId
   * @return
   */
  List<ProjectProcessQuestions> getByProcessId(Long processId);

  /**
   * 通过申请流程删除
   * @param processId
   */
  void deleteByProcessId(Long processId);
}
