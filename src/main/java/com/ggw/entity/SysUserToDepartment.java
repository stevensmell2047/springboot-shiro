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
 * @since 2022-06-01 11:56:58
 */
@Getter
@Setter
@TableName("gw_sys_user_to_department")
public class SysUserToDepartment {

    @TableId("id")
    private Long id;

    @TableField("admin_id")
    private Long adminId;

    @TableField("dep_id")
    private Long depId;


}
