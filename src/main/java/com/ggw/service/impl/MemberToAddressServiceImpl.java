package com.ggw.service.impl;

import com.ggw.entity.MemberToAddress;
import com.ggw.dao.MemberToAddressMapper;
import com.ggw.service.MemberToAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员地址映射表 服务实现类
 * </p>
 *
 * @author qizhuo
 * @since 2022-06-05 11:43:15
 */
@Service
public class MemberToAddressServiceImpl extends ServiceImpl<MemberToAddressMapper, MemberToAddress> implements MemberToAddressService {

}
