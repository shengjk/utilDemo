package graphx

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

object GraphxPergalDemo {
	def main(args:Array[String]){
		
		//设置运行环境
		val conf = new SparkConf().setAppName("myGraphPractice").setMaster("local[4]")
		val sc=new SparkContext(conf)
		
//		//屏蔽日志
//		Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
//		Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
		
		val vertexArray = Array(
			(1L, ("Alice", 28)),(2L, ("Bob", 27)),(3L, ("Charlie", 65)),(4L, ("David", 42)),
			(5L, ("Ed", 55)),(6L, ("Fran", 50))
		)
		//边的数据类型ED:Int
		val edgeArray = Array(
			Edge(2L, 1L, 7),Edge(2L, 4L, 2),Edge(3L, 2L, 4),Edge(3L, 6L, 3),
			Edge(4L, 1L, 1),Edge(5L, 2L, 2),Edge(5L, 3L, 8),Edge(5L, 6L, 3)
		)
		
		//构造vertexRDD和edgeRDD
		val vertexRDD: RDD[(Long, (String, Int))] = sc.parallelize(vertexArray)
		val edgeRDD: RDD[Edge[Int]] = sc.parallelize(edgeArray)
		
		//构造图Graph[VD,ED]
		val graph: Graph[(String, Int), Int] = Graph(vertexRDD, edgeRDD)
		
		
		val sourceId:VertexId=5//定义源点
		val initialGraph=graph.mapVertices((id,_)=>if (id==sourceId) 0 else Double.PositiveInfinity)
		//pregel函数有两个参数列表
		val shorestPath=initialGraph.pregel(initialMsg=Double.PositiveInfinity,
			maxIterations=100,
			activeDirection=EdgeDirection.Out)(
			
			//1-顶点属性迭代更新方式，与上一次迭代后保存的属性相比，取较小值
			//（将从源点到顶点的最小距离放在顶点属性中）
			(id,dist,newDist)=>math.min(dist,newDist),
			
			//2-Send Message，在所有能到达目的点的邻居中，计算邻居顶点属性+边属性
			//即（邻居-源点的距离+邻居-目的点的距离，并将这个距离放在迭代器中
			triplet=>{
				if(triplet.srcAttr+triplet.attr<triplet.dstAttr){
					Iterator((triplet.dstId,triplet.srcAttr+triplet.attr))
				}else{
					Iterator.empty
				}
			},
			
			//3-Merge Message，相当于Reduce函数
			//对所有能达到目的点的邻居发送的消息，进行min-reduce
			//邻居中最终reduce后最小的结果，作为newDist,发送至目的点，
			//至此，目的点中有新旧两个dist了，在下一次迭代开始的时候，步骤1中就可以进行更新迭代了
			(a,b)=>math.min(a,b))
		
		shorestPath.vertices.map(x=>(x._2,x._1)).top(30).foreach(print)
		
		/*outprint(shorest distance,vertexId)
		 * 8.0,3)(5.0,1)(4.0,4)(3.0,6)(2.0,2)(0.0,5)
		 */
	}
}