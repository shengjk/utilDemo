package util.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerUtil {
	
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("bootstrap.servers", "bigdata-dev-mq:9092");
		props.put("group.id", "test");
		props.put("auto.offset.reset", "latest");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList("canal-monitor-order"));
//		consumer.assign(Arrays.asList(new TopicPartition("canal-monitor-order", 0)));
		
		ArrayList<String> strings = new ArrayList<>();
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records){
				System.out.println("offset" + record.offset() + "key"
						+ record.key() + "value" + record.value());
				
//				strings.add(record.value());
//
//				if (strings.size()==10000){
//					outPutList("./",strings,"order_infos.txt",true);
//					strings.clear();
//				}
			}
//			outPutList("./",strings,"order_infos.txt",true);
		}
	}
	
	public static void outPutList(String path,ArrayList<String> text,String FileName,boolean isAppent) throws Exception{
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(new File(path+System.getProperty("file.separator")+FileName),isAppent));
			for (String textValue:text ) {
				byte[] data = textValue.getBytes();
				os.write(data, 0, data.length);
			}
			os.flush();
			System.out.println("写入成功");
		} catch (Exception e) {
			System.out.println("文件写入失败 {}"+e);
			throw new RuntimeException("文件写入失败 ",e);
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("流关闭失败 {}"+e);
			}
		}
	}
	
}