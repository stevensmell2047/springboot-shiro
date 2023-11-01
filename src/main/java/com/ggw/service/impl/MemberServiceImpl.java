package com.ggw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ggw.entity.Member;
import com.ggw.dao.MemberMapper;
import com.ggw.entity.SysAdminLoginLog;
import com.ggw.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.util.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

  @Autowired
  private MemberMapper memberMapper;

  @Override
  public IPage<Member> getMemberList(Integer type, String username, String realName, String phone,
      String idCard, String email, Integer status, Integer page, Integer limit, String orderName,
      Integer orderNum) {
    IPage<Member> iPage = new Page<>(page, limit);
    QueryWrapper<Member> wrapper = new QueryWrapper<>();
    wrapper.eq("member_type", type);
    if (null != username) {
      wrapper.like("username", username);
    }
    if (null != realName) {
      wrapper.like("real_name", realName);
    }
    if (null != phone) {
      wrapper.like("phone_num", phone);
    }
    if (null != idCard) {
      wrapper.like("id_card", idCard);
    }
    if (null != email) {
      wrapper.like("email", email);
    }
    if (null != status) {
      wrapper.eq("status", status);
    }
    if (ObjectUtils.isNotNull(orderNum)) {
      if (orderName != null && !"id".equals(orderName)) {
        orderName = ReflectUtils.getValue(new Member(), orderName);
      }
      if (orderNum == 0) {
        wrapper.orderByAsc(orderName);
      } else if (orderNum == 1) {
        wrapper.orderByDesc(orderName);
      } else {
        wrapper.orderByDesc("id");
      }
    } else {
      wrapper.orderByDesc("id");
    }
    return memberMapper.selectPage(iPage, wrapper);
  }
}
