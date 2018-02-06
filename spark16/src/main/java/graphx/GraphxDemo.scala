package graphx

import java.util.Date

import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by shengjk1 on 2017/4/5 0005.
  */
object GraphxDemo {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf().setAppName("GraphxDemo").setMaster("local[2]")
		val sc = new SparkContext(conf)
		//无论是对于Edge还是Vertex而言数据类型仅仅针对于Property,并且Property可以是复合类型或者是组装类型
		val users: RDD[(VertexId, (String, String))] = sc.parallelize(Array((3L, ("rxin", "student")), (7L, ("jgonzal", "postdoc")),
			(5L, ("franklin", "prof")), (2L, ("istoica", "prof"))))
		
		val relationships: RDD[(Edge[String])] = sc.parallelize(Array(Edge(3L, 7L, "collab"), Edge(5L, 3L, "advisor"),
			Edge(2L, 5L, "colleague"), Edge(5L, 7L, "pi")))
		
		/*
		图视图
			  val vertices: VertexRDD[VD]
			  val edges: EdgeRDD[ED]
			  val triplets: RDD[EdgeTriplet[VD, ED]]
		 */
		val graph0: Graph[(String, String), (String)] = Graph(users, relationships)
		//		println("============vertices "+graph.vertices.filter{case (id,(name,pos))=>pos=="postdoc"}
		//				.count())
		//		println("============ "+graph.edges.filter(e=>e.srcId>e.dstId).count())
		/*
		属性图是有方向的，在属性图中attr表示为Property的value
		三边属性图：
		rxin  student  is the collab of jgonzal  postdoc
		franklin  prof  is the advisor of rxin  student
		istoica  prof  is the colleague of franklin  prof
		franklin  prof  is the pi of jgonzal  postdoc
		就内容而言srcAttr与dstAttr的内容是一致的
		srcAttr其实就是VerTex Table中的V
		
		三边属性图中attr是边属性的值
		 */
		/*
			        ------------>
		     srcAttr			 destAttr           attr
		 顶点  顶点属性值     顶点   顶点属性值     边属性值
 		((3,(rxin,student)),(7,(jgonzal,postdoc)),collab)
		
		      srcAttr			  destAttr		   attr
		((3,(rxin,student)),(7,(jgonzal,postdoc)),collab)
		((5,(franklin,prof)),(3,(rxin,student)),advisor)
		((2,(istoica,prof)),(5,(franklin,prof)),colleague)
		((5,(franklin,prof)),(7,(jgonzal,postdoc)),pi)
			 */
		//		graph0.triplets.collect().foreach(println(_))
		//		graph0.triplets.map(triplet => triplet.srcAttr._1 + "  " + triplet.srcAttr._2 + "  is the " + triplet.attr + " of " +
		//				triplet.dstAttr._1 + "  " + triplet.dstAttr._2).collect().foreach(println(_))
		//		graph.triplets.map(
		//			triplet => triplet.srcAttr._1 + " is the " + triplet.attr + " of " + triplet.dstAttr._1
		//		).collect().foreach(println(_))
		/*
		图信息     val numEdges: Long
				  val numVertices: Long
				  val inDegrees: VertexRDD[Int]
				  val outDegrees: VertexRDD[Int]
				  val degrees: VertexRDD[Int]
		 */
		//本属性图每一个节点的入度
		//		graph.inDegrees.foreach(println(_))
		//		println("====================")
		//		graph.degrees.foreach(println(_))
		//		//本属性图有多少条边
		//		println(" ================== "+graph.numEdges)
		
		/*
		图缓存
			  def persist(newLevel: StorageLevel = StorageLevel.MEMORY_ONLY): Graph[VD, ED]
			  def cache(): Graph[VD, ED]
			  def unpersistVertices(blocking: Boolean = true): Graph[VD, ED]
		 */
		
