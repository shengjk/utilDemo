package scalassssss

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object TopN {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("TopN").setMaster("local[3]")
    val sc = new SparkContext(conf)
    
    val lines = sc.textFile("top.txt")
    val pairs = lines.map(value => (value.toInt, value))
    val sorted = pairs.sortByKey(false)
    val temp = sorted.map(pair => pair._2)
    temp.take(7).foreach { x => println(x) }
  }
}