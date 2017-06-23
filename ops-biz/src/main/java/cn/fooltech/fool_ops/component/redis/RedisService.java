package cn.fooltech.fool_ops.component.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;


@Component
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 将数据储存到redis中
     *
     * @param key     储存redis的key
     * @param t       储存redis的value
     * @param expired 过期时间
     * @param <T>
     */

    @SuppressWarnings("unchecked")
    public <T> void set(final String key, final T t, final int expired) {
        if (null == key || 0 == key.length() || null == t) {
            return;
        }
        try {
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {

                    try {
                        byte[] bufferKey = key.getBytes("utf-8");

                        //System.out.println("===================================");
                        //System.out.println(JSON.toJSONString(t));
                        //System.out.println("===================================");


                        byte[] bufferVal = JSON.toJSONString(t).getBytes("utf-8");
                        connection.setEx(bufferKey, expired, bufferVal);

                    }catch (UnsupportedEncodingException e2){
                        System.out.println(e2 + "储存数据获取byte buffer失败");
                    }

                    return null;
                }
            });
        } catch (Exception e) {
            System.out.println(e + "储存数据失败");
        }
    }

    public <T> void setm(final String key, final String field, final T t) {
        if (null == key || 0 == key.length() || null == t) {
            return;
        }
        try {
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {

                    try {
                        byte[] bufferKey = key.getBytes("utf-8");

                        //System.out.println("===================================");
                        //System.out.println(JSON.toJSONString(t));
                        //System.out.println("===================================");


                        byte[] bufferVal = JSON.toJSONString(t).getBytes("utf-8");
                        byte[] bufferField = field.getBytes("utf-8");
                        //connection.setEx(bufferKey, expired, bufferVal);
                        connection.hSet(bufferKey, bufferField, bufferVal);
                    }catch (UnsupportedEncodingException e2){
                        System.out.println(e2 + "储存数据获取byte buffer失败");
                    }

                    return null;
                }
            });
        } catch (Exception e) {
            System.out.println(e + "储存数据失败");
        }
    }


    /**
     * 通过key值获取value
     *
     * @param key
     * @param typeReference
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final String key, final TypeReference typeReference) {
        try {
            return (T) redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {

                    try {
                        byte[] val = connection.get(key.getBytes());
                        if (null == val || 0 == val.length) {
                            return null;
                        }

                        String cacheVal = new String(val, "utf-8");

                        //System.out.println("===================================");
                        //System.out.println(cacheVal);
                        //System.out.println("===================================");

                        T result = (T) JSON.parseObject(cacheVal, typeReference);
                        return result;
                    }catch (UnsupportedEncodingException e2){
                        System.out.println(e2 + "获取数据转换utf-8失败");
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            System.out.println(e + "获取失败");
        }
        return null;
    }

    /**
     * 通过key值获取value
     *
     * @param key
     * @param typeReference
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getm(final String key, final String field, final TypeReference typeReference) {
        try {
            return (T) redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {

                    try {

                        byte[] val = connection.hGet(key.getBytes(), field.getBytes());
                        if (null == val || 0 == val.length) {
                            return null;
                        }

                        String cacheVal = new String(val, "utf-8");

                        T result = (T) JSON.parseObject(cacheVal, typeReference);
                        return result;
                    }catch (UnsupportedEncodingException e2){
                        System.out.println(e2 + "获取数据转换utf-8失败");
                        return null;
                    }catch (Exception e3) {
                        e3.printStackTrace();
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e + "获取失败");
        }
        return null;
    }

    //
//
//    @SuppressWarnings("unchecked")
    public void expired(final String key, final int expired) {
        try {
            // 获取序列化转换器
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    if (!StringUtils.isEmpty(key)) {
                        connection.expire(key.getBytes(), expired);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            System.out.println(e + "重置时间失败");

        }
//
    }

    //
//    /**
//     * 根据key删除redis中的信息
//     * @param key
//     * @param <T>
//     */
//    @SuppressWarnings("unchecked")
    public <T> void remove(final String key) {
        try {
            final byte[] keys = key.getBytes();
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.del(keys);
                    return null;
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }

    }


}
