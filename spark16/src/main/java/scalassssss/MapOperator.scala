package scalassssss

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by root on 2016/6/7.
  */
object MapOperator {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("LineCount").setMaster("local")
    val sc = new SparkContext(conf)

    val numbers = Array(1,2,3,4,5)
    val numberRDD = sc.parallelize(numbers)
    val results = numberRDD.map(_ * 10)
    results.foreach(println(_))
  }
}
