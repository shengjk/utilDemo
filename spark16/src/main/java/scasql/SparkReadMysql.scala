package scasql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by shengjk on 2016/8/8.
  */
object SparkReadMysql {
  def main(args: Array[String]): Unit = {
    val conf =new SparkConf().setAppName("SparkReadMysql").setMaster("local")
    val sc =new SparkContext(conf)
    val sqlContext  =new SQLContext(sc)

    val jdbcDF = sqlContext.read.
      format("jdbc")
      .options(
        Map("url" -> "jdbc:mysql://192.168.10.1:3306/bigdata",
          "driver" -> "com.mysql.jdbc.Driver",
          "dbtable" -> "person",
          "user" -> "root",
          "password" -> "123456")).load()
  }

}
