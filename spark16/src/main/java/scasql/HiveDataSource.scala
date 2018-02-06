package scasql

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 小省 on 2016/7/7.
  */
object HiveDataSource {
  def main(args: Array[String]) {
    val conf =new SparkConf().setAppName("HiveDataSource")
    val sc =new SparkContext(conf)
    val hiveContext=new HiveContext(sc)

    hiveContext.sql("drop table if exists ttt ")
  }

}