		/*
		Transform vertex and edge attributes  每个操作都产生一个新的图
		
			  def mapVertices[VD2](map: (VertexID, VD) => VD2): Graph[VD2, ED]
			  def mapEdges[ED2](map: Edge[ED] => ED2): Graph[VD, ED2]
			  def mapEdges[ED2](map: (PartitionID, Iterator[Edge[ED]]) => Iterator[ED2]): Graph[VD, ED2]
			  def mapTriplets[ED2](map: EdgeTriplet[VD, ED] => ED2): Graph[VD, ED2]
			  def mapTriplets[ED2](map: (PartitionID, Iterator[EdgeTriplet[VD, ED]]) => Iterator[ED2])
    : Graph[VD, ED2]
    
    
    	每个操作都产生一个新的图，这个新的图包含通过用户自定义的map操作修改后的顶点或边的属性.

		注意，每种情况下图结构都不受影响。这些操作的一个重要特征是它允许所得图形重用原有图形的结构索引(indices)。下面的两行代码在逻辑上是等价的，但是第一个不保存结构索引，所以 不会从GraphX系统优化中受益。
		val newVertices = graph.vertices.map { case (id, attr) => (id, mapUdf(id, attr)) }
		val newGraph = Graph(newVertices, graph.edges)
		另一种方法是用mapVertices⇒VD2)(ClassTag[VD2]):Graph[VD2,ED])保存索引。
		val newGraph = graph.mapVertices((id, attr) => mapUdf(id, attr))
		这些操作经常用来初始化的图形，用作特定计算或者用来处理项目不需要的属性。
		 */
		
		val vertexLines: RDD[String] = sc.textFile("E:\\gitProject\\sssk\\graphdata\\day05-vertices.txt")
		val vertices: RDD[(VertexId, (String, Long))] = vertexLines map { line => {
			val cols = line.split(",", -1)
			(cols(0).toLong, (cols(1), cols(2).toLong))
		}
		}
		vertices.collect().foreach(println(_))
		/*
			(1,(Taro,100))
			(2,(Jiro,200))
			(3,(Sabo,300))
		 */
		
		val format = new java.text.SimpleDateFormat("yyyy/MM/dd")
		val edgeLines: RDD[String] = sc.textFile("E:\\gitProject\\sssk\\graphdata\\day05-edges.txt")
		val edges: RDD[Edge[(Long, Date)]] = edgeLines.map(line => {
			val cols = line.split(",", -1)
			Edge(cols(0).toLong, cols(1).toLong,
				(cols(2).toLong, format.parse(cols(3))))
		})
		println("E =======")
		edgeLines.collect().foreach(println(_))
		/*
		1,2,100,2014/12/1
		2,3,200,2014/12/2
		3,1,300,2014/12/3
		 */
		
		val graph: Graph[(String, Long), (Long, Date)] = Graph(vertices, edges)
		/*
		(2,(Jiro,200))
		(1,(Taro,100))
		(3,(Sabo,300))
		 */
		graph.vertices.collect().foreach(println(_))
		/*
		Edge(1,2,(100,Mon Dec 01 00:00:00 CST 2014))
		Edge(2,3,(200,Tue Dec 02 00:00:00 CST 2014))
		Edge(3,1,(300,Wed Dec 03 00:00:00 CST 2014))
		 */
		graph.edges.collect().foreach(println(_))
		/*
		It is strongly recommended that this Graph is persisted in  memory, otherwise saving it on a file will require recomputation.
		graph.checkpoint()
		 */
		//		graph.cache();
		//修改属性
		val graph1: Graph[Long, (Long, Date)] = graph.mapVertices((vid: VertexId, attr: (String, Long)) => attr._1.length * attr._2)
		/*
		(2,(Jiro,200))变成了(2,800)
		 */
		//		graph1.vertices.collect().foreach(println(_))
		//		graph1.edges.collect().foreach(println(_))
		
		val graph2: Graph[(String, Long), (Long)] = graph.mapEdges(edge => edge.attr._1)
		//		graph2.vertices.collect().foreach(println(_))
		/*
		Edge(1,2,(100,Mon Dec 01 00:00:00 CST 2014))变成了Edge(1,2,100)
		 */
		//		graph2.edges.collect().foreach(println(_))
		
		
		//		graph.vertices.collect().foreach(println(_))
		//		graph.edges.collect().foreach(println(_))
		
		/*     src				   dest
		 顶点  顶点属性值     顶点   顶点属性值     边属性值
 		((3,(rxin,student)),(7,(jgonzal,postdoc)),collab)
		
		
		((1,(Taro,100)),(2,(Jiro,200)),(100,Mon Dec 01 00:00:00 CST 2014))
		((2,(Jiro,200)),(3,(Sabo,300)),(200,Tue Dec 02 00:00:00 CST 2014))
		((3,(Sabo,300)),(1,(Taro,100)),(300,Wed Dec 03 00:00:00 CST 2014))
		 */
		graph.triplets.collect.foreach(println(_))
		/*
			((1,(Taro,100)),(2,(Jiro,200)),400)
			((2,(Jiro,200)),(3,(Sabo,300)),700)
			((3,(Sabo,300)),(1,(Taro,100)),700)
		 */
		graph.mapTriplets(edge => edge.srcAttr._2 + edge.attr._1 + edge.dstAttr._2).triplets.collect().foreach(println(_))
		
