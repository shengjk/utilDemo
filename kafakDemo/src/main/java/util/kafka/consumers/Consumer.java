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

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import util.kafka.productor.KafkaProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
 * 消费者api分上层api和底层api，这里是采用上层api的消费者例子（无需关系消息的offset，只是希望获得数据）
 */
public class Consumer extends Thread {
	private final ConsumerConnector consumer;
	private final String topic;

	public Consumer(String topic) {
		consumer = kafka.consumer.Consumer
				.createJavaConsumerConnector(createConsumerConfig());
		this.topic = topic;
	}

	private static ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect", KafkaProperties.zkConnect);
		props.put("group.id", KafkaProperties.groupId);//消费者获取哪一个groupId，一个系统一个groupId 
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");//向zookeeper上

		return new ConsumerConfig(props);

	}
// push消费方式，服务端推送过来。主动方式是pull
	public void run() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
				.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		
		while (it.hasNext()){
			//逻辑处理
			System.out.println("consumer:"+new String(it.next().message()));
			
		}
			
	}

	public static void main(String[] args) {
		Consumer consumerThread = new Consumer(KafkaProperties.topic);
		consumerThread.start();
	}
}
