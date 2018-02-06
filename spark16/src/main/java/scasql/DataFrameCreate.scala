package scasql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 小省 on 2016/6/30.
  */
object DataFrameCreate {
  def main(args: Array[String]) {
    val conf =new SparkConf().setAppName("DataFrameCreate")
      .setMaster("local[4]")

    val sc =new SparkContext(conf)

    val sqlcontext =new SQLContext(sc)
    val df=sqlcontext.read.json("students.json")



    df.printSchema()
//    df.show()
//    df.select("name").show()
//    df.show(2)

    df.select("name","score").show()
    df.select(df.col("name"),df.col("score")).show()
    df.select(df.col("score").+(1)).show()
    df.filter(df.col("score").>(90)).show()
    df.groupBy(df.col("score")).count().show()

    import sqlcontext.implicits._
    sc.stop()
  }

}
