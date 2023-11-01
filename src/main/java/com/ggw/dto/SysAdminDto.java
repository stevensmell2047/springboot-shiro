package com.ggw.dto;

import com.ggw.entity.SysAdmin;
import java.util.List;
import lombok.Data;

/**
 * @author qizhuo
 * @date 2022/6/2 11:01
 */
@Data
public class SysAdminDto extends SysAdmin {

  private Long departmentId;
  private Long positionId;
  private String department;
  private String position;
  private String roles;
  private List<Long> roleIds;
}
