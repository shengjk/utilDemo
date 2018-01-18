package util;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by shengjk1 on 2017/12/26
 */
public class JedisDemo {
	/**
	 * 在不同的线程中使用相同的Jedis实例会发生奇怪的错误。但是创建太多的实现也不好因为这意味着会建立很多sokcet连接，也会导致奇怪的错误发生。单一Jedis实例不是线程安全的。为了避免这些问题，可以使用JedisPool, JedisPool是一个线程安全的网络连接池。可以用JedisPool创建一些可靠Jedis实例，可以从池中拿到Jedis的实例。这种方式可以解决那些问题并且会实现高效的性能.
	 * @param args
	 */
	public static void main(String[] args) {
	
		//单机版的
		Jedis jedis=new Jedis("localhost");
		System.out.println("Connection to server sucessfully");
		System.out.println("Server is running: "+jedis.ping());
		jedis.set("w3ckey","Redis tutorial");
		System.out.println(jedis.get("w3ckey"));

		jedis.close();
		/*
		JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
		//控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
		//如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
		jedisPoolConfig.setMaxTotal(500);
		//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
		jedisPoolConfig.setMaxIdle(5);
		//表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
		jedisPoolConfig.setMaxWaitMillis(1000 * 100);
		//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
		jedisPoolConfig.setTestOnBorrow(true);
		
		JedisPool jedisPool=new JedisPool(jedisPoolConfig,"localhost");
		
		Jedis jedis=null;
		
		try {
			jedis = jedisPool.getResource();
			System.out.println(jedis.get("aaa"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null!=jedis){
				jedis.close();
			}
		}
		
		jedisPool.close();*/
		
		
		//集群版的
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		// 最大连接数
		poolConfig.setMaxTotal(1);
		// 最大空闲数
		poolConfig.setMaxIdle(1);
		// 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
		// Could not get a resource from the pool
		poolConfig.setMaxWaitMillis(1000);
		Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
		nodes.add(new HostAndPort("127.0.0.1", 7000));
		nodes.add(new HostAndPort("127.0.0.1", 7001));
		nodes.add(new HostAndPort("127.0.0.1", 7002));
		JedisCluster cluster = new JedisCluster(nodes, poolConfig);
		String name = cluster.get("name");
		System.out.println(name);
		cluster.set("age", "18");
		Long setnx = cluster.setnx("a", "a");
		System.out.println(setnx);
		System.out.println(cluster.get("age"));
		try {
			cluster.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
