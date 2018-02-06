package graphx

import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by shengjk1 on 2017/4/7 0007.
  *
  *
  * aggregateMessages
  *
  * pregel
  */
object GraphxDemo3 {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf().setAppName("GraphxDemo3_aggregete").setMaster("local[2]")
		val sc = new SparkContext(conf)
		
		val VLines: RDD[(VertexId, (String, Int))] = sc.parallelize(Array((4L, ("David", 18)), (1L, ("Alice", 28)), (6L, ("Fran", 40)),
			(3L, ("Charlie", 30)), (2L, ("Bob", 70)), (5L, ("Ed", 55))))
		
		val ELines: RDD[(Edge[Int])] = sc.parallelize(Array(Edge(4L, 2L, 2), Edge(2L, 1L, 7), Edge(4L, 5L, 8), Edge(2L, 4L, 2),
			Edge(5L, 6L, 3), Edge(3L, 2L, 4), Edge(6L, 1L, 2), Edge(3L, 6L, 3), Edge(6L, 2L, 8), Edge(4L, 1L, 1),
			Edge(6L, 4L, 3), Edge(4L, 2L, 110)))
		
		
		val graph: Graph[(String, Int), (Int)] = Graph(VLines, ELines);
		/*
		((4,(David,18)),(2,(Bob,70)),110)
		((6,(Fran,40)),(1,(Alice,28)),2)
		((6,(Fran,40)),(2,(Bob,70)),8)
		((6,(Fran,40)),(4,(David,18)),3)
		 */
		graph.triplets.collect().foreach(println(_))
		
		////方括号内的元组(Int,Int)是函数返回值的类型，也就是Reduce函数（mergeMsg )右侧得到的值（count，totalAge）
		val olderFollowers: VertexRDD[(Int, Int)] = graph.aggregateMessages[(Int, Int)](
			triplet => {
				if (triplet.srcAttr._2 > triplet.dstAttr._2) {
					triplet.sendToDst(1, triplet.srcAttr._2)
				}
			}, //(1)--函数左侧是边三元组，也就是对边三元组进行操作，有两种发送方式sendToSrc和 sendToDst
			//对count和Age不断相加（reduce），最终得到总的count和totalAge
			(a, b) => (a._1 + b._1, a._2 + b._2)//(2)相当于Reduce函数，a，b各代表一个元组(count，Age)
			//(3)可选项,TripletFields.All/Src/Dst
//			TripletFields.All
//			TripletFields.Src
//			TripletFields.Dst
		)
		println("=============================")
		/*
		(4,(2,110))
		(6,(1,55))
		(1,(2,110))
		 */
		olderFollowers.collect().foreach(println)
		
		val averageOfOlderFollowers: VertexRDD[(Int, Int)] =olderFollowers.mapValues((id,value)=>value match {
			case (count,totalAge)=>(count,totalAge/count)
		})
//		val averageOfOlderFollowers: VertexRDD[(Int, Int)] =olderFollowers.mapValues((id,values)=>(values._1,values._2/values._1))
//		averageOfOlderFollowers.collect().foreach(println)
		
		
	}
	
}
