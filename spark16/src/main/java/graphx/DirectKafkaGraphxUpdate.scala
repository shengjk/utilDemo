package graphx

import kafka.serializer.StringDecoder
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.graphx._

object DirectKafkaGraphxUpdate {
	
	def updateFunction(newValues: Seq[Set[Long]], runningCount: Option[Set[Long]]): Option[Set[Long]] = {
		def show(x: Option[Set[Long]]) = x match {
			case Some(s) => s
			case None => Set()
		}
		if(newValues.isEmpty){
			return runningCount
		}else{
			return Some(newValues(0) ++ show(runningCount))
		}
	}
	
	def functionToCreateContext(brokers:String,topics:String): StreamingContext = {
		val sparkConf = new SparkConf().setAppName("DirectKafkaGraphx")
		val ssc = new StreamingContext(sparkConf,Seconds(30))   // new context
		ssc.checkpoint("checkpoints")   // set checkpoint directory
		val topicsSet = topics.split(",").toSet
		val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
		val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
			ssc, kafkaParams, topicsSet)
		val lines = messages.map(_._2)
		val words = lines.map(_.split(","))
		val cleanedDStream = words.transform(rdd=>{
			rdd.map(x=>Edge(x(1).toInt,x(2).toInt,1))
		})
		cleanedDStream.print()
		val graphDStream=cleanedDStream.transform(rdd=>
			Graph.fromEdges(rdd,"a").collectNeighborIds(EdgeDirection.Out).map(e=>(e._1,e._2.toSet))
		
		);
		val graphDStreams = graphDStream.updateStateByKey[Set[Long]](updateFunction(_,_))
		graphDStreams.print()
		graphDStreams.saveAsTextFiles("sstest/kafka_graph_streamings","txt")
		return ssc
	}
	
	
	def main(args: Array[String]) {
		//System.setProperty("hadoop.home.dir", "E:\\software\\hadoop-2.5.2");
		//StreamingExamples.setStreamingLogLevels()
		val brokers = "101.271.251.161:9092"
		val topics = "page_visits"
		if (args.length < 2) {
			System.err.println(s"""
								  |Usage: DirectKafkaWordCount <brokers> <topics>
								  |  <brokers> is a list of one or more Kafka brokers
								  |  <topics> is a list of one or more kafka topics to consume from
								  |
        """.stripMargin)
		}else{
			val Array(brokers, topics) = args
		}
		val ssc = StreamingContext.getOrCreate("checkpoints", ()=>functionToCreateContext(brokers, topics))
		//Start the computation
		ssc.start()
		ssc.awaitTermination()
	}
}