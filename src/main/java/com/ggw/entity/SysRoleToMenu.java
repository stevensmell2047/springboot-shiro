package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色菜单表
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-31 10:11:47
 */
@Getter
@Setter
@TableName("gw_sys_role_to_menu")
public class SysRoleToMenu {

    @TableId("id")
    private Long id;

    @TableField("role_id")
    private Long roleId;

    @TableField("menu_id")
    private Long menuId;


}
