package scalassssss

import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by root on 2016/6/20.
  * 二次排序
  */
object SecondSort {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SecondSort").setMaster("local[10]")
    val sc = new SparkContext(conf)

    val lines = sc.textFile("sort.txt")
    val pairs = lines.map(line => (new SecondSortKey(line.split("\\s+")(0).toInt,line.split("\\s+")(2).toInt) , line))
    val sortedPairs = pairs.sortByKey(false)
    val results = sortedPairs.map(_._2)
    results.take(7).foreach(println(_))
  }
}
