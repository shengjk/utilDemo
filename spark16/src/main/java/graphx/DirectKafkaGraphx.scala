package graphx

import kafka.serializer.StringDecoder
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.graphx._

object DirectKafkaGraphx {
	def main(args: Array[String]) {
		//System.setProperty("hadoop.home.dir", "E:\\software\\hadoop-2.5.2");
		//StreamingExamples.setStreamingLogLevels()
		val brokers = "101.271.251.121:9092"
		val topics = "page_visits"
		if (args.length < 2) {
			System.err.println("Continue")
		}else{
			val Array(brokers, topics) = args
		}
		
		// Create context with 2 second batch interval
		val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount")
		val ssc = new StreamingContext(sparkConf, Seconds(10))
		//ssc.checkpoint(".")
		val topicsSet = topics.split(",").toSet
		val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
		val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
		val lines = messages.map(_._2)
		val words = lines.map(_.split(","))
		val cleanedDStream = words.transform(rdd=>{
			rdd.map(x=>Edge(x(1).toInt,x(2).toInt,1))
		})
		cleanedDStream.print()
		val graphDStream=cleanedDStream.transform(rdd=>
			Graph.fromEdges(rdd,"a").collectNeighborIds(EdgeDirection.Out).map(e=>(e._1,e._2.toSet))
		
		);
		graphDStream.print()
		
		ssc.start()
		ssc.awaitTermination()
	}
}