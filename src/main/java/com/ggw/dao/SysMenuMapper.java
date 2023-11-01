package com.ggw.dao;

import com.ggw.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author qizhuo
 * @since 2022-05-30 09:53:14
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {


}
