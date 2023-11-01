package com.ggw.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 项目管理 Mapper 接口
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 08:43:10
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

}
