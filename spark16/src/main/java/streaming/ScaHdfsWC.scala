package streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Duration, Durations, StreamingContext}

/**
  * Created by shengjk1 on 2016/8/11.
  */
object ScaHdfsWC {
  def main(args: Array[String]): Unit = {
    val conf =new SparkConf().setAppName("ScaHdfsWC")
      .setMaster("local")
//    val ssc =new StreamingContext(conf,Duration.apply(10000))
    val ssc =new StreamingContext(conf,Durations.milliseconds(10000))
    val lines=ssc.textFileStream("hdfs://10.16.30.54:8020/test")
    lines.flatMap(x =>x.split(" ")).map(x=>(x,1)).print()

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()

  }

}
