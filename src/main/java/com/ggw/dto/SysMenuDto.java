package com.ggw.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * { name: 'SysUser', title: '用户管理', icon: 'el-icon-s-custom', path: '/sys/users', component:
 * 'sys/User', children: [] },
 */
@Data
public class SysMenuDto implements Serializable {

  private Long id;
  private String name;
  private String label;
  private String icon;
  private String path;
  private String component;
  private Integer sortNum;
  private List<SysMenuDto> children = new ArrayList<>();

}
