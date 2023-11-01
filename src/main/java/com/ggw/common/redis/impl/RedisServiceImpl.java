package com.ggw.common.redis.impl;

import com.alibaba.fastjson.JSON;
import com.ggw.common.redis.RedisResult;
import com.ggw.common.redis.RedisService;
import com.ggw.util.NumStringUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.*;

/**
 * Redis 服务接口实现类
 *
 * @author liwei
 * 16/10/30 下午5:28
 */
@Component("redisService")
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);


    private static JedisPool pool = null;

    @Resource
    private Environment env;


    /**
     * 初始化操作
     */
    @Override
    @PostConstruct
    public void init() {
        if (pool != null) {
            return;
        }
        /**
         JedisPoolConfig config = new JedisPoolConfig();
         config.setMaxIdle(dynProps4Files.getInt("REDIS_MAX_IDLE", JedisPoolConfig.DEFAULT_MAX_IDLE));
         config.setMaxTotal(dynProps4Files.getInt("REDIS_MAX_TOTAL", JedisPoolConfig.DEFAULT_MAX_TOTAL));
         config.setMaxWaitMillis(dynProps4Files.getLong("REDIS_MAX_WAIT", JedisPoolConfig.DEFAULT_MAX_WAIT_MILLIS));
         config.setTestOnBorrow(true);
         pool = new JedisPool(config, dynProps4Files.getProperty("REDIS_HOST"),dynProps4Files.getInt("REDIS_PORT", 6379),dynProps4Files.getInt("REDIS_MAX_WAIT", 1000), dynProps4Files.getProperty("REDIS_PASSWORD", null));
         */

        JedisPoolConfig config = new JedisPoolConfig();
        int maxIdle = env.getProperty("spring.redis.pool.max-idle") == null ? JedisPoolConfig.DEFAULT_MAX_IDLE : Integer.valueOf(env.getProperty("spring.redis.pool.max-idle"));
        int maxTotal = env.getProperty("spring.redis.pool.max-active") == null ? JedisPoolConfig.DEFAULT_MAX_TOTAL : Integer.valueOf(env.getProperty("spring.redis.pool.max-active"));
        long maxWaitMillis = env.getProperty("spring.redis.pool.max-wait") == null ? JedisPoolConfig.DEFAULT_MAX_WAIT_MILLIS : Long.valueOf(env.getProperty("spring.redis.pool.max-wait"));
        int port = env.getProperty("spring.redis.port") == null ? 6379 : Integer.valueOf(env.getProperty("spring.redis.port"));

        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(true);
        config.setBlockWhenExhausted(true);

        int timeout = env.getProperty("spring.redis.timeout") == null ? 0 : Integer.valueOf(env.getProperty("spring.redis.timeout"));
        String password = env.getProperty("spring.redis.password");
        if (org.apache.commons.lang3.StringUtils.isEmpty(password)) {
            pool = new JedisPool(config, env.getProperty("spring.redis.host"), port, timeout);
        } else {
            pool = new JedisPool(config, env.getProperty("spring.redis.host"), port, timeout, password);
        }

    }

    @Override
    @PreDestroy
    public void destroy() {
        try {
            if (pool != null) {
                pool.destroy();
            }

        } catch (Exception e) {
            //do nothing
        }
    }

    /**
     * 从连接池里取连接（用完连接后必须销毁）
     *
     * @return
     */
    private Jedis getResource() {
        return pool.getResource();

    }

    /**
     * 用完后，销毁连接（必须）
     *
     * @param jedis
     */
    private void destroyResource(Jedis jedis) {
        if (jedis == null) {
            return;
        }
        IOUtils.closeQuietly(jedis);
    }

    /**
     * 根据key取数据
     *
     * @param key
     * @return
     */
    public String get(String key) {

        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return StringUtils.EMPTY;
        }
        Jedis jedis = this.getResource();
        try {
            return jedis.get(key);
        } finally {
            this.destroyResource(jedis);
        }

    }

    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @return
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }
        if (clazz == null) {
            logger.warn("Params clazz is null!");
            return null;
        }
        String value = get(key);
        if (StringUtils.isBlank(value) || StringUtils.equalsIgnoreCase(value, BLANK_CONTENT)) {
            return null;
        }
        T obj = null;
        try {
            obj = om.readValue(value, clazz);
        } catch (IOException e) {
            logger.error("Can not unserialize obj to [{}] with string [{}]", clazz.getName(), value);
        }
        return obj;
    }

    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @return
     */
    @Override
    public <T> RedisResult<T> getResult(String key, Class<T> clazz) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }
        if (clazz == null) {
            logger.warn("Params clazz is null!");
            return null;
        }
        RedisResult<T> redisResult = new RedisResult<T>();

        String value = get(key);
        if (StringUtils.isBlank(value)) {
            redisResult.setExist(false);
            return redisResult;
        }
        //到此步，则表明redis中存在key
        redisResult.setExist(true);
        if (StringUtils.equalsIgnoreCase(value, BLANK_CONTENT)) {
            return redisResult;
        }
        T obj = null;
        try {
            obj = om.readValue(value, clazz);
            redisResult.setResult(obj);
        } catch (IOException e) {
            logger.error("Can not unserialize obj to [{}] with string [{}]", clazz.getName(), value);
            //到此步直接视为无值
            redisResult.setExist(false);
        }
        return redisResult;
    }

    /**
     * 根据key取 Collection 对象数据
     *
     * @param key
     * @param elementClazz 集合元素类型
     * @return
     */
    @Override
    public <T> RedisResult<T> getListResult(String key, Class<T> elementClazz) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }

        if (elementClazz == null) {
            logger.warn("Params elementClazz is null!");
            return null;
        }
        RedisResult<T> redisResult = new RedisResult<T>();

        String value = get(key);
        if (StringUtils.isBlank(value)) {
            redisResult.setExist(false);
            return redisResult;
        }

        //到此步，则表明redis中存在key
        redisResult.setExist(true);
        if (StringUtils.equalsIgnoreCase(value, BLANK_CONTENT)) {
            return redisResult;
        }

        List<T> list = null;
        try {
            list = om.readValue(value, getCollectionType(List.class, elementClazz));
            redisResult.setListResult(list);
        } catch (IOException e) {
            logger.error("Can not unserialize list to [{}] with string [{}]", elementClazz.getName(), value);
            //到此步直接视为无值
            redisResult.setExist(false);
        }

        return redisResult;
    }

    private static <T> JavaType getCollectionType(Class<? extends Collection> collectionClazz, Class<T> elementClazz) {
        return om.getTypeFactory().constructCollectionType(collectionClazz, elementClazz);
    }

    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param obj
     * @param expireTime 缓存 内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    public String set(String key, Object obj, int expireTime) {
        String value = RedisService.BLANK_CONTENT;
        if (obj != null) {
            try {
                value = RedisService.om.writeValueAsString(obj);
            } catch (IOException e) {
                logger.error("Can not write object to redis:" + obj.toString(), e);
            }
        }
        return set(key, value, expireTime);
    }

    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param value
     * @param expireTime 缓存 内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    public String set(String key, String value, int expireTime) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }

        if (value == null) {

            logger.warn("Params value is null!");
            return null;
        }

        Jedis jedis = this.getResource();
        try {
            String result = jedis.set(key, value);
            if (expireTime > 0) {
                jedis.expire(key, expireTime);
            }
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param obj
     * @return
     */
    @Override
    public String set(String key, Object obj) {
        String value = RedisService.BLANK_CONTENT;
        if (obj != null) {
            try {
                value = RedisService.om.writeValueAsString(obj);
            } catch (IOException e) {
                logger.error("Can not write object to redis:" + obj.toString(), e);
            }
        }
        return set(key, value);
    }


    /**
     * 写入/修改 缓存内容(默认有过期时间 1小时)
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String set(String key, String value) {
        return this.set(key, value, DEFAULT_EXPIRE_TIME);
    }


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
    @Override
    public String set(String key, String value, String nxxx, String expx, long expiredTime) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }

        if (value == null) {
            logger.warn("Params value is null!");
            return null;
        }

        Jedis jedis = this.getResource();
        try {
            return jedis.set(key, value, nxxx, expx, expiredTime);
        } finally {
            this.destroyResource(jedis);
        }

    }

    /**
     * 仅当redis中不含对应的key时，设定缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，expireTime必须大于0
     * @return
     */
    @Override
    public String setnx(String key, String value, long expiredTime) {
        return this.set(key, value, NXXX_SET_IF_NOT_EXISTS, EXPX_SECONDS, expiredTime);
    }

    /**
     * 仅当redis中含有对应的key时，修改缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，expireTime必须大于0
     * @return
     */
    @Override
    public String setxx(String key, String value, long expiredTime) {
        return this.set(key, value, NXXX_SET_IF_EXISTS, EXPX_SECONDS, expiredTime);
    }

    /**
     * 根据key删除缓存
     *
     * @param keys
     * @return
     */
    public Long delete(String... keys) {
        if (keys == null || keys.length == 0) {
            logger.warn("Params keys is null or 0 length!");
            return -1L;
        }
        Jedis jedis = this.getResource();
        try {
            return jedis.del(keys);
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * 判断对应的key是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        if (StringUtils.isBlank(key)) {
            //不接受空值
            return false;
        }
        Jedis jedis = this.getResource();
        try {
            return jedis.exists(key);
        } finally {
            this.destroyResource(jedis);
        }


    }

    /**
     * redis 加法运算
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Long incrBy(String key, long value) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }
        Jedis jedis = this.getResource();
        try {
            return jedis.incrBy(key, value);
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * 设定redis 对应的key的剩余存活时间
     *
     * @param key
     * @param seconds
     */
    @Override
    public void setTTL(String key, int seconds) {
        if (seconds < 0) {
            return;
        }
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return;
        }
        Jedis jedis = this.getResource();
        try {
            jedis.expire(key, seconds);
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 添加风控有关的redis操作接口
     * @params:
     * @Date: 上午10:52 2018/3/22
     */
    /**
     * 发布消息到指定的频道
     *
     * @param channel
     * @param message
     */
    public void publish(final String channel, final String message) {
        Jedis jedis = this.getResource();
        jedis.publish(channel, message);
        this.destroyResource(jedis);
    }

    /**
     * 订阅给定的一个或多个频道的信息
     *
     * @param jedisPubSub
     * @param channels
     */
    public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        Jedis jedis = this.getResource();
        jedis.subscribe(jedisPubSub, channels);
        this.destroyResource(jedis);
    }

    /**
     * 将脚本 script 添加到脚本缓存中，但并不立即执行这个脚本
     *
     * @param script
     * @param <T>
     * @return
     */
    public <T> T scriptLoad(final String script) {
        Jedis jedis = this.getResource();
        T obj = null;
        try {
            obj = (T) jedis.scriptLoad(script);
            return obj;
        } finally {
            this.destroyResource(jedis);
        }
    }


    /**
     * 对 Lua 脚本进行求值
     *
     * @param sha
     * @param keycount
     * @param args
     * @param <T>
     * @return
     */
    public <T> T evalsha(final String sha, final int keycount, final String... args) {
        Jedis jedis = this.getResource();
        T obj = null;
        try {
            obj = (T) jedis.evalsha(sha, keycount, args);
            return obj;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /*************************************风控操作接口END*************************************/

    /**
     * 根据通配符表达式查询key值的set，通配符仅支持*
     *
     * @param pattern 如 ke6*abc等
     * @return
     */
    public Set<String> keys(String pattern) {

        if (StringUtils.isBlank(pattern)) {
            logger.warn("Params pattern is blank!");
            return Collections.emptySet();
        }
        Jedis jedis = this.getResource();
        try {
            return jedis.keys(pattern);
        } finally {
            this.destroyResource(jedis);
        }
    }

    /***************************************************jedis操作set***************************************************/

    /**
     * @Author: Sim
     * @Description: 把一个或多个元素添加到指定集合
     * @params:
     * @Date: 下午1:29 2018/3/30
     */
    public Long sadd(String key, String members) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(members)) {
            logger.warn("key or member is blank");
            return null;
        }
        Jedis jedis = this.getResource();
        Long result = null;
        try {
            result = jedis.sadd(key, members);

            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 添加元素到指定集合并设置集合的有效期
     * @params:
     * @Date: 下午4:02 2018/3/30
     */
    public boolean sadd(String key, String members, int expireTime) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(members)) {
            logger.warn("key or member is blank");
            return false;
        }
        Jedis jedis = this.getResource();
        List<Object> result = null;
        try {
            // 开始事务
            Transaction transaction = jedis.multi();
            transaction.sadd(key, members);
            transaction.expire(key, expireTime);
            // 执行事务 result中返回两个Long类型的1
            result = transaction.exec();
            for (Object rt : result) {
                if (!isStatusOk(rt.toString())) {
                    return false;
                }
            }
            return true;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 返回集合所有成员
     * @params:
     * @Date: 下午1:36 2018/3/30
     */
    public Set<String> smembers(String key) {
        if (StringUtils.isBlank(key)) {
            logger.warn("key is blank");
            return null;
        }
        Jedis jedis = this.getResource();
        Set<String> result = null;
        try {
            result = jedis.smembers(key);
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 判断元素是否是集合成员
     * @params:
     * @Date: 下午1:39 2018/3/30
     */

    public boolean sismember(String key, String memeber) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(memeber)) {
            logger.warn("key or member is blank");
            return false;
        }

        Jedis jedis = this.getResource();
        boolean result;
        try {
            result = jedis.sismember(key, memeber);

            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 移除一个或多个元素，不存的元素会被忽略
     * @params:
     * @Date: 下午1:44 2018/3/30
     */
    public Long srem(String key, String... members) {
        if (StringUtils.isBlank(key)) {
            logger.warn("key is blank");
            return null;
        }

        Jedis jedis = this.getResource();
        Long result = null;
        try {
            result = jedis.srem(key, members);
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 随机移除并返回一个元素
     * @params:
     * @Date: 下午1:57 2018/3/30
     */
    public String spop(String key) {
        if (StringUtils.isBlank(key)) {
            logger.warn("key is blank");
            return null;
        }

        Jedis jedis = this.getResource();
        String result;
        try {
            result = jedis.spop(key);
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 把指定成员从一个集合移动到目标集合，指定成员不存在，不执行任何操作
     * @params:
     * @Date: 下午1:59 2018/3/30
     */
    public Long smove(String srckey, String dstkey, String member) {
        if (StringUtils.isBlank(srckey) || StringUtils.isBlank(dstkey) || StringUtils.isBlank(member)) {
            logger.warn("srckey or dstkey or member is blank");
            return null;
        }

        Jedis jedis = this.getResource();
        Long result;
        try {
            result = jedis.smove(srckey, dstkey, member);
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 返回指定集合的并集
     * @params:
     * @Date: 下午1:51 2018/3/30
     */
    public Set<String> sunion(String... keys) {
        Jedis jedis = this.getResource();
        Set<String> result = null;

        try {
            result = jedis.sunion(keys);
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 返回多个集合的交集
     * @params:
     * @Date: 下午1:53 2018/3/30
     */
    public Set<String> sinter(String... keys) {
        Jedis jedis = this.getResource();

        Set<String> result;
        try {
            result = jedis.sinter(keys);
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 返回多个集合的差集
     * @params:
     * @Date: 下午1:54 2018/3/30
     */
    public Set<String> sdiff(String... keys) {
        Jedis jedis = this.getResource();
        Set<String> result;
        try {
            result = jedis.sdiff(keys);
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 当key不存在时放入值，超时时间单位为秒
     * @params:
     * @Date: 下午3:14 2018/3/30
     */
    public boolean addn(String key, String value, Long expiredTime) {
        if (StringUtils.isBlank(key)) {
            logger.warn("key is blank");
            return false;
        }

        Jedis jedis = this.getResource();
        String result = null;
        try {
            result = jedis.set(key, value, "NX", "EX", expiredTime == null ? 1L : expiredTime);
            return isStatusOk(result);
        } finally {
            this.destroyResource(jedis);
        }
    }

    private final String OK_CODE = "OK";
    private final String OK_MULTI_CODE = "+OK";

    /**
     * 判断 返回值是否ok.
     */
    public boolean isStatusOk(String status) {
        return (status != null) && (OK_CODE.equals(status) || OK_MULTI_CODE.equals(status) || status.equals("1"));
    }

    /***************************************************jedis操作set End***************************************************/

    /**
     * 将对象转为json字符串。若对象为null，则返回 {@link RedisService#BLANK_CONTENT}
     *
     * @param object
     * @return
     */
    @Override
    public String toJsonString(Object object) {
        if (object == null) {
            return BLANK_CONTENT;
        }

        if ((object instanceof Collection) && CollectionUtils.isEmpty((Collection) object)) {
            return BLANK_CONTENT;
        }

        if ((object instanceof Map) && CollectionUtils.isEmpty((Map) object)) {
            return BLANK_CONTENT;
        }

        try {
            return om.writeValueAsString(object);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String makeSerializedString(Object value) {
        if (value == null) {
            return BLANK_CONTENT;
        }

        if ((value instanceof Collection) && ((Collection) value).size() == 0) {
            return BLANK_CONTENT;
        }

        if ((value instanceof Map) && ((Map) value).size() == 0) {
            return BLANK_CONTENT;
        }


        return JSON.toJSONString(value);
    }

    /**
     * @param key
     * @return
     * @Title: generateIncr
     * @Description: Atomically increments by one the current value.
     */
    @Override
    public long generateIncr(String key) {
        Jedis jedis = this.getResource();
        long result = 0;
        try {
            result = jedis.incrBy(key, 1);
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * 自增map
     *
     * @param key
     * @param field
     * @param time
     * @param unit
     * @return
     */
    @Override
    public long getAndIncrFromHash(String key, String field, int time, TimeUnit unit) {
        Jedis jedis = this.getResource();
        long result = 0;
        try {
            if (jedis.hexists(key, field)) {
                return jedis.hincrBy(key, field, 1);
            }
            jedis.hset(key, field, "1");
            jedis.expire(key, time);
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * 插入HASH值，并设置整个HASH过期时间
     */
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = this.getResource();
        try {
            return jedis.hgetAll(key);
        } finally {
            this.destroyResource(jedis);
        }
    }

    public void putHashValueWithExpireDate(String key, String field, Object value, int time, TimeUnit unit) {
        Jedis jedis = this.getResource();
        try {
            jedis.hset(key, field, JSON.toJSONString(value));
            jedis.expire(key, time);
        } finally {
            this.destroyResource(jedis);
        }
    }

    public void putHashValue(String key, String field, Object value) {
        Jedis jedis = this.getResource();
        try {
            jedis.hset(key, field, JSON.toJSONString(value));
        } finally {
            this.destroyResource(jedis);
        }
    }

    public <T> T getHashObject(String key, String field, Class<T> clazz) {
        Jedis jedis = this.getResource();
        try {
            if (!jedis.hexists(key, field)) {
                return null;
            }
            String json = jedis.hget(key, field);
            return json == null ? null : JSON.parseObject(json, clazz);
        } finally {
            this.destroyResource(jedis);
        }
    }

    public <T> List<T> getHashList(String key, String field, Class<T> clazz) {
        Jedis jedis = this.getResource();
        try {
            if (!jedis.hexists(key, field)) {
                return null;
            }
            String json = jedis.hget(key, field);
            return json == null ? null : JSON.parseArray(json, clazz);
        } finally {
            this.destroyResource(jedis);
        }
    }

    public String getStringFromHash(String key, String field) {
        Jedis jedis = this.getResource();
        try {
            if (!jedis.hexists(key, field)) {
                return null;
            }
            String json = jedis.hget(key, field);
            return json == null ? null : JSON.parseObject(json, String.class);
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * 删除HASH值
     *
     * @param key
     * @param field
     */
    public void delHashValue(String key, String field) {
        Jedis jedis = this.getResource();
        try {
            jedis.hdel(key, field);
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 获取在线用户数 TODO 数据量大要测试性能问题
     * @params:
     * @Date: 下午4:02 2018/7/23
     */
    public long getKeysLen(String key) {
        Jedis jedis = this.getResource();
        try {
            Set<String> set = jedis.keys(key);
            return set.size();
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * @Author: Sim
     * @Description: 生成随机的交易流水号
     * @params:
     * @Date: 下午1:46 2018/5/8
     */
    @Override
    public String getTradeNo(String prefix) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        StringBuilder tradeNo = new StringBuilder();
        tradeNo.append(prefix);
        tradeNo.append(sdf.format(now));
        tradeNo.append(NumStringUtils.numToSixUpperString(generateIncr("trade_sequence")));
        return tradeNo.toString();
    }

}
