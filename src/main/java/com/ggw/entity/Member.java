package com.ggw.entity;

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
 * 会员表
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
@Getter
@Setter
@TableName("gw_member")
public class Member {

  @TableId("id")
  private Long id;

  @TableField("username")
  private String username;

  @TableField("password")
  private String password;

  @TableField("real_name")
  private String realName;

  @TableField("phone_num")
  private String phoneNum;

  @TableField("id_card")
  private String idCard;

  @TableField("email")
  private String email;

  @TableField("job")
  private Integer job;
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField("reg_time")
  private Date regTime;

  /**
   * 0:普通会员，1：协议会员
   */
  @TableField("member_type")
  private Integer memberType;

  /**
   * 0:正常，1：冻结
   */
  @TableField("status")
  private Integer status;

  /**
   * 0:男，1：女
   */
  @TableField("sex")
  private Integer sex;


}
