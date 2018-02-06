package scasql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 小省 on 2016/7/1.
  */
object LoadSave {
  def main(args: Array[String]) {
    val conf =new SparkConf().setAppName("LoadSave").setMaster("local[4]")
    val sc =new SparkContext(conf)
    val sqlContext=new SQLContext(sc)

    val DF=sqlContext.read.load("users.parquet")
    DF.printSchema()
    DF.show()
    // 'overwrite', 'append', 'ignore', 'error'.
    DF.write.mode(saveMode = "Error").format("json").save("a")

    sc.stop()
  }

}
