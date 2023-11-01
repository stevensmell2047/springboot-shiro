package com.ggw.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}
