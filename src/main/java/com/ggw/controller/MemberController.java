package com.ggw.controller;


import com.ggw.annotation.Log;
import com.ggw.common.R;
import com.ggw.entity.Member;
import com.ggw.service.MemberAddressService;
import com.ggw.service.MemberService;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
@RestController
@RequestMapping("/member")
public class MemberController {

  @Autowired
  private MemberService memberService;
  @Autowired
  private MemberAddressService memberAddressService;

  @Log(action = "列表", modelName = "会员管理", description = "查询会员列表")
  @ApiOperation(value = "查询会员列表", notes = "查询会员列表接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('ordinaryMember')")
  @PostMapping("/list")
  public R list(@NotNull(message = "类型不能为空") @RequestParam(defaultValue = "0") Integer type,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String realName,
      @RequestParam(required = false) String phoneNum,
      @RequestParam(required = false) String idCard,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) Integer status,
      @NotNull(message = "页码不能为空") @RequestParam(value = "page", required = true) Integer page,
      @NotNull(message = "页数不能为空") @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(required = false) String orderName,
      @RequestParam(required = false) Integer orderNum) {

    return R.SUCCESS(
        memberService.getMemberList(type, username, realName, phoneNum, idCard, email, status, page,
            limit, orderName, orderNum));
  }

  @Log(action = "邮寄地址", modelName = "会员管理", description = "查询邮寄地址")
  @ApiOperation(value = "查询邮寄地址", notes = "查询邮寄地址接口")
  @ApiImplicitParams({
  })
  @PreAuthorize("hasAuthority('ordinaryMember')")
  @PostMapping("/address")
  public R address(@NotNull(message = "ID不能为空") @RequestParam Long id) {
    Member member = memberService.getById(id);
    if (null == member) {
      return R.FAIL("ID不存在");
    }
    return R.SUCCESS(
        memberAddressService.getByMemberId(id));
  }
}
