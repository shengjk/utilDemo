package scalassssss

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Put, HTable}
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by CMDI-W on 2016/2/29.
  */
object WriteHbaseTest {

  def readHbase(sc:SparkContext) = {
    val getTableName = "TestTable"
    val configuration = HBaseConfiguration.create()
    configuration.set(TableInputFormat.INPUT_TABLE, getTableName)  //设定表名

    val hBaseRDD = sc.newAPIHadoopRDD(configuration, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    val resultRDD = hBaseRDD.map(tuple => tuple._2)

    val arrayRDD = resultRDD.map( res => res.raw() )    //将Result类型转换为Array类型

    val columnRDD = arrayRDD.map (
      res => res.map(
        arr => {
          val rowName = new String(arr.getRow);          //取出rowName，columnName，value
          (rowName, List(( new String (arr.getQualifier) , new String(arr.getValue))))
        }

      )
    )
    columnRDD.map(res => res.reduce{ (x,y) => (x._1,x._2++y._2)})
  }


  def writeHbase(t:RDD[(String, List[(String, String)])])={
    val putTableName = "TestTableOut"

    t.foreachPartition(
      iter => {
        val hbaseConf = HBaseConfiguration.create();
        val hBaseTable = new HTable(hbaseConf,putTableName);
        //val hBaseTable = new HTable(configuration,putTableName);
        iter.foreach(
          tuple => {
            val put = new Put(tuple._1.getBytes());
            val result = tuple._2;
            for( i <- result.indices){
              put.add("PM".getBytes(), result(i)._1.getBytes(), result(i)._2.getBytes())
            }
            hBaseTable.put(put)
          }
        )
      }
    )
  }

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("WriteHbase")
    val sc = new SparkContext(conf)
    writeHbase(readHbase(sc))

    System.exit(0)
  }

}