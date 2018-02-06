package sudf

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by root on 2016/8/8.
  */
object UDAF {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("UDAF").setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val names = Array("Yasaka", "Xuruyun", "Wangfei", "Liangyongqi", "Xuruyun", "Xuruyun")
    val namesRDD = sc.parallelize(names)
    val namesRowRDD = namesRDD.map(name=>Row(name))
    val structType = StructType(Array(StructField("name",StringType,true)))
    val namesDF = sqlContext.createDataFrame(namesRowRDD, structType)

    namesDF.show()
    namesDF.registerTempTable("names")

    sqlContext.udf.register("strGroupCount", new StringGroupCount)

    sqlContext.sql("select name, strGroupCount(name) from names group by name")
        .collect().foreach(println)
  }
}
