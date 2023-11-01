package com.ggw.service;

import com.ggw.entity.MemberAddress;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员地址表 服务类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
public interface MemberAddressService extends IService<MemberAddress> {

  /**
   * 通过会员ID查询邮寄地址
   * @param memberId
   * @return
   */
  MemberAddress getByMemberId(Long memberId);
}
