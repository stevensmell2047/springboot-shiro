package com.ggw.dao;

import com.ggw.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

  @Select("select admin_id from gw_sys_user_to_role where role_id=#{roleId}")
  List<Long> getUserByRoleId(@Param("roleId") Long roleId);
}
