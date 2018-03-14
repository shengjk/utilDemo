//package com.ishansong.util
//
//import java.util
//import java.util.{ TreeSet}
//import java.util.concurrent.{Callable, TimeUnit}
//
//import com.github.rholder.retry.{RetryerBuilder, StopStrategies, WaitStrategies}
//import org.apache.spark.SparkConf
//import org.slf4j
//import org.slf4j.LoggerFactory
//import redis.clients.jedis.{HostAndPort, Jedis, JedisCluster, JedisPool}
//
//import scala.collection.mutable.ArrayBuffer
//
///**
//  * Create by mr on 2018/1/29
//  */
//class RedisUtils(val pattern: String, val conf: SparkConf){
//	@transient
//	val logger: slf4j.Logger = LoggerFactory.getLogger("com.ishansong.util.RedisUtils")
//
//	private val rediserrorTask = new Callable[Array[String]]() {
//		@throws[Exception]
//		override def call: Array[String] = {
//			val conn = new JedisCluster(new HostAndPort(conf.get("redis.host"), Integer.parseInt(conf.get("redis.port"))), 2000 * 10);
//			val treeSet: TreeSet[String] = new TreeSet[String]()
//			val nodes: java.util.Map[String, JedisPool] = conn.getClusterNodes
//			for (k <- nodes.keySet().toArray) {
//				var jedis: Jedis = null
//				try {
//					val jedisPool: JedisPool = nodes.get(k)
//					jedis = jedisPool.getResource
//					treeSet.addAll(jedis.keys(pattern))
//				} catch {
//					case e: Exception => {
//						conn.close()
//						throw e
//					}
//				} finally {
//					if (null != jedis) {
//						jedis.close()
//					}
//				}
//			}
//			conn.close()
//			var arr = ArrayBuffer[String]()
//			val iter: util.Iterator[String] = treeSet.iterator()
//			while (iter.hasNext) {
//				arr += iter.next().toString
//			}
//			val array: Array[String] = arr.toArray
//			array
//		}
//	}
//
//	def keys(): Array[String]  = {
//		val retryer = RetryerBuilder.newBuilder[Array[String]].retryIfException()
//				.withWaitStrategy(WaitStrategies.fixedWait(200, TimeUnit.MILLISECONDS))
//				.withStopStrategy(StopStrategies.stopAfterAttempt(1000)) // 重试3次后停止
//				.build();
//
//		try
//			retryer.call(rediserrorTask)
//		catch {
//			case e: Exception =>
//				logger.error("RedisUtils error ", e)
//				throw new RuntimeException("redis error ", e)
//		}
//	}
//}
//
//
//
//
//object RedisUtils {
//	@transient
//	val logger: slf4j.Logger = LoggerFactory.getLogger("com.ishansong.util.RedisUtils")
//
//	def keys(pattern: String)(implicit conf: SparkConf): Array[String] = {
//		val conn = new JedisCluster(new HostAndPort(conf.get("redis.host"), Integer.parseInt(conf.get("redis.port"))), 2000 * 10);
//		val treeSet: TreeSet[String] = new TreeSet[String]()
//		val nodes: java.util.Map[String, JedisPool] = conn.getClusterNodes
//		for (k <- nodes.keySet().toArray) {
//			var jedis: Jedis = null
//			try {
//				val jedisPool: JedisPool = nodes.get(k)
//				jedis = jedisPool.getResource
//				treeSet.addAll(jedis.keys(pattern))
//			} catch {
//				case e: Exception => {
//					conn.close()
//					logger.error("RedisUtils error ", e)
//					throw e
//				}
//			} finally {
//				if (null != jedis) {
//					jedis.close()
//				}
//			}
//		}
//		conn.close()
//		var arr = ArrayBuffer[String]()
//		val iter: util.Iterator[String] = treeSet.iterator()
//		while (iter.hasNext) {
//			arr += iter.next().toString
//		}
//		val array: Array[String] = arr.toArray
//		array
//	}
//
//
//
//
//
//}
