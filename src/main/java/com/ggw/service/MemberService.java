package com.ggw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ggw.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
public interface MemberService extends IService<Member> {

  /**
   * 获取会员列表
   * @param type
   * @param username
   * @param realName
   * @param phone
   * @param idCard
   * @param email
   * @param status
   * @param page
   * @param limit
   * @param orderName
   * @param orderNum
   * @return
   */
  IPage<Member> getMemberList(Integer type, String username, String realName, String phone,
      String idCard, String email, Integer status, Integer page, Integer limit, String orderName,
      Integer orderNum);
}
