package util.kafka.productor;

/*
 *kafka 自定义分区
 */
//public class TestPartition implements Partitioner {
//
//	public TestPartition(VerifiableProperties props) {
//
//	}
//
//	@Override
//	public int partition(Object key, int numPartition) {
//		// TODO Auto-generated method stub
//		System.out.println("numPartition" + numPartition);
//		String k = (String) key;
//		if (k == null) {
//			Random radom = new Random();
//			return radom.nextInt(numPartition);
//
//		} else {
//			k = (String) key;
//			int result = Math.abs(k.hashCode()) % numPartition;
//			return result;
//		}
//	}
//
//}

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class TestPartition implements Partitioner{
	
	@Override
	/**
	 * Cluster(id = gQ1Tya57TYaakZap-DkOpQ, nodes = [bogon:9093 (id: 1 rack: null), bogon:9092 (id: 0 rack: null)], partitions = [Partition(topic = test1, partition = 1, leader = 1, replicas = [1,], isr = [1,]), Partition(topic = test1, partition = 0, leader = 0, replicas = [0,], isr = [0,])])
	 */
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		System.out.println("topic=============== "+topic);
		System.out.println("key=============== "+key);
		System.out.println("keyBytes=============== "+new String(keyBytes));
		System.out.println("value=============== "+value);
		System.out.println("valueBytes=============== "+new String(valueBytes));
		System.out.println("cluster=============== "+cluster.toString());
		return 3;
	}
	
	@Override
	public void close() {
		System.out.println("aaaaa11111111111");
	}
	
	@Override
	public void configure(Map<String, ?> configs) {
		System.out.println("aaaaa");
	}
}