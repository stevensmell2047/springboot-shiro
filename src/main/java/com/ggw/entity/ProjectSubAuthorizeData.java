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
 * @since 2022-06-06 12:13:10
 */
@Getter
@Setter
@TableName("gw_project_sub_authorize_data")
public class ProjectSubAuthorizeData {

    @TableId("id")
    private Long id;

    @TableField("sub_authorize_id")
    private Long subAuthorizeId;

    @TableField("authorize_id")
    private Long authorizeId;

    @TableField("key")
    private String key;

    @TableField("value")
    private String value;


}
