package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@Getter
@Setter
@TableName("gw_sys_menu")
public class SysMenu {

  @TableId("id")
  private Long id;

  /**
   * 菜单名称
   */
  @TableField("name")
  private String name;

  /**
   * 父菜单
   */
  @TableField("parent_id")
  private Long parentId;

  /**
   * 菜单类型：0：目录，1：菜单，2：按钮
   */
  @TableField("type")
  private Integer type;

  /**
   * 代码控制权限标识符
   */
  @TableField("code")
  private String code;

  @TableField("path")
  private String path;
  @TableField("component")
  private String component;
  @TableField("icon")
  private String icon;

  /**
   * 排序
   */
  @TableField("order_num")
  private Integer orderNum;

  @TableField(exist = false)
  private List<SysMenu> children = new ArrayList<>();
}