		/*
		Modify the graph structure   不改变原来的图
		
				def reverse: Graph[VD, ED]
				def subgraph(
							  epred: EdgeTriplet[VD,ED] => Boolean = (x => true),
							  vpred: (VertexID, VD) => Boolean = ((v, d) => true))
							: Graph[VD, ED]
				def mask[VD2, ED2](other: Graph[VD2, ED2]): Graph[VD, ED]
				def groupEdges(merge: (ED, ED) => ED): Graph[VD, ED]
		 */
		
		println("++++++++++ ")
		//src与dest反转
		val graph4: Graph[(String, String), (String)] = graph0.reverse
		/*
		((3,(rxin,student)),(7,(jgonzal,postdoc)),collab)
		((5,(franklin,prof)),(3,(rxin,student)),advisor)
		((2,(istoica,prof)),(5,(franklin,prof)),colleague)
		 */
		graph0.triplets.collect().foreach(println(_))
		
		graph0.triplets.map(triplet => triplet.srcAttr._1 + " is the " + triplet.attr + " of " + triplet.dstAttr._1)
		.collect().foreach(println)
//		graph4.triplets.collect().foreach(println(_))
		
		/*
		subgraph⇒Boolean,(VertexId,VD)⇒Boolean):Graph[VD,ED])操作
		利用顶点和边的谓词（predicates），返回的图仅仅包含满足顶点谓词的顶点、
		满足边谓词的边以及满足顶点谓词的连接顶点（connect vertices）。
		subgraph操作可以用于很多场景，如获取 感兴趣的顶点和边组成的图或者获取清除断开链接后的图。
		 */
		//		graph.subgraph(vpred = (id, attr) => attr._2 != "student")
		//				.vertices
		//				.collect()
		//				.foreach(println(_))
		//
		/*
		图构造者
		Graph.groupEdges⇒ED):Graph[VD,ED]) 需要重新分区图，因为它假定相同的边将会被分配到同一个分区，
		所以你必须在调用groupEdges之前调用Graph.partitionBy:Graph[VD,ED])
		 */
		
		/*
		其他
		 def partitionBy(partitionStrategy: PartitionStrategy): Graph[VD, ED]
		 */
		
