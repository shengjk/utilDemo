package util.xmht;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Properties;


/**
 * Created by shengjk1.
 * <p>
 * 生产者可以保证权限认证
 * SSL is supported only for the new Kafka Producer and Consumer, the older API is not supported
 */
public class ConsumerZbdba {
	private static Logger logger = Logger.getLogger(ConsumerZbdba.class);
	
	public static void main(String[] args) {

//		String path=args[0];
//		Properties prop = new Properties();
//			try {
//				InputStream in = ConfigurationManager.class
//						.getClassLoader().getResourceAsStream(path);
//				prop.load(in);
//			} catch (Exception e) {
//				logger.error("scanError ConfigurationManager 读取配置文件失败 "+e);
//				throw new RuntimeException("scanError ConfigurationManager 读取配置文件失败 "+e);
//			}


//		new ConsumerZbdba("test").start();// 使用kafka集群中创建好的主题 test

//
		Properties props = new Properties();
/* 定义kakfa 服务的地址，不需要将所有broker指定上 */
//		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "centos11:9092,centos12:9092,centos13:9092");
//		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka02-dev.bi-report.v5q.cn:9092,kafka03-dev.bi-report.v5q.cn:9292");
		props.put("zookeeper.connect", "bi-test-004:2181/kafka01cluster");
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "bigdata-mq-test-001:9092");
/* 制定consumer group */
		
		props.put("group.id", "order5test");
		props.put("auto.offset.reset", "earliest");
/* 是否自动确认offset */
//		props.put("enable.auto.commit", "true");
//		props.put(ProducerConfig.CLIENT_ID_CONFIG, "myApiKey");
//		props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "F:\\dev\\server.keystore.jks");
//		props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "123456");
//		props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "F:\\dev\\server.truststore.jks");
//		props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "123456");
//		props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
//		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
//


///* 自动确认offset的时间间隔 */
//		props.put("auto.commit.interval.ms", "1000");
//		props.put("session.timeout.ms", "30000");
/* key的序列化类 */
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
/* value的序列化类 */
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
 /* 定义consumer */
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
/* 消费者订阅的topic, 可同时订阅多个 */
		consumer.subscribe(Arrays.asList("order5test"));
//		consumer.assign();
//		consumer.seek();

 /* 读取数据，读取超时时间为100ms */
		while (true) {
			System.out.println("+++==");
			ConsumerRecords<String, String> records = consumer.poll(10000);
			for (ConsumerRecord<String, String> record : records) {
				
				/*
				offset = 34053, partition=0,topic=bdscanlog3,key = null, value = 02-42-AC-11-00-09|1481695738901|33986|1
				 */
				System.out.printf("offset = %d,timestamp=%s partition=%s,topic=%s,key = %s, value = %s", record.offset(),record.timestamp(), record.partition(), record.topic(), record.key(), record.value() + "\n");
//				System.out.printf("offset = %d,partition=%s,topic=%s,key = %s, value = %s", record.offset(),record.partition(), record.topic(), record.key(), record.value() + "\n");

				
				
// 				OutputStream os = null;
//				try {
//					os = new BufferedOutputStream(new FileOutputStream(new File("./txt.txt"), true));//+File.separator+"txt.txt"
//					byte[] data = (record.value()+"\n").getBytes();
//					os.write(data, 0, data.length);
//					os.flush();
//					System.out.println("写入成功");
//				} catch (Exception e) {
//					e.printStackTrace();
//					System.out.println("文件写入失败");
//				} finally {
//					try {
//						if (null != os)
//							os.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
			}
		}
	}
}
