package util.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by shengjk1 on 2017/12/7
 */
public class KProducer {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("partitioner.class", "xmht.kafka1.productor.TestPartition");
		Producer producer = new KafkaProducer<String, String>(props);
		
		producer.send(new ProducerRecord<String, String>("test1","111111","111111"));
//		producer.close();
		
	}
}
