package com.ggw.common.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.codehaus.jackson.map.ObjectMapper;
import redis.clients.jedis.JedisPubSub;

public interface RedisService {
    /**
     * NX: 当且仅当缓存中特定的key不存在时设定数据
     */
    String NXXX_SET_IF_NOT_EXISTS = "nx";
    /**
     * XX: 当且仅当缓存中特定的key存在时设定数据
     */
    String NXXX_SET_IF_EXISTS = "xx";

    /**
     * EX:缓存失效的时间单位：秒
     */
    String EXPX_SECONDS = "ex";

    /**
     * EX:缓存失效的时间单位：毫秒
     */
    String EXPX_MILLISECOND = "px";

    /**
     * 默认过期时间 ，3600(秒)
     */
    int DEFAULT_EXPIRE_TIME = 3600;

    ObjectMapper om = new ObjectMapper();


    /**
     * 空白占位符
     */
    String BLANK_CONTENT = "__BLANK__";

    /**
     * 初始化操作
     */
    void init();


    void destroy();

    //    /**
    //     * 从连接池里取连接（用完连接后必须销毁）
    //     *
    //     * @return
    //     */
    //     Jedis getResource();

    //    /**
    //     * 用完后，销毁连接（必须）
    //     *
    //     * @param jedis
    //     */
    //    void destroyResource(Jedis jedis);

    /**
     * 根据key取数据
     *
     * @param key
     * @return
     */
    String get(String key);


    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @return
     */
    <T> T get(String key, Class<T> clazz);


    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> RedisResult<T> getResult(String key, Class<T> clazz);

    /**
     * 根据key取 Collection 对象数据
     *
     * @param key
     * @param elementClazz 集合元素类型
     * @param <T>
     * @return
     */
    <T> RedisResult<T> getListResult(String key, Class<T> elementClazz);


    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param obj
     * @return
     */
    String set(String key, Object obj);


    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param value
     * @return
     */
    String set(String key, String value);


    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param obj
     * @param expireTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    String set(String key, Object obj, int expireTime);


    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param value
     * @param expireTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    String set(String key, String value, int expireTime);

    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param value
     * @param nxxx        缓存写入值模式 详见 {@link RedisService#NXXX_SET_IF_EXISTS}, {@link RedisService#NXXX_SET_IF_NOT_EXISTS}
     * @param expx        缓存超时时间单位 详见{@link RedisService#EXPX_SECONDS}, {@link RedisService#EXPX_MILLISECOND}
     * @param expiredTime 缓存存活时长，必须 大于0
     * @return
     */
    String set(String key, String value, String nxxx, String expx, long expiredTime);

    /**
     * 仅当redis中不含对应的key时，设定缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    String setnx(String key, String value, long expiredTime);

    /**
     * 仅当redis中含有对应的key时，修改缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    String setxx(String key, String value, long expiredTime);


    /**
     * 根据key删除缓存,
     *
     * @param keys
     * @return
     */
    Long delete(String... keys);

    /**
     * 判断对应的key是否存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);


    /**
     * redis 加法运算
     *
     * @param key
     * @param value
     * @return 运算结果
     */
    Long incrBy(String key, long value);

    /**
     * 设定redis 对应的key的剩余存活时间
     *
     * @param key
     * @param seconds
     */
    void setTTL(String key, int seconds);

    /**
     * 根据通配符表达式查询key值的set，通配符仅支持*
     *
     * @param pattern 如 ke6*abc等
     * @return
     */
    Set<String> keys(String pattern);

    void publish(final String channel, final String message);

    /**
     * 订阅给定的一个或多个频道的信息
     *
     * @param jedisPubSub
     * @param channels
     */
    void subscribe(final JedisPubSub jedisPubSub, final String... channels);

    /**
     * 将脚本 script 添加到脚本缓存中，但并不立即执行这个脚本
     *
     * @param script
     * @param <T>
     * @return
     */
    <T> T scriptLoad(final String script);


    /**
     * 对 Lua 脚本进行求值
     *
     * @param sha
     * @param keycount
     * @param args
     * @param <T>
     * @return
     */
    <T> T evalsha(final String sha, final int keycount, final String... args);
    /**
     * 将对象转为json字符串。若对象为null，则返回 {@link RedisService#BLANK_CONTENT}
     *
     * @param object
     * @return
     */
    String toJsonString(Object object);

    /**
     * json序列化对象。
     *
     * @param value
     * @return 返回序列化后的字符串。若value为null，则返回 {@link RedisService#BLANK_CONTENT}
     */
    String makeSerializedString(Object value);

    Long sadd(String key, String members);

    boolean sadd(String key, String members, int expireTime);

    Set<String> smembers(String key);

    boolean sismember(String key, String memeber);

    Long srem(String key, String... members);

    String spop(String key);

    Long smove(String srckey, String dstkey, String member);

    Set<String> sunion(String... keys);

    Set<String> sinter(String... keys);

    Set<String> sdiff(String... keys);

    boolean addn(String key, String value, Long expiredTime);

    long generateIncr(String key);

    long getAndIncrFromHash(String key, String field, int time, TimeUnit unit);

    Map<String, String> hgetAll(String key);

    void putHashValueWithExpireDate(String key, String field, Object value, int time, TimeUnit unit);

    void putHashValue(String key, String field, Object value);

    String getStringFromHash(String key, String field);

    <T> List<T> getHashList(String key, String field, Class<T> clazz);

    <T> T getHashObject(String key, String field, Class<T> clazz);

    void delHashValue(String key, String field);

    long getKeysLen(String key);

    String getTradeNo(String prefix);
}
