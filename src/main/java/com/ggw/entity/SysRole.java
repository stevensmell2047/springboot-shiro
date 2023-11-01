package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@Getter
@Setter
@TableName("gw_sys_role")
public class SysRole {

  @TableId("id")
  private Long id;

  @TableField("name")
  private String name;

  @TableField("description")
  private String description;

  @TableField("creater")
  private String creater;

  @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private Date createTime;

  @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
  @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  @TableField("last_update")
  private String lastUpdate;

  @TableField("status")
  private Integer status;
  @TableField("code")
  private String code;

}
