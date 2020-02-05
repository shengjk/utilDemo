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
package util.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.LoggerFactory;
import util.kafka.productor.KafkaProperties;

import java.util.ArrayList;
import java.util.Properties;

/*
 * 消费者api分上层api和底层api，这里是采用上层api的消费者例子（无需关系消息的offset，只是希望获得数据）
 */
public class Consumer extends Thread {
	protected final static org.slf4j.Logger logger = LoggerFactory.getLogger(Consumer.class);
	
	private final KafkaConsumer consumer;
	private final String topic;

	public Consumer(String topic) {
		Properties props = new Properties();
//		props.put("zookeeper.connect", KafkaProperties.zkConnect);
		props.put("group.id", KafkaProperties.groupId);//消费者获取哪一个groupId，一个系统一个groupId
//		props.put("zookeeper.session.timeout.ms", "400");
//		props.put("zookeeper.sync.time.ms", "200");
//		props.put("auto.commit.interval.ms", "1000");//向zookeeper上
//		props.put("enable.auto.commit","true");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put("bootstrap.servers", KafkaProperties.broker_list);//向zookeeper上
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		
		
		consumer =new KafkaConsumer(props);
		this.topic = topic;
	}

// push消费方式，服务端推送过来。主动方式是pull
	@Override
	public void run() {
		//Auto offset commit failed for group order5test11: Commit cannot be completed since the group has already rebalanced and assigned the partitions to another member. This means that the time between subsequent calls to poll() was longer than the configured max.poll.interval.ms, which typically implies that the poll loop is spending too much time message processing. You can address this either by increasing the session timeout or by reducing the maximum size of batches returned in poll() with max.poll.records.
//		consumer.subscribe(Collections.singletonList("test"));
		ArrayList<TopicPartition> topicPartitions = new ArrayList<>();
//		topicPartitions.add(new TopicPartition("canal-monitor-order",1));
//		topicPartitions.add(new TopicPartition("canal-monitor-order",0));
//		consumer.assign(topicPartitions);
		ArrayList<String> strings = new ArrayList<>();
		strings.add("recmd_dsp");
		consumer.subscribe(strings);
//		consumer.assign();
		try {
			while (true) {
				try {
					ConsumerRecords<String, String> records = consumer.poll(5000);
					for (ConsumerRecord<String, String> record : records) {
						//key 相当于partition
						System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
						System.out.println("============ " + record.timestamp());
					}
				}catch (WakeupException e){
					logger.info("=================");
					continue;
				}
				consumer.wakeup();
//			consumer.commitAsync();
				logger.info("poll");
//			try {
//				Thread.currentThread().sleep(3000);
//			} catch (InterruptedException e) {
//			}
			}
		}catch (WakeupException e){
			System.out.println(e);
		}finally {
			consumer.close();
		}
		
	}

	public void shutDown(){
		consumer.wakeup();
	}
	
	
	public static void main(String[] args) {
		Consumer consumerThread = new Consumer(KafkaProperties.topic);
		consumerThread.start();
		
//		try {
//			TimeUnit.SECONDS.sleep(2);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		consumerThread.shutDown();
	}
}
