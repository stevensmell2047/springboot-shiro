package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ggw.dto.SysDepartmentDto;
import com.ggw.dto.SysMenuDto;
import com.ggw.entity.SysDepartment;
import com.ggw.dao.SysDepartmentMapper;
import com.ggw.entity.SysMenu;
import com.ggw.entity.SysUserToDepartment;
import com.ggw.service.SysDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.service.SysUserToDepartmentService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-01 11:19:18
 */
@Service
public class SysDepartmentServiceImpl extends
    ServiceImpl<SysDepartmentMapper, SysDepartment> implements SysDepartmentService {

  @Autowired
  private SysDepartmentMapper mapper;
  @Autowired
  private SysUserToDepartmentService sysUserToDepartmentService;

  @Override
  public List<SysDepartmentDto> getDepTree() {
    List<SysDepartment> departments = mapper.selectList(new QueryWrapper<>());
    // 转树状结构
    List<SysDepartment> depTree = buildTreeMenu(departments);
    // 实体转DTO
    return convert(depTree);
  }

  @Override
  public SysDepartment getDepByName(String name) {
    QueryWrapper<SysDepartment> wrapper = new QueryWrapper<>();
    wrapper.eq("name", name);
    return mapper.selectOne(wrapper);
  }

  @Override
  public SysDepartment getDepWithoutName(String newName, String oldName) {
    QueryWrapper<SysDepartment> wrapper = new QueryWrapper<>();
    wrapper.eq("name", newName);
    wrapper.ne("name", oldName);
    return mapper.selectOne(wrapper);
  }

  @Override
  public List<SysDepartment> getSubDepartmentById(Long id) {
    QueryWrapper<SysDepartment> wrapper=new QueryWrapper<>();
    wrapper.eq("parent_id",id);
    return mapper.selectList(wrapper);
  }

  private List<SysDepartmentDto> convert(List<SysDepartment> depTree) {
    List<SysDepartmentDto> depDtos = new ArrayList<>();

    depTree.forEach(m -> {
      SysDepartmentDto dto = new SysDepartmentDto();

      dto.setId(m.getId());
      dto.setParentId(m.getParentId());
      dto.setLabel(m.getName());
      dto.setManagerId(m.getManagerId());
      dto.setParent(m.getParent());
      dto.setManager(m.getManager());

      if (m.getChildren().size() > 0) {
        // 子节点调用当前方法进行再次转换
        dto.setChildren(convert(m.getChildren()));
      }

      depDtos.add(dto);
    });

    return depDtos;
  }

  private List<SysDepartment> buildTreeMenu(List<SysDepartment> deps) {

    List<SysDepartment> finalDeps = new ArrayList<>();

    // 先各自寻找到各自的孩子
    for (SysDepartment dep : deps) {
      for (SysDepartment e : deps) {
        if (dep.getId() == e.getParentId()) {
          dep.getChildren().add(e);
        }
      }
      // 提取出父节点
      if (dep.getParentId() == 0L) {
        finalDeps.add(dep);
      }
    }
    return finalDeps;
  }
}
