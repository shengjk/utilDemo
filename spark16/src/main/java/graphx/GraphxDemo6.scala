package graphx

import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by shengjk1 on 2017/4/10 0010.
  */
object GraphxDemo6 {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf()
		val sc = new SparkContext("local", "GraphxDemo6", conf)
		
		val myVertices=sc.makeRDD(Array((1L, "Ann"), (2L, "Bill"),
			(3L, "Charles"), (4L, "Diane"), (5L, "Went to gym this morning")))
		
		val myEdges=sc.makeRDD(Array(Edge(1L, 2L, "is-friends-with"),
			Edge(2L, 3L, "is-friends-with"), Edge(3L, 4L, "is-friends-with"),
			Edge(4L, 5L, "Likes-status"), Edge(3L, 5L, "Wrote-status")))
		
		val myGraph=Graph(myVertices,myEdges)
		//不保证特定的顺序
		myGraph.vertices.collect().foreach(println)
		myGraph.edges.collect().foreach(println)
		//返回EdgeTriplet[VD, ED] RDD is subclass of Edge[ED]
		//as you will see.having easy access to both the edge and vertex data
		//makes many graph=processing tasks easier
		//((1,Ann),(2,Bill),is-friends-with)
		myGraph.triplets.collect.foreach(println)
		println("==================")
		//((1,Ann),(2,Bill),(is-friends-with,true))
		myGraph.mapTriplets(
			t=>(t.attr,t.attr=="is-friends-with" &&t.srcAttr.toLowerCase.contains("a") ))
				.triplets.collect().foreach(println)
		myGraph.mapEdges(t=>(t.attr,t.attr=="is-friends-with" ))
				.triplets.collect().foreach(println)
	}
	
}
