package util.kafka.test;

import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * Created by shengjk1 on 2018/1/4
 */
public class TestPartition implements org.apache.kafka.clients.producer.Partitioner {
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		return 2;
	}
	
	@Override
	public void close() {
	
	}
	
	@Override
	public void configure(Map<String, ?> configs) {
	
	}
	


}
