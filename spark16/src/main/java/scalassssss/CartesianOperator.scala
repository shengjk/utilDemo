package scalassssss

import org.apache.spark.{SparkConf, SparkContext}
import java.util.Arrays
/**
  * Created by 小省 on 2016/7/7.
  */
/*
笛卡尔积
 */
object CartesianOperator {
  def main(args: Array[String]) {

    val conf=new SparkConf().setAppName("CartesianOperator").setMaster("local[4]")
    val sc =new SparkContext(conf)

    val arr1=Array("T恤衫","夹克","皮大衣","衬衫","毛衣")
    val arr2=Array("西裤","内裤","铅笔裤","皮裤","牛仔裤")

    val lines1=sc.parallelize(arr1)
    val lines2=sc.parallelize(arr2)

    lines1.cartesian(lines2).foreach(println(_))
  }

}
