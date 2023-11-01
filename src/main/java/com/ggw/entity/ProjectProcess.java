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
 * 申请流程表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Getter
@Setter
@TableName("gw_project_process")
public class ProjectProcess {

    @TableId("id")
    private Long id;

    @TableField("project_id")
    private Long projectId;

    @TableField("description")
    private String description;

    @TableField("condition")
    private String condition;

    @TableField("creator")
    private String creator;

    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("last_update")
    private String lastUpdate;

    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
