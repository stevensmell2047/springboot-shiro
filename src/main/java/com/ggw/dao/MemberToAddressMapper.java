package com.ggw.dao;

import com.ggw.entity.MemberToAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 会员地址映射表 Mapper 接口
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
@Mapper
public interface MemberToAddressMapper extends BaseMapper<MemberToAddress> {

}
