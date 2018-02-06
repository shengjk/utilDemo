package graphx

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by shengjk1 on ${date}
  */
object GraphFramesDemo {
	
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf().setAppName("GraphxDemo").setMaster("local[2]")
		val sc = new SparkContext(conf)
		val sqlContext =new SQLContext(sc)
		sqlContext.setConf("spark.sql.shuffle.partitions", "10")
			// Create a Vertex DataFrame with unique ID column "id"
		val v = sqlContext.createDataFrame(List(
			("a", "Alice", 34),
			("b", "Bob", 36),
			("c", "Charlie", 30)
		)).toDF("id", "name", "age")
		// Create an Edge DataFrame with "src" and "dst" columns
		val e = sqlContext.createDataFrame(List(
			("a", "b", "friend"),
			("b", "c", "follow"),
			("c", "b", "follow")
		)).toDF("src", "dst", "relationship")
		// Create a GraphFrame
		import org.graphframes.GraphFrame
		val g = GraphFrame(v, e)
		g.triplets.repartition(1).write.format("com.databricks.spark.csv").mode("overwrite").option("header", "true").save("D:\\g2")
		
		g.toGraphX.triplets.collect().foreach(println)
		// Query: Get in-degree of each vertex.
		g.inDegrees.show()
		
		// Query: Count the number of "follow" connections in the graph.
		g.edges.filter("relationship = 'follow'").count()
		
		// Run PageRank algorithm, and show results.
		val results = g.pageRank.resetProbability(0.01).maxIter(20).run()
		results.vertices.select("id", "pagerank").show()
	}
	
}
