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
 * 登录日志表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-04 11:25:25
 */
@Getter
@Setter
@TableName("gw_sys_admin_login_log")
public class SysAdminLoginLog {

    @TableId("id")
    private Long id;

    /**
     * 用户ID
     */
    @TableField("admin_id")
    private Long adminId;

    /**
     * 登录IP
     */
    @TableField("ip")
    private String ip;

    /**
     * 登录地址
     */
    @TableField("address")
    private String address;

    /**
     * 访问客户端类型:0:PC，1：移动端
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 用户账号
     */
    @TableField("admin")
    private String admin;

    /**
     * 日志描述
     */
    @TableField("description")
    private String description;

    /**
     * 方法参数
     */
    @TableField("action_args")
    private String actionArgs;

    /**
     * 类名称
     */
    @TableField("class_name")
    private String className;

    /**
     * 方法名称
     */
    @TableField("method_name")
    private String methodName;

    /**
     * 是否成功 1:成功 2异常
     */
    @TableField("succeed")
    private String succeed;

    /**
     * 异常堆栈信息
     */
    @TableField("message")
    private String message;

    /**
     * 模块名称
     */
    @TableField("model_name")
    private String modelName;

    /**
     * 操作
     */
    @TableField("action")
    private String action;


}
