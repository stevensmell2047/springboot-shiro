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
 * 项目管理
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Getter
@Setter
@TableName("gw_project")
public class Project {

    @TableId("id")
    private Long id;

    @TableField("name")
    private String name;

    @TableField("manager_id")
    private Long managerId;

    @TableField("manager")
    private String manager;

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

    @TableField("status")
    private Integer status;

    /**
     * 备注
     */
    @TableField("description")
    private String description;
    /**
     * 主管单位数量
     */
    @TableField("organizerNum")
    private String organizer_num;
    /**
     * 合作机构数量
     */
    @TableField("authorize_num")
    private String authorizeNum;
    /**
     * 请求数量
     */
    @TableField("req_num")
    private String reqNum;
    /**
     * 总额度
     */
    @TableField("quota")
    private String quota;
    /**
     * 可用额度
     */
    @TableField("available_quota")
    private String availableQuota;
}
