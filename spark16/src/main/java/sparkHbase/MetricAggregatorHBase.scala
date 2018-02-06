package sparkHbase

import java.util.Properties

import kafka.producer._

import org.apache.hadoop.hbase.{ HBaseConfiguration, HColumnDescriptor, HTableDescriptor }
import org.apache.hadoop.hbase.client.{ HBaseAdmin, Put }
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.{ PairRDDFunctions, RDD }
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.kafka._

object MetricAggregatorHBase {
//  def main(args : Array[String]) {
//    if (args.length < 6) {
//      System.err.println("Usage: MetricAggregatorTest <master> <zkQuorum> <group> <topics> <destHBaseTableName> <numThreads>")
//      System.exit(1)
//    }
//
//    val Array(master, zkQuorum, group, topics, hbaseTableName, numThreads) = args
//
//    val conf = HBaseConfiguration.create()
//    conf.set("hbase.zookeeper.quorum", zkQuorum)
//
//    // Initialize hBase table if necessary
//    val admin = new HBaseAdmin(conf)
//    if (!admin.isTableAvailable(hbaseTableName)) {
//      val tableDesc = new HTableDescriptor(hbaseTableName)
//      tableDesc.addFamily(new HColumnDescriptor("metric"))
//      admin.createTable(tableDesc)
//    }
//
//    // setup streaming context
//    val ssc = new StreamingContext(master, "MetricAggregatorTest", Seconds(2),
//      System.getenv("SPARK_HOME"), StreamingContext.jarOfClass(this.getClass))
//    ssc.checkpoint("checkpoint")
//
//    val topicpMap = topics.split(",").map((_, numThreads.toInt)).toMap
//    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicpMap)
//      .map { case (key, value) => ((key, Math.floor(System.currentTimeMillis() / 60000).toLong * 60), value.toInt) }
//
//    val aggr = lines.reduceByKeyAndWindow(add _, Minutes(1), Minutes(1), 2)
//
//    aggr.foreach(line => saveToHBase(line, zkQuorum, hbaseTableName))
//
//    ssc.start
//
//    ssc.awaitTermination
//  }

  def add(a : Int, b : Int) = { (a + b) }

  def saveToHBase(rdd : RDD[((String, Long), Int)], zkQuorum : String, tableName : String) = {
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", zkQuorum)

    val jobConfig = new JobConf(conf)
    jobConfig.set(TableOutputFormat.OUTPUT_TABLE, tableName)
    jobConfig.setOutputFormat(classOf[TableOutputFormat])

    new PairRDDFunctions(rdd.map { case ((metricId, timestamp), value) => createHBaseRow(metricId, timestamp, value) }).saveAsHadoopDataset(jobConfig)
  }

  def createHBaseRow(metricId : String, timestamp : Long, value : Int) = {
    val record = new Put(Bytes.toBytes(metricId + "~" + timestamp))

    record.add(Bytes.toBytes("metric"), Bytes.toBytes("col"), Bytes.toBytes(value.toString))

    (new ImmutableBytesWritable, record)
  }

}

// Produces some random words between 1 and 100.
object MetricDataProducer {

  def main(args : Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage: MetricDataProducer <metadataBrokerList> <topic> <messagesPerSec>")
      System.exit(1)
    }

    val Array(brokers, topic, messagesPerSec) = args

    // ZooKeeper connection properties
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")

    val config = new ProducerConfig(props)
    val producer = new Producer[String, String](config)

    // Send some messages
    while (true) {
      val messages = (1 to messagesPerSec.toInt).map { messageNum =>
      {
        val metricId = scala.util.Random.nextInt(10)
        val value = scala.util.Random.nextInt(1000)
        new KeyedMessage[String, String](topic, metricId.toString, value.toString)
      }
      }.toArray

      producer.send(messages : _*)
      Thread.sleep(100)
    }
  }
}