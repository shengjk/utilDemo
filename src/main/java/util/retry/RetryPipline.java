package util.retry;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by shengjk1 on 2018/3/9
 */
public class RetryPipline {
	private RedisCluster redisCluster;
	
	private Callable<RedisCluster.RedisClusterPipeline> pipelineCallable = new Callable<RedisCluster.RedisClusterPipeline>() {
		
		public RedisCluster.RedisClusterPipeline call() throws Exception {
			return redisCluster.pipelined(); // do something useful here
		}
	};
	
	public RetryPipline(RedisCluster redisCluster) {
		this.redisCluster = redisCluster;
	}
	
	
	public RedisCluster.RedisClusterPipeline getPiple() throws Exception {
		Retryer<RedisCluster.RedisClusterPipeline> build = RetryerBuilder.<RedisCluster.RedisClusterPipeline>newBuilder().retryIfException()
				.withWaitStrategy(WaitStrategies.fixedWait(100, TimeUnit.MILLISECONDS))
				.withStopStrategy(StopStrategies.stopAfterAttempt(999999))
				.build();
		
		try {
			return build.call(pipelineCallable);
		} catch (Exception e) {
			throw e;
		}
	}
	
}
