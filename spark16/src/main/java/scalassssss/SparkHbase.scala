package scalassssss

import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{HTable, Put}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by jk on 2016/7/26.
  */
object SparkHbase {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SparkHbase").setMaster("local")
    val sc = new SparkContext(conf)
    val readFile = sc.textFile("/path/to/file").map(x => x.split(","))
    val tableName = "table"

    readFile.foreachPartition{
      x=> {
        val myConf = HBaseConfiguration.create()
        myConf.set("hbase.zookeeper.quorum", "web102,web101,web100")
        myConf.set("hbase.zookeeper.property.clientPort", "2181")
        myConf.set("hbase.defaults.for.version.skip", "true")
        val myTable = new HTable(myConf, TableName.valueOf(tableName))
        myTable.setAutoFlush(false, false)//关键点1
        myTable.setWriteBufferSize(3*1024*1024)//关键点2
        x.foreach { y => {
          println(y(0) + ":::" + y(1))
          val p = new Put(Bytes.toBytes(y(0)))
          p.add("Family".getBytes, "qualifier".getBytes, Bytes.toBytes(y(1)))
          myTable.put(p)
        }
        }
        myTable.flushCommits()//关键点3
      }
    }
  }

}
