package com.ggw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 会员地址映射表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
@Getter
@Setter
@TableName("gw_member_to_address")
public class MemberToAddress {

    @TableId("id")
    private Long id;

    @TableField("member_id")
    private Long memberId;

    @TableField("address_id")
    private Long addressId;


}
