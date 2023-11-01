package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-01 11:19:18
 */
@Getter
@Setter
@TableName("gw_sys_department")
public class SysDepartment {

  @TableId("id")
  private Long id;

  @TableField("parent_id")
  private Long parentId;

  @TableField("name")
  private String name;
  @TableField("parent")
  private String parent;

  @TableField("manager")
  private String manager;

  @TableField("creator")
  private String creator;

  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private Date createTime;

  @TableField("last_update")
  private String lastUpdate;

  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  /**
   * 负责人ID
   */
  @TableField("manager_id")
  private Long managerId;

  @TableField(exist = false)
  private List<SysDepartment> children = new ArrayList<>();

}
