package graphx

import org.apache.spark.graphx.lib.PageRank
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.neo4j.spark.Neo4jGraph
import org.neo4j.spark.Neo4jGraph.execute

object GraphxDemo111 {
	def main(args: Array[String]) = {
		
		val conf = new SparkConf()
		conf.set("spark.neo4j.bolt.url", "bolt://localhost:7687")
		conf.set("spark.neo4j.bolt.user", "neo4j")
		conf.set("spark.neo4j.bolt.password", "123456")
		val sc = new SparkContext("local", "test", conf)
		// day09-vertices.csv
		// 1,Taro,100
		// 2,Jiro,200
		// 3,Sabo,300
		val vertexLines: RDD[String] = sc.textFile("E:\\gitProject\\sssk\\graphdata\\day05-vertices.txt")
		val v: RDD[(VertexId, (String, Long))] = vertexLines.map(line => {
			val cols = line.split(",")
			(cols(0).toLong, (cols(1), cols(2).toLong))
		})
		
		// day09-01-edges.csv
		// 1,2,100,2014/12/1
		// 2,3,200,2014/12/2
		// 3,1,300,2014/12/3
		val format = new java.text.SimpleDateFormat("yyyy/MM/dd")
		val edgeLines: RDD[String] = sc.textFile("E:\\gitProject\\sssk\\graphdata\\day05-edges.txt")
		val e: RDD[Edge[((Long, java.util.Date))]] = edgeLines.map(line => {
			val cols = line.split(",")
			Edge(cols(0).toLong, cols(1).toLong, (cols(2).toLong, format.parse(cols(3))))
		})
		
		val graph: Graph[(String, Long), (Long, java.util.Date)] = Graph(v, e)
		val graph1=PageRank.run(graph, 5)
//		graph1.triplets.collect().foreach(println)
//		println(Neo4jGraph.saveGraph(sc,graph1,"graphx2","graphx2"))
		println(Neo4jGraph.saveGraph(sc,graph))
		
		
		println(graph.vertices.repartition(1).mapPartitions[Long](
			p=>{
				Iterator.apply[Long](1090)
			}
		
		).sum().toLong)
		
//		graph.mapEdges(edge => edge.attr._1).triplets.collect().foreach(println)
		sc.stop
	}
}