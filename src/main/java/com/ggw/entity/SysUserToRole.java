package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-31 10:11:47
 */
@Getter
@Setter
@TableName("gw_sys_user_to_role")
public class SysUserToRole {

    @TableId("id")
    private Long id;

    @TableField("admin_id")
    private Long adminId;

    @TableField("role_id")
    private Long roleId;


}
