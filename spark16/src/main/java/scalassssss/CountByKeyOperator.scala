package scalassssss

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by root on 2016/6/13.
  */
object CountByKeyOperator {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SaveAsTextFileOperator")
      .setMaster("local")
    val sc = new SparkContext(conf)

    val studentList = Array(Tuple2("80s","yulei"),Tuple2("80s","fengqili")
      ,Tuple2("80s","gaohaitao"),Tuple2("70s","wangfei"),Tuple2("70s","xuruyun")
      ,Tuple2("70s","xuwei"))
    val students = sc.parallelize(studentList)
    val counts = students.countByKey()
    println(counts)
  }
}
