package scasql

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext}

/**
  * Created by shengjk on 2016/8/8.
  */
object UDF {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("UDF").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val names = Array("Yasaka", "Xuruyun", "Wangfei", "Liangyongqi")
    val namesRDD=sc.parallelize(names,4)

    //RDD转化DF
    //1.RDD转化RowRDD
    val nameRowRDD=namesRDD.map(name =>Row(name))
    //2.生成元数据
    val st =StructType(Array(StructField("name",StringType,true)))
    //生成DF
    val nameDF=sqlContext.createDataFrame(nameRowRDD,st)

    nameDF.registerTempTable("names")
    sqlContext.udf.register("strlen",(str:String)=>str.length)
    sqlContext.sql("select name,strlen(name) from names").collect().foreach(println)

  }

}
