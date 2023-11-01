package com.ggw.service.impl;

import com.ggw.entity.MemberAddress;
import com.ggw.dao.MemberAddressMapper;
import com.ggw.entity.MemberToAddress;
import com.ggw.service.MemberAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ggw.service.MemberToAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员地址表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
@Service
public class MemberAddressServiceImpl extends
    ServiceImpl<MemberAddressMapper, MemberAddress> implements MemberAddressService {

  @Autowired
  private MemberAddressMapper mapper;

  @Override
  public MemberAddress getByMemberId(Long memberId) {
    return mapper.getByMemberId(memberId);
  }
}
