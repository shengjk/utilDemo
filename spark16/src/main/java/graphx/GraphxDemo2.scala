package graphx

import org.apache.spark.graphx.{GraphLoader, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object GraphxDemo2 {
		
		def main(args: Array[String]) = {
			
			val conf = new SparkConf()
			val sc = new SparkContext("local", "test", conf)
			
			// 利用edge信息生成图
			//Source Id <\t> Target Id
			// dataset info
			// 1 2
			// 2 3
			// 3 1
			val graph = GraphLoader.edgeListFile(sc, "E:\\gitProject\\sssk\\graphdata\\day05-edges1.txt").cache()
			/*     src				    dest
			 顶点  顶点属性值     顶点   顶点属性值     边属性值
 			((3,(rxin,student)),(7,(jgonzal,postdoc)),collab)
			((1,1),(2,1),1)
			((2,1),(3,1),1)
			((3,1),(1,1),1)
			 */
			graph.triplets.collect().foreach(println(_))
			
			// 以[vid, name]形式读取vertex信息
			// day03-vertices.csv
			// 1,Taro
			// 2,Jiro
			val vertexLines = sc.textFile("E:\\gitProject\\sssk\\graphdata\\day05-vertices1.txt");
			val users: RDD[(VertexId, String)] = vertexLines.map(line => {
				val cols = line.split(",")
				(cols(0).toLong, cols(1))
			})
			
			/*
			
			 */
			// 将users中的vertex属性添加到graph中，生成graph2
			// 使用joinVertices操作，用user中的属性替换图中对应Id的属性
			// 先将图中的顶点属性置空
			//返回值的类型就是graph顶点属性的类型，不能新增，也不可以减少（即不能改变原始graph顶点属性类型和个数）
			val graph2 = graph.mapVertices((id, attr) => "").joinVertices(users) { (vid, empty, user) => user }
			/*
			((1,Taro),(2,Jiro),1)
			((2,Jiro),(3,),1)
			((3,),(1,Taro),1)
			 */
			graph2.triplets.collect().foreach(println(_))
			
			
			println("================ ")
			/*
			((1,),(2,),1)
			((2,),(3,),1)
			((3,),(1,),1)
			 */
			graph.mapVertices((id,attr)=>"").triplets.collect().foreach(println(_))
			
			println("\n\n~~~~~~~~~ Confirm Vertices Internal of graph2 ")
			graph2.vertices.collect.foreach(println(_))
			// (1,Taro)
			// (2,Jiro)
			// (3,)
			
			// 使用outerJoinVertices将user中的属性赋给graph中的顶点，如果图中顶点不在user中，则赋值为None
			//与前面JoinVertices不同之处在于map函数右侧类型是VD2，不再是VD，因此不受原图graph顶点属性类型VD的限制，在outerJoinVertices中使用者可以随意定义自己想要的返回类型，从而可以完全改变图的顶点属性值的类型和属性的个数
			
			val graph3 = graph.mapVertices((id, attr) => "").outerJoinVertices(users)
			{ (vid, empty, user) => user.getOrElse("None") }
			
			println("\n\n~~~~~~~~~ Confirm Vertices Internal of graph3 ")
			graph3.vertices.collect.foreach(println(_))
			// (2,Jiro)
			// (1,Taro)
			// (3,None)
			// 结果表明，如果graph的顶点在user中，则将user的属性赋给graph中对应的顶点，否则赋值为None。
			
			
			sc.stop
		}
	}