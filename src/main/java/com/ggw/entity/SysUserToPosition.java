package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户职位映射表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-02 12:33:53
 */
@Getter
@Setter
@TableName("gw_sys_user_to_position")
public class SysUserToPosition {

    @TableId("id")
    private Long id;

    @TableField("position_id")
    private Long positionId;

    @TableField("admin_id")
    private Long adminId;


}
