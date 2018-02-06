package scasql

import org.apache.spark.sql.types.{StructType,StructField,StringType};
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 小省 on 2016/6/30.
  */
object object2dfsch {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("object2df")
      .setMaster("local[4]")

    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    val people=sc.textFile("person.txt")

    //当下面是*的话这里保证顺序
    val schemaString="age name"

    import org.apache.spark.sql.Row

    val schema =
      StructType(
        schemaString.split(" ")
          .map(fieldName => StructField(fieldName, StringType, true)))

    val rowRDD=people.map(_.split(",")).map(p => Row(p(0),p(1).trim))

    val perpleDF=sqlContext.createDataFrame(rowRDD,schema)

    perpleDF.registerTempTable("people")
//可以保证顺序 select name ,age from people
    val result=sqlContext.sql("select * from people")
    result.map(t => "Name"+t(0)).collect().foreach(println(_))

  }

}
