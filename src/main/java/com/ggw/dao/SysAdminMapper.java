package com.ggw.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.dto.SysAdminDto;
import com.ggw.entity.SysAdmin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ggw.entity.SysMenu;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@Mapper
public interface SysAdminMapper extends BaseMapper<SysAdmin> {

  @Select("select admin_id from gw_sys_user_to_role where role_id=#{roleId}")
  List<Long> getUserByRoleId(@Param("roleId") Long roleId);

  @Select("select admin_id from gw_sys_user_to_role where role_id in (select role_id from gw_sys_role_to_menu where menu_id=#{menuId})")
  List<Long> getUserByMenuId(@Param("menuId") Long menuId);

  @Select("select * from gw_sys_menu where id in (SELECT DISTINCT rm.menu_id\n"
      + "        FROM gw_sys_user_to_role ur\n"
      + "        LEFT JOIN gw_sys_role_to_menu rm ON ur.role_id = rm.role_id\n"
      + "\n"
      + "        WHERE ur.admin_id = #{userId})")
  List<SysMenu> getUserMenus(@Param("userId") Long userId);

  @Select("SELECT DISTINCT rm.menu_id\n"
      + "        FROM gw_sys_user_to_role ur\n"
      + "        LEFT JOIN gw_sys_role_to_menu rm ON ur.role_id = rm.role_id\n"
      + "\n"
      + "        WHERE ur.admin_id = #{userId}")
  List<Long> getUserMenuIds(@Param("userId") Long userId);

  @Select("<script>"
      + "select *,dm.name as department,pn.name as position"
      + ","
      + "("
      + "select string_agg(name,',') from gw_sys_user_to_role as ro \n"
      + "left join gw_sys_role as rl\n"
      + "on ro.role_id=rl.id\n"
      + "where admin_id=ur.id) as roles\n"
      + " from ("
      + "select sa.id,sa.username,sa.phone,sa.email,sa.job_num,sa.address,sa.name,sa.sex,sa.status,sa.create_time,sa.creator,sa.update_time,sa.last_update\n"
      + "from gw_sys_admin as sa left join gw_sys_user_to_department as de\n"
      + "on sa.id=de.admin_id\n"
      + "<if test='depId!=null'>"
      + "where de.dep_id = #{depId} "
      + "</if>"
      + "order by #{orderName} "
      + "<if test='orderNum==0'>"
      + "asc \n"
      + "</if>"
      + "<if test='orderNum==1'>"
      + "desc \n"
      + "</if>"
      + ") as ur "
      + " left join gw_sys_user_to_department as dt \n"
      + " on ur.id=dt.admin_id\n"
      + " left join gw_sys_department as dm\n"
      + " on dt.dep_id=dm.id\n"
      + " left join gw_sys_user_to_position as ps\n"
      + " on ur.id=ps.admin_id\n"
      + " left join gw_sys_position as pn\n"
      + " on ps.position_id=pn.id"
      + "</script>")
  IPage<SysAdminDto> getUsersByDepId(IPage<SysAdmin> iPage, @Param("depId") Long depId,
      @Param("orderName") String orderName, @Param("orderNum") Integer orderNum);

  @Select("<script>"
      + "select *,dm.name as department,pn.name as position,"
      + "(\n"
      + "select string_agg(name,',') from gw_sys_user_to_role as ro \n"
      + "left join gw_sys_role as rl\n"
      + "on ro.role_id=rl.id\n"
      + "where admin_id=ur.id) as roles\n"
      + "from ("
      + "select sa.id,sa.username,sa.phone,sa.email,sa.job_num,sa.address,sa.name,sa.sex,sa.status,sa.create_time,sa.creator,sa.update_time,sa.last_update\n "
      + "from gw_sys_admin as sa left join gw_sys_user_to_position as de\n"
      + "on sa.id=de.admin_id\n"
      + "<if test='positionId!=null'>"
      + "where de.position_id = #{positionId} \n"
      + "</if>"
      + "order by #{orderName} "
      + "<if test='orderNum==0'>"
      + "asc \n"
      + "</if>"
      + "<if test='orderNum==1'>"
      + "desc \n"
      + "</if>"
      + ") as ur\n"
      + "\t left join gw_sys_user_to_department as dt \n"
      + "\t on ur.id=dt.admin_id\n"
      + "\t left join gw_sys_department as dm\n"
      + "\t on dt.dep_id=dm.id\n"
      + "\t left join gw_sys_user_to_position as ps\n"
      + "\t on ur.id=ps.admin_id\n"
      + "\t left join gw_sys_position as pn\n"
      + "\t on ps.position_id=pn.id"
      + "</script>")
  IPage<SysAdminDto> getUsersByPositionId(IPage<SysAdmin> iPage,
      @Param("positionId") Long positionId,
      @Param("orderName") String orderName, @Param("orderNum") Integer orderNum);

  IPage<SysAdminDto> getUserList(IPage<SysAdmin> iPage,
      @Param("depId") Long depId,
      @Param("positionId") Long positionId,
      @Param("username") String username,
      @Param("name") String name,
      @Param("position") String position,
      @Param("department") String department,
      @Param("address") String address,
      @Param("phone") String phone,
      @Param("email") String email,
      @Param("orderName") String orderName, @Param("orderNum") Integer orderNum);

  SysAdminDto getUserInfo(@Param("userId") Long userId);
}
