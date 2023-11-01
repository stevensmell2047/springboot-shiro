package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 会员地址表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
@Getter
@Setter
@TableName("gw_member_address")
public class MemberAddress {

    @TableId("id")
    private Long id;

    @TableField("addressee")
    private String addressee;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("area")
    private String area;

    @TableField("street")
    private String street;

    @TableField("phone_num")
    private String phoneNum;


}