		//属性操作
		//		graph.edges.foreach(println(_))
		
		
		/*
		连接操作
		两种聚合方式，可以完成各种图的聚合操作
		Join RDDs with the graph
			def joinVertices[U](table: RDD[(VertexID, U)])(mapFunc: (VertexID, VD, U) => VD): Graph[VD, ED]
  			def outerJoinVertices[U, VD2](other: RDD[(VertexID, U)])
      (mapFunc: (VertexID, VD, Option[U]) => VD2)
    : Graph[VD2, ED]
		 */
		
		
		/*
		
		相邻聚合（Neighborhood Aggregation）
       			图分析任务的一个关键步骤是汇总每个顶点附近的信息。例如我们可能想知道每个用户的追随者的数量或者每个用户的追随者的平均年龄。许多迭代图算法（如PageRank，最短路径和连通体） 多次聚合相邻顶点的属性。
       聚合消息(aggregateMessages)
 				GraphX中的核心聚合操作是 aggregateMessages，它主要功能是向邻边发消息，合并邻边收到的消息，返回messageRDD。这个操作将用户定义的sendMsg函数应用到图的每个边三元组(edge triplet)，然后应用mergeMsg函数在其目的顶点聚合这些消息。
		
		Aggregate information about adjacent triplets
			图的邻边信息聚合，collectNeighborIds都是效率不高的操作，优先使用aggregateMessages，这也是GraphX最重要的操作之一
			  def collectNeighborIds(edgeDirection: EdgeDirection): VertexRDD[Array[VertexID]]
			  def collectNeighbors(edgeDirection: EdgeDirection): VertexRDD[Array[(VertexID, VD)]]
			  def aggregateMessages[Msg: ClassTag](
											  sendMsg: EdgeContext[VD, ED, Msg] => Unit,
											  mergeMsg: (Msg, Msg) => Msg,
											  tripletFields: TripletFields = TripletFields.All)
											: VertexRDD[A]
		
		
		
			为了查看邻近点，这里不仅可以使用aggregateMessages函数，还可以使用collectNeighborIds和collectNeighbors更方便（某些情况效果不佳）
			val neigh=graphs.collectNeighborIds(EdgeDirection.Either)
			其中EdgeDirection提供三种邻近点方向，Either，In和Out，生产的为VertexRDD类型，然后采用filter查看某点的邻近点：
			neigh.filter(e=>e._1==17).collect
		
		
		
		 */
		
		
		
		
		/*
		
		Iterative graph-parallel computation
				def pregel[A](initialMsg: A, maxIterations: Int, activeDirection: EdgeDirection)(
							  vprog: (VertexID, VD, A) => VD,
							  sendMsg: EdgeTriplet[VD, ED] => Iterator[(VertexID,A)],
							  mergeMsg: (A, A) => A)
							: Graph[VD, ED]
		
		 */
		
		
		/*
		图的算法API
		Basic graph algorithms
			  def pageRank(tol: Double, resetProb: Double = 0.15): Graph[Double, Double]
			  def connectedComponents(): Graph[VertexID, ED]
			  def stronglyConnectedComponents(numIter: Int): Graph[VertexID, ED]
			
			 TriangleCount主要用途之一是用于社区发现
				   def triangleCount(): Graph[Int, ED]
				   利用triangleCount计算社交网络三角关系数量。
					注，由于triangleCount算法对图有前提要求，首先必须(srcId < dstId)，其次存储方式 使用Graph.partitionBy。
					val graph = GraphGenerators.logNormalGraph(sc, 100,2500).partitionBy(PartitionStrategy.RandomVertexCut)
					过滤边产生子图：
					val graphs = graph.subgraph（epred = e=>e.srcId < e.dstId）
					然后使用算法：
					val triCounts = graphs.triangleCount()；
					产生的triCounts类型为：org.apache.spark.graphx.Graph[Int,Int]
		 */
		
		val triCounts=graph.triangleCount();
		println("结构化=================")
		
		
		/*
		//-----------------度的Reduce，统计度的最大值-----------------
def max(a:(VertexId,Int),b:(VertexId,Int)):(VertexId,Int)={
            if (a._2>b._2) a  else b }

val totalDegree=graph.degrees.reduce((a,b)=>max(a, b))
val inDegree=graph.inDegrees.reduce((a,b)=>max(a,b))
val outDegree=graph.outDegrees.reduce((a,b)=>max(a,b))

print("max total Degree = "+totalDegree)
print("max in Degree = "+inDegree)
print("max out Degree = "+outDegree)
//小技巧：如何知道a和b的类型为(VertexId,Int)？
//当你敲完graph.degrees.reduce((a,b)=>，再将鼠标点到a和b上查看，
//就会发现a和b是(VertexId,Int)，当然reduce后的返回值也是(VertexId,Int)
//这样就很清楚自己该如何定义max函数了

//平均度
val sumOfDegree=graph.degrees.map(x=>(x._2.toLong)).reduce((a,b)=>a+b)
val meanDegree=sumOfDegree.toDouble/graph.vertices.count().toDouble
print("meanDegree "+meanDegree)
println

//------------------使用RDD自带的统计函数进行度分布分析--------
//度的统计分析
//最大，最小
val degree2=graph.degrees.map(a=>(a._2,a._1))
//graph.degrees是VertexRDD[Int],即（VertexID，Int）。
//通过上面map调换成map(a=>(a._2,a._1)),即RDD[(Int,VetexId)]
//这样下面就可以将度（Int）当作键值（key）来操作了，
//包括下面的min，max，sortByKey，top等等，因为这些函数都是对第一个值也就是key操作的
//max degree
print("max degree = " + (degree2.max()._2,degree2.max()._1))
println

//min degree
print("min degree =" +(degree2.min()._2,degree2.min()._1))
println

//top（N） degree"超级节点"
print("top 3 degrees:\n")
degree2.sortByKey(true, 1).top(3).foreach(x=>print(x._2,x._1))
println

/*输出结果：
 * max degree = (2,4)//（Vetext，degree）
 * min degree =（1,2)
 * top 3 degrees:
 * (2,4)(5,3)(3,3)
 */
		 */
	}
	
}
