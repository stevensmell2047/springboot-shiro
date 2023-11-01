package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 流程材料表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Getter
@Setter
@TableName("gw_project_process_data")
public class ProjectProcessData {

    @TableId("id")
    private Long id;

    @TableField("name")
    private String name;

    /**
     * 材料类型：0：原件，1：复印件
     */
    @TableField("type")
    private Integer type;

    /**
     * 是否必填：0：是，1：否
     */
    @TableField("isRequired")
    private Integer isRequired;

    /**
     * 材料形式：0：电子，1：纸质
     */
    @TableField("form")
    private Integer form;

    /**
     * 电子材料格式
     */
    @TableField("electronic_material_format")
    private String electronicMaterialFormat;

    /**
     * 纸质材料份数
     */
    @TableField("paper_num")
    private Integer paperNum;

    /**
     * 是否需要盖章或盖手印，0：是，1否
     */
    @TableField("stamp")
    private Integer stamp;

    /**
     * 填报须知
     */
    @TableField("Instructions")
    private String instructions;

    /**
     * 受理标准
     */
    @TableField("acceptance")
    private String acceptance;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 模板文件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 项目Id
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 流程ID
     */
    @TableField("process_id")
    private Long processId;


}
