package scalassssss

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 小省 on 2016/7/11.
  */
object MapPartitionsOperator {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("MapPartitionsOperator").setMaster("local")
    val sc = new SparkContext(conf)

    val listRDD = sc.parallelize(Array("yasaka", "yasaka1", "yasaka2"))

    val map = Map("yasaka" -> 12, "yasaka1" -> 22, "yasaka2" -> 32)

        val rdd = listRDD.mapPartitions(
          x => {
            var list = List[Int]()
            while (x.hasNext) {
             list= Integer.valueOf(
                  map(x.next())
              ):: list
            }
            list.iterator
          }
        )

    println("*****************************************")
    rdd.foreach(println(_))

    sc.stop()
  }

}
