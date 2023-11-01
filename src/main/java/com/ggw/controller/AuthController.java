package com.ggw.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.ggw.base.Constant;
import com.ggw.common.R;
import com.ggw.common.redis.RedisService;
import com.ggw.entity.SysAdmin;
import com.ggw.entity.SysRole;
import com.ggw.service.SysAdminService;
import com.ggw.service.SysRoleService;
import com.google.code.kaptcha.Producer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

/**
 * @author qizhuo
 * @date 2022/5/30 14:53
 */
@RestController
public class AuthController {

  @Autowired
  private Producer producer;

  @Autowired
  private RedisService redisService;
  @Autowired
  private SysAdminService sysAdminService;
  @Autowired
  private SysRoleService sysRoleService;

  @GetMapping("/captcha")
  public R captcha() throws IOException {

    String key = UUID.randomUUID().toString();
    String code = producer.createText();

    // 为了测试
    key = "aaaaa";
    code = "11111";

    BufferedImage image = producer.createImage(code);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(image, "jpg", outputStream);

    BASE64Encoder encoder = new BASE64Encoder();
    String str = "data:image/jpeg;base64,";

    String base64Img = str + encoder.encode(outputStream.toByteArray());

    redisService.set(Constant.CAPTCHA_KEY + key, code, 120);

    return R.SUCCESS(
        MapUtil.builder()
            .put("key", key)
            .put("captchaImg", base64Img)
            .build()

    );
  }

  /**
   * 获取用户信息接口
   *
   * @param principal
   * @return
   */
  @GetMapping("/sys/userInfo")
  public R userInfo(Principal principal) {

    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());
    List<SysRole> sysRole = sysRoleService.getRoleByUserId(sysUser.getId());
    List<String> roleNames = sysRole.stream().map(SysRole::getCode).collect(Collectors.toList());

    return R.SUCCESS(MapUtil.builder()
        .put("id", sysUser.getId())
        .put("username", sysUser.getUsername())
        .put("roles", roleNames)
        .map()
    );
  }

  /**
   * 清理redis 用户权限cache接口
   *
   * @param principal
   * @return
   */
  @PostMapping("/sys/clear")
  public R clear(Principal principal) {

    SysAdmin sysUser = sysAdminService.getUserByName(principal.getName());
    sysAdminService.clearUserAuthorityCache(sysUser.getId());
    return R.SUCCESS("清理成功");
  }
}
