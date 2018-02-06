package graphx

import java.util.Date

import org.apache.spark.graphx.lib.PageRank
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.graphframes.GraphFrame
import org.neo4j.spark.{Neo4jConfig, Neo4jGraph}

/**
  * Created by shengjk1 on 2017/4/5 0005.
  *
  * Spark can also serve as external Graph Compute solution, where you
		export data of selected subgraphs from Neo4j to Spark,
		compute the analytic aspects, and
		write the results back to Neo4j
		to be used in your Neo4j operations and Cypher queries.
  *
  */
object GraphxDemo11 {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf().setAppName("GraphxDemo").setMaster("local[2]")
		conf.set("spark.neo4j.bolt.url", "bolt://localhost:7687")
		conf.set("spark.neo4j.bolt.user", "neo4j")
		conf.set("spark.neo4j.bolt.password", "123456")
		val sc = new SparkContext(conf)
		//		Neo4jConfig()
		val g = Neo4jGraph.loadGraph(sc, label1 = "Person", relTypes = Seq("KNOWS"), label2 = "Person")
		// g: org.apache.spark.graphx.Graph[Any,Int] = org.apache.spark.graphx.impl.GraphImpl@574985d
		// What's the size of the graph?
		print(g.vertices.count) // res0: Long = 999937
		print(g.edges.count) // res1: Long = 999906
//		g.vertices.collect().foreach(println)
		g.triplets.collect().foreach(println)
		val g2 = PageRank.run(g, 5)
		/*
		((4,0.15),(5,0.27749999999999997),1.0)
		((5,0.27749999999999997),(2,0.38587499999999997),1.0)
		((10,0.15),(8,1.5685514531249993),1.0)
		((3,0.15),(8,1.5685514531249993),1.0)
		((7,0.15),(9,0.27749999999999997),1.0)
		((8,1.5685514531249993),(8,1.5685514531249993),1.0)
		()
		 */
		println(g2.triplets.collect().foreach(println))
		/*
		(4,0.15)
		(8,1.5685514531249993)
		(10,0.15)
		(2,0.38587499999999997)
		(3,0.15)
		(7,0.15)
		(9,0.27749999999999997)
		(5,0.27749999999999997)
		()
		 */
		println(g2.vertices.collect().foreach(println))
		/*
		Edge(4,5,1.0)
		Edge(5,2,1.0)
		Edge(10,8,1.0)
		Edge(3,8,1.0)
		Edge(7,9,1.0)
		Edge(8,8,1.0)
		()

		 */
		println(g2.edges.collect().foreach(println))
		val v = g2.vertices.take(5)
		// v: Array[(org.apache.spark.graphx.VertexId, Double)] = Array((185012,0.15), (612052,1.0153273593749998), (354796,0.15), (182316,0.15), (199516,0.38587499999999997))
		
//		println(Neo4jGraph.saveGraph(sc, g2, "rank12","rrr"))
		
		val g3: GraphFrame = GraphFrame.fromGraphX(g2)
		
		// res2: (Long, Long) = (999937,0)
		
		/*
		FOREACH (x in range(1,10) | CREATE (:Person {name:"name"+x, age: x%2}));
		UNWIND range(1,10) as x
		MATCH (n),(m) WHERE id(n) = x AND id(m)=toInt(rand()*10)
		CREATE (n)-[:KNOWS]->(m);
		
		((7,List()),(10,List()),1)
		((7,List()),(11,List()),1)
		((7,List()),(12,List()),1)
		((7,List()),(13,List()),1)
		((7,List()),(14,List()),1)
		((7,List()),(15,List()),1)
		((7,List()),(16,List()),1)
		((7,List()),(17,List()),1)
		((7,List()),(18,List()),1)
		((8,List()),(10,List()),1)
		((8,List()),(11,List()),1)
		((8,List()),(12,List()),1)
		((8,List()),(13,List()),1)
		((8,List()),(14,List()),1)
		((8,List()),(15,List()),1)
		((8,List()),(16,List()),1)
		((8,List()),(17,List()),1)
		((8,List()),(18,List()),1)
		((8,List()),(19,List()),1)
		((9,List()),(11,List()),1)
		((9,List()),(12,List()),1)
		((9,List()),(13,List()),1)
		((9,List()),(14,List()),1)
		((9,List()),(15,List()),1)
		((9,List()),(16,List()),1)
		((9,List()),(17,List()),1)
		((9,List()),(18,List()),1)
		((9,List()),(19,List()),1)
		((9,List()),(20,List()),1)
		((10,List()),(12,List()),1)
		((10,List()),(13,List()),1)
		((10,List()),(14,List()),1)
		((10,List()),(15,List()),1)
		((10,List()),(16,List()),1)
		((10,List()),(17,List()),1)
		((10,List()),(18,List()),1)
		((10,List()),(19,List()),1)
		((10,List()),(20,List()),1)
		((10,List()),(21,List()),1)
		((11,List()),(13,List()),1)
		((11,List()),(14,List()),1)

		
		 */
	}
	
}
