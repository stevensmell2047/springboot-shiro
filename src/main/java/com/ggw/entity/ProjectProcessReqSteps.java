package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 申请步骤表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Getter
@Setter
@TableName("gw_project_process_req_steps")
public class ProjectProcessReqSteps {

    @TableId("id")
    private Long id;

    @TableField("name")
    private String name;

    @TableField("days")
    private Integer days;

    @TableField("description")
    private String description;

    @TableField("project_id")
    private Long projectId;

    /**
     * 流程ID
     */
    @TableField("process_id")
    private Long processId;


}
