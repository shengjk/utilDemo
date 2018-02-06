package scasql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

/**
  * Created by 小省 on 2016/7/1.
  */
object RDD2DataFrameDynamic {
  def main(args: Array[String]) {
    val conf =new SparkConf().setAppName("RDD2DataFrameDynamic")
//            .setMaster("local[4]")
            .setMaster("spark://192.168.1.111:7077")
            .setJars(List("E:\\gitProject\\sssk\\target\\spark-1.0-SNAPSHOT-jar-with-dependencies.jar"))
    val sc =new SparkContext(conf)
    val sqlct =new SQLContext(sc)
    
//    Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
//    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    
    val lines=sc.textFile("students.txt");
    val row=lines.map(_.split(",")).map(x => Row(x(0).toInt,x(1),x(2).toInt))

    //schem
    val schemaString="age name score"


    val schema =
      StructType(
        schemaString.split(" ")
          .map(fieldName =>{
                var a=StructField(fieldName, IntegerType, true)
                if(fieldName.equalsIgnoreCase("age")){
                  a=StructField(fieldName, IntegerType, true)
                }else if(fieldName.equalsIgnoreCase("name")){
                  a=StructField(fieldName, StringType, true)
                }else{
                  a=StructField(fieldName, IntegerType, true)

            }
            a
          }
            ))
  
  

    val perpleDF=sqlct.createDataFrame(row,schema)
    perpleDF.show()
    perpleDF.printSchema()
    perpleDF.registerTempTable("people")
    //可以保证顺序 select name ,age from people
    val result=sqlct.sql("select age,name,* from people")
    result.map(t => "Name"+t(0)).collect().foreach(println(_))
    result.collect().foreach(x=>print(x(0),x(1)))

  }

}
