package com.brian.taotao.content.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.brian.taotao.jedis.JedisClient;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {

    @Test
    public void testJedis() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.set("jedis-key", "123456");
        String result = jedis.get("jedis-key");
        System.out.println(result);
        Long incr = jedis.incr("jedis-key");
        System.out.println(incr);
        jedis.close();
    }

    @Test
    public void testJedisPool() {
        JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
        Jedis jedis = jedisPool.getResource();
        String s = jedis.get("jedis-key");
        System.out.println(s);
        jedis.close();
        jedisPool.close();
    }


    public void testJedisCluster() {
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("127.0.0.1", 7001));
        nodes.add(new HostAndPort("127.0.0.1", 7002));
        nodes.add(new HostAndPort("127.0.0.1", 7003));
        nodes.add(new HostAndPort("127.0.0.1", 7004));
        nodes.add(new HostAndPort("127.0.0.1", 7005));
        nodes.add(new HostAndPort("127.0.0.1", 7006));
        JedisCluster cluster = new JedisCluster(nodes);
        cluster.set("cluster-key", "123");
        String s = cluster.get("cluster-key");
        System.out.println(s);
        cluster.close();
    }

    @Test
    public void testJedisClient() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext
                ("classpath:spring/applicationContext-redis.xml");
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("jedis-client", "123");
        String s = jedisClient.get("jedis-client");
        System.out.println(s);
    }
}
