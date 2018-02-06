package scasql

import java.io.File
import java.nio.file.{Files, Path}
import java.sql.DriverManager
import java.util

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive.HiveContext;

/**
  * Created by 小省 on 2016/7/6.
  */
object JDBCDataSource {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("JDBCDataSource").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc);

    val options = new util.HashMap[String, String]()
    options.put("url","jdbc:mysql://centos1:3306/test")
    options.put("user", "root")
    options.put("password", "123456")
    options.put("dbtable", "student")

    val studentInfo = sqlContext.read.options(options).format("jdbc").load()
    options.put("dbtable", "score")

    val studentScore = sqlContext.read.options(options).format("jdbc").load()

    val rdd1 = studentInfo.map(x => (x.getString(0), x.getInt(1)))
    val rdd2 = studentScore.map(x => (x.getString(0), x.getInt(1)))

    val studentRDD = rdd1.join(rdd2).filter(x => x._2._2 > 0)

    studentRDD.foreach(println(_))

    val goodStrudenRow = studentRDD.map(x => Row(x._1, x._2._1, x._2._2))
    //Pairdd转化为df

    //fileds
    val fileds = List(StructField("name", StringType, true),
      StructField("age", IntegerType, true),
      StructField("score", IntegerType, true))

    val df = sqlContext.createDataFrame(goodStrudenRow, StructType(fileds))
    val rowArray = df.collect()
    for (row <- rowArray) {
      println(row)
    }
//    import scala.collection.JavaConversions._
//
//    val lines=  Files.readAllLines(new File("C:\\Users\\WUJUN\\Desktop\\2222222222222222\\students.txt").toPath)


  }


}
