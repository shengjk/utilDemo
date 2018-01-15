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
/*
 为了更好的实现负载均衡和消息的顺序性，kafka的producer在分发消息时可以通过分发策略发送给指定的partition。
 实现分发的程序是需要制定消息的key值，而kafka通过key进行策略分发。
 */
/*
 * metadata.broker.list 默认值：无，必填
格式为host1:port1,host2:port2，这是一个broker列表，用于获得元数据(topics，partitions和replicas)，建立起来的socket连接用于发送实际数据，这个列表可以是broker的一个子集，或者一个VIP，指向broker的一个子集。
request.required.acks 默认值：0

用来控制一个produce请求怎样才能算完成，准确的说，是有多少broker必须已经提交数据到log文件，并向leader发送ack，可以设置如下的值：

0，意味着producer永远不会等待一个来自broker的ack，这就是0.7版本的行为。这个选项提供了最低的延迟，但是持久化的保证是最弱的，当server挂掉的时候会丢失一些数据。
1，意味着在leader replica已经接收到数据后，producer会得到一个ack。这个选项提供了更好的持久性，因为在server确认请求成功处理后，client才会返回。如果刚写到leader上，还没来得及复制leader就挂了，那么消息才可能会丢失。
-1，意味着在所有的ISR都接收到数据后，producer才得到一个ack。这个选项提供了最好的持久性，只要还有一个replica存活，那么数据就不会丢失。
request.timeout.ms 默认值：10000

请求超时时间。

producer.type 默认值：sync

决定消息是否应在一个后台线程异步发送。合法的值为sync，表示异步发送；sync表示同步发送。设置为async则允许批量发送请求，这回带来更高的吞吐量，但是client的机器挂了的话会丢失还没有发送的数据。

serializer.class 默认值：kafka.serializer.DefaultEncoder

The serializer class for messages. The default encoder takes a byte[] and returns the same byte[].

消息的序列化类，默认是的encoder处理一个byte[]，返回一个byte[]。

key.serializer.class 默认值：无

The serializer class for keys (defaults to the same as for messages if nothing is given).

key的序列化类，默认跟消息的序列化类一样。

partitioner.class 默认值：kafka.producer.DefaultPartitioner

用来把消息分到各个partition中，默认行为是对key进行hash。

compression.codec 默认值：none

允许指定压缩codec来对消息进行压缩，合法的值为：none，gzip，snappy。

compressed.topics 默认值：null

允许你指定特定的topic对其进行压缩。如果compression codec设置了除NoCompressionCodec以外的值，那么仅会对本选项指定的topic进行压缩。如果compression codec为NoCompressionCodec，那么压缩就不会启用。

message.send.max.retries 默认值：3

如果producer发送消息失败了会自动重发，本选项指定了重发的次数。注意如果是非0值，那么可能会导致重复发送，就是说的确发送了消息，但是没有收到ack，那么就还会发一次。

retry.backoff.ms 默认值：100

在每次重发之前，producer会刷新相关的topic的元数据，来看看是否选出了一个新leader。由于选举leader会花一些时间，此选项指定了在刷新元数据前等待的时间。

topic.metadata.refresh.interval.ms 默认值：600 * 1000

当出现错误时(缺失partition，leader不可用等)，producer通常会从broker拉取最新的topic的元数据。它也会每隔一段时间轮询(默认是每隔10分钟)。如果设置了一个负数，那么只有当发生错误时才会刷新元数据，当然不推荐这样做。有一个重要的提示：只有在消息被发送后才会刷新，所以如果producer没有发送一个消息的话，则元数据永远不会被刷新。

queue.buffering.max.ms 默认值：5000

当使用异步模式时，缓冲数据的最大时间。例如设为100的话，会每隔100毫秒把所有的消息批量发送。这会提高吞吐量，但是会增加消息的到达延时。

queue.buffering.max.messages 默认值：10000

在异步模式下，producer端允许buffer的最大消息数量，如果producer无法尽快将消息发送给broker，从而导致消息在producer端大量沉积，如果消息的条数达到此配置值，将会导致producer端阻塞或者消息被抛弃。

queue.enqueue.timeout.ms 默认值：-1

当消息在producer端沉积的条数达到 queue.buffering.max.meesages 时， 阻塞一定时间后，队列仍然没有enqueue(producer仍然没有发送出任何消息) 。 此时producer可以继续阻塞或者将消息抛弃，此timeout值用于控制 阻塞 的时间，如果值为-1 则 无阻塞超时限制，消息不会被抛弃；如果值为0 则

立即清空队列，消息被抛弃。

batch.num.messages 默认值：200

在异步模式下，一个batch发送的消息数量。producer会等待直到要发送的消息数量达到这个值，之后才会发送。但如果消息数量不够，达到queue.buffer.max.ms时也会直接发送。

send.buffer.bytes 默认值：100 * 1024

socket的发送缓存大小。

client.id 默认值：""
这是用户可自定义的client id，附加在每一条消息上来帮助跟踪。

更多配置细节可以参考 kafka.producer.ProducerConfig 类。
 */

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.kafka.common.utils.Utils;

