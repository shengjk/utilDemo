package scalassssss

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by root on 2016/6/7.
  */
object FlatMapOperator {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("LineCount").setMaster("local")
    val sc = new SparkContext(conf)

    val lineArray = Array("hello xuruyun" , "hello xuruyun", "hello wangfei")
    val lines = sc.parallelize(lineArray)
    // {hello,xuruyun,hello,xuruyun,hello,wangfei}
    val words = lines.flatMap(_.split(" "))
    words.foreach(println(_))
  }
}
