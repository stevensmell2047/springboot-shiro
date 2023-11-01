package com.ggw.dto;

import com.ggw.entity.ProjectProcess;
import com.ggw.entity.ProjectProcessData;
import com.ggw.entity.ProjectProcessQuestions;
import com.ggw.entity.ProjectProcessReqSteps;
import java.util.List;
import lombok.Data;

/**
 * @author qizhuo
 * @date 2022/6/5 22:13
 */
@Data
public class ProjectProcessDto extends ProjectProcess {

  private List<ProjectProcessData> dataList;
  private List<ProjectProcessReqSteps> stepList;
  private List<ProjectProcessQuestions> questionsList;
}
