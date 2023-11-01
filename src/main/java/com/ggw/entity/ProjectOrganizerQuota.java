package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 主管单位额度表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Getter
@Setter
@TableName("gw_project_organizer_quota")
public class ProjectOrganizerQuota {

    @TableId("id")
    private Long id;

    @TableField("organizer_id")
    private Long organizerId;

    @TableField("authorize_id")
    private Long authorizeId;

    @TableField("sub_authorize_id")
    private Long subAuthorizeId;

    @TableField("quota")
    private Integer quota;

    @TableField("used_quota")
    private Integer usedQuota;


}
