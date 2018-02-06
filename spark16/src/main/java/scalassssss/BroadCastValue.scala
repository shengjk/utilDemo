package scalassssss

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 小省 on 2016/7/11.
  */
object BroadCastValue {
  def main(args: Array[String]) {
    val conf =new SparkConf().setAppName("BroadCastValue").setMaster("local")
    val sc =new SparkContext(conf)

     val f =2

    sc.parallelize(Array(1,2,3,4,5)).foreach(x =>{
      println(x*f)
      x*f
    })

    sc.stop()
  }

}
