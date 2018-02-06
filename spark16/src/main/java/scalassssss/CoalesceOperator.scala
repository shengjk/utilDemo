package scalassssss

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 小省 on 2016/7/7.
  */
object CoalesceOperator {
  def main(args: Array[String]) {
    val conf=new SparkConf().setAppName("CartesianOperator").setMaster("local[4]")
    val sc =new SparkContext(conf)

    val staffList=Array("xuruyun1","xuruyun2","xuruyun3"
      ,"xuruyun4","xuruyun5","xuruyun6"
      ,"xuruyun7","xuruyun8","xuruyun9"
      ,"xuruyun10","xuruyun11","xuruyun12")

//    sc.parallelize(staffList,6).mapPartitionsWithIndex()
  }
}
