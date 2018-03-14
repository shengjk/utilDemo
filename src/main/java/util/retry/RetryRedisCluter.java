package util.retry;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import redis.clients.jedis.HostAndPort;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by shengjk1 on 2018/3/9
 */
public class RetryRedisCluter {
	private  Set<HostAndPort> nodes;
	
	private Callable<RedisCluster> redisClusterCallable = new Callable<RedisCluster>() {
		
		public RedisCluster call() throws Exception {
			return new RedisCluster(nodes); // do something useful here
		}
	};
	
	public RetryRedisCluter(Set<HostAndPort> nodes) {
		this.nodes = nodes;
	}
	
	
	public RedisCluster getRedisCluster() throws Exception {
		Retryer<RedisCluster> build = RetryerBuilder.<RedisCluster>newBuilder().retryIfException()
				.withWaitStrategy(WaitStrategies.fixedWait(20, TimeUnit.MILLISECONDS))
				.withStopStrategy(StopStrategies.stopAfterAttempt(9999999))
				.build();
		
		try {
			return build.call(redisClusterCallable);
		} catch (Exception e) {
			throw e;
		}
	}
	
}
