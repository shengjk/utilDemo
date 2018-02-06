package graphx

/**
  * Created by shengjk1 on 2017/4/11 0011.
  */
import org.apache.spark._
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

/*
spark-shell
二度人脉
 */
object GraphxDemo7 {
	val conf = new SparkConf().setAppName("GraphxDemo3_aggregete").setMaster("local[2]")
	val sc = new SparkContext(conf)
	
	val friendsGraph = GraphLoader.edgeListFile(sc, "data/friends.txt")
	val totalRounds: Int = 3 // total N round
	var targetVerticeID: Long = 6 // target vertice
	
	// round one
	var roundGraph = friendsGraph.mapVertices((id, vd) => Map())
	var roundVertices = roundGraph.aggregateMessages[Map[Long, Integer]](
		ctx => {
			if (targetVerticeID == ctx.srcId) {
				// only the edge has target vertice should send msg
				ctx.sendToDst(Map(ctx.srcId -> totalRounds))
			}
		},
		_ ++ _
	)
	
	for (i <- 2 to totalRounds) {
		val thisRoundGraph = roundGraph.outerJoinVertices(roundVertices){ (vid, data, opt) => opt.getOrElse(Map[Long, Integer]()) }
		roundVertices = thisRoundGraph.aggregateMessages[Map[Long, Integer]](
			ctx => {
				val iterator = ctx.srcAttr.iterator
				while (iterator.hasNext) {
					val (k, v) = iterator.next
					if (v > 1) {
						val newV = v - 1
						ctx.sendToDst(Map(k -> newV))
						ctx.srcAttr.updated(k, newV)
					} else {
						// do output and remove this entry
					}
				}
			},
			(newAttr, oldAttr) => {
				if (oldAttr.contains(newAttr.head._1)) { // optimization to reduce msg
					oldAttr.updated(newAttr.head._1, 1) // stop sending this ever
				} else {
					oldAttr ++ newAttr
				}
			}
		)
	}
	
	val result = roundVertices.map(_._1).collect
}
