package scalassssss

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 小省 on 2016/7/11.
  */
/*
全局的，共享变量都是全局的
 */
object AccumulatorValue {
  def main(args: Array[String]) {
    val conf =new SparkConf().setAppName("AccumulatorValue").setMaster("local")
    val sc =new SparkContext(conf)
//与java不同的是直接sc.
    val accumulator=sc.accumulator(0,"out accumulator")
    //计数器
    sc.parallelize(Array(1,2,3,4,5)).foreach(x =>{accumulator+= 1;println(accumulator)})

    println(accumulator)

    sc.stop()
  }

}
