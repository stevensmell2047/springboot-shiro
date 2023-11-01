package com.ggw.controller;

import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.entity.ProjectAuthorize;
import com.ggw.entity.ProjectSubAuthorize;
import com.ggw.entity.ProjectSubAuthorizeData;
import com.ggw.entity.SysAdmin;
import com.ggw.service.ProjectAuthorizeService;
import com.ggw.service.ProjectSubAuthorizeService;
import com.ggw.service.SysAdminService;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qizhuo
 * @date 2022/6/5 22:59
 */
@RestController
@RequestMapping("/project/authorize/sub")
public class ProjectSubAuthorizeController {

  @Autowired
  private ProjectSubAuthorizeService projectSubAuthorizeService;
  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private ProjectAuthorizeService projectAuthorizeService;

  @Log(action = "列表", modelName = "下级机构", description = "查询下级机构")
  @ApiOperation(value = "查询下级机构", notes = "查询下级机构接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('authorize:sub:list')")
  @PostMapping("/list")
  public R list(
      @NotNull(message = "ID不能为空") @RequestParam(value = "page", required = true) Long id,
      @RequestParam(required = false) String name,
      @NotNull(message = "页码不能为空") @RequestParam(value = "page", required = true) Integer page,
      @NotNull(message = "页数不能为空") @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(required = false) String orderName,
      @RequestParam(required = false) Integer orderNum) {

    return R.SUCCESS(
        projectSubAuthorizeService.getSubAuthorized(id, name, page, limit, orderName, orderNum));
  }

  @Log(action = "新增", modelName = "下级机构", description = "新增下级机构")
  @ApiOperation(value = "新增下级机构", notes = "新增下级机构接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('authorize:sub:add')")
  @PostMapping("/add")
  public R list(
      @NotNull(message = "Id不能为空") @RequestParam(value = "id", required = true) Long id,
      @NotNull(message = "值不能为空") @RequestParam(value = "subMap", required = true)
          Map<String, Object> subMap, Principal principal) {
    SysAdmin admin = sysAdminService.getUserByName(principal.getName());
    ProjectAuthorize authorize = projectAuthorizeService.getById(id);
    if (authorize == null) {
      return R.FAIL("机构不存在");
    }
    List<ProjectSubAuthorizeData> dataList = new ArrayList<>();
    ProjectSubAuthorize subAuthorize = new ProjectSubAuthorize();
    subAuthorize.setAuthorizeId(id);
    subAuthorize.setCreator(admin.getUsername());
    subAuthorize.setCreateTime(new Date());
    Iterator<String> iter = subMap.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      if (key.equals("name")) {
        String value = String.valueOf(subMap.get(key));
        if (null == value) {
          return R.FAIL("名称不能为空");
        } else {
          subAuthorize.setName(value);
        }
      }
      if (key.equals("quota")) {
        String value = String.valueOf(subMap.get(key));
        if (null == value) {
          return R.FAIL("配额不能为空");
        } else {
          subAuthorize.setQuota(Integer.parseInt(value));
        }
      }
      if (!key.equals("name") && !key.equals("quota")) {
        ProjectSubAuthorizeData data = new ProjectSubAuthorizeData();
        data.setKey(key);
        data.setValue(String.valueOf(subMap.get(key)));
        data.setAuthorizeId(id);
        dataList.add(data);
      }
    }
    projectSubAuthorizeService.addSubAuthorized(subAuthorize, dataList);
    return R.SUCCESS("新增成功");
  }

  @Log(action = "删除", modelName = "下级机构", description = "删除下级机构")
  @ApiOperation(value = "删除下级机构", notes = "删除下级机构接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('authorize:sub:delete')")
  @PostMapping("/delete")
  public R list(
      @NotNull(message = "Id不能为空") @RequestParam(value = "id", required = true) Long id) {
    ProjectSubAuthorize authorize = projectSubAuthorizeService.getById(id);
    if (authorize == null) {
      return R.FAIL("子机构不存在");
    }
    projectSubAuthorizeService.deleteBySubAuthorizedId(id);
    return R.SUCCESS("删除成功");
  }
}
