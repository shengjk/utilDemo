package scasql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 小省 on 2016/6/30.
  */
case class Person(  age: Int, aname: String)
object object2df {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("object2df")
      .setMaster("local[4]")

    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    // this is used to implicitly convert an RDD to a DataFrame.
    import sqlContext.implicits._

    // Define the schema using a case class.
    // Note: Case classes in Scala 2.10 can support only up to 22 fields. To work around this limit,
    // you can use custom classes that implement the Product interface.


    // Create an RDD of Person objects and register it as a table.
    val people = sc.textFile("person.txt").map(_.split(","))
      .map(p => { Person(p(0).trim.toInt,p(1))}).toDF()
    people.registerTempTable("people")

    // SQL statements can be run by using the sql methods provided by sqlContext.
    val teenagers = sqlContext.sql("SELECT * FROM people ")

    // The results of SQL queries are DataFrames and support all the normal RDD operations.
    // The columns of a row in the result can be accessed by field index:
//    teenagers.map(t => "Name: " + t(0)).collect().foreach(println)

//    // or by field name:
//    teenagers.map(t => "Name: " + t.getAs[String]("name")).collect().foreach(println)

    teenagers.map(_.getValuesMap[Any](List("name", "age"))).collect().foreach(println)
    teenagers.toJSON.first().foreach(print(_))

    sc.stop()


  }

}
