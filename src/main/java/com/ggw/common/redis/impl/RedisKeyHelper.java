package com.ggw.common.redis.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Redis Key 辅助类
 */
@Component
@Lazy(false)
public class RedisKeyHelper {

    /**
     * 应用级别前缀
     */
    public static String REQ_TOKEN = "TOKEN_";
    /**
     * 被顶掉的token
     */
    public static String KICKED_TOKEN = "KICKED_TOKEN_";
    /**
     *警告信息
     */
    public static String MONITOR = "MONITOR_";
    /**
     * 登录用户ID前缀
     */
    public static String REQ_LOGIN = "LOGIN_";
    /**
     * 管理员登录TOKEN
     */
    public static String ADMIN_TOKEN = "ADMIN_TOKEN_";
    /**
     * 管理员用户信息
     */
    public static String ADMIN_USER = "ADMIN_USER_";
    /**
     * 应用级别前缀
     */
    private static String SYS_PREFIX = "PROJECT_";
    /**
     * 用户信息前缀
     */
    private static String UIC_PREFIX = SYS_PREFIX + "UIC_";
    /**
     * 分布式互斥锁前缀
     */
    private static String LOCK_PREFIX = SYS_PREFIX + "LOCK_";

    /**
     * 单次登录的默认有效时长（单位：秒）
     */
    public static final int DEFAULT_LOGIN_TIMEOUT = 3600 * 24 * 7;

    /**
     * 签到记录的默认有效时长（单位：秒）
     */
    public static final int MMB_SIGN_TIMEOUT = 3600 * 12;
    /**
     * redis中加息券信息的保存时间（单位：秒）
     */
    public static final int PROM_IRC_TIMEOUT = 60 * 3;
    /**
     * redis中借款人信息默认有效效时间（单位：秒）
     */
    public static final int BORROWER_EXPIRE_TIMEOUT = 60 * 3;//redis缓存失效时间

    /**
     * redis中代理人信息
     */
    public static final String PROXY_PREFIX = "PROXY_";

    /**
     * redis中二级代理人信息
     */
    public static final String PROXY_SND_PREFIX = "PROXY_SND_";



    //交易中心KEY配置==end==========================================================================================
    //用户中心KEY配置==========================================================================================
    /**
     * redis中登录用户token的key前缀
     */
    public static String LOGIN_TOKEN_KEY_PREFIX = UIC_PREFIX + "LOGIN_TOKEN_";
    /**
     * redis中登录用户USERID的key前缀
     */
    public static String LOGIN_UID_KEY_PREFIX = UIC_PREFIX + "LOGIN_UID_";
    /**
     * 用户信息前缀（手机号）
     */
    public static String UIC_MOBILE_PREFIX = UIC_PREFIX + "MOB_";
    /**
     * 用户角色前缀
     */
    public static String UIC_ROLE_PREFIX = UIC_PREFIX + "B_R_";
    public static String UIC_ROLE_CANCEL_SUFFIX = UIC_PREFIX + "CNL_";
    //用户中心KEY配置==end==========================================================================================

    /**
     * 构建分布式锁的key
     *
     * @param clazz
     * @param key
     * @return
     */
    public String makeLockKey(Class clazz, String key) {
        return buildKeyString(LOCK_PREFIX, clazz.getSimpleName(), key);
    }


    /**
     * 构建分布式锁的key
     *
     * @param key
     * @return
     */
    public String makeLockKey(String key) {
        return buildKeyString(LOCK_PREFIX, key);
    }



    public static String buildKeyString(Object... objs) {
        if (objs == null || objs.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object obj : objs) {
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append("_");
            }
            if (obj instanceof Class) {
                builder.append(((Class) obj).getName());
            } else {
                builder.append(obj);
            }
        }
        return builder.toString();
    }

    /**
     * 热门赛事
     */
    public static final String ODDS_HOT_LIST = "ODDS_HOT_LIST";

    /**
     * 推荐赛事
     */
    public static final String ODDS_REC_LIST = "ODDS_REC_LIST";
    /**
     * 验证码
     */
    public static final String VERIFY_CODE = "VERIFY_CODE";

}
