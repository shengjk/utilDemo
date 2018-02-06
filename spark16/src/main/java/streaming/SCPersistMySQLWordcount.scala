package streaming

import java.sql.{Connection, Driver, DriverManager}

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Durations, StreamingContext}

/**
  * Created by shengjk1 on 2016/8/11.
  */
object SCPersistMySQLWordcount {
  Class.forName("com.mysql.jdbc.Driver")

  def main(args: Array[String]): Unit = {
    val conf =new SparkConf().setAppName("SCPersistMySQLWordcount")
      .setMaster("local")

    val ssc =new StreamingContext(conf,Durations.milliseconds(5000))

    val lines=ssc.textFileStream("hdfs://10.16.30.54:8020/test")
    val line=lines.flatMap(x =>x.split(" ")).map(x=>(x,1))
    line.print()

    line.foreachRDD(pairRDD=>pairRDD.foreachPartition{x =>
      val conn=DriverManager.getConnection("jdbc:mysql://192.168.2.23:3306/test", "root","hadoop")


      while (x.hasNext){
        val a=x.next()
        val sql = "insert into wordcount(word,count) " + "values('" + a._1 + "'," + a._2 + ")";
        val stamt=conn.createStatement()
        stamt.execute(sql)

      }

    })

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()


  }

}
