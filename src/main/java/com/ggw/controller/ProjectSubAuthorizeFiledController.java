package com.ggw.controller;

import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.entity.ProjectAuthorize;
import com.ggw.entity.ProjectSubAuthorizeField;
import com.ggw.entity.SysAdmin;
import com.ggw.service.ProjectAuthorizeService;
import com.ggw.service.ProjectSubAuthorizeFieldService;
import com.ggw.service.SysAdminService;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qizhuo
 * @date 2022/6/5 23:16
 */
@RestController
@RequestMapping("/project/authorize/field")
public class ProjectSubAuthorizeFiledController {

  @Autowired
  private ProjectAuthorizeService projectAuthorizeService;
  @Autowired
  private ProjectSubAuthorizeFieldService projectSubAuthorizeFieldService;
  @Autowired
  private SysAdminService sysAdminService;

  @Log(action = "列表", modelName = "合作机构", description = "查询下级机构字段列表")
  @ApiOperation(value = "查询下级机构字段", notes = "查询下级机构字段列表")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('project:field:list')")
  @PostMapping("/list")
  public R list(
      @NotNull(message = "ID不能为空") @RequestParam(value = "page", required = true) Long id) {
    ProjectAuthorize authorize = projectAuthorizeService.getById(id);
    if (authorize == null) {
      return R.FAIL("机构不存在");
    }
    return R.SUCCESS(projectSubAuthorizeFieldService.getByAuthorizeId(id));
  }

  @Log(action = "新增", modelName = "合作机构", description = "新增下级机构字段")
  @ApiOperation(value = "新增下级机构字段", notes = "新增下级机构字段接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('project:field:add')")
  @PostMapping("/add")
  public R add(
      @NotNull(message = "ID不能为空") @RequestParam(value = "page", required = true) Long id,
      @NotNull(message = "唯一标识不能为空") @RequestParam(value = "key", required = true) String key,
      @NotNull(message = "字段名称不能为空") @RequestParam(value = "name", required = true) String name,
      @NotNull(message = "字段类型不能为空") @RequestParam(value = "type", required = true) Integer type,
      @NotNull(message = "字段值不能为空") @RequestParam(value = "type", required = true) String value,
      Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    ProjectAuthorize authorize = projectAuthorizeService.getById(id);
    if (authorize == null) {
      return R.FAIL("机构不存在");
    }
    ProjectSubAuthorizeField keyFiled = projectSubAuthorizeFieldService.getByKey(key);
    if (null != keyFiled) {
      return R.FAIL("唯一标识已存在");
    }
    ProjectSubAuthorizeField field = new ProjectSubAuthorizeField();
    field.setAuthorizeId(id);
    field.setKey(key);
    field.setName(name);
    field.setType(type);
    field.setValue(value);
    field.setCreator(admin.getUsername());
    field.setCreateTime(new Date());
    projectSubAuthorizeFieldService.save(field);
    return R.SUCCESS("新增成功");
  }

  @Log(action = "编辑", modelName = "合作机构", description = "修改下级机构字段")
  @ApiOperation(value = "修改下级机构字段", notes = "修改下级机构字段接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('project:field:update')")
  @PostMapping("/update")
  public R update(
      @NotNull(message = "ID不能为空") @RequestParam(value = "page", required = true) Long id,
      @NotNull(message = "字段名称不能为空") @RequestParam(value = "name", required = true) String name,
      Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    ProjectSubAuthorizeField field = projectSubAuthorizeFieldService.getById(id);
    if (field == null) {
      return R.FAIL("字段不存在");
    }
    field.setName(name);
    field.setLastUpdate(admin.getUsername());
    field.setUpdateTime(new Date());
    projectSubAuthorizeFieldService.saveOrUpdate(field);
    return R.SUCCESS("编辑成功");
  }

  @Log(action = "删除", modelName = "合作机构", description = "删除下级机构字段")
  @ApiOperation(value = "修改下级机构字段", notes = "修改下级机构字段接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('project:field:delete')")
  @PostMapping("/delete")
  public R delete(
      @NotNull(message = "ID不能为空") @RequestParam(value = "page", required = true) Long id) {
    ProjectSubAuthorizeField field = projectSubAuthorizeFieldService.getById(id);
    if (field == null) {
      return R.FAIL("字段不存在");
    }
    projectSubAuthorizeFieldService.removeById(id);
    return R.SUCCESS("删除成功");
  }
}