import java.util.Properties;
import java.util.Random;

public class PartitionProducer extends Thread {
	private final kafka.javaapi.producer.Producer<Integer, String> producer;
//	private final kafka.javaapi.producer.Producer<String, String> producer;
	private final String topic;
	private final Properties props = new Properties();

	public PartitionProducer(String topic) {
		//encoder必须和下一步的keyedmessage使用相同的类型
		props.put("serializer.class", "kafka.serializer.StringEncoder");// 字符串消息
		// key.serializer.class默认为serializer.class
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");
		
		
		//定义了生产者如何找到一个或多个Broker去确定每个topic的Leader。至少应该包括两个
		props.put("metadata.broker.list", KafkaProperties.broker_list);
		//决定topic中的分区发送规则,可选的
		props.put("partitioner.class", "xmht.kafka1.productor.TestPartition");
		//设置kafka知否需要broker的回应。如果不设置可能将导致数据丢失。
		/*
		 * 此处可以设置为0 生产者不等待broker的回应。会有最低能的延迟和最差的保证性（在服务器失败后会导致信息丢失）
         此处可以设置为1生产者会收到leader的回应在leader写入之后。（在当前leader服务器为复制前失败可能会导致信息丢失）
         此处可以设置为-1生产者会收到leader的回应在全部拷贝完成之后。  (不会丢失信息)
		 */
		props.put("request.required.acks", "1");
		
		
		props.put("compression.codec", "2"); // snappy
		//定义生产者.
		//此处泛型的第一个type是分区的key的类型。第二个是消息的类型。与上面Properties中定义的对应。
		producer = new kafka.javaapi.producer.Producer<Integer, String>(new ProducerConfig(props));
//		producer=new kafka.javaapi.producer.Producer<String, String>(new ProducerConfig(props));
		this.topic = topic;
//		System.out.println("topic:"+topic);
	}

	public void run() {
		// order_id,order_amt,create_time,province_id
		Random random = new Random();
		String[] order_amt = { "10.10", "20.10", "50.2","60.0", "80.1" };
		String[] province_id = { "1","2","3","4","5","6","7","8" };
		int i =0 ;
		while(true) {
			i ++ ;
			String messageStr = i+"\t"+order_amt[random.nextInt(5)]+"\t"+province_id[random.nextInt(8)] ;
			System.out.println("product:"+messageStr);
			//用随机的分区发送策略。Integer
			
			producer.send(new KeyedMessage<Integer, String>(topic, messageStr));
			//此处泛型的第一个type是分区的key的类型。第二个是消息的类型。与上面Properties.class中定义的对应。
			//此处我们将"key"设置为分区的key值。注意如果你没有设置键值，即使你定义了一个分区类，kafka也将使用随机发送.
//			producer.send(new KeyedMessage<String, String>(topic,"key", messageStr));
			Utils.sleep(100) ;
			if (i==10) {
				break;
			}
		}

	}

	public static void main(String[] args) {
		PartitionProducer producerThread = new PartitionProducer(KafkaProperties.topic);
		producerThread.start();
	}
}
