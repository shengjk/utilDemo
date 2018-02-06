package scalassssss

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by root on 2016/6/7.
  */
object GroupByKeyOperator {

//tuple2
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("LineCount").setMaster("local")
    val sc = new SparkContext(conf)

    val scoreList = Array(Tuple2("xuruyun", 150), Tuple2("liangyongqi",100),
      Tuple2("wangfei",100),Tuple2("wangfei",80))
    val scores = sc.parallelize(scoreList)
    val groupedScores = scores.groupByKey()
    groupedScores.foreach(score => {
      println(score)
      score._2.foreach(everyScore => println(everyScore))
      println("=========================")
    })
  }
}
