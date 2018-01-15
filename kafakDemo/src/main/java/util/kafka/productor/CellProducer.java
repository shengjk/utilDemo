/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package util.kafka.productor;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.kafka.common.utils.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.Random;

public class CellProducer extends Thread {
	
//	bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 5 --topic cmcccdr
	private final kafka.javaapi.producer.Producer<Integer, String> producer;//用来创建和推送消息
	private final String topic;
	private final Properties props = new Properties();

	public CellProducer(String topic) {
		props.put("serializer.class", "kafka.serializer.StringEncoder");// 字符串消息
		props.put("metadata.broker.list", KafkaProperties.broker_list);
		producer = new kafka.javaapi.producer.Producer<Integer, String>(
				new ProducerConfig(props));//ProducerConfig:配置Producer，比如定义要连接的brokers、partition class、serializer class、partition key等
		this.topic = topic;
	}

	public void run() {
		// order_id,order_amt,create_time,province_id
		Random random = new Random();
		String[] cell_num = { "29448-37062", "29448-51331", "29448-51331","29448-51333", "29448-51343" };//基站
		String[] drop_num = { "0","1","2"};//掉话1(信号断断续续)  断话2(完全断开)
		
//		Producer.java
//		record_time,imei,cell,ph_num,call_num,drop_num,duration,drop_rate,net_type,erl
//		2011-06-28 14:24:59.867,356966,29448-37062,0,0,0,0,0,G,0
//		2011-06-28 14:24:59.867,352024,29448-51331,0,0,0,0,0,G,0
//		2011-06-28 14:24:59.867,353736,29448-51331,0,0,0,0,0,G,0
//		2011-06-28 14:24:59.867,353736,29448-51333,0,0,0,0,0,G,0
//		2011-06-28 14:24:59.867,351545,29448-51333,0,0,0,0,0,G,0
//		2011-06-28 14:24:59.867,353736,29448-51343,1,0,0,8,0,G,0
		int i =0 ;
		 NumberFormat nf = new DecimalFormat("000000");//数据格式化的格式
		while(true) {  //模拟基站源源不断地产生数据。
			i ++ ;
//			String messageStr = i+"\t"+cell_num[random.nextInt(cell_num.length)]+"\t"+DateFmt.getCountDate(null, DateFmt.date_long)+"\t"+drop_num[random.nextInt(drop_num.length)] ;
			String testStr = nf.format(random.nextInt(10)+1);//将随机产生的1—10数据按照固定格式进行格式化。
			String messageStr = i+"\t"+("29448-"+testStr)+"\t"+drop_num[random.nextInt(drop_num.length)] ;
			System.out.println("product:"+messageStr);
			producer.send(new KeyedMessage<Integer, String>(topic, messageStr));//KeyedMessage：定义要发送的消息对象，比如定义发送给哪个topic，partition key和发送的内容等
			Utils.sleep(1000) ;
//			if (i==500) {
//				break;
//			}
		}

	}

	public static void main(String[] args) {
		CellProducer producerThread = new CellProducer(KafkaProperties.Cell_Topic);
		
		producerThread.start();
		Random random = new Random();
		 NumberFormat nf = new DecimalFormat("000000");
		  String testStr = nf.format(random.nextInt(100000)+1);
		  System.out.println(testStr);
		
	}
}
