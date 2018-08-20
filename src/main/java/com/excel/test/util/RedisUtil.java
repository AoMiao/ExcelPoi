package com.excel.test.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
* <p>
* create by AooMiao on 2018-08-20
*/

@Component
public class RedisUtil {
    
    //@Autowired
    RedisTemplate<String, Object> redisTemplate;
    private StringRedisTemplate template;

    //@Autowired
    public RedisUtil(StringRedisTemplate template) {//spring boot默认注入redisTemplate
        super();
        this.template = template;
        System.out.println("redisTemplate:"+redisTemplate);
    }

    public RedisUtil() {
        super();
    }
    
    public ValueWrapper get(Object key) {
        final String keyf = key.toString();
        Object object = null;
        object = redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] key = keyf.getBytes();
                byte[] value = connection.get(key);
                if (value == null) {
                    return null;
                }
                return toObject(value);
            }
        });
        return (object != null ? new SimpleValueWrapper(object) : null);
    }

    public void put(Object key, Object value) {
        final String keyf = key.toString();
        final Object valuef = value;
        final long liveTime = 1 * 60;//缓存1分钟
        redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] keyb = keyf.getBytes();
                byte[] valueb = toByteArray(valuef);
                connection.set(keyb, valueb);
                if (liveTime > 0) {
                    connection.expire(keyb, liveTime);
                }
                return 1L;
            }
        });
    }

    public byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    public Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public void evict(Object key) {
        final String keyf = key.toString();
        redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection)
                    throws DataAccessException {
                return connection.del(keyf.getBytes());
            }
        });
    }

    public void clear() {
        redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    public <T> T get(Object key, Class<T> type) {

        return null;
    }

    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }

    /**
     * 根据前缀批量删除key
     *
     * @param prex
     */
    public void deleteByPrex(String prex) {
        Set<String> keys = redisTemplate.keys(prex + "*");
        redisTemplate.delete(keys);
    }

    /**
     * 根据后缀缀批量删除key
     * 少用，量大时性能差（为什么？请研究列查询）
     *
     * @param suffix
     */
    public void deleteBySuffix(String suffix) {
        Set<String> keys = redisTemplate.keys("*" + suffix);
        redisTemplate.delete(keys);

    }

    /**
     * 删除多个key
     *
     * @param keys
     */
    public void deleteByKeys(String... keys) {
        redisTemplate.delete(Arrays.asList(keys));
    }
    
    
    
}
