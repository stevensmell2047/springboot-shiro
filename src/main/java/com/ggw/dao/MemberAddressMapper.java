package com.ggw.dao;

import com.ggw.entity.MemberAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ggw.entity.MemberToAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 会员地址表 Mapper 接口
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
@Mapper
public interface MemberAddressMapper extends BaseMapper<MemberAddress> {

  @Select("select * from gw_member_address as ma left join gw_member_to_address as ta "
      + "on ma.id=ta.address_id "
      + "where ta.member_id=#{memberId}")
  MemberAddress getByMemberId(@Param("memberId") Long memberId);
}
