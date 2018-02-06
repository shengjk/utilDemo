package graphx

import org.apache.spark.SparkConf
import org.neo4j.driver.v1.{Record, Session, StatementResult}
import org.neo4j.spark.Neo4jConfig

/**
  * Created by shengjk1 on 2017/4/12 0012.
  *
  *
  * export data of selected subgraphs from Neo4j to Spark,
	compute the analytic aspects, and
	write the results back to Neo4j
	to be used in your Neo4j operations and Cypher queries.
  */
object GraphxOperNeo4j {
	def main(args: Array[String]): Unit = {
		val conf=new SparkConf().setAppName("GraphxOperNeo4j").setMaster("local[2]")
		conf.set("spark.neo4j.bolt.url","bolt://localhost:7687")
		conf.set("spark.neo4j.bolt.user","neo4j")
		conf.set("spark.neo4j.bolt.password","123456")
		val neo4jConfig=Neo4jConfig(conf)
		println(neo4jConfig.url)
		//		val sc =new SparkContext(conf)
		val session:Session=neo4jConfig.driver(neo4jConfig).session()
		val rs:StatementResult=session.run("match (p:Person {name:\"jiaj\"}) return p")
//		println(rs.list().size()+" ==============") //1
//		println(rs.list().size()+" ==============") //0
//		println(rs.list().size()+" ==============") //0
		val record: Record=rs.next()//NoSuchRecordException
		println(record.toString)
		println(s"${record.get("p")}")
		val node=record.get("p").asNode()
		val itera=record.get("p").asNode().labels().iterator()
		while(itera.hasNext){
			println(itera.next())
		}
		println(node.get("born"))
		println(node.id())
		session.close()
		
	}
	
}
