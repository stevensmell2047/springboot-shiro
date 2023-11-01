package com.ggw.dto;

import com.ggw.entity.SysDepartment;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @author qizhuo
 * @date 2022/6/1 23:29
 */
@Data
public class SysDepartmentDto {

  private Long id;
  private String label;
  private Long parentId;
  private Long managerId;
  private String parent;
  private String manager;
  private List<SysDepartmentDto> children = new ArrayList<>();
}
