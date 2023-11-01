package com.ggw.controller;


import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.dto.SysPositionDto;
import com.ggw.entity.SysAdmin;
import com.ggw.entity.SysDepartment;
import com.ggw.entity.SysPosition;
import com.ggw.service.SysAdminService;
import com.ggw.service.SysPositionService;
import com.ggw.service.SysUserToPositionService;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 职位表 前端控制器
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-02 12:33:53
 */
@RestController
@RequestMapping("/sys/position")
public class SysPositionController {

  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private SysPositionService sysPositionService;
  @Autowired
  private SysUserToPositionService sysUserToPositionService;

  @Log(action="列表",modelName= "职位管理",description="查询职位列表")
  @ApiOperation(value = "查询职位列表", notes = "查询职位列表接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:position:list')")
  @PostMapping("/list")
  public R list(Principal principal) {
    List<SysPosition> list = sysPositionService.list();
    List<SysPositionDto> result = new ArrayList<>();
    if (list.size() > 0) {
      for (SysPosition item : list) {
        SysPositionDto dto=new SysPositionDto();
        dto.setId(item.getId());
        dto.setLabel(item.getName());
        result.add(dto);
      }
    }
    return R.SUCCESS(result);
  }
  @ApiOperation(value = "根据部门ID查询所有用户", notes = "根据部门ID查询所有用户接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:position:list')")
  @PostMapping("/users")
  public R users(@RequestParam(required = false) Long id,
      @NotNull(message = "页码不能为空") @RequestParam(value = "page", required = true) Integer page,
      @NotNull(message = "页数不能为空") @RequestParam(value = "limit", required = true) Integer limit,
      @NotNull(message = "排序字段不能为空") @RequestParam String orderName,
      @NotNull(message = "排序规则不能为空") @RequestParam Integer orderNum) {
    if(null!=id){
      SysPosition sysPosition = sysPositionService.getById(id);
      if (null == sysPosition) {
        return R.FAIL("ID不存在");
      }
    }

    return R.SUCCESS(sysAdminService.getUsersByPositionId(id,page,limit,orderName,orderNum));
  }
  @Log(action="新增",modelName= "职位管理",description="新增职位")
  @ApiOperation(value = "新增职位", notes = "新增职位接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:position:add')")
  @PostMapping("/add")
  public R add(@NotNull(message = "名称不能为空") @RequestParam String name, Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());

    SysPosition sysPosition = sysPositionService.getByName(name);
    if (null != sysPosition) {
      return R.FAIL("名称已存在");
    }
    SysPosition position = new SysPosition();
    position.setName(name);
    position.setCreator(admin.getUsername());
    position.setCreateTime(new Date());
    sysPositionService.save(position);
    return R.SUCCESS("创建成功");
  }
  @Log(action="编辑",modelName= "职位管理",description="编辑职位")
  @ApiOperation(value = "编辑职位", notes = "更新职位接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:position:update')")
  @PostMapping("/update")
  public R update(@NotNull(message = "ID不能为空") @RequestParam Long id,
      @NotNull(message = "名称不能为空") @RequestParam String name, Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    SysPosition sysPosition = sysPositionService.getById(id);
    if (null == sysPosition) {
      return R.FAIL("ID不存在");
    }
    SysPosition position = sysPositionService
        .getWithoutName(name, sysPosition.getName());
    if (null != position) {
      return R.FAIL("名称已存在");
    }
    sysPosition.setName(name);
    sysPosition.setLastUpdate(admin.getUsername());
    sysPosition.setUpdateTime(new Date());
    sysPositionService.saveOrUpdate(sysPosition);
    return R.SUCCESS("更新成功");
  }
  @Log(action="详情",modelName= "职位管理",description="获取职位信息")
  @ApiOperation(value = "获取职位信息", notes = "获取职位接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:position:update')")
  @PostMapping("/detail")
  public R detail(@NotNull(message = "ID不能为空") @RequestParam Long id) {
    SysPosition position = sysPositionService.getById(id);
    if (null == position) {
      return R.FAIL("ID不存在");
    }

    return R.SUCCESS(position);
  }
  @Log(action="删除",modelName= "删除职位",description="删除职位")
  @ApiOperation(value = "删除职位", notes = "删除职位接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('sys:position:delete')")
  @DeleteMapping("/delete")
  public R delete(@NotNull(message = "ID不能为空") @RequestParam Long id) {
    SysPosition position = sysPositionService.getById(id);
    if (null == position) {
      return R.FAIL("ID不存在");
    }
    int num = sysUserToPositionService.countUsersByPositionId(id);
    if (num > 0) {
      return R.FAIL("当前职位存在用户,不能删除");
    }
    sysPositionService.removeById(id);
    return R.SUCCESS("删除成功");
  }
}
