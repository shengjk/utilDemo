package util.kafka.kafka.test

import kafka.api.{OffsetRequest, PartitionOffsetRequestInfo, TopicMetadataRequest}
import kafka.common.TopicAndPartition
import kafka.consumer.SimpleConsumer
import org.I0Itec.zkclient.ZkClient

/**
  * Created by shengjk1 on 2016/11/22.
  */
object Test {
  //
  val zkClient: ZkClient = new ZkClient("")
  val partitionOffset = zkClient.readData[String](s"${topicDirs.consumerOffsetDir}/${i}")
  val tp = TopicAndPartition(topic, i)

  val requestMin = OffsetRequest(Map(tp -> PartitionOffsetRequestInfo(OffsetRequest.EarliestTime, 1)))
  val consumerMin = new SimpleConsumer("broker_host", 9092, 10000, 10000, "getMinOffset")  //注意这里的 broker_host，因为这里会导致查询不到，解决方法在下面
  val curOffsets = consumerMin.getOffsetsBefore(requestMin).partitionErrorAndOffsets(tp).offsets
  var nextOffset = partitionOffset.toLong
  if (curOffsets.length > 0 && nextOffset < curOffsets.head) {  // 通过比较从 kafka 上该 partition 的最小 offset 和 zk 上保存的 offset，进行选择
    nextOffset = curOffsets.head
  }
  fromOffsets += (tp -> nextOffset) //设置正确的 offset，这里将 nextOffset 设置为 0（0 只是一个特殊值），可以观察到 offset 过期的现象











//寻找 kafka leader   因为 val consumerMin = new SimpleConsumer("broker_host", 9092, 10000, 10000, "getMinOffset")
  val topic_name = "topic_name"     //topic_name 表示我们希望获取的 topic 名字
  val topic2 = List(topic_name)
  val req = new TopicMetadataRequest(topic2, 0)
  val getLeaderConsumer = new SimpleConsumer("broker_host", 9092, 10000, 10000, "OffsetLookup")  // 第一个参数是 kafka broker 的host，第二个是 port
  val res = getLeaderConsumer.send(req)
  val topicMetaOption = res.topicsMetadata.headOption
  val partitions = topicMetaOption match {
    case Some(tm) =>
      tm.partitionsMetadata.map(pm => (pm.partitionId, pm.leader.get.host)).toMap[Int, String]  // 将结果转化为 partition -> leader 的映射关系
    case None =>
      Map[Int, String]()
  }

}
