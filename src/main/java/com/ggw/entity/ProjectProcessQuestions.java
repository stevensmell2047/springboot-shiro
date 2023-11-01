package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 申请流程常见问题表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Getter
@Setter
@TableName("gw_project_process_questions")
public class ProjectProcessQuestions {

    @TableId("id")
    private Long id;

    @TableField("project_id")
    private Long projectId;

    @TableField("process_id")
    private Long processId;

    @TableField("question")
    private String question;

    @TableField("answer")
    private String answer;


}
